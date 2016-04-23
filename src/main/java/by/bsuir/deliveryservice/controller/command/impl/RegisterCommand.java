package by.bsuir.deliveryservice.controller.command.impl;

import by.bsuir.deliveryservice.controller.command.ActionCommand;
import by.bsuir.deliveryservice.controller.command.CommandException;
import static by.bsuir.deliveryservice.controller.util.ParameterName.*;

import by.bsuir.deliveryservice.controller.util.PagePath;
import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.manager.MessageManager;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.impl.UserServiceImpl;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterCommand implements ActionCommand {
    private static UserService userService = UserServiceImpl.getInstance();

    /**
     * Handles request to the servlet by trying to register a new user
     * @param request request from the servlet, containing user's login, password and email
     * @param response
     * @return path to the login page in case of success or to the registration page in case of invalid input
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String page;
        String login = request.getParameter(USER_LOGIN);
        String pass = request.getParameter(USER_PASSWORD);
        String phone = request.getParameter(USER_PHONE);
        User user = new User(login, pass, phone);
        MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(MESSAGE_MANAGER));
        JsonObject message = new JsonObject();
        //TODO validate
        try {
            if (userService.registerUser(user) != null) {
                message.addProperty(SUCCESS, messageManager.getProperty("user.register.success"));
                page = PagePath.LOGIN;
            } else {
                message.addProperty(ERROR, messageManager.getProperty("user.register.error"));
                page = PagePath.REGISTRATION;
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(message.toString());
        } catch (ServiceException|IOException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
