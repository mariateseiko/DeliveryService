package by.bsuir.deliveryservice.service.impl.export;

import by.bsuir.deliveryservice.entity.Office;
import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.Shipping;
import by.bsuir.deliveryservice.entity.User;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;

public class AgreementExportServiceTest
{
    protected final AgreementExportServiceImpl serviceImpl =
            (AgreementExportServiceImpl)
                    AgreementExportServiceFactory.getService();

    @Test
    public void basicTest() throws Exception
    {
        File pdfFile = File.createTempFile("agreement-", ".pdf"); // Hardcoded
        System.out.println("File: " + pdfFile.getCanonicalPath());

        User courier = new User();

        courier.setFullName("%COURIER_FULL_NAME%");

        User partner = new User();

        partner.setFullName("%PARTNER_FULL_NAME%");
        partner.setPhone("+375 00 123 45 67");
        partner.setPassport("%PASSPORT%");

        Shipping shipping = new Shipping("%SHIPPING%");

        Office office = new Office();

        office.setName("%OFFICE%");
        office.setCredentials("%CREDENTIALS%");

        Order o = new Order();

        o.setDate(new Date());
        o.setDistance(12345.00);
        o.setFrom("%FROM%");
        o.setTo("%TO%");
        o.setPartner(partner);
        o.setShipping(shipping);
        o.setWeight(0.00);
        o.setTotal(1.00);
        o.setOffice(office);

        serviceImpl.impl_exportToPdf(pdfFile, o);

        assertTrue(pdfFile.exists() && pdfFile.length() > 0);
    }
}
