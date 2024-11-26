import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/notes")
public class NoteServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/note_taking_app";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "password";

    // JDBC connection
    private Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (Exception e) {
            throw new ServletException("DB connection failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sql = "SELECT * FROM notes ORDER BY created_at DESC";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            request.setAttribute("notes", rs);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Error retrieving notes", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get data from the form
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        if (title == null || content == null || title.isEmpty() || content.isEmpty()) {
            response.getWriter().write("Error: Title and content must be provided.");
            return;
        }

        // Insert note into the database
        String sql = "INSERT INTO notes (title, content) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.executeUpdate();

            response.sendRedirect("notes");

        } catch (SQLException e) {
            throw new ServletException("Error inserting note", e);
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
