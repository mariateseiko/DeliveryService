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
import by.bsuir.deliveryservice.dao.OfficeDao;
import by.bsuir.deliveryservice.entity.Office;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The data access object (DAO) implementation for the OFFICE entity.
 */

public class OfficeDaoImpl implements OfficeDao
{
    private static final String SELECT_BY_ID_QUERY =
            "SELECT * FROM `office` WHERE off_ID=?";

    private static final String SQL_UPDATE_OFFICE =
            "UPDATE `office` " +
                    "SET off_Name=?, off_Credentials=? " +
                    "WHERE off_ID=?";

    // -----------------------------------------------------------------------

    /**
     * Selects a primary office, that is put into the field "Executor" of a
     * document.
     *
     * @return
     *     the office or {@code null}, if the office with ID 1 is not exists
     *     in a database.
     *
     * @throws DaoException
     *     if dniwe.
     */
    @Override
    public Office getPrimary() throws DaoException
    {
        return selectById(1L);
    }

    @Override
    public Long insert(Office entity) throws DaoException
    {
        throw new DaoException("INSERT not supported");
    }

    @Override
    public Office selectById(Long id) throws DaoException
    {
        Office office = null;

        try (Connection c = provideConnection();
             PreparedStatement stm = c.prepareStatement(SELECT_BY_ID_QUERY)) {

            stm.setLong(1, id);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {

                office = new Office();

                office.setId(id);
                office.setName(rs.getString("off_Name"));
                office.setCredentials(rs.getString("off_Credentials"));

            }

        } catch (SQLException | NamingException e) {
            throw new DaoException("failed to select a record " +
                    "with ID=" + id, e);
        }

        return office;
    }

    @Override
    public void update(Long id, Office entity) throws DaoException
    {
        try (Connection c = provideConnection();
             PreparedStatement stm = c.prepareStatement(SQL_UPDATE_OFFICE)) {

            stm.setString(1, entity.getName());
            stm.setString(2, entity.getCredentials());
            stm.setLong(3, id);

            stm.executeUpdate();

        } catch (SQLException | NamingException e) {
            throw new DaoException("failed to update 'OFFICE' with " +
                    "ID=" + id, e);
        }
    }

    // -----------------------------------------------------------------------

    private OfficeDaoImpl() {}

    private static final OfficeDao singleton = new OfficeDaoImpl();

    public  static OfficeDao getSingleton()
    {
        return singleton;
    }
}
