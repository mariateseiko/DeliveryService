package by.bsuir.deliveryservice.service.impl;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.service.OrderService;
import by.bsuir.deliveryservice.service.ServiceException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class OrderServiceImplTest extends ServiceTest {
   private final OrderService orderService = OrderServiceImpl.getInstance();

    @Test
    public void selectOrderById() throws ServiceException {
        Long orderId = 1l;
        Order order = orderService.viewOrder(orderId);
        Assert.assertNotNull(order);
        Assert.assertEquals((long)orderId, order.getId());
    }

    @Test
    public void selectLists() throws ServiceException {
        Assert.assertNotNull(orderService.viewApplications());
        Assert.assertNotNull(orderService.viewOrders());
        Assert.assertTrue(orderService.viewApplications().size() > 0);
        Assert.assertTrue(orderService.viewOrders().size() > 0);
    }

    @Test
    public void selectUsersLists() throws ServiceException {
        Long userId = 1l;
        List<Order> orders = orderService.viewUserOrders(userId);
        List<Order> applcs = orderService.viewUserApplications(userId);
        Assert.assertNotNull(orders);
        Assert.assertNotNull(applcs);
        Assert.assertTrue(!orders.isEmpty());
        Assert.assertTrue(!applcs.isEmpty());
        Assert.assertEquals(orders.get(1).getPartner().getId(), (long)userId);
        Assert.assertEquals(applcs.get(1).getPartner().getId(), (long)userId);
    }
}
