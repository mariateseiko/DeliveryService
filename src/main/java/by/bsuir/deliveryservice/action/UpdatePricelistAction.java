package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.Shipping;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.ShippingService;
import by.bsuir.deliveryservice.service.impl.ShippingServiceImpl;
import com.opensymphony.xwork2.Action;

import java.util.List;

public class UpdatePricelistAction implements Action {
    private Shipping shipping = new Shipping();
    private boolean success = false;
    private ShippingService shippingService = ShippingServiceImpl.getInstance();

    @Override
    public String execute() throws Exception {
        try {
            shippingService.updateShipping(shipping.getId(), shipping);
            success = true;
            return SUCCESS;
        } catch (ServiceException e) {
            return ERROR;
        }
    }

    public void setId(Long id) {
        shipping.setId(id);
    }

    public void setName(String name) {
        shipping.setName(name);
    }

    public void setPricePerKg(Double pricePerKg) {
        shipping.setPricePerKg(pricePerKg);
    }

    public void setPricePerKm(Double pricePerKm) {
        shipping.setPricePerKm(pricePerKm);
    }

    public boolean isSuccess() {
        return success;
    }
}
