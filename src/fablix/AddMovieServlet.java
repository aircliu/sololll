package fablix;

import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "AddMovieServlet", urlPatterns = "/api/add-movie")
public class AddMovieServlet extends HttpServlet {
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
        
        String title = req.getParameter("title");
        String yearStr = req.getParameter("year");
        String director = req.getParameter("director");
        String starName = req.getParameter("starName");
        String genreName = req.getParameter("genreName");
        
        if (title == null || title.isEmpty() || yearStr == null || yearStr.isEmpty() 
            || director == null || director.isEmpty() || starName == null || starName.isEmpty()
            || genreName == null || genreName.isEmpty()) {
            responseJson.addProperty("success", false);
            responseJson.addProperty("message", "All fields are required.");
            out.write(responseJson.toString());
            return;
        }
        
        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            responseJson.addProperty("success", false);
            responseJson.addProperty("message", "Invalid year format.");
            out.write(responseJson.toString());
            return;
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
                // Call the stored procedure
                try (CallableStatement cstmt = conn.prepareCall("{CALL add_movie(?, ?, ?, ?, ?)}")) {
                    cstmt.setString(1, title);
                    cstmt.setInt(2, year);
                    cstmt.setString(3, director);
                    cstmt.setString(4, starName);
                    cstmt.setString(5, genreName);
                    
                    boolean hasResults = cstmt.execute();
                    StringBuilder messages = new StringBuilder();
                    
                    // Process results
                    do {
                        if (hasResults) {
                            try (ResultSet rs = cstmt.getResultSet()) {
                                while (rs.next()) {
                                    String message = rs.getString(1);
                                    messages.append(message).append("<br>");
                                    
                                    // Check if the movie already exists
                                    if (message.contains("Movie already exists")) {
                                        responseJson.addProperty("success", false);
                                        responseJson.addProperty("message", message);
                                        out.write(responseJson.toString());
                                        return;
                                    }
                                }
                            }
                        }
                        hasResults = cstmt.getMoreResults();
                    } while (hasResults || cstmt.getUpdateCount() != -1);
                    
                    responseJson.addProperty("success", true);
                    responseJson.addProperty("message", messages.toString());
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