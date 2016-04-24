package by.bsuir.deliveryservice.service.impl;

import by.bsuir.deliveryservice.dao.DaoException;
import by.bsuir.deliveryservice.dao.OrderDao;
import by.bsuir.deliveryservice.dao.impl.OrderDaoImpl;
import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.service.OrderService;
import by.bsuir.deliveryservice.service.ServiceException;

public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private static OrderService instance = new OrderServiceImpl();

    private OrderServiceImpl() {
        orderDao = OrderDaoImpl.getInstance();
    }

    @Override
    public Long placeOrder(Order order) throws ServiceException {
        try {
            return orderDao.insert(order);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void cancelOrder(Long orderId) throws ServiceException {
        try {
            orderDao.delete(orderId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
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
    public void approveOrder(Long orderId) throws ServiceException {
        try {
            orderDao.updateApprovedStatus(true, orderId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void declineOrder(Long orderId) throws ServiceException {
        try {
            orderDao.updateApprovedStatus(false, orderId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
