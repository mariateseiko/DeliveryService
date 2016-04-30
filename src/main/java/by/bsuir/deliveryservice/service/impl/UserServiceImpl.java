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
    public User loginUser(User user) throws ServiceException {
        try {
            String hashedPassword = Hasher.md5Hash(user.getPassword());
            user = userDao.selectByLoginPassword(user.getFullName(), hashedPassword);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public Long registerUser(User user) throws ServiceException {
        try {
            String hashedPassword = Hasher.md5Hash(user.getPassword());
            user.setPassword(hashedPassword);
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
