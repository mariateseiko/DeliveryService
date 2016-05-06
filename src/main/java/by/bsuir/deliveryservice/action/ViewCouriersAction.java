package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.impl.UserServiceImpl;
import com.opensymphony.xwork2.Action;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ViewCouriersAction implements Action{
    private List<User> couriers;
    private static UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute() throws Exception {
        try {
            couriers = userService.viewCouriers();
        } catch(ServiceException e) {
            return ERROR;
        }
        return SUCCESS;
    }

    public List<User> getCouriers() {
        return couriers;
    }
}
