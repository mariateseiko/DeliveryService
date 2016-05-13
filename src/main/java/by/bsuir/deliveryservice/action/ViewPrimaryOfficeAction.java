package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.Office;
import by.bsuir.deliveryservice.service.OfficeService;
import by.bsuir.deliveryservice.service.impl.OfficeServiceImpl;
import com.opensymphony.xwork2.Action;

public class ViewPrimaryOfficeAction implements Action
{
    private Office office;

    // ----------------------------------------------------------------------

    private static final OfficeService officeService =
            OfficeServiceImpl.getInstance();

    // ----------------------------------------------------------------------

    @Override
    public String execute() throws Exception
    {
        try {
            office = officeService.getPrimaryOffice();
        } catch (Exception e) {
            return ERROR;
        }

        return SUCCESS;
    }

    // ----------------------------------------------------------------------

    public Office getOffice()
    {
        return office;
    }
}
