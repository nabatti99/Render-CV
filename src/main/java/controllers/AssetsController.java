package controllers;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AssetsController {
	public static Path getAsset(String relativePath) throws Exception {
		URL assetBase = AssetsController.class.getResource("/../../assets");
		Path assetBasePath = Paths.get(assetBase.toURI());
		
		Path assetPath = Paths.get(assetBasePath.toString(), relativePath);
		return assetPath;
	}
}
