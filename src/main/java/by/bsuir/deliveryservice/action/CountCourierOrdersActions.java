package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.impl.UserServiceImpl;
import com.opensymphony.xwork2.*;

import java.util.List;
import by.bsuir.deliveryservice.service.OrderService;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.impl.OrderServiceImpl;
import com.opensymphony.xwork2.Action;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CountCourierOrdersActions implements com.opensymphony.xwork2.Action {
    private static final String USER = "user";
    private int countOrderCouriers;
    private static OrderService orderService = OrderServiceImpl.getInstance();

    @Override
    public String execute() throws Exception {
        long courierId;
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(USER);
        courierId = user.getId();
        try {
            countOrderCouriers = orderService.selectCountCourierOrders(courierId);
        } catch(ServiceException e) {
            return ERROR;
        }
        return SUCCESS;
    }

    public int getCountOrderCouriers() {
        return countOrderCouriers;
    }
}



