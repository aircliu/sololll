package fablix;

import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import org.jasypt.util.password.StrongPasswordEncryptor;

@WebServlet(name = "EmployeeLoginServlet", urlPatterns = "/api/employee-login")
public class EmployeeLoginServlet extends HttpServlet {
    private static final String URL  = "jdbc:mysql://localhost:3306/moviedb";
    private static final String USER = "mytestuser";
    private static final String PASS = "My6$Password";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        JsonObject outJson = new JsonObject();
        
        // Get reCAPTCHA response from the form
        String gRecaptchaResponse = req.getParameter("g-recaptcha-response");
        
        // Verify reCAPTCHA
        try {
            RecaptchaVerifyUtils.verify(gRecaptchaResponse);
        } catch (Exception e) {
            outJson.addProperty("success", false);
            outJson.addProperty("message", "Please complete the reCAPTCHA verification.");
            out.write(outJson.toString());
            return;
        }
        
        // If reCAPTCHA is verified, proceed with login
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection c = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = c.prepareStatement(
                         "SELECT fullname, password FROM employees WHERE email=?")) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    // Get the encrypted password from the database
                    String encryptedPassword = rs.getString("password");
                    
                    // Use StrongPasswordEncryptor to check if the password matches
                    StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
                    
                    // Verify the provided password against the encrypted password
                    if (encryptor.checkPassword(password, encryptedPassword)) {
                        // Password is correct
                        HttpSession s = req.getSession(true);
                        s.setAttribute("employee", rs.getString("fullname"));
                        s.setAttribute("email", email);
                        outJson.addProperty("success", true);
                    } else {
                        // Password is incorrect
                        outJson.addProperty("success", false);
                        outJson.addProperty("message", "Invalid password.");
                    }
                } else {
                    // Email not found
                    outJson.addProperty("success", false);
                    outJson.addProperty("message", "Invalid email.");
                }
            }
            out.write(outJson.toString());
        } catch (Exception e) {
            outJson.addProperty("success", false);
            outJson.addProperty("message", "Server error: " + e.getMessage());
            out.write(outJson.toString());
            out.close();
        }
    }
}