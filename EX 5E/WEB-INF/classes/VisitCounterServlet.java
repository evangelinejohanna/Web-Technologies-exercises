import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/visitCounter")
public class VisitCounterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the session (if it doesn't exist, a new one is created)
        HttpSession session = request.getSession(true);

        Integer visitCount = (Integer) session.getAttribute("visitCount");
        if (visitCount == null) {
            visitCount = 0; // Initialize visit count if it's the user's first visit
        }

        // Increment the visit count
        visitCount++;

        session.setAttribute("visitCount", visitCount);

        response.setContentType("text/html");

        // Write the visit count to the response
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h2>Welcome to the Visit Counter Page!</h2>");
        out.println("<p>You have visited this page " + visitCount + " time(s).</p>");
        out.println("</body></html>");
    }
}
