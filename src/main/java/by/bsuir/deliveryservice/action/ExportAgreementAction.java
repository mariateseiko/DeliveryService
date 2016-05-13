/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
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
