import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/notes")
public class NoteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private List<String> notes = new ArrayList<>();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the session or create a new one
        HttpSession session = request.getSession(true);

        // Handle cookie: Check if the user has visited before
        Cookie[] cookies = request.getCookies();
        boolean newUser = true;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("visited".equals(cookie.getName())) {
                    newUser = false;
                    break;
                }
            }
        }

        // If new user, set a cookie
        if (newUser) {
            Cookie visitedCookie = new Cookie("visited", "true");
            visitedCookie.setMaxAge(60 * 60 * 24); // Set cookie expiry for 24 hours
            response.addCookie(visitedCookie);
        }

        // Display the list of notes
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h1>Note Taking App</h1>");
        out.println("<h2>Your Notes:</h2>");

        if (notes.isEmpty()) {
            out.println("<p>No notes available. Add a note below!</p>");
        } else {
            out.println("<ul>");
            for (String note : notes) {
                out.println("<li>" + note + " <a href='delete?note=" + note + "'>Delete</a></li>");
            }
            out.println("</ul>");
        }

        // Display session info: Show user ID (stored in session) and URL rewriting
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            userId = "Guest";
            session.setAttribute("userId", userId);
        }
        out.println("<p>User ID: " + userId + "</p>");

        // Form to add a new note
        out.println("<h3>Add a New Note:</h3>");
        out.println("<form action='notes' method='POST'>");
        out.println("Note: <input type='text' name='note' required>");
        out.println("<input type='hidden' name='userId' value='" + userId + "' />");
        out.println("<button type='submit'>Add Note</button>");
        out.println("</form>");

        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle note addition (with HTTP session and hidden form field)
        String newNote = request.getParameter("note");
        String userId = request.getParameter("userId"); // Get user ID from hidden form field

        if (newNote != null && !newNote.trim().isEmpty()) {
            notes.add(newNote);
        }

        // Redirect to the GET method to display updated list with URL rewriting
        response.sendRedirect("notes?userId=" + userId);
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Delete a note from the list
        String noteToDelete = request.getParameter("note");
        if (noteToDelete != null) {
            notes.remove(noteToDelete);
        }

        // Redirect to the GET method to display updated list
        response.sendRedirect("notes");
    }
}
