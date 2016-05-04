package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.Shipping;
import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.service.OrderService;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.impl.OrderServiceImpl;
import com.opensymphony.xwork2.Action;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class PlaceOrderAction implements Action {
    private Order order = new Order();
    private static final String USER = "user";
    private Boolean success;
    private static OrderService orderService = OrderServiceImpl.getInstance();

    @Override
    public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(USER);
        order.setPartner(new User(user.getId()));
        try {
            orderService.placeOrder(order);
            success = true;
        } catch (ServiceException e) {
            return ERROR;
        }
        return SUCCESS;
    }

    public void setWeight(Double weight) {
        order.setWeight(weight);
    }

    public void setDistance(Double distance) {
        order.setDistance(distance);
    }

    public void setFrom(String from) {
        order.setFrom(from);
    }

    public void setTo(String to) {
        order.setTo(to);
    }

    public void setShipping(String shipping) {
        order.setShipping(new Shipping(shipping));
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
