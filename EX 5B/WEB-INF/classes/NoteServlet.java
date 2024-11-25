import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/notes")
public class NoteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private List<String> notes = new ArrayList<>();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

        // Form to add a new note
        out.println("<h3>Add a New Note:</h3>");
        out.println("<form action='notes' method='POST'>");
        out.println("Note: <input type='text' name='note' required>");
        out.println("<button type='submit'>Add Note</button>");
        out.println("</form>");

        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Add a new note to the list
        String newNote = request.getParameter("note");
        if (newNote != null && !newNote.trim().isEmpty()) {
            notes.add(newNote);
        }
        // Redirect to the GET method to display updated list
        response.sendRedirect("notes");
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
