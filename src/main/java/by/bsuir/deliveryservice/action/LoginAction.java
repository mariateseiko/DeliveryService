package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.impl.UserServiceImpl;
import com.opensymphony.xwork2.Action;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginAction implements Action {
    private User user;
    private static UserService userService = UserServiceImpl.getInstance();
    private static final String USER = "user";
    @Override
    public String execute() throws Exception {
        try {
            if ((user = userService.loginUser(user) )!= null) {
                HttpServletRequest request = ServletActionContext.getRequest();
                HttpSession session = request.getSession();
                session.setAttribute(USER, user);
            }
        } catch (ServiceException e) {
            return ERROR;
        }
        return SUCCESS;
    }

    public void setEmail(String email) {
        user.setEmail(email);
    }

    public void setPassword(String password) { user.setPassword(password); }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
