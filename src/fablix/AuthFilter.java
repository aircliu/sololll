package fablix;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthFilter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        HttpServletResponse s = (HttpServletResponse) res;
        String path = r.getServletPath();
        
        System.out.println("AuthFilter processing path: " + path);
        
        // Check login status
        boolean loggedIn = r.getSession(false) != null &&
                r.getSession(false).getAttribute("user") != null;
        boolean isEmployee = r.getSession(false) != null &&
                r.getSession(false).getAttribute("employee") != null;
        
        // Allow access to login pages and public resources
        boolean publicPath = path.equals("/login") || 
                path.equals("/login.html") || 
                path.equals("/login.js") ||
                path.equals("/api/login") || 
                path.startsWith("/css") || 
                path.startsWith("/images") ||
                path.equals("/style.css") || 
                path.contains("fonts.googleapis.com") ||
                // New files without underscores
                path.equals("/empdashboard.html") || 
                path.equals("/empdashboard.js") || 
                path.equals("/api/employee-login");
        
        // Allow employee access to dashboard pages if logged in as employee
        boolean employeePath = (path.equals("/employeedashboard.html") || 
                        path.equals("/employeedashboard.js") ||
                        path.startsWith("/api/add-") || 
                        path.equals("/api/metadata")) && isEmployee;
        
        // Determine if access should be allowed
        if (path.startsWith("/employeedashboard") || 
    path.startsWith("/api/add-") || 
    path.equals("/api/metadata")) {
    s.sendRedirect(r.getContextPath() + "/empdashboard.html");
} else {
    s.sendRedirect(r.getContextPath() + "/login.html");
}
    
    @Override
    public void destroy() {
        System.out.println("AuthFilter destroyed");
    }
}