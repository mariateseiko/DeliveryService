package by.bsuir.deliveryservice.service;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.OrderStatus;

public interface OrderService {
    Long placeOrder(Order order) throws ServiceException;
    void cancelOrder(Long orderId) throws ServiceException;
    Order viewOrder(Long orderId) throws ServiceException;
    void updateOrderStatus(Long orderId, OrderStatus status) throws ServiceException;
}
