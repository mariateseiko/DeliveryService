/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.service;

import by.bsuir.deliveryservice.dao.OrderDao;
import by.bsuir.deliveryservice.dao.ShippingDao;
import by.bsuir.deliveryservice.dao.UserDao;
import by.bsuir.deliveryservice.dao.impl.OrderDaoImpl;
import by.bsuir.deliveryservice.dao.impl.ShippingDaoImpl;
import by.bsuir.deliveryservice.dao.impl.UserDaoImpl;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

import java.io.File;
import java.io.IOException;

public abstract class AbstractExportService
{
    protected final OrderDao orderDao = OrderDaoImpl.getInstance();
    protected final ShippingDao shippingDao = ShippingDaoImpl.getInstance();
    protected final UserDao userDao = UserDaoImpl.getInstance();

    // -----------------------------------------------------------------------

    /**
     * Returns a temporary file for service needs.
     *
     * @param prefix
     *     specifies the prefix of a filename.
     *
     * @param fileExt
     *     the extension of a filename without leading "." (dot).
     *
     * @return
     *     the temporary file.
     *
     * @throws IOException
     *     if an I/O error has occured. See the description.
     */
    protected static File getTemporaryFile(String prefix, String fileExt)
            throws IOException
    {
        File tmpFile = File.createTempFile(prefix + '-', null);
        return new File(tmpFile.getCanonicalPath() + '.' + fileExt);
    }

    // -----------------------------------------------------------------------

    protected static BaseFont newTimesNewRomanBaseFont()
            throws IOException, DocumentException
    {
        return BaseFont.createFont("C:\\Windows\\Fonts\\times.ttf",
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    }
}
