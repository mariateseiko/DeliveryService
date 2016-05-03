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
            throw new DaoException("failed to execute a statement", e);
        }

        return office;
    }

    @Override
    public void update(Long id, Office entity) throws DaoException
    {
        throw new DaoException("UPDATE not supported");
    }

    // -----------------------------------------------------------------------

    private OfficeDaoImpl() {}

    private static final OfficeDao singleton = new OfficeDaoImpl();

    public  static OfficeDao getSingleton()
    {
        return singleton;
    }
}
