package by.bsuir.deliveryservice.service;

import by.bsuir.deliveryservice.entity.Order;

public interface OrderService {
    Long placeOrder(Order order) throws ServiceException;
    Boolean cancelOrder(Long orderId) throws ServiceException;
    Order viewOrder(Long orderId) throws ServiceException;
    Boolean approveOrder(Long orderId) throws ServiceException;
    Boolean declineOrder(Long orderId) throws ServiceException;;
}
