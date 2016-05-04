/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.service.impl.export;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.Shipping;
import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.service.AbstractExportService;
import by.bsuir.deliveryservice.service.OrderListExportService;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class OrderListExportServiceTest
{
    protected final OrderListExportServiceImpl serviceImpl =
            (OrderListExportServiceImpl)
                    OrderListExportServiceFactory.getService();

    @Test
    public void basicTest() throws Exception
    {
        File pdfFile = File.createTempFile("orderList", ".pdf"); // Hardcoded
        System.out.println("File: " + pdfFile.getCanonicalPath());

        User courier = new User();
        List<Order> orders = new ArrayList<>();

        courier.setFullName("%COURIER_FULL_NAME%");

        User partner = new User();

        partner.setFullName("%PARTNER_FULL_NAME%");
        partner.setPhone("+375 00 123 45 67");

        Shipping shipping = new Shipping("%SHIPPING%");

        Order o = new Order();

        o.setDate(new Date());
        o.setDistance(12345.00);
        o.setFrom("%FROM%");
        o.setTo("%TO%");
        o.setPartner(partner);
        o.setShipping(shipping);
        o.setWeight(0.00);
        o.setTotal(1.00);

        Random random = new Random();
        int count = 5 + random.nextInt(10);

        for (int i = 1; i < count; i++) {
            orders.add(o);
        }

        serviceImpl.impl_exportToPdf(pdfFile, courier, orders);

        Assert.assertTrue(pdfFile.exists() && pdfFile.length() > 0);
    }
}
