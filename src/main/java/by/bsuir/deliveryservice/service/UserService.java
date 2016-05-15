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

package by.bsuir.deliveryservice.service;


import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.entity.UserRole;

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
    User viewUser(long userId) throws ServiceException;

    void updateUser(User user) throws ServiceException;

    void updateRole(Long userId, UserRole role) throws ServiceException;

    List<User> viewAllUsers() throws ServiceException;

    List<User> viewCouriers() throws ServiceException;
}
