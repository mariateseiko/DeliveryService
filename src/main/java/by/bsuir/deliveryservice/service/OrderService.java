package by.bsuir.deliveryservice.service;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.OrderStatus;

import javax.xml.ws.Service;
import java.util.List;

public interface OrderService {
    Long placeOrder(Order order) throws ServiceException;
    void updateOrderStatus(Long orderId, OrderStatus status) throws ServiceException;
    void assignCourier(Long orderId, Long courierId) throws ServiceException;
    Double calculatePrice(Double weight, Double distance, Long shippingId) throws ServiceException;

    void declineOrder(Long orderId) throws ServiceException;
    void deliverOrder(Long orderId) throws ServiceException;
    void cancelOrder(Long orderId) throws ServiceException;
    void approveOrder(Long orderId) throws ServiceException;

    Order viewOrder(Long orderId) throws ServiceException;
    List<Order> viewUserApplications(Long userId) throws ServiceException;
    List<Order> viewApplications() throws ServiceException;
    List<Order> viewCourierOrders(Long courierId) throws ServiceException;
    List<Order> viewOrders() throws ServiceException;
    List<Order> viewUserOrders(Long userId) throws ServiceException;

    int selectCountCourierOrders(Long courierId) throws ServiceException;
}
