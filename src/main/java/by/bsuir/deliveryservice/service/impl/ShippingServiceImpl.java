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
    public void updateShipping(Long shippingId, Shipping shipping) throws ServiceException {
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
