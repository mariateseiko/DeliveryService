package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.impl.UserServiceImpl;
import com.opensymphony.xwork2.Action;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UpdateProfileAction implements Action {
    private User user = new User();
    private static UserService userService = UserServiceImpl.getInstance();
    private static final String USER = "user";
    private Boolean success;

    @Override
    public String execute() throws Exception {
        long userId;
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        userId = ((User)session.getAttribute(USER)).getId();
        user.setId(userId);
        try {
            userService.updateUser(user);
            success = false;
        } catch (ServiceException e) {
            return ERROR;
        }
        return SUCCESS;
    }

    public void setName(String name) {
        user.setFullName(name);
    }

    public void setPhone(String phone) { user.setPhone(phone); }

    public void setPassport(String passport) { user.setPassport(passport); }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getSuccess() {
        return success;
    }
}
