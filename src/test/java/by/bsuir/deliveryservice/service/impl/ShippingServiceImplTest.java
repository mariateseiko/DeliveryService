package by.bsuir.deliveryservice.service.impl;

import by.bsuir.deliveryservice.entity.Shipping;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.ShippingService;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ShippingServiceImplTest extends ServiceTest {
    private final ShippingService shippingService = ShippingServiceImpl.getInstance();

    @Test
    public void selectShippings() throws ServiceException {
        List<Shipping> shippings = shippingService.viewPricelist();
        Assert.assertNotNull(shippings);
        Assert.assertTrue(!shippings.isEmpty());
    }
}

