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
import java.util.Locale;

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
        String urlLower = url.toLowerCase(Locale.ROOT);
        String context = req.getServletContext().getContextPath();

        // Public pages and static resources must stay reachable before login.
        boolean publicPage = urlLower.endsWith("index.html") || urlLower.endsWith("index.xhtml") || urlLower.endsWith("login.xhtml")
                || urlLower.endsWith("emailverification.xhtml") || urlLower.endsWith("register.xhtml")
                || urlLower.endsWith("emailrecovery.xhtml") || urlLower.endsWith("userrecovery.xhtml")
                || urlLower.contains("/jakarta.faces.resource/") || hasStaticExtension(urlLower);

        boolean loggedIn = session != null && session.isLogged();

        // Keep unauthenticated users out of application pages and logged-in users out of auth forms.
        if (!loggedIn && !publicPage) {
            resp.sendRedirect(context + "/login.xhtml");
        } else if (loggedIn && (urlLower.endsWith("login.xhtml") || urlLower.endsWith("register.xhtml") || urlLower.endsWith("emailverification.xhtml") || urlLower.endsWith("emailrecovery.xhtml") || urlLower.endsWith("userrecovery.xhtml"))) {
            resp.sendRedirect(context + "/default.xhtml");
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean hasStaticExtension(String urlLower) {
        return urlLower.endsWith(".css")
                || urlLower.endsWith(".js")
                || urlLower.endsWith(".png")
                || urlLower.endsWith(".jpg")
                || urlLower.endsWith(".jpeg")
                || urlLower.endsWith(".gif")
                || urlLower.endsWith(".svg")
                || urlLower.endsWith(".ico")
                || urlLower.endsWith(".woff")
                || urlLower.endsWith(".woff2")
                || urlLower.endsWith(".ttf");
    }

    @Override
    public void destroy() { }
}
