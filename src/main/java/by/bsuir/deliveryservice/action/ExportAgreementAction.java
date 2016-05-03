/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.service.AgreementExportService;
import by.bsuir.deliveryservice.service.DocFormat;
import by.bsuir.deliveryservice.service.impl.export.AgreementExportServiceFactory;

import java.io.File;

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

        if (!docType.equalsIgnoreCase("PDF"))
            return DocExportAction.FILE_NOT_SUPPORTED;

        File file = service.exportToFile(DocFormat.PDF, orderId);
        setFileToDownload(file);

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
