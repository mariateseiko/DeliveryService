package by.bsuir.deliveryservice.controller.command.impl;

import by.bsuir.deliveryservice.controller.command.ActionCommand;
import by.bsuir.deliveryservice.controller.command.CommandException;
import by.bsuir.deliveryservice.controller.util.ParameterName;
import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class RegisterCommand implements ActionCommand {
    private static UserService userService = UserServiceImpl.getInstance();

    /**
     * Handles request to the servlet by trying to register a new user
     * @param request request from the servlet, containing user's login, password and email
     * @return path to the login page in case of success or to the registration page in case of invalid input
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page = null;
        String login = request.getParameter(ParameterName.USER_LOGIN);
        String pass = request.getParameter(ParameterName.USER_PASSWORD);
        String email = request.getParameter(ParameterName.USER_EMAIL);
        User user = new User(login, pass, email);
        //TODO validate
        try {
            if (userService.registerUser(login, pass, email) != null) {
                //TODO add message and redirect to login

            } else {
                //TODO add message and redirect to login

            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }

        return page;
    }
}
