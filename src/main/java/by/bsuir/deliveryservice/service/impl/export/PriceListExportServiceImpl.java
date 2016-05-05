/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.service.impl.export;

import by.bsuir.deliveryservice.dao.ShippingDao;
import by.bsuir.deliveryservice.dao.impl.ShippingDaoImpl;
import by.bsuir.deliveryservice.entity.Shipping;
import by.bsuir.deliveryservice.service.AbstractExportService;
import by.bsuir.deliveryservice.service.DocFormat;
import by.bsuir.deliveryservice.service.PriceListExportService;
import by.bsuir.deliveryservice.service.ServiceException;
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

public final class PriceListExportServiceImpl extends AbstractExportService
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

            switch (format) {
                case PDF:
                    throw new ServiceException(format.toString() +
                            " not supported for this document");
            }

            File file = getTemporaryFile(PREFIX, format.toString());

            List<Shipping> shippings = shippingDao.selectAllShippings();

            switch (format) {
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
}
