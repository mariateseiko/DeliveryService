package by.bsuir.deliveryservice.service.impl;

import by.bsuir.deliveryservice.dao.DaoException;
import by.bsuir.deliveryservice.dao.UserDao;
import by.bsuir.deliveryservice.dao.impl.UserDaoImpl;
import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.entity.UserRole;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.util.Hasher;

public class UserServiceImpl implements UserService {
    private static UserDao userDao = UserDaoImpl.getInstance();
    private static UserService instance = new UserServiceImpl();

    private UserServiceImpl() { }

    public static UserService getInstance() {
        return instance;
    }

    @Override
    public User loginUser(String login, String password) throws ServiceException {
        User user;
        try {
            String hashedPassword = Hasher.md5Hash(password);
            user = userDao.selectByLoginPassword(login, hashedPassword);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public Long registerUser(String login, String password, String email) throws ServiceException {
        try {
            String hashedPassword = Hasher.md5Hash(password);
            User user = new User();
            user.setLogin(login);
            user.setPassword(hashedPassword);
            user.setEmail(email);
            user.setRole(UserRole.CLIENT);
            return userDao.insert(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User selectUser(long userId) throws ServiceException {
        User user;
        try {
            user = userDao.selectById(userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return user;
    }

}
