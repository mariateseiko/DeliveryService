package by.bsuir.deliveryservice.service.impl;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.service.OrderService;
import by.bsuir.deliveryservice.service.ServiceException;
import org.junit.Assert;
import org.junit.Test;

public class OrderServiceImplTest extends ServiceTest {
    private final OrderService orderService = OrderServiceImpl.getInstance();

    @Test
    public void selectOrderById() throws ServiceException {
        Order order = orderService.viewOrder(1l);
        Assert.assertNotNull(order);
    }
}
