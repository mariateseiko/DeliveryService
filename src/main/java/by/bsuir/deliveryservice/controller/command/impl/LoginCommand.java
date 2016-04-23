package by.bsuir.deliveryservice.controller.command.impl;

import by.bsuir.deliveryservice.controller.command.ActionCommand;
import by.bsuir.deliveryservice.controller.command.CommandException;
import by.bsuir.deliveryservice.controller.util.PagePath;
import by.bsuir.deliveryservice.controller.util.ParameterName;
import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginCommand implements ActionCommand {
    private static UserService userService = UserServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page = null;
        String login = request.getParameter(ParameterName.USER_LOGIN);
        String pass = request.getParameter(ParameterName.USER_PASSWORD);
        User user;
        try {
            if ((user = userService.loginUser(login, pass) )!= null) {
                HttpSession session = request.getSession();
                session.setAttribute(ParameterName.USER, user);
                page = PagePath.INDEX;

            } else {
                //TODO
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
