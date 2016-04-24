package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.Message;
import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.manager.MessageManager;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.impl.UserServiceImpl;
import com.opensymphony.xwork2.Action;
import org.apache.struts2.ServletActionContext;

import static by.bsuir.deliveryservice.controller.util.ParameterName.MESSAGE_MANAGER;

public class RegisterAction implements Action {
    private User user;
    private Message message = new Message();
    private static UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute() throws Exception {
        MessageManager messageManager = (MessageManager)(ServletActionContext.getRequest()
                .getSession().getAttribute(MESSAGE_MANAGER));
        try {
            if (userService.registerUser(user) != null) {
                message.setSuccessMessage(messageManager.getProperty("user.register.success"));
                return SUCCESS;
            } else {
                message.setErrorMessage(messageManager.getProperty("user.register.error"));
                return ERROR;
            }
        } catch (ServiceException e) {
            //TODO
            return ERROR;        }
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
