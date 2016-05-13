/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.service.ActExportService;
import by.bsuir.deliveryservice.service.AgreementExportService;
import by.bsuir.deliveryservice.service.DocFormat;
import by.bsuir.deliveryservice.service.impl.export.ActExportServiceFactory;
import by.bsuir.deliveryservice.service.impl.export.AgreementExportServiceFactory;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

public class ExportActAction extends DocExportAction
{
    /*
     * The ID of an order to generate the agreement for.
     */
    private Long orderId;

    /*
     * The service instance.
     */
    private static final ActExportService service =
            ActExportServiceFactory.getService();

    // ----------------------------------------------------------------------

    @Override
    public String execute() throws Exception
    {
        String docType = this.getDocType();

        if (!docType.equalsIgnoreCase(DocFormat.PDF.toString()))
            return DocExportAction.FILE_NOT_SUPPORTED;

        File file = service.exportToFile(DocFormat.PDF, orderId);
        setFileToDownload(file);
        setFileName(String.format("act-%d-%s.%s", orderId,
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
