/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.service;

import java.io.File;

public interface OrderListExportService
{
    File exportToFile(DocFormat format, Long courierId)
            throws ServiceException;
}
