package by.bsuir.deliveryservice.service;

import by.bsuir.deliveryservice.entity.Shipping;

import java.util.List;

public interface ShippingService {
    void updateShipping(Integer shippingId, Shipping shipping) throws ServiceException;
    List<Shipping> viewPricelist() throws ServiceException;
}
