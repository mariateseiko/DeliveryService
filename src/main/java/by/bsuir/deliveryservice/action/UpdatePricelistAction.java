package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.Shipping;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.ShippingService;
import by.bsuir.deliveryservice.service.impl.ShippingServiceImpl;
import com.opensymphony.xwork2.Action;

public class UpdatePricelistAction implements Action {
    private Shipping[] shippings;
    private boolean success = false;
    private ShippingService shippingService = ShippingServiceImpl.getInstance();

    @Override
    public String execute() throws Exception {
        try {
            for (Shipping shipping : shippings) {
                shippingService.updateShipping(shipping.getId(), shipping);
            }
            success = true;
            return SUCCESS;
        } catch (ServiceException e) {
            return ERROR;
        }
    }

    public void setShippings(Shipping[] shippings) {
        this.shippings = shippings;
    }

    public boolean isSuccess() {
        return success;
    }
}
