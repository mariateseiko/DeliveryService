package by.bsuir.deliveryservice.service.impl.export;

import by.bsuir.deliveryservice.entity.Shipping;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PriceListExportServiceTest
{
    protected final PriceListExportServiceImpl serviceImpl =
            (PriceListExportServiceImpl)
                    PriceListExportServiceFactory.getService();

    @Test
    public void testExportToXls() throws Exception
    {
        File xlsFile = File.createTempFile("prices-", ".xls"); // Hardcoded
        System.out.println("File: " + xlsFile.getCanonicalPath());

        List<Shipping> shippings = new ArrayList<>();

        Shipping s = new Shipping();

        s.setName("%NAME%");
        s.setPricePerKg(1.00);
        s.setPricePerKm(2.00);

        Random random = new Random();
        int count = 5 + random.nextInt(10);

        for (int i = 1; i < count; i++) {
            shippings.add(s);
        }

        serviceImpl.impl_exportToXls(xlsFile, shippings);

        Assert.assertTrue(xlsFile.exists() && xlsFile.length() > 0);
    }

    @Test
    public void testExportToCsv() throws Exception
    {
        File csvFile = File.createTempFile("prices-", ".csv"); // Hardcoded
        System.out.println("File: " + csvFile.getCanonicalPath());

        List<Shipping> shippings = new ArrayList<>();

        Shipping s = new Shipping();

        s.setName("%NAME%");
        s.setPricePerKg(1.00);
        s.setPricePerKm(2.00);

        Random random = new Random();
        int count = 5 + random.nextInt(10);

        for (int i = 1; i < count; i++) {
            shippings.add(s);
        }

        serviceImpl.impl_exportToCsv(csvFile, shippings);

        Assert.assertTrue(csvFile.exists() && csvFile.length() > 0);
    }

    @Test
    public void testExportToPdf() throws Exception
    {
        File pdfFile = File.createTempFile("prices-", ".pdf"); // Hardcoded
        System.out.println("File: " + pdfFile.getCanonicalPath());

        List<Shipping> shippings = new ArrayList<>();

        Shipping s = new Shipping();

        s.setName("%NAME%");
        s.setPricePerKg(1.00);
        s.setPricePerKm(2.00);

        Random random = new Random();
        int count = 5 + random.nextInt(10);

        for (int i = 1; i < count; i++) {
            shippings.add(s);
        }

        serviceImpl.impl_exportToPdf(pdfFile, shippings);

        Assert.assertTrue(pdfFile.exists() && pdfFile.length() > 0);
    }
}
