package by.bsuir.deliveryservice.controller.command;

import by.bsuir.deliveryservice.entity.UserRole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CommandName {
    LOGIN(UserRole.GUEST),
    REGISTER(UserRole.GUEST);
    private List<UserRole> allowedUsers = new ArrayList<>();
    CommandName(UserRole... allowedUsers) {
        this.allowedUsers.addAll(Arrays.asList(allowedUsers));
    }

    /**
     * Defines whether a user with it's specified role is allowed to perform the command
     * @param role user's role
     * @return true if user is allowed to perform the command
     */
    public boolean isRoleAllowed(UserRole role) {
        return allowedUsers.contains(role);
    }

}
