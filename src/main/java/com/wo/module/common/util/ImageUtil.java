package com.wo.module.common.util;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageUtil implements Serializable {

	private static final long serialVersionUID = 6933325978173034013L;

	public static String getImageContentsAsBase64(String physcalPath) {
    	try {
    		Path path = Paths.get(physcalPath);
        	byte[] data = null;
    		data = Files.readAllBytes(path);
    		return Base64.getEncoder().encodeToString(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
}
