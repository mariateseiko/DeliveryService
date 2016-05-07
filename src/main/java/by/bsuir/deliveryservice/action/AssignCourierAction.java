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

public class AssignCourierAction implements Action {
    private long courierId;
    private long orderId;
    private Boolean success;
    private static OrderService orderService = OrderServiceImpl.getInstance();

    @Override
    public String execute() throws Exception {
        try {
            orderService.assignCourier(orderId, courierId);
            success = true;
        } catch (ServiceException e) {
            return ERROR;
        }
        return SUCCESS;
    }


    public void setCid(long courierId) {
        this.courierId = courierId;
    }

    public void setOid(long orderId) {
        this.orderId = orderId;
    }

    public Boolean getSuccess() {
        return success;
    }
}
