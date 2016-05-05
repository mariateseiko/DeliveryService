package by.bsuir.deliveryservice.service;

import java.io.File;
import java.util.Date;

public interface FinanceReportExportService
{
    File exportToFile(DocFormat format, Date from) throws ServiceException;
}
