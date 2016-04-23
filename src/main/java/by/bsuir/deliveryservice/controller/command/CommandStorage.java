package by.bsuir.deliveryservice.controller.command;



import by.bsuir.deliveryservice.controller.command.impl.EmptyCommand;
import by.bsuir.deliveryservice.controller.command.impl.LoginCommand;
import by.bsuir.deliveryservice.controller.command.impl.RegisterCommand;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread-safe singleton for storing all possible system commands and providing them on demand.
 */
public class CommandStorage {
    private HashMap <CommandName, ActionCommand> commands = new HashMap<>();
    private static CommandStorage instance;
    private static AtomicBoolean isNull = new AtomicBoolean(true);
    private static ReentrantLock lock = new ReentrantLock();

    private CommandStorage(){
        commands.put(CommandName.LOGIN, new LoginCommand());
        commands.put(CommandName.REGISTER, new RegisterCommand());
    }

    public static CommandStorage getInstance() {
        if (isNull.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new CommandStorage();
                    isNull.set(false);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    /**
     * Returns a command for corresponding command parameter in a http-request
     * @param request http request from the servlet
     * @return corresponding action command or an empty command in case command is not specified
     * @throws CommandException if command parameter is invalid
     */
    public ActionCommand getCommand(HttpServletRequest request) throws CommandException {
        ActionCommand current = new EmptyCommand();
        String action = request.getParameter("command");
        if (action == null || action.isEmpty()) {
            return current;
        }
        try {
            CommandName commandName = CommandName.valueOf(action.toUpperCase());
            current = commands.get(commandName);
        } catch (IllegalArgumentException e) {
            throw new CommandException(e);
        }
        return current;
    }

    /**
     * Returns a command for corresponding string name
     * @param command command's name
     * @return orresponding action command or an empty command in case of null or empty string
     */
    public ActionCommand getCommand(String command){
        ActionCommand current = new EmptyCommand();
        if (command == null || command.isEmpty()) {
            return current;
        }
        CommandName commandName = CommandName.valueOf(command.toUpperCase());
        current = commands.get(commandName);
        return current;
    }
}
