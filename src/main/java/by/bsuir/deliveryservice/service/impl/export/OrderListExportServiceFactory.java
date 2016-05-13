package by.bsuir.deliveryservice.service.impl.export;

import by.bsuir.deliveryservice.service.OrderListExportService;

public final class OrderListExportServiceFactory
{
    private OrderListExportServiceFactory() {}

    // ----------------------------------------------------------------------

    private static final OrderListExportService service =
            new OrderListExportServiceImpl();

    /**
     * Returns the instance of {@link OrderListExportService}.
     *
     * @return
     *     a concrete service instance.
     */
    public  static OrderListExportService getService()
    {
        return service;
    }
}
