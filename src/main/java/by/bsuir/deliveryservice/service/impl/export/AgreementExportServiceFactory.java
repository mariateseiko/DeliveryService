/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.service.impl.export;

import by.bsuir.deliveryservice.service.AgreementExportService;

public final class AgreementExportServiceFactory
{
    private AgreementExportServiceFactory() {}

    // ----------------------------------------------------------------------

    private static final AgreementExportService service =
            new AgreementExportServiceImpl();

    /**
     * Returns the instance of {@link AgreementExportService}.
     *
     * @return
     *     a concrete service instance.
     */
    public  static AgreementExportService getService()
    {
        return service;
    }
}
