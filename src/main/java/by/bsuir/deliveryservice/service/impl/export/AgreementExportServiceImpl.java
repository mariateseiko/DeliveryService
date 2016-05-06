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

            File file = getTemporaryFile(PREFIX, DocFormat.PDF.toString());

            impl_exportToPdf(file, o);

            return file;

        } catch (Exception e) {
            throw new ServiceException("failed to export AGREEMENT", e);
        }
    }

    // ----------------------------------------------------------------------

    public void impl_exportToPdf(File file, Order o) throws Exception
    {
        try {

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file));

            try {

                doc.open();

                doc.add(makeHeader(o));
                doc.add(makeHeaderOrderDate(o));
                doc.add(makeContentText(o));
                doc.add(makeSubjectContextTitle(o));
                doc.add(makeSubjectContext(o));
                doc.add(makeConditionsContextTitle(o));
                doc.add(makeConditionsContext(o));
                doc.add(makeResponsibilityContextTitle(o));
                doc.add(makeResponsibilityContext(o));
                doc.add(makePaymentContextTitle(o));
                doc.add(makePaymentContext(o));
                doc.add(makeForceMajeureContextTitle(o));
                doc.add(makeForceMajeureContext(o));
                doc.add(makeValidityContextTitle(o));
                doc.add(makeValidityContext(o));
                doc.newPage();
                doc.add(makeCustomerExecutorCredentialsTableTitle(o));
                doc.add(makeCustomerExecutorCredentialsTable(o));

            } finally {
                doc.close();
            }

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
        p.setSpacingBefore(15);

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
        p.setSpacingBefore(15);

        return p;
    }

    protected Element makeSubjectContextTitle(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.BOLD);

        String s = "1. Предмет договора";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(15);

        return p;
    }

    protected Element makeSubjectContext(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.NORMAL);

        String s = "1.1. Перевозчик обязуется выполнить автотранспортные " +
                "услуги по перевозке груза Заказчика, а Заказчик обязуется " +
                "оплатить эти услуги в соответствии с условиями, описанными " +
                "в данном договоре.";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.setSpacingBefore(15);

        return p;
    }

    protected Element makeConditionsContextTitle(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.BOLD);

        String s = "2. Условия договора";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(15);

        return p;
    }

    protected Element makeConditionsContext(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.NORMAL);

        String s = "2.1. Заказчик за 24 часа посылает заявку через веб-форму," +
                " в которой указывается время и место предоставления " +
                "транспортных средств под погрузку, место доставки груза, " +
                "характер и вес груза.\n" +
                "2.2. Перевозчик принимает заявку или отказывается от " +
                "исполнения заявки, о чем обязан сообщить Заказчику.";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.setSpacingBefore(15);

        return p;
    }

    protected Element makeResponsibilityContextTitle(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.BOLD);

        String s = "3. Ответственности сторон";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(15);

        return p;
    }

    protected Element makeResponsibilityContext(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.NORMAL);

        String s = "3.1. Перевозчик обязан своевременно и качественно " +
                "выполнять перевозку грузов Заказчика в соответствии с " +
                "заявленными требованиями, принять на себя ответственность за" +
                " сохранность в пути всех перевозимых по настоящему договору " +
                "грузов за исключением форс-мажорных обстоятельств.";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.setSpacingBefore(15);

        return p;
    }

    protected Element makePaymentContextTitle(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.BOLD);

        String s = "4. Оплата";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(15);

        return p;
    }

    protected Element makePaymentContext(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.NORMAL);

        String s = "4.1. Стоимость выполненных работ на автотранспортные " +
                "услуги по перевозке груза определяется по договоренности " +
                "сторонами в протоколе согласования цен и в Акте выполненных " +
                "работ (услуг).";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.setSpacingBefore(15);

        return p;
    }

    protected Element makeForceMajeureContextTitle(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.BOLD);

        String s = "5. Форс-мажор";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(15);

        return p;
    }

    protected Element makeForceMajeureContext(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.NORMAL);

        String s = "5.1. Стороны могут быть освобождены от ответственности в " +
                "случае наступления форс-мажорных обстоятельств.";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.setSpacingBefore(15);

        return p;
    }

    protected Element makeValidityContextTitle(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.BOLD);

        String s = "6. Срок действия";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(15);

        return p;
    }

    protected Element makeValidityContext(Order o) throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.NORMAL);

        String s = "6.1. Настоящий договор вступает в силу с момента его " +
                "подписания и действует 1 (один) год с даты его подписания.";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.setSpacingBefore(15);

        return p;
    }

    protected Element makeCustomerExecutorCredentialsTableTitle(Order o)
            throws Exception
    {
        Font font = new Font(newTimesNewRomanBaseFont(), FONT_SIZE,
                Font.BOLD);

        String s = "7. Реквизиты сторон";

        Paragraph p = new Paragraph(s, font);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(15);

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
        table.setSpacingBefore(25);

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
