/*
 * Copyright (c) 2016, Andrew Grivachevsky, Anastasiya Kostyukova,
 * Maria Teseiko
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
