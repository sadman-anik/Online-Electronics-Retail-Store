package ebusiness.filter;

import ebusiness.controller.AuthenticationController;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {

    @Inject
    private AuthenticationController session;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String url = req.getRequestURI();
        String context = req.getServletContext().getContextPath();

        boolean publicPage = url.endsWith("index.html") || url.endsWith("index.xhtml") || url.endsWith("login.xhtml")
                || url.endsWith("emailVerification.xhtml") || url.endsWith("register.xhtml")
                || url.endsWith("emailRecovery.xhtml") || url.endsWith("userRecovery.xhtml")
                || url.contains("/jakarta.faces.resource/") || url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png");

        boolean loggedIn = session != null && session.isLogged();

        if (!loggedIn && !publicPage) {
            resp.sendRedirect(context + "/login.xhtml");
        } else if (loggedIn && (url.endsWith("login.xhtml") || url.endsWith("register.xhtml") || url.endsWith("emailVerification.xhtml") || url.endsWith("emailRecovery.xhtml") || url.endsWith("userRecovery.xhtml"))) {
            resp.sendRedirect(context + "/default.xhtml");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() { }
}
