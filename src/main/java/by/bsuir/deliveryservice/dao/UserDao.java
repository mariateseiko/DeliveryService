package by.bsuir.deliveryservice.dao;

import by.bsuir.deliveryservice.entity.User;

public interface UserDao extends GenericDao<Long, User> {
    User selectByLoginPassword(String login, String password) throws DaoException;
}
