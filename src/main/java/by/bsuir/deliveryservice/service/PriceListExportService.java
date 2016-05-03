/*
 * Copyright © 2016, Andrew Grivachevsky
 * All rights reserved.
 */

package by.bsuir.deliveryservice.service;

import java.io.File;

public interface PriceListExportService
{
    File exportToFile(DocFormat format) throws ServiceException;
}
