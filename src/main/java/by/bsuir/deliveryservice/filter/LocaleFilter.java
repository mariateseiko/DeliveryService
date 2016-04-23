package by.bsuir.deliveryservice.filter;


import by.bsuir.deliveryservice.manager.MessageManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filters all incoming requests and check whether {@code MessageManager} for retrieving server-side messages is present
 * in the session. If not, adds it to the session as an attribute depending on the request's locale.
 */
@WebFilter(urlPatterns = {"/*"})
public class LocaleFilter implements Filter {
    private static final String ATTR_MESSAGE_MANAGER = "messageManager";
    private static final String ATTR_LOCALE = "locale";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpSession session = request.getSession();
        if (session.getAttribute(ATTR_MESSAGE_MANAGER) == null) {
            MessageManager messageManager;
            String locale = "en_US";
            switch(request.getLocale().getLanguage()) {
                case "en":
                    messageManager = MessageManager.EN;
                    break;
                case "ru":
                    messageManager = MessageManager.RU;
                    locale = "ru_RU";
                    break;
                default:
                    messageManager = MessageManager.EN;
            }
            session.setAttribute(ATTR_MESSAGE_MANAGER, messageManager);
            session.setAttribute(ATTR_LOCALE, locale);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
