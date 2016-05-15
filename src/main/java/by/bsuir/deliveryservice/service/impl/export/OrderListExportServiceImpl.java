/*
 * Copyright (c) 2016, Andrew Grivachevsky, Anastasiya Kostyukova,
 * Maria Teseiko
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
