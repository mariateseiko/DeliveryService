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

import by.bsuir.deliveryservice.dao.DaoException;
import by.bsuir.deliveryservice.dao.OfficeDao;
import by.bsuir.deliveryservice.dao.OrderDao;
import by.bsuir.deliveryservice.dao.ShippingDao;
import by.bsuir.deliveryservice.dao.impl.OfficeDaoImpl;
import by.bsuir.deliveryservice.dao.impl.OrderDaoImpl;
import by.bsuir.deliveryservice.dao.impl.ShippingDaoImpl;
import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.OrderStatus;
import by.bsuir.deliveryservice.entity.Shipping;
import by.bsuir.deliveryservice.service.OrderService;
import by.bsuir.deliveryservice.service.ServiceException;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao = OrderDaoImpl.getInstance();
    private final OfficeDao officeDao = OfficeDaoImpl.getSingleton();
    private final ShippingDao shippingDao = ShippingDaoImpl.getInstance();

    private static OrderService instance = new OrderServiceImpl();

    public static OrderService getInstance() { return instance; }
    private OrderServiceImpl() { }

    @Override
    public Long placeOrder(Order order) throws ServiceException {
        try {
            order.setStatus(OrderStatus.AWAITING);
            order.setOffice(officeDao.getPrimary());
            long shippingId = shippingDao.selectShippingIdByName(order.getShipping().getName());
            order.getShipping().setId(shippingId);
            order.setTotal(calculatePrice(order.getWeight(), order.getDistance(), order.getShipping().getId()));
            return orderDao.insert(order);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void cancelOrder(Long orderId) throws ServiceException {
        updateOrderStatus(orderId, OrderStatus.CANCELED);
    }

    @Override
    public void approveOrder(Long orderId) throws ServiceException {
        updateOrderStatus(orderId, OrderStatus.DELIVERY);
    }

    @Override
    public Order viewOrder(Long orderId) throws ServiceException {
        try {
            return orderDao.selectById(orderId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> viewUserApplications(Long userId) throws ServiceException {
        try {
            return orderDao.selectUserApplications(userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> viewApplications() throws ServiceException {
        try {
            return orderDao.selectApplications();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> viewCourierOrders(Long courierId) throws ServiceException {
        try {
            return orderDao.selectCourierOrders(courierId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> viewOrders() throws ServiceException {
        try {
            return orderDao.selectOrders();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> viewUserOrders(Long userId) throws ServiceException {
        try {
            return orderDao.selectUserOrders(userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) throws ServiceException {
        try {
            orderDao.updateStatus(status, orderId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void assignCourier(Long orderId, Long courierId) throws ServiceException {
        try {
            orderDao.updateCourier(courierId, orderId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Double calculatePrice(Double weight, Double distance, Long shippingId) throws ServiceException  {
        Double total = 0d;
        try {
            Shipping shipping = shippingDao.selectById(shippingId);
            total += weight * shipping.getPricePerKg() + distance * shipping.getPricePerKm();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return total;
    }

    @Override
    public void declineOrder(Long orderId) throws ServiceException {
        updateOrderStatus(orderId, OrderStatus.DECLINED);
    }

    @Override
    public void deliverOrder(Long orderId) throws ServiceException {
        updateOrderStatus(orderId, OrderStatus.DELIVERED);
    }

    @Override
    public int selectCountCourierOrders(Long courierId) throws ServiceException{
        int count = 0;
        try {
            count = orderDao.selectCountCourierOrders(courierId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return count;
    }
}
