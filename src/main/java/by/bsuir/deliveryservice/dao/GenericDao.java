package by.bsuir.deliveryservice.dao;

import by.bsuir.deliveryservice.entity.Entity;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public interface GenericDao<K, T extends Entity> {
    K insert(T entity) throws DaoException;
    T selectById(K id) throws DaoException;
    void update(T entity) throws DaoException;
    void delete(K id) throws DaoException;
    default Connection provideConnection() throws SQLException, NamingException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource ds = (DataSource) envContext.lookup("jdbc/delivery");
        return ds.getConnection();
    }
}
