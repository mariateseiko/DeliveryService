package by.bsuir.deliveryservice.service.impl;

import by.bsuir.deliveryservice.dao.OfficeDao;
import by.bsuir.deliveryservice.dao.impl.OfficeDaoImpl;
import by.bsuir.deliveryservice.entity.Office;
import by.bsuir.deliveryservice.service.OfficeService;
import by.bsuir.deliveryservice.service.ServiceException;

public class OfficeServiceImpl implements OfficeService
{
    private OfficeServiceImpl() {}

    private static final OfficeService instance = new OfficeServiceImpl();

    public  static OfficeService getInstance()
    {
        return instance;
    }

    // -----------------------------------------------------------------------

    private final OfficeDao officeDao = OfficeDaoImpl.getSingleton();

    // -----------------------------------------------------------------------

    @Override
    public Office getPrimaryOffice() throws ServiceException
    {
        try {
            return officeDao.getPrimary();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updatePrimaryOffice(Office office) throws ServiceException
    {
        try {
            officeDao.update((long) 1, office);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
