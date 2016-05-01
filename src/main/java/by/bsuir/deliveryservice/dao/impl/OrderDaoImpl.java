package by.bsuir.deliveryservice.dao.impl;

import by.bsuir.deliveryservice.dao.DaoException;
import by.bsuir.deliveryservice.dao.OrderDao;
import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.OrderStatus;
import by.bsuir.deliveryservice.entity.Shipping;
import by.bsuir.deliveryservice.entity.User;

import javax.naming.NamingException;
import java.sql.*;

public class OrderDaoImpl implements OrderDao {
    private static final String SELECT_ORDER_BY_ID = "SELECT * " +
            "FROM `order` " +
            "JOIN `user` ON `order`.ord_partner = `user`.usr_id " +
            "LEFT JOIN `user` ON `order`.ord_employee = `user`.usr_id " +
            "JOIN `shipping` ON `order`.ord_shipping = `shipping`.shp_ID " +
            "WHERE `order`.ord_id=?";

    private static final String INSERT_ORDER = "INSERT INTO `order` (ord_partner, ord_date, ord_office, ord_status, ord_from, " +
            "ord_to, ord_distance, ord_weight, ord_shipping, ord_deliveryDate, ord_total) " +
            "VALUES (?, now(),?,?,?,?,?,?,?,?,?)";

    private static final String UPDATE_ORDER_STATUS = "UPDATE `order` SET ord_status=? WHERE ord_id=?";
    private static final String UPDATE_ORDER_COURIER = "UPDATE `order` SET ord_courier=? WHERE ord_id=?";

    private static final String DELETE_ORDER = "DELETE FROM `order` WHERE ord_id=?";

    private static final String SELECT_STATUS_ID_BY_NAME = "SELECT ost_id FROM `status` WHERE ost_Name = ?";

    private static OrderDao instance = new OrderDaoImpl();
    private OrderDaoImpl() {}
    public static OrderDao getInstance() {
        return instance;
    }

    private Integer selectStatusIdByName(String name) throws DaoException {
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_STATUS_ID_BY_NAME)) {
            st.setString(1, name);
            ResultSet resultSet = st.executeQuery();
            resultSet.next();
            return resultSet.getInt("ost_id");
        } catch (SQLException | NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
    }

    @Override
    public Long insert(Order order) throws DaoException {
        Long result = null;
        try (Connection cn = provideConnection();
            PreparedStatement st = cn.prepareStatement(INSERT_ORDER, PreparedStatement.RETURN_GENERATED_KEYS)) {
            st.setLong(1, order.getPartner().getId());
            st.setLong(2, order.getOffice().getId());
            st.setLong(3, selectStatusIdByName(order.getStatus().toString()));
            st.setString(4, order.getFrom());
            st.setString(5, order.getTo());
            st.setDouble(6, order.getDistance());
            st.setDouble(7, order.getWeight());
            st.setLong(8, order.getShipping().getId());
            st.setDate(9, new java.sql.Date(order.getDeliveryDate().getTime()));
            st.setDouble(10, order.getTotal());
            st.executeUpdate();
            ResultSet resultSet = st.getGeneratedKeys();
            if (resultSet.next()){
                result = resultSet.getLong(1);
            }
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return result;
    }

    @Override
    public Order selectById(Long orderId) throws DaoException {
        Order order = null;
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_ORDER_BY_ID)) {
            st.setLong(1, orderId);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                order = new Order();
                order.setId(orderId);
                order.setPartner(new User(resultSet.getLong("ord_partner"), resultSet.getString("usr_login")));
                order.setCourier(new User(resultSet.getLong("ord_courier"), resultSet.getString("usr_fullName")));
                order.setStatus(OrderStatus.valueOf(resultSet.getString("ost_name")));
                order.setFrom(resultSet.getString("ord_from"));
                order.setTo(resultSet.getString("ord_to"));
                order.setDistance(resultSet.getDouble("distance"));
                order.setWeight(resultSet.getDouble("weight"));
                order.setTotal(resultSet.getDouble("total"));
                order.setShipping(new Shipping(resultSet.getString("shp_name"),
                        resultSet.getDouble("shp_pricePerKg"),
                        resultSet.getDouble("shp_pricePerKm")));
                order.setDate(resultSet.getDate("ord_date"));
                order.setDeliveryDate(resultSet.getDate("ord_deliveryDate"));
            }
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return order;
    }

    @Override
      public void update(Long orderId, Order order) throws DaoException {
        throw new DaoException("Full UPDATE not supported");
    }

    @Override
    public void updateStatus(OrderStatus status, Long orderId) throws DaoException {
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(UPDATE_ORDER_STATUS)) {
            st.setInt(1, selectStatusIdByName(status.toString()));
            st.setLong(2, orderId);
            st.executeUpdate();
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
    }

    @Override
    public void updateCourier(Long courierId, Long orderId) throws DaoException {
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(UPDATE_ORDER_COURIER)) {
            st.setLong(1, courierId);
            st.setLong(2, orderId);
            st.executeUpdate();
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
    }

    @Override
    public void delete(Long id) throws DaoException {
        try (Connection cn = provideConnection();
            PreparedStatement st = cn.prepareStatement(DELETE_ORDER)) {
            st.setLong(1, id);
            st.executeUpdate();
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
    }
}
