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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

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
                case XLS:
                    return impl_exportToXls(shippingDao.selectAllShippings());
                case CSV:
                    return impl_exportToCsv(shippingDao.selectAllShippings());
            }

        } catch (Exception e) {
            throw new ServiceException("failed to export PRICELIST", e);
        }

        return null;
    }

    // ----------------------------------------------------------------------

    protected File impl_exportToCsv(List<Shipping> items) throws Exception
    {
        File file = getTemporaryFile(PREFIX, DocFormat.CSV.toString());

        CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(';');

        try (OutputStreamWriter osw = new OutputStreamWriter(
                new FileOutputStream(file),
                Charset.forName("cp1251").newEncoder()))
        {
            CSVPrinter csv = null;

            try {

                csv = new CSVPrinter(osw, csvFormat);

                /* Print header */

                csv.printRecord("Тип", "Цена/км", "Цена/кг");

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

        return file;
    }

    // ----------------------------------------------------------------------

    protected File impl_exportToXls(List<Shipping> items) throws Exception
    {
        File file = getTemporaryFile(PREFIX, DocFormat.XLS.toString());

        Workbook wb = null;
        Cell c;
        Row row;

        try {

            DateFormat dateFormat = DateFormat.getDateInstance();

            wb = new HSSFWorkbook();
            Sheet earningsSheet = wb.createSheet(String.format(
                    "Прайс-лист (%s)", dateFormat.format(new Date())));

            int rowIndex = 0;
            row = earningsSheet.createRow(rowIndex++);

            /* Header cell style */

            CellStyle cshDelivery = wb.createCellStyle();
            CellStyle cshPricePerKm = wb.createCellStyle();
            CellStyle cshPricePerKg = wb.createCellStyle();

            cshDelivery.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);
            cshDelivery.setAlignment(CellStyle.ALIGN_CENTER);

            cshPricePerKm.setFillBackgroundColor(HSSFColor.AQUA.index);
            cshPricePerKm.setAlignment(CellStyle.ALIGN_CENTER);

            cshPricePerKg.setFillBackgroundColor(HSSFColor.AQUA.index);
            cshPricePerKg.setAlignment(CellStyle.ALIGN_CENTER);

            c = row.createCell(0);
            c.setCellValue("Тип доставки");
            c.setCellStyle(cshDelivery);

            c = row.createCell(1);
            c.setCellValue("Цена/км");
            c.setCellStyle(cshPricePerKm);

            c = row.createCell(2);
            c.setCellValue("Цена/кг");
            c.setCellStyle(cshPricePerKg);

            for (Shipping s : items) {

                row = earningsSheet.createRow(rowIndex++);

                c = row.createCell(0);
                c.setCellValue(s.getName());

                c = row.createCell(1);
                c.setCellValue(String.format("%.2f", s.getPricePerKm()));
                c.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER); // ?

                c = row.createCell(2);
                c.setCellValue(String.format("%.2f", s.getPricePerKg()));
                c.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER); // ?
            }

            earningsSheet.autoSizeColumn(0);
            earningsSheet.autoSizeColumn(1);
            earningsSheet.autoSizeColumn(2);

            wb.write(new FileOutputStream(file)); // Save to file

        } finally {

            if (wb != null) {
                wb.close();
            }

        }

        return file;
    }
}
