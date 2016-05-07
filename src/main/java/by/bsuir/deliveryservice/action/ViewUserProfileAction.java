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

public class ViewUserProfileAction implements Action {
    public static final Logger LOG =Logger.getLogger(LoginAction.class);
    private User user;

    private Long userId;
    private static UserService userService = UserServiceImpl.getInstance();
    private static final String USER = "user";

    @Override
    public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        if (request.getParameter("userId") != null) {
            userId = Long.parseLong(request.getParameter("userId"));
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER);
        userId = user.getId();

        try {
            user = userService.viewUser(userId);
        } catch (ServiceException e) {
            LOG.error(e.getStackTrace());
            return ERROR;
        }
        return SUCCESS;
    }

    public User getUser() {
        return user;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserId(String userId) {
        this.userId = Long.parseLong(userId);
    }
}
