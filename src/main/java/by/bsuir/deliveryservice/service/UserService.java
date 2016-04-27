package by.bsuir.deliveryservice.service;


import by.bsuir.deliveryservice.entity.User;

import java.util.List;

/**
 * Represents an interface of a service for user-related actions
 */
public interface UserService {
    /**
     * Attempts to authenticate and authorize a user with a given login and password
     * @param user user to login
     * @return {@code User} object with id and role or null, if credentials are invalid
     * @throws ServiceException if DaoException occurred
     */
    User loginUser(User user) throws ServiceException;

    /**
     * Attempts to register a new user with given personal info. Login should be unique in the system
     * @param user user to registrate
     * @return id of the registered user or null if registration failed
     * @throws ServiceException if DaoException occurred
     */
    Long registerUser(User user) throws ServiceException;

    /**
     * Retrieves a user with a specified id
     * @param userId id of the user to retrieve
     * @return a user with the specified id or null if such doesn't exist
     * @throws ServiceException if DaoException occurred
     */
    User selectUser(long userId) throws ServiceException;

}
