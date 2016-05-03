/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.service.impl.export;

import by.bsuir.deliveryservice.service.PriceListExportService;

public class PriceListExportServiceFactory
{
    private PriceListExportServiceFactory() {}

    // ----------------------------------------------------------------------

    private static final PriceListExportService service =
            new PriceListExportServiceImpl();

    /**
     * Returns the instance of {@link PriceListExportService}.
     *
     * @return
     *     a concrete service instance.
     */
    public  static PriceListExportService getService()
    {
        return service;
    }
}
