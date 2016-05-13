package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.service.DocFormat;
import by.bsuir.deliveryservice.service.FinanceReportExportService;
import by.bsuir.deliveryservice.service.impl.export.FinanceReportServiceFactory;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

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

        DocFormat docFormat;

        try {
            docFormat = DocFormat.valueOf(docType);
        } catch (Exception e) {
            throw new IllegalArgumentException("unknown file format", e);
        }

        File file = service.exportToFile(docFormat, sinceDate);
        setFileToDownload(file);

        return super.execute();
    }

    // ----------------------------------------------------------------------

    public Date getSinceDate()
    {
        return sinceDate;
    }

    @TypeConversion(converter = "by.bsuir.deliveryservice.action.util.StringToDateConverter")
    public void setSinceDate(Date sinceDate)
    {
        this.sinceDate = sinceDate;
    }
}
