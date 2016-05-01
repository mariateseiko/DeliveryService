package by.bsuir.deliveryservice.dao;

import by.bsuir.deliveryservice.entity.Shipping;

public interface ShippingDao extends GenericDao<Integer, Shipping> {
    public Integer selectShippingIdByName(String name) throws DaoException;
}
