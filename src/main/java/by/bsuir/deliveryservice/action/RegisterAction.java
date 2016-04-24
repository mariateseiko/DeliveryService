package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.impl.UserServiceImpl;
import com.opensymphony.xwork2.Action;

public class RegisterAction implements Action {
    private User user;
    private boolean result;
    private static UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute() throws Exception {
        try {
            if (userService.registerUser(user) != null) {
                result = true;
            } else {
                result = false;
            }
        } catch (ServiceException e) {
            return ERROR;
        }
        return SUCCESS;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
