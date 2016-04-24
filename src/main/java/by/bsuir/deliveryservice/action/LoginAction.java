package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.Message;
import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.manager.MessageManager;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.impl.UserServiceImpl;
import com.opensymphony.xwork2.Action;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.bsuir.deliveryservice.controller.util.ParameterName.*;

public class LoginAction implements Action {
    private User user;
    private Message message;
    private static UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute() throws Exception {
        try {
            if ((user = userService.loginUser(user) )!= null) {
                HttpServletRequest request = ServletActionContext.getRequest();
                HttpSession session = request.getSession();
                session.setAttribute(USER, user);
                return SUCCESS;
            } else {
                message = new Message();
                MessageManager messageManager =
                        (MessageManager)(ServletActionContext.getRequest().getSession().getAttribute(MESSAGE_MANAGER));
                message.setErrorMessage(messageManager.getProperty("user.invalid.login"));
                return ERROR;
            }
        } catch (ServiceException e) {
            //TODO
            return ERROR;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
