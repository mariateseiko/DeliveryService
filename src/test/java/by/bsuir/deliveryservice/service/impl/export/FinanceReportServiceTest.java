package by.bsuir.deliveryservice.service.impl.export;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.Shipping;
import by.bsuir.deliveryservice.entity.User;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class FinanceReportServiceTest
{
    protected final FinanceReportServiceImpl serviceImpl =
            (FinanceReportServiceImpl) FinanceReportServiceFactory.getService();

    @Test
    public void exportToXlsTest() throws Exception
    {
        File xlsFile = File.createTempFile("finance-", ".xls"); // Hardcoded
        System.out.println("File: " + xlsFile.getCanonicalPath());

        List<Order> orders = new ArrayList<>();

        User partner = new User();

        partner.setFullName("%PARTNER_FULL_NAME%");
        partner.setPhone("+375 00 123 45 67");
        partner.setPassport("MP1234567 01 января 2012 г. " +
                "Фрунзенским РУВД г. Минск");

        Order o = new Order();

        o.setDate(new Date());
        o.setDistance(12345.00);
        o.setFrom("%FROM%");
        o.setTo("%TO%");
        o.setPartner(partner);
        o.setTotal(1.00);

        Random random = new Random();
        int count = 5 + random.nextInt(10);

        for (int i = 1; i < count; i++) {
            orders.add(o);
        }

        serviceImpl.impl_exportToXls(xlsFile, new Date(), orders);

        Assert.assertTrue(xlsFile.exists() && xlsFile.length() > 0);
    }

    @Test
    public void testExportToCsv() throws Exception
    {
        File csvFile = File.createTempFile("finance-", ".csv"); // Hardcoded
        System.out.println("File: " + csvFile.getCanonicalPath());

        List<Order> orders = new ArrayList<>();

        User partner = new User();

        partner.setFullName("%PARTNER_FULL_NAME%");
        partner.setPhone("+375 00 123 45 67");
        partner.setPassport("MP1234567 01 января 2012 г. " +
                "Фрунзенским РУВД г. Минск");

        Order o = new Order();

        o.setDate(new Date());
        o.setDistance(12345.00);
        o.setFrom("%FROM%");
        o.setTo("%TO%");
        o.setPartner(partner);
        o.setTotal(1.00);

        Random random = new Random();
        int count = 5 + random.nextInt(10);

        for (int i = 1; i < count; i++) {
            orders.add(o);
        }

        serviceImpl.impl_exportToCsv(csvFile, orders);

        Assert.assertTrue(csvFile.exists() && csvFile.length() > 0);
    }

    @Test
    public void testExportToPdf() throws Exception
    {
        File pdfFile = File.createTempFile("finance-", ".pdf"); // Hardcoded
        System.out.println("File: " + pdfFile.getCanonicalPath());

        List<Order> orders = new ArrayList<>();

        User partner = new User();

        partner.setFullName("%PARTNER_FULL_NAME%");
        partner.setPhone("+375 00 123 45 67");
        partner.setPassport("MP1234567 01 января 2012 г. " +
                "Фрунзенским РУВД г. Минск");

        Order o = new Order();

        o.setDate(new Date());
        o.setDistance(12345.00);
        o.setFrom("%FROM%");
        o.setTo("%TO%");
        o.setPartner(partner);
        o.setTotal(1.00);

        Random random = new Random();
        int count = 5 + random.nextInt(10);

        for (int i = 1; i < count; i++) {
            orders.add(o);
        }

        serviceImpl.impl_exportToPdf(pdfFile, new Date(), orders);

        Assert.assertTrue(pdfFile.exists() && pdfFile.length() > 0);
    }
}
