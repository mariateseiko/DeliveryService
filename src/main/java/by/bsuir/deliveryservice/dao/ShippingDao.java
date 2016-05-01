package by.bsuir.deliveryservice.dao;

import by.bsuir.deliveryservice.entity.Shipping;

import java.util.List;

public interface ShippingDao extends GenericDao<Long, Shipping> {
    Long selectShippingIdByName(String name) throws DaoException;
    List<Shipping> selectAllShippings() throws DaoException;
}
