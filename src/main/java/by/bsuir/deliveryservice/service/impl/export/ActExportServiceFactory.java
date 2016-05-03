/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.service.impl.export;

import by.bsuir.deliveryservice.service.ActExportService;

public final class ActExportServiceFactory
{
    private ActExportServiceFactory() {}

    // ----------------------------------------------------------------------

    private static final ActExportService service =
            new ActExportServiceImpl();

    /**
     * Returns the instance of {@link ActExportService}.
     *
     * @return
     *     a concrete service instance.
     */
    public  static ActExportService getService()
    {
        return service;
    }
}
