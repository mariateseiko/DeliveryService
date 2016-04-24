package by.bsuir.deliveryservice.dao;

import by.bsuir.deliveryservice.entity.Entity;

public interface GenericDao<K, T extends Entity> {
    K insert(T entity) throws DaoException;
    T selectById(K id) throws DaoException;
    void update(T entity) throws DaoException;
    void delete(K id) throws DaoException;
}
