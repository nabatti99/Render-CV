

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.gson.Gson;

import controllers.AuthenticationController;
import models.User;

@WebServlet("/Auth")
public class AuthenticationServlet extends HttpServlet {

	public AuthenticationServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("Login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter writer = response.getWriter();
		HashMap<String, String> responseData = new HashMap<String, String>();
		
		try {
			String type = request.getParameter("type").toString();
			switch (type) {
			case "SIGN_IN": {
				String idToken = request.getParameter("idToken").toString();
				User user = new User(idToken);

				response.addCookie(new Cookie("idToken", idToken));
				responseData.put("status", "Success");
				writer.write(new Gson().toJson(responseData));
				
				return;
			} case "SIGN_OUT": {
				Cookie cookie = new Cookie("idToken", "");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
				responseData.put("status", "Success");
				writer.write(new Gson().toJson(responseData));
				
				return;
			} case "SIGN_UP": {
				String email = request.getParameter("email");
				String password = request.getParameter("password");
				
				UserRecord newUser = AuthenticationController.createNewAccount(email, password);
				responseData.put("status", "Success");
				responseData.put("email", newUser.getEmail());
				responseData.put("password", password);
				writer.write(new Gson().toJson(responseData));
				return;
			}
			default:
				throw new IllegalArgumentException("Not found 'type' parameter");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

}
