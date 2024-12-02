import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/notes")
public class NoteServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/note_taking_app";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    // JDBC connection
    private Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("DB connection failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String sql = "SELECT * FROM notes ORDER BY created_at DESC";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            out.println("<h1>Notes</h1>");
            while (rs.next()) {
                out.println("<div><strong>" + rs.getString("title") + "</strong>");
                out.println("<p>" + rs.getString("content") + "</p>");
                out.println("<em>Created at: " + rs.getTimestamp("created_at") + "</em></div><hr>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("<p>Error retrieving notes</p>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get data from the form
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        if (title == null || content == null || title.isEmpty() || content.isEmpty()) {
            response.setContentType("text/html");
            response.getWriter().write("<p>Error: Title and content must be provided.</p>");
            return;
        }

        // Insert note into the database
        String sql = "INSERT INTO notes (title, content) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.executeUpdate();

            // Display the newly added note
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h1>New Note Added</h1>");
            out.println("<div><strong>" + title + "</strong>");
            out.println("<p>" + content + "</p>");
            out.println("<em>Created at: " + new Timestamp(System.currentTimeMillis()) + "</em></div>");
            out.println("<hr>");

            // Show all notes after adding a new one
            out.println("<h2>All Notes</h2>");
            String selectSql = "SELECT * FROM notes ORDER BY created_at DESC";
            try (Statement stmtSelect = connection.createStatement();
                    ResultSet rs = stmtSelect.executeQuery(selectSql)) {
                while (rs.next()) {
                    out.println("<div><strong>" + rs.getString("title") + "</strong>");
                    out.println("<p>" + rs.getString("content") + "</p>");
                    out.println("<em>Created at: " + rs.getTimestamp("created_at") + "</em></div><hr>");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write("<p>Error retrieving notes after adding new one.</p>");
            }

        } catch (SQLException e) {
            response.setContentType("text/html");
            response.getWriter().write("<p>Error inserting note: " + e.getMessage() + "</p>");
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}