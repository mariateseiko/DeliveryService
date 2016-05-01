package by.bsuir.deliveryservice.dao.impl;

import by.bsuir.deliveryservice.dao.DaoException;
import by.bsuir.deliveryservice.dao.ShippingDao;
import by.bsuir.deliveryservice.entity.Shipping;


import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShippingDaoImpl implements ShippingDao {
    private static ShippingDao instance = new ShippingDaoImpl();
    private ShippingDaoImpl() {}
    public static ShippingDao getInstance() {
        return instance;
    }

    private static final String INSERT_SHIPPING = "INSERT INTO `shipping` (shp_name, shp_pricePerKg, shp_pricePerKm) " +
            "VALUES (?,?,?)";
    private static final String SELECT_BY_ID = "SELECT * FROM `shipping` WHERE shp_id = ?";
    private static final String UPDATE_SHIPPING = "UPDATE `shipping` SET shp_name=?, shp_pricePerKg=?, shp_pricePerKm=? WHERE shp_id=?";
    private static final String SELECT_SHIPPING_ID_BY_NAME = "SELECT shp_id FROM `shipping` WHERE shp_Name = ?";
    private static final String SELECT_ALL_SHIPPINGS = "SELECT * FROM `shipping`";
    @Override
    public Integer insert(Shipping shipping) throws DaoException {
        Integer result = null;
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(INSERT_SHIPPING, PreparedStatement.RETURN_GENERATED_KEYS)) {
            st.setString(1, shipping.getName());
            st.setDouble(2, shipping.getPricePerKg());
            st.setDouble(3, shipping.getPricePerKm());
            st.executeUpdate();
            ResultSet resultSet = st.getGeneratedKeys();
            if (resultSet.next()){
                result = resultSet.getInt(1);
            }
        } catch (SQLException |NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return result;
    }

    @Override
    public Shipping selectById(Integer shippingId) throws DaoException {
        Shipping shipping = null;
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_BY_ID)) {
            st.setInt(1, shippingId);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                shipping = new Shipping();
                shipping.setName(resultSet.getString("shp_name"));
                shipping.setPricePerKg(resultSet.getDouble("shp_pricePerKg"));
                shipping.setPricePerKm(resultSet.getDouble("shp_pricePerKm"));
            }
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return shipping;
    }

    @Override
    public void update(Shipping shipping) throws DaoException {
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(UPDATE_SHIPPING)) {
            st.setString(1, shipping.getName());
            st.setDouble(2, shipping.getPricePerKg());
            st.setDouble(3, shipping.getPricePerKm());
            st.setLong(4, shipping.getId());
            st.executeUpdate();
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
    }

    @Override
    public void delete(Integer id) throws DaoException {
        throw new DaoException("DELETE shipping is not supported");
    }

    @Override
    public Integer selectShippingIdByName(String name) throws DaoException {
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_SHIPPING_ID_BY_NAME)) {
            st.setString(1, name);
            ResultSet resultSet = st.executeQuery();
            resultSet.next();
            return resultSet.getInt("shp_id");
        } catch (SQLException | NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
    }

    @Override
    public List<Shipping> selectAllShippings() throws DaoException {
        List<Shipping> shippings = new ArrayList<>();
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_BY_ID)) {
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                Shipping shipping = new Shipping();
                shipping.setId(resultSet.getInt("shp_id"));
                shipping.setName(resultSet.getString("shp_name"));
                shipping.setPricePerKg(resultSet.getDouble("shp_pricePerKg"));
                shipping.setPricePerKm(resultSet.getDouble("shp_pricePerKm"));
                shippings.add(shipping);
            }
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
        return shippings;
    }
}
