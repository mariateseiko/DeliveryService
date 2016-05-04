package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.service.OrderService;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.impl.OrderServiceImpl;
import com.opensymphony.xwork2.Action;

import java.util.List;

public class ViewOrdersAction implements Action {
    private List<Order> orders;
    private static OrderService orderService = OrderServiceImpl.getInstance();

    @Override
    public String execute() throws Exception {
        try {
            orders = orderService.viewOrders();
        } catch(ServiceException e) {
            return ERROR;
        }
        return SUCCESS;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
