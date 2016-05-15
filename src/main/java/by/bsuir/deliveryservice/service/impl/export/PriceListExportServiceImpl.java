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

import by.bsuir.deliveryservice.dao.ShippingDao;
import by.bsuir.deliveryservice.dao.impl.ShippingDaoImpl;
import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.Shipping;
import by.bsuir.deliveryservice.service.AbstractExportService;
import by.bsuir.deliveryservice.service.DocFormat;
import by.bsuir.deliveryservice.service.PriceListExportService;
import by.bsuir.deliveryservice.service.ServiceException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceListExportServiceImpl extends AbstractExportService
    implements PriceListExportService
{
    PriceListExportServiceImpl() {}

    // ----------------------------------------------------------------------

    public static final String PREFIX = "priceList";

    // ----------------------------------------------------------------------

    private static final ShippingDao shippingDao =
            ShippingDaoImpl.getInstance();

    // ----------------------------------------------------------------------

    @Override
    public File exportToFile(DocFormat format) throws ServiceException
    {
        try {
/*
            switch (format) {
                case PDF:
                    throw new ServiceException(format.toString() +
                            " not supported for this document");
            }
*/

            File file = getTemporaryFile(PREFIX, format.toString());

            List<Shipping> shippings = shippingDao.selectAllShippings();

            switch (format) {
                case PDF:
                    impl_exportToPdf(file, shippings);
                    break;
                case XLS:
                    impl_exportToXls(file, shippings);
                    break;
                case CSV:
                    impl_exportToCsv(file, shippings);
                    break;
            }

            return file;

        } catch (Exception e) {
            throw new ServiceException("failed to export a price list", e);
        }
    }

    // ----------------------------------------------------------------------

    public void impl_exportToCsv(File file, List<Shipping> items)
            throws Exception
    {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(';');

        try (OutputStreamWriter osw = new OutputStreamWriter(
                new FileOutputStream(file),
                Charset.forName("cp1251").newEncoder()))
        {
            CSVPrinter csv = null;

            try {

                csv = new CSVPrinter(osw, csvFormat);

                /* Print header */

                csv.printRecord("Тип", "Стоимость/км", "Стоимость/кг");

                /* Print record list */

                for (Shipping item : items) {
                    csv.printRecord(item.getName(), item.getPricePerKm(),
                            item.getPricePerKg());
                }

            } finally {

                if (csv != null) {
                    csv.flush();
                    csv.close();
                }

            }
        }
    }

    // ----------------------------------------------------------------------

    public void impl_exportToXls(File file, List<Shipping> items)
            throws Exception
    {
        final int C_NAME = 0;
        final int C_PRICE_PER_KM = 1;
        final int C_PRICE_PER_KG = 2;

        final Map<String, CellStyle> cellStyles = new HashMap<>();

        Workbook wb = null;
        Cell c;
        Row row;

        try {

            wb = new HSSFWorkbook();

            /* Init cell styles */

            Font titleFont = wb.createFont();
            titleFont.setBold(true);

            Font tableHeaderFont = wb.createFont();
            tableHeaderFont.setBold(true);

            Font cellFont = wb.createFont();

            CellStyle cs = wb.createCellStyle();

            cs.setFont(titleFont);
            cs.setAlignment(CellStyle.ALIGN_CENTER);
            cs.setVerticalAlignment(CellStyle.ALIGN_FILL);

            cellStyles.put("TITLE", cs);

            cs = wb.createCellStyle();

            cs.setAlignment(CellStyle.ALIGN_CENTER);
            cs.setVerticalAlignment(CellStyle.ALIGN_FILL);
            cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cs.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
            cs.setFont(tableHeaderFont);
            cs.setBorderTop(CellStyle.BORDER_THIN);
            cs.setBorderBottom(CellStyle.BORDER_THIN);
            cs.setBorderLeft(CellStyle.BORDER_THIN);
            cs.setBorderRight(CellStyle.BORDER_THIN);

            cellStyles.put("TBL_HEADER", cs);

            cs = wb.createCellStyle();

            cs.setAlignment(CellStyle.ALIGN_CENTER);
            cs.setVerticalAlignment(CellStyle.ALIGN_FILL);
            cs.setFont(cellFont);
            cs.setBorderTop(CellStyle.BORDER_THIN);
            cs.setBorderBottom(CellStyle.BORDER_THIN);
            cs.setBorderLeft(CellStyle.BORDER_THIN);
            cs.setBorderRight(CellStyle.BORDER_THIN);

            cellStyles.put("CELL", cs);

            /* Template */

            DateFormat dateFormat = DateFormat.getDateInstance();

            Sheet earningsSheet = wb.createSheet(String.format(
                    "Прайс-лист (%s)", dateFormat.format(new Date())));

            int rowIndex = 0;

            row = earningsSheet.createRow(rowIndex);

            c = row.createCell(C_NAME);
            c.setCellValue("ПРАЙС-ЛИСТ");
            c.setCellStyle(cellStyles.get("TITLE"));

            earningsSheet.addMergedRegion(new CellRangeAddress(rowIndex,
                    rowIndex, C_NAME, C_PRICE_PER_KG));

            rowIndex++;

            row = earningsSheet.createRow(rowIndex++);

            c = row.createCell(C_NAME);
            c.setCellValue("Тип доставки");
            c.setCellStyle(cellStyles.get("TBL_HEADER"));

            c = row.createCell(C_PRICE_PER_KM);
            c.setCellValue("Стоимость/км");
            c.setCellStyle(cellStyles.get("TBL_HEADER"));

            c = row.createCell(C_PRICE_PER_KG);
            c.setCellValue("Стоимость/кг");
            c.setCellStyle(cellStyles.get("TBL_HEADER"));

            for (Shipping s : items) {

                row = earningsSheet.createRow(rowIndex++);

                c = row.createCell(C_NAME);
                c.setCellValue(s.getName());
                c.setCellStyle(cellStyles.get("CELL"));

                c = row.createCell(C_PRICE_PER_KM);
                c.setCellValue(String.format("%.2f", s.getPricePerKm()));
                c.setCellStyle(cellStyles.get("CELL"));

                c = row.createCell(C_PRICE_PER_KG);
                c.setCellValue(String.format("%.2f", s.getPricePerKg()));
                c.setCellStyle(cellStyles.get("CELL"));
            }

            earningsSheet.autoSizeColumn(C_NAME);
            earningsSheet.autoSizeColumn(C_PRICE_PER_KM);
            earningsSheet.autoSizeColumn(C_PRICE_PER_KG);

            wb.write(new FileOutputStream(file)); // Save to file

        } finally {

            if (wb != null) {
                wb.close();
            }

        }
    }

    // ----------------------------------------------------------------------

    public void impl_exportToPdf(File file, List<Shipping> items)
            throws Exception
    {
        Document d = null;

        try {
            d = new Document();
            PdfWriter.getInstance(d, new FileOutputStream(file));
            d.open();

            d.add(impl_PdfMakeHeader());
            d.add(impl_PdfMakePriceListTable(items));
            d.add(impl_PdfMakeDateHeader(new Date()));

            d.close();
        } finally {
            if (d != null) d.close();
        }
    }

    // ----------------------------------------------------------------------

    protected Element impl_PdfMakeHeader() throws Exception
    {
        DateFormat dateFormat = DateFormat.getDateInstance();

        String s = "ПРАЙС-ЛИСТ\nна услуги по доставке грузов";

        Paragraph p = new Paragraph(s,
                new com.itextpdf.text.Font(newTimesNewRomanBaseFont(), 14,
                        com.itextpdf.text.Font.BOLD));
        p.setAlignment(Element.ALIGN_CENTER);

        return p;
    }

    protected Element impl_PdfMakePriceListTable(List<Shipping> shippings)
            throws Exception
    {
        PdfPTable table = new PdfPTable(new float[]{24, 12, 12});
        PdfPCell cell;

        table.setSpacingBefore(25);
        table.setWidthPercentage(100);
        table.setSplitRows(true);

        /* Name */

        cell = new PdfPCell();

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPaddingBottom(10);

        cell.setPhrase(new Phrase("Тип",
                new com.itextpdf.text.Font(newTimesNewRomanBaseFont())));

        table.addCell(cell);

        /* PricePerKm */

        cell = new PdfPCell();

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        cell.setPhrase(new Phrase("Цена/км",
                new com.itextpdf.text.Font(newTimesNewRomanBaseFont())));

        table.addCell(cell);

        /* PricePerKg */

        cell = new PdfPCell();

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        cell.setPhrase(new Phrase("Цена/кг",
                new com.itextpdf.text.Font(newTimesNewRomanBaseFont())));

        table.addCell(cell);

        /* Rows */

        DateFormat dateFormat = DateFormat.getDateInstance();

        for (int i = 0; i < shippings.size(); i++) {

            Shipping sh = shippings.get(i);

            /* Name */

            cell = new PdfPCell();

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell.setPhrase(new Phrase(sh.getName(),
                    new com.itextpdf.text.Font(newTimesNewRomanBaseFont(), 8)));

            table.addCell(cell);

            /* PricePerKm */

            cell = new PdfPCell();

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell.setPhrase(new Paragraph(String.format("%.2f",
                    sh.getPricePerKm()),
                    new com.itextpdf.text.Font(newTimesNewRomanBaseFont(), 8)));

            table.addCell(cell);

            /* PricePerKg */

            cell = new PdfPCell();

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell.setPhrase(new Paragraph(String.format("%.2f",
                    sh.getPricePerKg()),
                    new com.itextpdf.text.Font(newTimesNewRomanBaseFont(), 8)));

            table.addCell(cell);
        }

        return table;
    }

    protected Element impl_PdfMakeDateHeader(Date date)
            throws Exception
    {
        DateFormat dateFormat = DateFormat.getDateInstance();

        String s = String.format("Цены представлены на %s г.",
                dateFormat.format(date));

        Paragraph p = new Paragraph(s,
                new com.itextpdf.text.Font(newTimesNewRomanBaseFont(), 8));
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingBefore(15);

        return p;
    }

}
