/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.service.DocFormat;
import by.bsuir.deliveryservice.service.OrderListExportService;
import by.bsuir.deliveryservice.service.impl.export.OrderListExportServiceFactory;

import java.io.File;

public class ExportOrderListAction extends DocExportAction
{
    /*
     * The ID of a courier to generate an order list for.
     */
    private Long courierId;

    /*
     * The service instance.
     */
    private static final OrderListExportService service =
            OrderListExportServiceFactory.getService();

    // ----------------------------------------------------------------------

    @Override
    public String execute() throws Exception
    {
        String docType = this.getDocType();

        File file = service.exportToFile(DocFormat.valueOf(docType),
                courierId);
        setFileToDownload(file);

        return super.execute();
    }

    // ----------------------------------------------------------------------

    public Long getCourierId()
    {
        return courierId;
    }

    public void setCourierId(Long courierId)
    {
        this.courierId = courierId;
    }
}
