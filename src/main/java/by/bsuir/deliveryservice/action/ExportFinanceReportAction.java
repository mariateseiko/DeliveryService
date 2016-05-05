package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.service.DocFormat;
import by.bsuir.deliveryservice.service.FinanceReportExportService;
import by.bsuir.deliveryservice.service.impl.export.FinanceReportServiceFactory;

import java.io.File;
import java.util.Date;

public class ExportFinanceReportAction extends DocExportAction
{
    /*
     * The beginning of the period.
     */
    private Date sinceDate;

    /*
     * The service instance.
     */
    private static final FinanceReportExportService service =
            FinanceReportServiceFactory.getService();

    // ----------------------------------------------------------------------

    @Override
    public String execute() throws Exception
    {
        String docType = this.getDocType();

        File file = service.exportToFile(DocFormat.valueOf(docType),
                sinceDate);
        setFileToDownload(file);

        return super.execute();
    }

    // ----------------------------------------------------------------------

    public Date getSinceDate()
    {
        return sinceDate;
    }

    public void setSinceDate(Date sinceDate)
    {
        this.sinceDate = sinceDate;
    }
}
