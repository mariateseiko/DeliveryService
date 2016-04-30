package by.bsuir.deliveryservice.dao;

import by.bsuir.deliveryservice.entity.Office;

public interface OfficeDao extends GenericDao<Long, Office>
{
    /**
     * Returns the primary office of a delivery service company.
     *
     * @return
     *     the office.
     */
    Office getPrimary() throws DaoException;
}
