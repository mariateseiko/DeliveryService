package by.bsuir.deliveryservice.service;

import by.bsuir.deliveryservice.entity.Office;

public interface OfficeService
{
    Office getPrimaryOffice() throws ServiceException;
    void updatePrimaryOffice(Office office) throws ServiceException;
}
