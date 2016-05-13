package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.Shipping;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.ShippingService;
import by.bsuir.deliveryservice.service.impl.ShippingServiceImpl;
import com.opensymphony.xwork2.Action;

import java.util.List;

/**
 * Created by Анастасия on 10.05.2016.
 */
public class UpdateShippingAction implements Action {
    private List<Shipping> shippings;
    private static ShippingService shippingService = ShippingServiceImpl.getInstance();
    @Override
    public String execute() throws Exception {
        try {
            shippings = shippingService.viewPricelist();
            return SUCCESS;
        } catch (ServiceException e) {
            return ERROR;
        }
    }

    public List<Shipping> getShippings() {
        return shippings;
    }
}
