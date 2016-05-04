/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.service.impl.export;

import by.bsuir.deliveryservice.dao.OrderDao;
import by.bsuir.deliveryservice.dao.UserDao;
import by.bsuir.deliveryservice.dao.impl.OrderDaoImpl;
import by.bsuir.deliveryservice.dao.impl.UserDaoImpl;
import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.service.AbstractExportService;
import by.bsuir.deliveryservice.service.DocFormat;
import by.bsuir.deliveryservice.service.OrderListExportService;
import by.bsuir.deliveryservice.service.ServiceException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class OrderListExportServiceImpl extends AbstractExportService
    implements OrderListExportService
{
    OrderListExportServiceImpl() {}

    // ----------------------------------------------------------------------

    public static final String PREFIX = "orders";

    // ----------------------------------------------------------------------

    private static final OrderDao orderDao = OrderDaoImpl.getInstance();
    private static final UserDao userDao = UserDaoImpl.getInstance();

    // ----------------------------------------------------------------------

    @Override
    public File exportToFile(DocFormat format,
                             Long courierId) throws ServiceException
    {
        try {

            switch (format) {
                case CSV:
                case XLS:
                    throw new ServiceException(format.toString() + " is not " +
                            "supported for this document");
            }

            File file = getTemporaryFile(PREFIX, format.toString());

            List<Order> courierOrders =
                    orderDao.selectCourierOrders(courierId);
            User courier = userDao.selectById(courierId);

            switch (format) {
                case PDF:
                    impl_exportToPdf(file, courier, courierOrders);
                    return file;
            }

        } catch (Exception e) {
            throw new ServiceException(String.format("failed to export an " +
                    "order list for courier ID %d", courierId), e);
        }

        return null;
    }

    // ----------------------------------------------------------------------

    public void impl_exportToPdf(File file, User courier,
                                 List<Order> orders) throws Exception
    {
        Document d = null;

        try {
            d = new Document();
            PdfWriter.getInstance(d, new FileOutputStream(file));
            d.open();

            d.add(impl_PdfMakeHeader());
            d.add(impl_PdfMakeCourierInfo(courier, new Date()));
            d.add(impl_PdfMakeOrderTable(orders));

            d.close();
        } finally {
            if (d != null) d.close();
        }
    }

    // ----------------------------------------------------------------------

    protected Element impl_PdfMakeHeader() throws Exception
    {
        Paragraph p = new Paragraph("СПИСОК ЗАКАЗОВ",
                new Font(newTimesNewRomanBaseFont(), 14, Font.BOLD));
        p.setAlignment(Element.ALIGN_CENTER);

        return p;
    }

    protected Element impl_PdfMakeCourierInfo(User courier, Date date)
            throws Exception
    {
        DateFormat dateFormat = DateFormat.getDateInstance();

        String s = String.format("Курьер: %s\nДата: %s",
                courier.getFullName(),
                dateFormat.format(date));

        Paragraph p = new Paragraph(s,
                new Font(newTimesNewRomanBaseFont()));
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingBefore(15);

        return p;
    }

    protected Element impl_PdfMakeOrderTable(List<Order> orders)
            throws Exception
    {
        PdfPTable table = new PdfPTable(new float[]{1, 3, 2, 1, 2, 1});
        PdfPCell cell;

        table.setSpacingBefore(15);
        table.setWidthPercentage(100);
        table.setSplitRows(true);

        /* ID */

        cell = new PdfPCell();

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPaddingBottom(10);

        cell.setPhrase(new Phrase("№"));

        table.addCell(cell);

        /* From & To */

        cell = new PdfPCell();

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        cell.setPhrase(new Phrase("Откуда/куда",
                new Font(newTimesNewRomanBaseFont())));

        table.addCell(cell);

        /* Partner */

        cell = new PdfPCell();

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        cell.setPhrase(new Phrase("Заказчик",
                new Font(newTimesNewRomanBaseFont())));

        table.addCell(cell);

        /* Weight */

        cell = new PdfPCell();

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        cell.setPhrase(new Phrase("Вес",
                new Font(newTimesNewRomanBaseFont())));

        table.addCell(cell);

        /* Shipping type */

        cell = new PdfPCell();

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        cell.setPhrase(new Phrase("Тип",
                new Font(newTimesNewRomanBaseFont())));

        table.addCell(cell);

        /* Total price */

        cell = new PdfPCell();

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        cell.setPhrase(new Phrase("Цена",
                new Font(newTimesNewRomanBaseFont())));

        table.addCell(cell);

        /* Rows */

        for (int i = 0; i < orders.size(); i++) {

            Order o = orders.get(i);

            /* ID */

            cell = new PdfPCell();

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell.setPhrase(new Phrase(String.format("%d.", i + 1),
                    new Font(newTimesNewRomanBaseFont(), 8)));

            table.addCell(cell);

            /* From & To */

            cell = new PdfPCell();

            cell.setPhrase(new Paragraph(String.format("%s\n%s",
                    o.getFrom(),
                    o.getTo()),
                    new Font(newTimesNewRomanBaseFont(), 8)));

            table.addCell(cell);

            /* Partner */

            cell = new PdfPCell();

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell.setPhrase(new Paragraph(String.format("%s\n%s",
                    o.getPartner().getFullName(),
                    o.getPartner().getPhone()),
                    new Font(newTimesNewRomanBaseFont(), 8)));

            table.addCell(cell);

            /* Weight */

            cell = new PdfPCell();

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell.setPhrase(new Phrase(String.format("%.2f", o.getWeight()),
                    new Font(newTimesNewRomanBaseFont(), 8)));

            table.addCell(cell);

            /* Shipping type */

            cell = new PdfPCell();

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell.setPhrase(new Phrase(o.getShipping().getName(),
                    new Font(newTimesNewRomanBaseFont(), 8)));

            table.addCell(cell);

            /* Total price */

            cell = new PdfPCell();

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell.setPhrase(new Phrase(String.format("%.2f", o.getTotal()),
                    new Font(newTimesNewRomanBaseFont(), 8)));

            table.addCell(cell);
        }

        return table;
    }
}
