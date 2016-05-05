package by.bsuir.deliveryservice.dao;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.OrderStatus;
import by.bsuir.deliveryservice.service.ServiceException;

import java.util.Date;
import java.util.List;

public interface OrderDao extends GenericDao<Long, Order> {
    void updateStatus(OrderStatus status, Long orderId) throws DaoException;
    void updateCourier(Long courierId, Long orderId) throws DaoException;
    List<Order> selectUserOrders(Long userId) throws DaoException;
    List<Order> selectUserApplications(Long userId) throws DaoException;
    List<Order> selectOrders() throws DaoException;
    List<Order> selectApplications() throws DaoException;
    List<Order> selectCourierOrders(Long courierId) throws DaoException;
    List<Order> selectOrdersSince(Date start) throws DaoException;
}
