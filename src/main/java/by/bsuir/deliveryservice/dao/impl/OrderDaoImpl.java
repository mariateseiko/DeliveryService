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
import by.bsuir.deliveryservice.dao.OrderDao;
import by.bsuir.deliveryservice.entity.Office;
import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.OrderStatus;
import by.bsuir.deliveryservice.entity.Shipping;
import by.bsuir.deliveryservice.entity.User;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private static final String SELECT_ORDER_BY_ID = "SELECT * " +
            "FROM `order` " +
            "JOIN `user` AS `partner` ON `order`.ord_partner = `partner`.usr_id " +
            "LEFT JOIN `user` AS `courier` ON `order`.ord_courier = `courier`.usr_id " +
            "JOIN `shipping` ON `order`.ord_shipping = `shipping`.shp_ID " +
            "JOIN `status` ON `status`.ost_id = `order`.ord_status " +
            "JOIN `office` ON `office`.off_id = `order`.ord_office " +
            "WHERE `order`.ord_id=?";

    private static final String INSERT_ORDER = "INSERT INTO `order` (ord_partner, ord_date, ord_office, ord_status, ord_from, " +
            "ord_to, ord_distance, ord_weight, ord_shipping, ord_total) " +
            "VALUES (?, now(),?,?,?,?,?,?,?,?)";

    private static final String UPDATE_ORDER_STATUS = "UPDATE `order` SET ord_status=? WHERE ord_id=?";
    private static final String UPDATE_ORDER_COURIER = "UPDATE `order` SET ord_courier=? WHERE ord_id=?";

    private static final String SELECT_STATUS_ID_BY_NAME = "SELECT ost_id FROM `status` WHERE ost_Name = ?";
    private static final String SELECT_ORDER_BY_USER_AND_STATUSES = "SELECT * FROM `order` " +
            "JOIN `user` AS `partner` ON `order`.ord_partner = `partner`.usr_id " +
            "JOIN `shipping` ON `order`.ord_shipping = `shipping`.shp_ID " +
            "JOIN `status` ON `status`.ost_id = `order`.ord_status " +
            "JOIN `office` ON `office`.off_id = `order`.ord_office " +
            "WHERE ord_partner=? AND ost_name IN (?,?)";

    private static final String SELECT_ORDER_BY_STATUSES = "SELECT * FROM `order` " +
            "JOIN `user` AS `partner` ON `order`.ord_partner = `partner`.usr_id " +
            "LEFT JOIN `user` AS `courier` ON `order`.ord_courier = `courier`.usr_id " +
            "JOIN `shipping` ON `order`.ord_shipping = `shipping`.shp_ID " +
            "JOIN `status` ON `status`.ost_id = `order`.ord_status " +
            "JOIN `office` ON `office`.off_id = `order`.ord_office " +
            "WHERE ost_name IN (?,?)";

    private static final String SELECT_COURIER_ORDERS = "SELECT * FROM `order` " +
            "JOIN `user` AS `partner` ON `order`.ord_courier = `partner`.usr_id " +
            "LEFT JOIN `user` AS `courier` ON `order`.ord_courier = `courier`.usr_id " +
            "JOIN `shipping` ON `order`.ord_shipping = `shipping`.shp_ID " +
            "JOIN `status` ON `status`.ost_id = `order`.ord_status " +
            "JOIN `office` ON `office`.off_id = `order`.ord_office " +
            "WHERE ord_courier=? AND ost_name = 'DELIVERY' " +
            "ORDER BY ord_deliverydate ASC";

    private static final String SELECT_COUNT_COURIER_ORDERS = "SELECT count(*) AS total FROM `order` \n" +
            "LEFT JOIN `user` AS `courier` ON `order`.ord_courier = `courier`.usr_id \n" +
            "JOIN `status` ON `status`.ost_id = `order`.ord_status \n" +
            "WHERE ord_courier=? AND ost_name = 'DELIVERY' ";

    private static final String SQL_SELECT_BETWEEN = "SELECT * " +
            "FROM `order` " +
            "JOIN `user` AS `partner` ON `order`.ord_partner = `partner`.usr_id " +
            "LEFT JOIN `user` AS `courier` ON `order`.ord_courier = `courier`.usr_id " +
            "JOIN `shipping` ON `order`.ord_shipping = `shipping`.shp_ID " +
            "JOIN `status` ON `status`.ost_id = `order`.ord_status " +
            "JOIN `office` ON `office`.off_id = `order`.ord_office " +
            "WHERE DATE(`order`.ord_Date) BETWEEN ? AND ?";

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
            st.setDouble(9, order.getTotal());
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
        Order order;
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_ORDER_BY_ID)) {
            st.setLong(1, orderId);
            ResultSet resultSet = st.executeQuery();
            resultSet.next();
            order = retrieveOrderFromResultSet(resultSet);
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
    public List<Order> selectUserOrders(Long userId) throws DaoException {
        return selectOrdersByUserAndStatuses(userId, new ArrayList<>(
                Arrays.asList(OrderStatus.DELIVERY, OrderStatus.DELIVERED)));
    }

    @Override
    public List<Order> selectUserApplications(Long userId) throws DaoException {
        return selectOrdersByUserAndStatuses(userId, new ArrayList<>(
                Arrays.asList(OrderStatus.AWAITING, OrderStatus.DECLINED)));
    }

    @Override
    public List<Order> selectOrders() throws DaoException {
        return selectOrdersByStatuses(new ArrayList<>(
                Arrays.asList(OrderStatus.DELIVERY, OrderStatus.DELIVERED)));
    }

    @Override
    public List<Order> selectApplications() throws DaoException {
        return selectOrdersByStatuses(new ArrayList<>(
                Arrays.asList(OrderStatus.AWAITING, OrderStatus.DECLINED)));
    }

    @Override
    public List<Order> selectCourierOrders(Long courierId) throws DaoException {
        List<Order> orders = new ArrayList<>();
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_COURIER_ORDERS)) {
            st.setLong(1, courierId);
            ResultSet resultSet = st.executeQuery();
            while(resultSet.next()) {
                orders.add(retrieveOrderFromResultSet(resultSet));
            }
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return orders;
    }

    private Order retrieveOrderFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getLong("ord_id"));
        order.setPartner(new User(resultSet.getLong("ord_partner"), resultSet.getString("partner.usr_login")));
        order.getPartner().setFullName(resultSet.getString("partner.usr_fullname"));
        order.getPartner().setPassport(resultSet.getString("partner.usr_passport"));
        order.getPartner().setPhone(resultSet.getString("partner" +
                ".usr_mobileno"));
        if (resultSet.getLong("ord_courier") != 0) {
            order.setCourier(new User(resultSet.getLong("ord_courier"), resultSet.getString("courier.usr_login")));
        }
        order.setStatus(OrderStatus.valueOf(resultSet.getString("ost_name")));
        order.setFrom(resultSet.getString("ord_from"));
        order.setTo(resultSet.getString("ord_to"));
        order.setDistance(resultSet.getDouble("ord_distance"));
        order.setWeight(resultSet.getDouble("ord_weight"));
        order.setTotal(resultSet.getDouble("ord_total"));
        order.setShipping(new Shipping(resultSet.getString("shp_name"),
                resultSet.getDouble("shp_pricePerKg"),
                resultSet.getDouble("shp_pricePerKm")));
        order.setDate(resultSet.getDate("ord_date"));
        order.setOffice(new Office(resultSet.getString("off_name"), resultSet.getString("off_credentials")));
        order.setDeliveryDate(resultSet.getDate("ord_deliveryDate"));
        return order;
    }

    private List<Order> selectOrdersByStatuses(List<OrderStatus> statuses) throws DaoException {
        List<Order> orders = new ArrayList<>();
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_ORDER_BY_STATUSES)) {
            st.setString(1, statuses.get(0).toString());
            st.setString(2, statuses.get(1).toString());
            ResultSet resultSet = st.executeQuery();
            while(resultSet.next()) {
                orders.add(retrieveOrderFromResultSet(resultSet));
            }
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return orders;
    }

    private List<Order> selectOrdersByUserAndStatuses(Long userId, List<OrderStatus> statuses) throws DaoException {
        List<Order> orders = new ArrayList<>();
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_ORDER_BY_USER_AND_STATUSES)) {
            st.setLong(1, userId);
            st.setString(2, statuses.get(0).toString());
            st.setString(3, statuses.get(1).toString());
            ResultSet resultSet = st.executeQuery();
            while(resultSet.next()) {
                orders.add(retrieveOrderFromResultSet(resultSet));
            }
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return orders;
    }

    @Override
    public List<Order> selectOrdersSince(Date start) throws DaoException
    {
        List<Order> orders = new ArrayList<>();

        try (Connection c = provideConnection();
             PreparedStatement stm =
                     c.prepareStatement(SQL_SELECT_BETWEEN)) {

            java.sql.Date sinceDate = new java.sql.Date(start.getTime());
            java.sql.Date tillDate = new java.sql.Date((new Date()).getTime());

            stm.setDate(1, sinceDate);
            stm.setDate(2, tillDate);

            ResultSet set = stm.executeQuery();

            while(set.next())
                orders.add(this.retrieveOrderFromResultSet(set));

        } catch (Exception e) {
            throw new DaoException("failed to execute a statement", e);
        }

        return orders;
    }

    public int selectCountCourierOrders(Long courierId) throws DaoException
    {
        int countOrders = 0;
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_COUNT_COURIER_ORDERS)) {
            st.setLong(1, courierId);
            ResultSet resultSet = st.executeQuery();
            if(resultSet.next()){
                countOrders = resultSet.getInt(1);
            }


        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return countOrders;
    }
}
