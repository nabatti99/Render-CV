

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controllers.AuthenticationController;
import controllers.FirestoreController;
import models.User;

/**
 * Servlet implementation class HistoryServlet
 */
@WebServlet("/History")
public class HistoryServlet extends HttpServlet {

    public HistoryServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		try {
			User user;
			try {
				String idToken = AuthenticationController.getIdToken(request.getCookies());
				user = new User(idToken);
				
			} catch (Exception e) {
				response.addCookie(new Cookie("idToken", ""));
				response.sendRedirect("./Auth");
				e.printStackTrace();
				return;
			}
			
			
			FirestoreController.getAllDocuments(user);
			user.documents.sort((doc1, doc2) -> {
				return doc2.timestamp.compareTo(doc1.timestamp);
			});
			session.setAttribute("documents", user.documents);
			
			request.getRequestDispatcher("History.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
