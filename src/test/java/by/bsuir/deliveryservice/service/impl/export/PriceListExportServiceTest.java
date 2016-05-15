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
