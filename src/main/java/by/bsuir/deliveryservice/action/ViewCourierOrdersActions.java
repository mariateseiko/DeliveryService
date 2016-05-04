package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.Order;
import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.service.OrderService;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.impl.OrderServiceImpl;
import com.opensymphony.xwork2.Action;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ViewCourierOrdersActions implements Action {
    private static final String USER = "user";
    private List<Order> orders;
    private static OrderService orderService = OrderServiceImpl.getInstance();

    @Override
    public String execute() throws Exception {
        long courierId;
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(USER);
        courierId = user.getId();
        try {
            orders = orderService.viewCourierOrders(courierId);
        } catch(ServiceException e) {
            return ERROR;
        }
        return SUCCESS;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
