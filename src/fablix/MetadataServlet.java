package fablix;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "MetadataServlet", urlPatterns = "/api/metadata")
public class MetadataServlet extends HttpServlet {
    private static final String URL  = "jdbc:mysql://localhost:3306/moviedb";
    private static final String USER = "mytestuser";
    private static final String PASS = "My6$Password";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
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
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
                DatabaseMetaData metaData = conn.getMetaData();
                
                // Get all tables
                JsonArray tablesArray = new JsonArray();
                try (ResultSet tables = metaData.getTables(null, "moviedb", null, new String[]{"TABLE"})) {
                    while (tables.next()) {
                        String tableName = tables.getString("TABLE_NAME");
                        
                        // Get columns for this table
                        JsonArray columnsArray = new JsonArray();
                        try (ResultSet columns = metaData.getColumns(null, "moviedb", tableName, null)) {
                            while (columns.next()) {
                                JsonObject columnObj = new JsonObject();
                                columnObj.addProperty("name", columns.getString("COLUMN_NAME"));
                                columnObj.addProperty("type", columns.getString("TYPE_NAME"));
                                columnsArray.add(columnObj);
                            }
                        }
                        
                        JsonObject tableObj = new JsonObject();
                        tableObj.addProperty("name", tableName);
                        tableObj.add("columns", columnsArray);
                        tablesArray.add(tableObj);
                    }
                }
                
                responseJson.addProperty("success", true);
                responseJson.add("tables", tablesArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseJson.addProperty("success", false);
            responseJson.addProperty("message", "Error: " + e.getMessage());
        }
        
        out.write(responseJson.toString());
    }
}