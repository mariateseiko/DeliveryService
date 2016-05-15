/*
 * Copyright (c) 2016, Andrew Grivachevsky, Anastasiya Kostyukova,
 * Maria Teseiko
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package by.bsuir.deliveryservice.service.impl;

import by.bsuir.deliveryservice.dao.DaoException;
import by.bsuir.deliveryservice.dao.UserDao;
import by.bsuir.deliveryservice.dao.impl.UserDaoImpl;
import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.entity.UserRole;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.util.Hasher;

import java.util.List;

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
            user = userDao.selectByLoginPassword(user.getLogin(), hashedPassword);
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
    public User viewUser(long userId) throws ServiceException {
        User user;
        try {
            user = userDao.selectById(userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public void updateUser(User user) throws ServiceException {
        try {
            userDao.update(user.getId(), user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateRole(Long userId, UserRole role) throws ServiceException {
        try {
            userDao.updateRole(userId, role);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> viewAllUsers() throws ServiceException {
        try {
            return userDao.selectUsers();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> viewCouriers() throws ServiceException {
        try {
            return userDao.selectCouriers();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
