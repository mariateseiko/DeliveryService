package by.bsuir.deliveryservice.dao;

import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.entity.UserRole;

public interface UserDao extends GenericDao<Long, User> {
    User selectByLoginPassword(String login, String password) throws DaoException;
    void updateRole(Long userId, UserRole role) throws DaoException;
}
