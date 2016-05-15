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
