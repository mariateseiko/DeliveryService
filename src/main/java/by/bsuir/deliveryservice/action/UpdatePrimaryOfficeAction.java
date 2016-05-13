package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.Office;
import by.bsuir.deliveryservice.service.OfficeService;
import by.bsuir.deliveryservice.service.impl.OfficeServiceImpl;
import com.opensymphony.xwork2.Action;

public class UpdatePrimaryOfficeAction implements Action
{
    private Office office;

    private boolean success = false;

    // ----------------------------------------------------------------------

    private static final OfficeService officeService =
            OfficeServiceImpl.getInstance();

    // ----------------------------------------------------------------------

    @Override
    public String execute() throws Exception
    {
        try {
            officeService.updatePrimaryOffice(office);
            success = true;
        } catch (Exception e) {
            return ERROR;
        }

        return SUCCESS;
    }

    // ----------------------------------------------------------------------

    public void setOffice(Office office)
    {
        this.office = office;
    }

    public boolean isSuccess()
    {
        return success;
    }
}
