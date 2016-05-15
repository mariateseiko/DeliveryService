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
    public Long insert(Shipping shipping) throws DaoException {
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
        return Long.valueOf(result);
    }

    @Override
    public Shipping selectById(Long shippingId) throws DaoException {
        Shipping shipping = null;
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_BY_ID)) {
            st.setLong(1, shippingId);
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
    public void update(Long shippingId, Shipping shipping) throws DaoException {
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(UPDATE_SHIPPING)) {
            st.setString(1, shipping.getName());
            st.setDouble(2, shipping.getPricePerKg());
            st.setDouble(3, shipping.getPricePerKm());
            st.setLong(4, shippingId);
            st.executeUpdate();
        } catch (SQLException|NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
    }

    @Override
    public Long selectShippingIdByName(String name) throws DaoException {
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_SHIPPING_ID_BY_NAME)) {
            st.setString(1, name);
            ResultSet resultSet = st.executeQuery();
            resultSet.next();
            return resultSet.getLong("shp_id");
        } catch (SQLException | NamingException e) {
            throw new DaoException("Request to database failed", e);
        }
    }

    @Override
    public List<Shipping> selectAllShippings() throws DaoException {
        List<Shipping> shippings = new ArrayList<>();
        try (Connection cn = provideConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_ALL_SHIPPINGS)) {
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
