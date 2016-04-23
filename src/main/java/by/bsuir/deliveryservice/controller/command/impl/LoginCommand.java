package by.bsuir.deliveryservice.controller.command.impl;

import static by.bsuir.deliveryservice.controller.util.ParameterName.*;

import by.bsuir.deliveryservice.controller.command.ActionCommand;
import by.bsuir.deliveryservice.controller.command.CommandException;
import by.bsuir.deliveryservice.controller.util.PagePath;
import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.manager.MessageManager;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.impl.UserServiceImpl;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCommand implements ActionCommand {
    private static UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String page;
        String login = request.getParameter(USER_LOGIN);
        String pass = request.getParameter(USER_PASSWORD);
        MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(MESSAGE_MANAGER));
        User user;
        try {
            if ((user = userService.loginUser(login, pass) )!= null) {
                HttpSession session = request.getSession();
                session.setAttribute(USER, user);
                page = PagePath.INDEX;
            } else {
                JsonObject message = new JsonObject();
                message.addProperty(ERROR, messageManager.getProperty("user.invalid.login"));
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(message.toString());
                page = PagePath.LOGIN;
            }
        } catch (ServiceException| IOException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
