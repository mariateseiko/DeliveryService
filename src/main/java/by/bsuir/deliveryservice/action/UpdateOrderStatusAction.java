package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.OrderStatus;
import by.bsuir.deliveryservice.service.OrderService;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.impl.OrderServiceImpl;
import com.opensymphony.xwork2.Action;

public class UpdateOrderStatusAction implements Action {
    private Long orderId;
    private OrderStatus status;
    private Boolean success;
    private static OrderService orderService = OrderServiceImpl.getInstance();

    @Override
    public String execute() throws Exception {
        try {
            orderService.updateOrderStatus(orderId, status);
            success = true;
        } catch (ServiceException e) {
            return ERROR;
        }
        return SUCCESS;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setStatus(String statusString) {
        status = OrderStatus.valueOf(statusString.toUpperCase());
    }

    public Boolean getSuccess() {
        return success;
    }
}
