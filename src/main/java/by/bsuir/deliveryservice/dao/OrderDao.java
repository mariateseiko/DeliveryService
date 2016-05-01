package by.bsuir.deliveryservice.dao;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.OrderStatus;
import by.bsuir.deliveryservice.service.ServiceException;

public interface OrderDao extends GenericDao<Long, Order> {
    void updateStatus(OrderStatus status, Long orderId) throws DaoException;
    void updateCourier(Long courierId, Long orderId) throws DaoException;
}
