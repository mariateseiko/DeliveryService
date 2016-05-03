/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.service.DocFormat;
import by.bsuir.deliveryservice.service.PriceListExportService;
import by.bsuir.deliveryservice.service.impl.export.PriceListExportServiceFactory;

import java.io.File;

public class ExportPriceListAction extends DocExportAction
{
    /*
     * The service instance.
     */
    private static final PriceListExportService service =
            PriceListExportServiceFactory.getService();

    // ----------------------------------------------------------------------

    @Override
    public String execute() throws Exception
    {
        String docType = this.getDocType();

        if (!docType.equalsIgnoreCase(DocFormat.CSV.toString()) ||
                !docType.equalsIgnoreCase(DocFormat.XLS.toString()))
            return DocExportAction.FILE_NOT_SUPPORTED;

        File file = service.exportToFile(DocFormat.valueOf(docType));
        setFileToDownload(file);

        return super.execute();
    }
}
