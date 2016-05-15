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

package by.bsuir.deliveryservice.action;

import by.bsuir.deliveryservice.entity.User;
import by.bsuir.deliveryservice.service.ServiceException;
import by.bsuir.deliveryservice.service.UserService;
import by.bsuir.deliveryservice.service.impl.UserServiceImpl;
import com.opensymphony.xwork2.Action;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginAction implements Action {
    public static final Logger LOG =Logger.getLogger(LoginAction.class);
    private User user = new User();
    private static UserService userService = UserServiceImpl.getInstance();
    private static final String USER = "user";
    
    @Override
    public String execute() throws Exception {
        try {
            if ((user = userService.loginUser(user) )!= null) {
                HttpServletRequest request = ServletActionContext.getRequest();
                HttpSession session = request.getSession();
                session.setAttribute(USER, user);
            }
        } catch (ServiceException e) {
            LOG.error(e.getStackTrace());
            return ERROR;
        }
        return SUCCESS;
    }

    public void setLogin(String login) {
        user.setLogin(login);
    }

    public void setPassword(String password) { user.setPassword(password); }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
