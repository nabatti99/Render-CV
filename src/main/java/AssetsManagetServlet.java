

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.AssetsController;

@WebServlet("/Static/*")
public class AssetsManagetServlet extends HttpServlet {
	
    public AssetsManagetServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filePath = request.getPathInfo();
		
		try {
			
			if (filePath == null)
				throw new Exception("Not found the fileName param");
			
			Path assetPath = AssetsController.getAsset(filePath);
			
			response.setContentType("*/*");
			response.getOutputStream().write(Files.readAllBytes(assetPath));
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}
