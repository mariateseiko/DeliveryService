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

public class ViewUserProfileAction implements Action {
    public static final Logger LOG =Logger.getLogger(LoginAction.class);
    private User user;

    private Long userId;
    private static UserService userService = UserServiceImpl.getInstance();
    private static final String USER = "user";

    @Override
    public String execute() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        if (request.getParameter("userId") != null) {
            userId = Long.parseLong(request.getParameter("userId"));
        }
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute(USER);
        userId = sessionUser.getId();
        try {
            user = userService.viewUser(userId);
        } catch (ServiceException e) {
            LOG.error(e.getStackTrace());
            return ERROR;
        }
        return SUCCESS;
    }

    public User getUser() {
        return user;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserId(String userId) {
        this.userId = Long.parseLong(userId);
    }
}
