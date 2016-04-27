package by.bsuir.deliveryservice.dao;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.service.ServiceException;

public interface OrderDao extends GenericDao<Long, Order> {
    void updateApprovedStatus(Boolean approved, Long orderId) throws DaoException;
}
