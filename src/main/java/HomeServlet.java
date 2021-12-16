

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.google.gson.Gson;

import controllers.AuthenticationController;
import controllers.ConvertController;
import controllers.FirestoreController;
import controllers.StorageController;
import models.Document;
import models.User;

@WebServlet("/Home")
@MultipartConfig(
	fileSizeThreshold=0, 				// 0 MB 
	maxFileSize=1024*1024*2,      		// 2 MB
	maxRequestSize=1024*1024*100)   	// 10 MB
public class HomeServlet extends HttpServlet {

    public HomeServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		response.sendRedirect("Home.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		HashMap<String, String> responseData = new HashMap<String, String>();
		
		HttpSession session = request.getSession();
		Part part = request.getPart("file");
		
		responseData.put("status", "Success");
		response.getOutputStream().write(new Gson().toJson(responseData).getBytes());
		
		try {
			String idToken = AuthenticationController.getIdToken(request.getCookies());
			
			User user;
			try {
				user = new User(idToken);				
			} catch (Exception e) {
				response.addCookie(new Cookie("idToken", ""));
				response.sendRedirect("./Auth");
				return;
			}
			
			Document document = FirestoreController.addNewDocument(user, part.getSubmittedFileName().replaceAll(".xlsx", "") + "_RenderCV.docx");
			
			ConvertController convertController = new ConvertController(part.getInputStream(), user, document);
			Thread job = convertController.createJob();
			
			job.start();
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}
}
