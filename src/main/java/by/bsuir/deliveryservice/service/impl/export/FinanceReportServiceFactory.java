package by.bsuir.deliveryservice.service.impl.export;

import by.bsuir.deliveryservice.service.FinanceReportExportService;

public class FinanceReportServiceFactory
{
    private FinanceReportServiceFactory() {}

    // ----------------------------------------------------------------------

    private static final FinanceReportExportService service =
            new FinanceReportServiceImpl();

    /**
     * Returns the instance of {@link FinanceReportExportService}.
     *
     * @return
     *     a concrete service instance.
     */
    public  static FinanceReportExportService getService()
    {
        return service;
    }
}
