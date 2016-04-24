package by.bsuir.deliveryservice.dao.impl;

import by.bsuir.deliveryservice.dao.DaoException;
import by.bsuir.deliveryservice.dao.OrderDao;
import by.bsuir.deliveryservice.dao.pool.ConnectionPool;
import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.User;

import java.sql.*;

public class OrderDaoImpl implements OrderDao {
    private static final String SELECT_ORDER_BY_ID = "SELECT * " +
            "FROM `order` " +
            "JOIN user ON `order`.user_id = `user`.user_id " +
            "WHERE `order`.order_id=?";

    private static final String INSERT_ORDER = "INSERT INTO `order` (user_id, `from`, `to`, distance, weight, price) " +
            "VALUES (?,?,?,?,?,?)";

    private static final String UPDATE_ORDER = "UPDATE `order` SET user_id=?, `from`=?, `to`=?, distance=?, " +
            "weight=?, price=?, approved=?";

    @Override
    public Long insert(Order order) throws DaoException {
        Long result = null;
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(INSERT_ORDER, PreparedStatement.RETURN_GENERATED_KEYS)) {
            st.setLong(1, order.getUser().getId());
            st.setString(2, order.getFrom());
            st.setString(3, order.getTo());
            st.setInt(4, order.getDistance());
            st.setInt(5, order.getWeight());
            st.setInt(6, order.getPrice());
            st.executeUpdate();
            ResultSet resultSet = st.getGeneratedKeys();
            if (resultSet.next()){
                result = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return result;
    }

    @Override
    public Order selectById(Long orderId) throws DaoException {
        Order order = null;
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_ORDER_BY_ID)) {
            st.setLong(1, orderId);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                order = new Order();
                order.setId(orderId);
                order.setUser(new User(resultSet.getLong("id"), resultSet.getString("login")));
                order.setApproved(resultSet.getBoolean("approved"));
                order.setFrom(resultSet.getString("from"));
                order.setTo(resultSet.getString("to"));
                order.setDistance(resultSet.getInt("distance"));
                order.setWeight(resultSet.getInt("weight"));
                order.setPrice(resultSet.getInt("price"));
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return order;
    }

    @Override
    public void update(Order order) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
             PreparedStatement st = cn.prepareStatement(UPDATE_ORDER)) {
            st.setLong(1, order.getUser().getId());
            st.setString(2, order.getFrom());
            st.setString(3, order.getTo());
            st.setInt(4, order.getDistance());
            st.setInt(5, order.getWeight());
            st.setInt(6, order.getPrice());
            st.setBoolean(6, order.getApproved());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
    }

    @Override
    public void delete(Order entity) throws DaoException {
        throw new DaoException("Method not supported");
    }
}
