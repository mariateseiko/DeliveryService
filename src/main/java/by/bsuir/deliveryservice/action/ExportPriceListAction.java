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

        DocFormat docFormat;

        try {
            docFormat = DocFormat.valueOf(docType);
        } catch (Exception e) {
            throw new IllegalArgumentException("unknown file format", e);
        }

        File file = service.exportToFile(docFormat);
        setFileToDownload(file);

        return super.execute();
    }
}
