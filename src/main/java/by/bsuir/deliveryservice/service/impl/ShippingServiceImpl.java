package by.bsuir.deliveryservice.service.impl;

import by.bsuir.deliveryservice.dao.DaoException;
import by.bsuir.deliveryservice.dao.ShippingDao;
import by.bsuir.deliveryservice.dao.impl.ShippingDaoImpl;
import by.bsuir.deliveryservice.entity.Shipping;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.ShippingService;

import java.util.List;

public class ShippingServiceImpl implements ShippingService {
    private final ShippingDao shippingDao = ShippingDaoImpl.getInstance();;
    private static ShippingService instance = new ShippingServiceImpl();

    public static ShippingService getInstance() { return instance; }
    private ShippingServiceImpl() { }
    @Override
    public void updateShipping(Integer shippingId, Shipping shipping) throws ServiceException {
        try {
            shippingDao.update(shippingId, shipping);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Shipping> viewPricelist() throws ServiceException {
        List<Shipping> shippings = null;
        try {
            shippings = shippingDao.selectAllShippings();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return shippings;
    }
}
