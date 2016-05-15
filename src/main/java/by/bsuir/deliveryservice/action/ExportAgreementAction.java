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

package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.service.AgreementExportService;
import by.bsuir.deliveryservice.service.DocFormat;
import by.bsuir.deliveryservice.service.impl.export.AgreementExportServiceFactory;

import java.io.File;
import java.util.Date;

public class ExportAgreementAction extends DocExportAction
{
    /*
     * The ID of an order to generate the agreement for.
     */
    private Long orderId;

    /*
     * The service instance.
     */
    private static final AgreementExportService service =
            AgreementExportServiceFactory.getService();

    // ----------------------------------------------------------------------

    @Override
    public String execute() throws Exception
    {
        String docType = this.getDocType();

        DocFormat docFormat;

        try {
            docFormat = DocFormat.valueOf(docType);
        } catch (Exception e) {
            throw new IllegalArgumentException("unknown file format", e);
        }

        File file = service.exportToFile(docFormat, orderId);
        setFileToDownload(file);
        setFileName(String.format("agreement-no-%d-%s.%s", orderId,
                FILENAME_DATE_FORMAT.format(new Date()), docType));

        return super.execute();
    }

    // ----------------------------------------------------------------------

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }
}
