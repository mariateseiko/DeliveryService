package by.bsuir.deliveryservice.service.impl.export;

import by.bsuir.deliveryservice.dao.OrderDao;
import by.bsuir.deliveryservice.dao.impl.OrderDaoImpl;
import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.service.AbstractExportService;
import by.bsuir.deliveryservice.service.DocFormat;
import by.bsuir.deliveryservice.service.FinanceReportExportService;
import by.bsuir.deliveryservice.service.ServiceException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FinanceReportServiceImpl extends AbstractExportService
        implements FinanceReportExportService
{
    FinanceReportServiceImpl() {}

    // ----------------------------------------------------------------------

    public static final String PREFIX = "finance";

    // ----------------------------------------------------------------------

    private static final OrderDao orderDao = OrderDaoImpl.getInstance();

    // ----------------------------------------------------------------------

    @Override
    public File exportToFile(DocFormat format, Date from)
            throws ServiceException
    {
        try {

            switch (format) {
                case PDF:
                    throw new ServiceException(format.toString() + " is not " +
                            "supported for this document");
            }

            File file = getTemporaryFile(PREFIX, format.toString());

            List<Order> orders = orderDao.selectOrdersSince(from);

            switch (format) {
                case XLS:
                    impl_exportToXls(file, from, orders);
                    break;
                case CSV:
                    impl_exportToCsv(file, orders);
                    break;
            }

            return file;

        } catch (Exception e) {
            throw new ServiceException("failed to export a " +
                    "finance report | from=" + from.toString(), e);
        }
    }

    // ----------------------------------------------------------------------

    public void impl_exportToXls(File file, Date from,
                                 List<Order> ordersFromSpecifiedDate)
            throws Exception
    {
        final int C_DATE = 0;
        final int C_AGREEMENT_NO = 1;
        final int C_SUM = 2;
        final int C_PARTNER = 3;

        final HashMap<String, CellStyle> cellStyles = new HashMap<>();

        HSSFWorkbook workbook = null;
        Cell c;
        Row row;

        try {

            workbook = new HSSFWorkbook();

            /* Init styles */

            Font boldFont = workbook.createFont();
            boldFont.setBold(true);

            CellStyle style = workbook.createCellStyle();

            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.ALIGN_FILL);
            style.setFont(boldFont);

            cellStyles.put("TITLE", style);

            style = workbook.createCellStyle();

            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.ALIGN_FILL);
            style.setFont(boldFont);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);

            cellStyles.put("HEADER", style);

            style = workbook.createCellStyle();

            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.ALIGN_FILL);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);

            cellStyles.put("TOTAL_SUM", style);

            style = workbook.createCellStyle();

            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.ALIGN_FILL);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);

            cellStyles.put("C_DATE", style);
            cellStyles.put("C_AGREEMENT_NO", style);
            cellStyles.put("C_SUM", style);

            style = workbook.createCellStyle();

            style.setAlignment(CellStyle.ALIGN_LEFT);
            style.setVerticalAlignment(CellStyle.ALIGN_FILL);
            style.setWrapText(true);
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setBorderRight(CellStyle.BORDER_THIN);

            cellStyles.put("C_PARTNER", style);

            /* Markup */

            DateFormat dateFormat = DateFormat.getDateInstance();

            Sheet sheet = workbook.createSheet(String.format("Отчет (с %s)",
                    dateFormat.format(from)));

            sheet.setColumnWidth(C_DATE, 12 * 256);
            sheet.setColumnWidth(C_AGREEMENT_NO, 24 * 256);
            sheet.setColumnWidth(C_SUM, 12 * 256);
            sheet.setColumnWidth(C_PARTNER, 32 * 256);

            int rowIndex = 0;
            row = sheet.createRow(rowIndex++);

            c = row.createCell(0);
            c.setCellValue("ФИНАНСОВЫЙ ОТЧЕТ");
            c.setCellStyle(cellStyles.get("TITLE"));

            sheet.addMergedRegion(new CellRangeAddress(0, 0, C_DATE,
                    C_PARTNER));

            row = sheet.createRow(rowIndex++);

            c = row.createCell(C_DATE);
            c.setCellValue("Дата");
            c.setCellStyle(cellStyles.get("HEADER"));

            c = row.createCell(C_AGREEMENT_NO);
            c.setCellValue("№ договора");
            c.setCellStyle(cellStyles.get("HEADER"));

            c = row.createCell(C_SUM);
            c.setCellValue("Сумма");
            c.setCellStyle(cellStyles.get("HEADER"));

            c = row.createCell(C_PARTNER);
            c.setCellValue("Конрагент");
            c.setCellStyle(cellStyles.get("HEADER"));

            double totalEarning = 0f;

            for (Order o : ordersFromSpecifiedDate) {

                row = sheet.createRow(rowIndex++);

                c = row.createCell(C_DATE);
                c.setCellValue(dateFormat.format(o.getDate()));
                c.setCellStyle(cellStyles.get("C_DATE"));

                c = row.createCell(C_AGREEMENT_NO);
                c.setCellValue(String.format("№%d от %s г.",
                        o.getId(),
                        dateFormat.format(o.getDate())));
                c.setCellStyle(cellStyles.get("C_AGREEMENT_NO"));

                c = row.createCell(C_SUM);
                c.setCellValue(String.format("+%.2f", o.getTotal()));
                c.setCellStyle(cellStyles.get("C_SUM"));

                c = row.createCell(C_PARTNER);
                c.setCellValue(o.getPartner().getFullName() + "\n" +
                        o.getPartner().getPassword());
                c.setCellStyle(cellStyles.get("C_PARTNER"));

                totalEarning += o.getTotal();
            }

            row = sheet.createRow(rowIndex);

            c = row.createCell(C_SUM);
            c.setCellValue(String.format("+%.2f", totalEarning));
            c.setCellStyle(cellStyles.get("TOTAL_SUM"));

            workbook.write(new FileOutputStream(file));

        } finally {
            if (workbook != null) workbook.close();
        }
    }

    // ----------------------------------------------------------------------

    public void impl_exportToCsv(File file,
                                 List<Order> ordersFromSpecifiedDate)
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

                csv.printRecord("Дата", "Договор", "Сумма", "Наименование",
                        "Паспорт");

                /* Print record list */

                DateFormat dateFormat = DateFormat.getDateInstance();

                for (Order o : ordersFromSpecifiedDate) {
                    csv.printRecord(dateFormat.format(o.getDate()),
                            String.format("№%d от %s г.",
                                    o.getId(),
                                    dateFormat.format(o.getDate())),
                            String.format("%.2f", o.getTotal()),
                            o.getPartner().getFullName(),
                            o.getPartner().getPassport());
                }

            } finally {

                if (csv != null) {
                    csv.flush();
                    csv.close();
                }

            }
        }
    }
}
