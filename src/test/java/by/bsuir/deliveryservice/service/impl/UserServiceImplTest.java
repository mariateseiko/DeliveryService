package by.bsuir.deliveryservice.service.impl;

import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.entity.UserRole;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.util.Hasher;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserServiceImplTest extends ServiceTest {
    private final UserService userService = UserServiceImpl.getInstance();

    @Test
    public void selectExistingUser() throws ServiceException {
        long userId = 1l;
        User user = userService.viewUser(userId);
        assertNotNull(user);
        assertEquals(userId, user.getId());
    }

    @Test
    public void selectNonexistentUser() throws ServiceException {
        long userId = 0;
        User user = userService.viewUser(userId);
        assertNull(user);
    }

    @Test
    public void validSignIn() throws ServiceException {
        User user = new User("test",
                "test", "test", "test", "test");
        userService.registerUser(user);
        user = userService.loginUser(new User("test", "test"));
        assertNotNull(user);
        assertEquals(user.getRole(), UserRole.CLIENT);
    }

    @Test
    public void invalidSignIn() throws ServiceException {
        User user = new User("test", Hasher.md5Hash("test11"), "test", "test", "test");
        user = userService.loginUser(user);
        assertNull(user);
    }

    @Test
    public void selectUsers() throws ServiceException {
        List<User> users = userService.viewCouriers();
        assertNotNull(users);
    }
}
