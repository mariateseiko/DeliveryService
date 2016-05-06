/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.service.impl.export;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.service.AbstractExportService;
import by.bsuir.deliveryservice.service.ActExportService;
import by.bsuir.deliveryservice.service.DocFormat;
import by.bsuir.deliveryservice.service.ServiceException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;

public class ActExportServiceImpl extends AbstractExportService
    implements ActExportService
{
    ActExportServiceImpl() {}

    // ----------------------------------------------------------------------

    public static final String PREFIX = "act";

    // ----------------------------------------------------------------------

    @Override
    public File exportToFile(DocFormat format, Long orderId)
            throws ServiceException
    {
        try {

            /* Filter unsupported file formats */

            switch (format) {
                case CSV:
                case XLS:
                    throw new ServiceException(format.toString() +
                            " not supported for this document");
            }

            /* Fetch data */

            Order o = orderDao.selectById(orderId);

            if (o == null)
                throw new ServiceException("an order with ID " + orderId + " " +
                        "does not exists");

            /* Run an appropriate document generator */

            File file = getTemporaryFile(PREFIX, DocFormat.PDF.toString());

            impl_exportToPdf(file, o);

            return file;

        } catch (Exception e) {
            throw new ServiceException("failed to export ACT", e);
        }
    }

    // ----------------------------------------------------------------------

    public void impl_exportToPdf(File file, Order o)
            throws ServiceException
    {
        try {

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file));

            try {

                doc.open();

                doc.add(makeHeader(o));
                doc.add(makeHeaderOrderDate(o));
                doc.add(makeContentText(o));
                doc.add(makeTable(o));
                doc.add(makeWarningText(o));
                doc.add(makeCustomerExecutorCredentialsTable(o));

            } finally {
                doc.close();
            }

        } catch (Exception e) {
            throw new ServiceException("failed to export", e);
        }
    }

    // -----------------------------------------------------------------------

    protected static final float FONT_SIZE = (float) 11.0;
    protected static final float HEADER_FONT_SIZE = (float) 14.0;

    protected Element makeHeader(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), HEADER_FONT_SIZE,
                Font.BOLD);

        Paragraph p = new Paragraph("АКТ №" + o.getId() +
                "\nприемки-сдачи выполненных работ\n\n", font);
        p.setAlignment(Element.ALIGN_CENTER);

        return p;
    }

    protected Element makeHeaderOrderDate(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.NORMAL);

        DateFormat dateFormat = DateFormat.getDateInstance();

        String s = dateFormat.format(o.getDeliveryDate()) + "\n\n";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_RIGHT);

        return p;
    }

    protected Element makeContentText(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.NORMAL);

        String s = String.format("%s, в дальнейшем именуемый как Заказчик, с " +
                        "одной стороны и %s, именуемый в дальнейшем как Исполнитель," +
                        " с другой стороны, составили настоящий акт о том, что " +
                        "Исполнитель выполнил, а Заказчик принял следующие " +
                        "работы:\n\n",
                o.getPartner().getFullName(),
                o.getOffice().getName());

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.setPaddingTop(15);

        return p;
    }

    protected Element makeTable(Order o)
            throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.NORMAL);

        PdfPTable table = new PdfPTable(new float[] {1, 10, 5, 3});
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        table.setWidthPercentage(100);

        PdfPCell hdrNo = new PdfPCell(
                new Paragraph("№", font));
        hdrNo.setHorizontalAlignment(Element.ALIGN_CENTER);
        hdrNo.setBackgroundColor(BaseColor.LIGHT_GRAY);

        PdfPCell hdrName = new PdfPCell(
                new Paragraph("Наименование", font));
        hdrName.setHorizontalAlignment(Element.ALIGN_CENTER);
        hdrName.setBackgroundColor(BaseColor.LIGHT_GRAY);

        PdfPCell hdrShipping = new PdfPCell(
                new Paragraph("Тип доставки", font));
        hdrShipping.setHorizontalAlignment(Element.ALIGN_CENTER);
        hdrShipping.setBackgroundColor(BaseColor.LIGHT_GRAY);

        PdfPCell hdrPrice = new PdfPCell(
                new Paragraph("Стоимость", font));
        hdrPrice.setHorizontalAlignment(Element.ALIGN_CENTER);
        hdrPrice.setBackgroundColor(BaseColor.LIGHT_GRAY);

        DateFormat dateFormat = DateFormat.getDateInstance();

        String rowNoText = String.format("%d", 1);
        String rowNameText = String.format("Доставка груза согласно договору " +
                "№%s от %s г.",
                o.getId(), dateFormat.format(o.getDate()));
        String rowShippingText = String.format("%s", o.getShipping().getName());
        String rowPriceText = String.format("%.2f", o.getTotal());

        table.addCell(hdrNo);
        table.addCell(hdrName);
        table.addCell(hdrShipping);
        table.addCell(hdrPrice);

        PdfPCell rowNoCell = new PdfPCell(new Paragraph(rowNoText, font));
        rowNoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        rowNoCell.setVerticalAlignment(Element.ALIGN_CENTER);

        PdfPCell rowNameCell = new PdfPCell(new Paragraph(rowNameText, font));
        rowNameCell.setPadding(5);
        rowNameCell.setVerticalAlignment(Element.ALIGN_CENTER);

        PdfPCell rowShippingCell =
                new PdfPCell(new Paragraph(rowShippingText, font));
        rowShippingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        rowShippingCell.setVerticalAlignment(Element.ALIGN_CENTER);

        PdfPCell rowPriceCell =
                new PdfPCell(new Paragraph(rowPriceText, font));
        rowPriceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        rowPriceCell.setVerticalAlignment(Element.ALIGN_CENTER);

        table.addCell(rowNoCell);
        table.addCell(rowNameCell);
        table.addCell(rowShippingCell);
        table.addCell(rowPriceCell);

        return table;
    }

    protected Element makeWarningText(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.NORMAL);

        String s = "Работы выполнены в полном объеме, в установленные сроки и" +
                " с надлежащим качеством. Стороны претензий к друг другу не " +
                "имеют.";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.setPaddingTop(15);

        return p;
    }

    protected Element makeCustomerExecutorCredentialsTable(Order o)
            throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.NORMAL);

        String customerName = o.getPartner().getFullName();
        String customerCredentials = o.getPartner().getPassport();

        String executorName = o.getOffice().getName();
        String executorCredentials = o.getOffice().getCredentials();

        PdfPTable table = new PdfPTable(2);
        table.setSpacingBefore(20);

        PdfPCell customerInfoHeaderCell = new PdfPCell(new Paragraph
                ("Заказчик:", font));
        customerInfoHeaderCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell executorInfoHeaderCell = new PdfPCell(new Paragraph
                ("Исполнитель:", font));
        executorInfoHeaderCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell customerNameCell = new PdfPCell(new Paragraph(customerName,
                font));
        customerNameCell.setBorder(Rectangle.NO_BORDER);
        customerNameCell.setPaddingTop(10);

        PdfPCell executorNameCell = new PdfPCell(new Paragraph(executorName,
                font));
        executorNameCell.setBorder(Rectangle.NO_BORDER);
        executorNameCell.setPaddingTop(10);

        PdfPCell customerCredentialsCell =
                new PdfPCell(new Paragraph(customerCredentials, font));
        customerCredentialsCell.setBorder(Rectangle.NO_BORDER);
        customerCredentialsCell.setPaddingTop(10);

        PdfPCell executorCredentialsCell =
                new PdfPCell(new Paragraph(executorCredentials, font));
        executorCredentialsCell.setBorder(Rectangle.NO_BORDER);
        executorCredentialsCell.setPaddingTop(10);

        PdfPCell signLine = new PdfPCell(new Paragraph("_______________"));
        signLine.setBorder(Rectangle.NO_BORDER);
        signLine.setPaddingTop(15);

        table.addCell(customerInfoHeaderCell);
        table.addCell(executorInfoHeaderCell);
        table.addCell(customerNameCell);
        table.addCell(executorNameCell);
        table.addCell(customerCredentialsCell);
        table.addCell(executorCredentialsCell);
        table.addCell(signLine);
        table.addCell(signLine);

        return table;
    }
}
