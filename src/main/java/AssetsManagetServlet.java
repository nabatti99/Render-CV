

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Static/*")
public class AssetsManagetServlet extends HttpServlet {
	
	private static String ASSET_PATH = "E:\\Study\\IT\\NetworkDeveloping\\Render CV\\src\\main\\webapp\\assets";
	
    public AssetsManagetServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName = request.getPathInfo();
		
		try {			
			if (fileName == null)
				throw new Exception("Not found the fileName param");
			
			Path filePath = Paths.get(ASSET_PATH, fileName);
			if (!Files.exists(filePath) || (filePath.toRealPath().compareTo(filePath) > 0))
				throw new Exception("Not Found" + filePath.toRealPath());
			
			response.setContentType("*/*");
			response.getOutputStream().write(Files.readAllBytes(filePath));
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}
