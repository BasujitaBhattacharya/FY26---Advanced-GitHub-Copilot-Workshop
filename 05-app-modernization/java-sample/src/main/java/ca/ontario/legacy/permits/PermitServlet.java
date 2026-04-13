/**
 * Legacy Java 8 Servlet — reference example for Module 05.
 *
 * LEGACY patterns demonstrated:
 * - HttpServlet (Servlet 3.x) instead of Spring Boot REST controller
 * - Raw PrintWriter for JSON responses (no Jackson ObjectMapper)
 * - Manual string concatenation for JSON
 * - Synchronous JDBC with direct DriverManager (no connection pool)
 * - Hard-coded connection string (should use JNDI or environment variables)
 * - No dependency injection — manual object creation
 *
 * MODERN Java 17+ equivalents:
 * - Spring Boot 3.x with @RestController and @GetMapping
 * - Spring Data JPA or modern JDBC with connection pooling (HikariCP)
 * - Record types for DTOs
 * - @ConfigurationProperties for config
 * - Constructor injection with @RequiredArgsConstructor
 */
package ca.Customer.legacy.permits;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/api/permits/*")
public class PermitServlet extends HttpServlet {

    // LEGACY: Hard-coded DB connection — should be in datasource config or env var
    private static final String DB_URL = "jdbc:sqlserver://localhost;databaseName=CustomerPermits;integratedSecurity=true";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();

        try {
            // LEGACY: DriverManager.getConnection — use DataSource/connection pool in modern Java
            Connection conn = DriverManager.getConnection(DB_URL);

            if (pathInfo == null || pathInfo.equals("/")) {
                // Return all permits
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Permits");
                ResultSet rs = stmt.executeQuery();

                StringBuilder json = new StringBuilder("[");
                boolean first = true;
                while (rs.next()) {
                    if (!first) json.append(",");
                    // LEGACY: Manual JSON string building — use Jackson or Gson
                    json.append("{")
                        .append("\"id\":").append(rs.getInt("PermitId")).append(",")
                        .append("\"applicant\":\"").append(rs.getString("ApplicantName")).append("\",")
                        .append("\"status\":\"").append(rs.getString("Status")).append("\"")
                        .append("}");
                    first = false;
                }
                json.append("]");
                out.print(json.toString());

            } else {
                // Return single permit by ID
                int id = Integer.parseInt(pathInfo.substring(1));
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Permits WHERE PermitId = ?");
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    out.print("{\"id\":" + rs.getInt("PermitId") +
                              ",\"applicant\":\"" + rs.getString("ApplicantName") + "\"" +
                              ",\"status\":\"" + rs.getString("Status") + "\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Permit not found\"}");
                }
            }

            conn.close(); // LEGACY: Manual connection closing — use try-with-resources

        } catch (SQLException e) {
            // LEGACY: Printing stack trace to response — should log and return proper error
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
            e.printStackTrace(); // LEGACY: stdout logging instead of SLF4J/Logback
        }
    }
}
