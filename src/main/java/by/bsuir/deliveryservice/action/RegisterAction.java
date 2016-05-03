package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.impl.UserServiceImpl;
import com.opensymphony.xwork2.Action;
import org.apache.log4j.Logger;

public class RegisterAction implements Action {
    public static final Logger LOG = Logger.getLogger(RegisterAction.class);
    private User user = new User();
    private boolean success;
    private static UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute() throws Exception {
        try {
            if (userService.registerUser(user) != null) {
                success = true;
            } else {
                success = false;
            }
        } catch (ServiceException e) {
            LOG.error(e.getStackTrace());
            return ERROR;
        }
        return SUCCESS;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setLogin(String login) {
        user.setLogin(login);
    }

    public void setPassword(String password) {
        user.setPassword(password);
    }

    public void setPhone(String phone) { user.setPhone(phone); }

    public void setPassport(String passport) { user.setPassport(passport); }

    public void setName(String fullName) { user.setFullName(fullName); }
}
