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
            order.setTotal(calculatePrice(order.getWeight(), order.getDistance(), order.getShipping().getId()));
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
}
