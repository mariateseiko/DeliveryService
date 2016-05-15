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

package by.bsuir.deliveryservice.dao.impl;

import by.bsuir.deliveryservice.dao.DaoException;
import by.bsuir.deliveryservice.dao.UserDao;
import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.entity.UserRole;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final static String SELECT_USER_BY_ID = "SELECT * FROM `user` "
            +"JOIN `role` on `user`.usr_role = `role`.rol_id "
            +"WHERE usr_id=? ";

    private final static String SELECT_USER_BY_LOGIN_PASSWORD = "SELECT * FROM `user` "
            +"JOIN `role` on `user`.usr_role = `role`.rol_id "
            +"WHERE usr_login=? AND usr_password=?";
    private final static String INSERT_USER = "INSERT INTO `user` (usr_login, usr_password, usr_fullname, " +
            "usr_mobileno, usr_role, usr_passport) VALUES(?, ?, ?, ?, ?, ?)";
    private final static String SELECT_ROLE_ID_BY_NAME = "SELECT rol_id FROM `role` WHERE rol_name=?";
    private final static String UPDATE_USER = "UPDATE `user` SET usr_fullname=?, usr_mobileno=?, usr_passport=? WHERE usr_id=?";
    private final static String UPDATE_ROLE = "UPDATE `user` SET usr_role=? WHERE usr_id=?";
    private final static String SELECT_USERS = "SELECT * FROM `user` JOIN `role` ON usr_role = rol_id";
    private final static String SELECT_COURIERS = "SELECT * FROM `user` JOIN `role` r ON usr_role = rol_id WHERE r.rol_Name='COURIER'";

    public static UserDao getInstance() {
        return instance;
    }
    private static UserDao instance = new UserDaoImpl();
    private UserDaoImpl() {}

    @Override
    public Long insert(User user) throws DaoException {
        Long result = null;
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS)) {
            st.setString(1, user.getLogin());
            st.setString(2, user.getPassword());
            st.setString(3, user.getFullName());
            st.setString(4, user.getPhone());
            st.setInt(5, selectRoleIdByName(user.getRole().toString().toUpperCase()));
            st.setString(6, user.getPassport());
            st.executeUpdate();
            ResultSet resultSet = st.getGeneratedKeys();
            if (resultSet.next()){
                result = resultSet.getLong(1);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            result = null;
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return result;
    }

    @Override
    public User selectById(Long id) throws DaoException {
        User user = null;
        try(Connection cn = provideConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_USER_BY_ID)) {
            st.setLong(1, id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("usr_id"));
                user.setLogin(resultSet.getString("usr_login"));
                user.setPhone(resultSet.getString("usr_mobileno"));
                user.setFullName(resultSet.getString("usr_fullname"));
                user.setPassport(resultSet.getString("usr_passport"));
                user.setRole(UserRole.valueOf(resultSet.getString("rol_name").toUpperCase()));
            }
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return user;
    }

    @Override
    public void update(Long id, User user) throws DaoException {
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(UPDATE_USER)) {
            st.setString(1, user.getFullName());
            st.setString(2, user.getPhone());
            st.setString(3, user.getPassport());
            st.setLong(4, id);
            st.executeUpdate();
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
    }

    @Override
    public User selectByLoginPassword(String login, String password) throws DaoException {
        User user = null;
        try (Connection cn = provideConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_USER_BY_LOGIN_PASSWORD)) {
            st.setString(1, login);
            st.setString(2, password);
            ResultSet resultSet = st.executeQuery();
            if(resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("usr_id"));
                user.setLogin(resultSet.getString("usr_login"));
                user.setPhone(resultSet.getString("usr_mobileno"));
                user.setFullName(resultSet.getString("usr_fullname"));
                user.setPassport(resultSet.getString("usr_passport"));
                user.setRole(UserRole.valueOf(resultSet.getString("rol_name").toUpperCase()));
            }
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return user;
    }

    @Override
    public void updateRole(Long userId, UserRole role) throws DaoException {
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(UPDATE_ROLE)) {
            st.setInt(1, selectRoleIdByName(role.toString()));
            st.setLong(2, userId);
            st.executeUpdate();
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
    }

    @Override
    public List<User> selectUsers() throws DaoException {
        List<User> users = new ArrayList<>();
        try (Connection cn = provideConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_USERS)) {
            ResultSet resultSet = st.executeQuery();
            while(resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("usr_id"));
                user.setLogin(resultSet.getString("usr_login"));
                user.setPhone(resultSet.getString("usr_mobileno"));
                user.setFullName(resultSet.getString("usr_fullname"));
                user.setPassport(resultSet.getString("usr_passport"));
                user.setRole(UserRole.valueOf(resultSet.getString("rol_name").toUpperCase()));
            }
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return users;
    }

    @Override
    public List<User> selectCouriers() throws DaoException {
        List<User> couriers = new ArrayList<>();
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_COURIERS)) {
            ResultSet resultSet = st.executeQuery();
            while(resultSet.next()) {
                User courier = new User();
                courier.setId(resultSet.getLong("usr_id"));
                courier.setLogin(resultSet.getString("usr_login"));
                courier.setPhone(resultSet.getString("usr_mobileno"));
                courier.setFullName(resultSet.getString("usr_fullname"));
                courier.setPassport(resultSet.getString("usr_passport"));
                courier.setRole(UserRole.valueOf("COURIER"));
                couriers.add(courier);
            }
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return couriers;
    }

    private int selectRoleIdByName(String name) throws DaoException {
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_ROLE_ID_BY_NAME)) {
            st.setString(1, name);
            ResultSet resultSet = st.executeQuery();
            resultSet.next();
            return resultSet.getInt("rol_id");
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
    }
}
