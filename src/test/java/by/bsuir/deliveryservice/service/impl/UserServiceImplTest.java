/*
 * Copyright (c) 2016, Andrew Grivachevsky, Anastasiya Kostyukova,
 * Maria Teseiko
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
