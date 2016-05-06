package by.bsuir.deliveryservice.dao;

import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.entity.UserRole;

import java.util.List;

public interface UserDao extends GenericDao<Long, User> {
    User selectByLoginPassword(String login, String password) throws DaoException;
    void updateRole(Long userId, UserRole role) throws DaoException;
    List<User> selectUsers() throws DaoException;
    List<User> selectCouriers() throws DaoException;
}
