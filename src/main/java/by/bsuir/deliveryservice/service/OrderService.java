package by.bsuir.deliveryservice.service;

import by.bsuir.deliveryservice.entity.Order;

public interface OrderService {
    Long placeOrder(Order order) throws ServiceException;
    void cancelOrder(Long orderId) throws ServiceException;
    Order viewOrder(Long orderId) throws ServiceException;
    void approveOrder(Long orderId) throws ServiceException;
    void declineOrder(Long orderId) throws ServiceException;;
}
