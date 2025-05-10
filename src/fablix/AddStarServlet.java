package fablix;

import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "AddStarServlet", urlPatterns = "/api/add-star")
public class AddStarServlet extends HttpServlet {
    private static final String URL  = "jdbc:mysql://localhost:3306/moviedb";
    private static final String USER = "mytestuser";
    private static final String PASS = "My6$Password";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JsonObject responseJson = new JsonObject();
        
        // Check if user is logged in as employee
        HttpSession session = req.getSession();
        if (session.getAttribute("employee") == null) {
            responseJson.addProperty("success", false);
            responseJson.addProperty("message", "Not authorized. Please log in as an employee.");
            out.write(responseJson.toString());
            return;
        }
        
        String starName = req.getParameter("starName");
        String birthYearStr = req.getParameter("birthYear");
        Integer birthYear = null;
        
        if (birthYearStr != null && !birthYearStr.isEmpty()) {
            try {
                birthYear = Integer.parseInt(birthYearStr);
            } catch (NumberFormatException e) {
                responseJson.addProperty("success", false);
                responseJson.addProperty("message", "Invalid birth year format.");
                out.write(responseJson.toString());
                return;
            }
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
                // Generate a new star ID
                String starId = null;
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS max_id FROM stars");
                    if (rs.next()) {
                        String maxId = rs.getString("max_id");
                        int numericPart = Integer.parseInt(maxId.substring(2)) + 1;
                        starId = "nm" + String.format("%07d", numericPart);
                    }
                }
                
                // Insert the new star
                String insertSql = "INSERT INTO stars (id, name, birthYear) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    pstmt.setString(1, starId);
                    pstmt.setString(2, starName);
                    if (birthYear != null) {
                        pstmt.setInt(3, birthYear);
                    } else {
                        pstmt.setNull(3, java.sql.Types.INTEGER);
                    }
                    
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        responseJson.addProperty("success", true);
                        responseJson.addProperty("starId", starId);
                        responseJson.addProperty("message", "Star added successfully.");
                    } else {
                        responseJson.addProperty("success", false);
                        responseJson.addProperty("message", "Failed to add star.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseJson.addProperty("success", false);
            responseJson.addProperty("message", "Error: " + e.getMessage());
        }
        
        out.write(responseJson.toString());
    }
}