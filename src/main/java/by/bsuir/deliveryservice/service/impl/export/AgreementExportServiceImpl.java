/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.service.impl.export;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.service.AbstractExportService;
import by.bsuir.deliveryservice.service.AgreementExportService;
import by.bsuir.deliveryservice.service.DocFormat;
import by.bsuir.deliveryservice.service.ServiceException;
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

public final class AgreementExportServiceImpl extends AbstractExportService
        implements AgreementExportService
{
    AgreementExportServiceImpl() {}

    // ----------------------------------------------------------------------

    public static final String PREFIX = "agreement";

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

            return impl_exportToPdf(o);

        } catch (Exception e) {
            throw new ServiceException("failed to export AGREEMENT", e);
        }
    }

    // ----------------------------------------------------------------------

    protected File impl_exportToPdf(Order o) throws Exception
    {
        try {

            File file = getTemporaryFile(PREFIX, DocFormat.PDF.toString());

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file));

            try {

                doc.open();

                doc.add(makeHeader(o));
                doc.add(makeHeaderOrderDate(o));
                doc.add(makeContentText(o));
                doc.add(makeAgreementText(o));
                doc.add(makeCustomerExecutorCredentialsTable(o));

            } finally {
                doc.close();
            }

            return file;

        } catch (Exception e) {
            throw new ServiceException("failed to export", e);
        }
    }

    // ----------------------------------------------------------------------

    protected static final float FONT_SIZE = (float) 11.0;
    protected static final float HEADER_FONT_SIZE = (float) 14.0;

    protected Element makeHeader(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), HEADER_FONT_SIZE,
                Font.BOLD);

        Paragraph p = new Paragraph("ДОГОВОР №" + o.getId() + "\nна " +
                "оказание услуг по доставке груза", font);
        p.setAlignment(Element.ALIGN_CENTER);

        return p;
    }

    protected Element makeHeaderOrderDate(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.NORMAL);

        DateFormat dateFormat = DateFormat.getDateInstance();

        Paragraph p = new Paragraph(dateFormat.format(o.getDate()), font);
        p.setAlignment(Element.ALIGN_RIGHT);
        p.setPaddingTop(15);

        return p;
    }

    protected Element makeContentText(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.NORMAL);

        String s = String.format("%s, в дальнейшем именуемый как Заказчик, " +
                        "с одной стороны и, %s, именуемый в дальнейшем как " +
                        "Исполнитель, с другой стороны, заключили договор о " +
                        "нижеследующем:",
                o.getPartner().getFullName(),
                o.getOffice().getName());

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.setPaddingTop(15);

        return p;
    }

    protected Element makeAgreementText(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.NORMAL);

        String s = "";

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

        String customerName = String.format(o.getPartner().getFullName());
        String customerCredentials = String.format(o.getOffice().getName());

        String executorName = String.format(o.getPartner().getPassport());
        String executorCredentials =
                String.format(o.getOffice().getCredentials());

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
