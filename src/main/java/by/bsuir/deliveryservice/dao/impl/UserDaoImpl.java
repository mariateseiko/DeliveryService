package by.bsuir.deliveryservice.dao.impl;

import by.bsuir.deliveryservice.dao.DaoException;
import by.bsuir.deliveryservice.dao.UserDao;
import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.entity.UserRole;

import javax.naming.NamingException;
import java.sql.*;

public class UserDaoImpl implements UserDao {
    private final static String SELECT_USER_BY_ID = "SELECT * FROM `user` "
            +"JOIN `role` on `user`.usr_role = `role`.rol_id "
            +"WHERE usr_id=? ";

    private final static String SELECT_USER_BY_LOGIN_PASSWORD = "SELECT * FROM `user` "
            +"JOIN `role` on `user`.usr_role = `role`.rol_id "
            +"WHERE usr_login=? AND usr_password=?";
    private final static String INSERT_USER = "INSERT INTO `user` (usr_login, usr_password, usr_fullname, " +
            "usr_mobileno, usr_role) VALUES(?, ?, ?, ?, ?)";
    private final static String SELECT_ROLE_ID_BY_NAME = "SELECT rol_id FROM `role` WHERE rol_name=?";

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
        User user = new User();
        try(Connection cn = provideConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_USER_BY_ID)) {
            st.setLong(1, id);
            ResultSet resultSet = st.executeQuery();
            resultSet.next();
            user.setId(resultSet.getLong("usr_id"));
            user.setLogin(resultSet.getString("usr_login"));
            user.setPhone(resultSet.getString("usr_mobileno"));
            user.setFullName(resultSet.getString("usr_fullname"));
            user.setPassport(resultSet.getString("usr_passport"));
            user.setRole(UserRole.valueOf(resultSet.getString("user_role.name").toUpperCase()));
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return user;
    }

    @Override
    public void update(User entity) throws DaoException {
        // TODO
    }

    @Override
    public void delete(Long id) throws DaoException {
        //TODO
    }

    @Override
    public User selectByLoginPassword(String login, String password) throws DaoException {
        User user = null;
        try(Connection cn = provideConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_USER_BY_LOGIN_PASSWORD)) {
            st.setString(1, login);
            st.setString(2, String.valueOf(password));
            ResultSet resultSet = st.executeQuery();
            if(resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("usr_id"));
                user.setLogin(resultSet.getString("usr_login"));
                user.setPhone(resultSet.getString("usr_mobileno"));
                user.setFullName(resultSet.getString("usr_fullname"));
                user.setPassport(resultSet.getString("usr_passport"));
                user.setRole(UserRole.valueOf(resultSet.getString("user_role.name").toUpperCase()));
            }
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return user;
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
