package com.mamdy.utils;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtility {
	@Autowired
	static
	ServletContext  context;
	private static final String ABS_PATH = "D:\\_DVT\\workspace\\catalog-service\\src\\main\\webapp\\assets\\images\\";
	private static String REAL_PATH = "";
	private static final Logger logger = LoggerFactory.getLogger(FileUploadUtility.class);
	public static void uplaodFile(HttpServletRequest request, MultipartFile file, String code) {
		
		//Recuperation du chemin réel
		REAL_PATH = request.getSession().getServletContext().getRealPath("assets/images/");
		logger.info(REAL_PATH);
		
		// s'aasurer que ces repertoires existe, sinon les créers
		if(!new File(ABS_PATH).exists()) {
			//creation du repertoir
			new File(ABS_PATH).mkdir();
		}
		
		if(!new File(REAL_PATH).exists()) {
			//creation du repertoir
			new File(REAL_PATH).mkdir();
		}
		
		try {
			//uploader le fichier dans le serveur(à l'occurrance notre serveur tomcat embarqué)
			file.transferTo(new File(REAL_PATH + code + ".jpg"));
			//uploader dans le repertoir de mon projet(webapp/assets/images)
			file.transferTo(new File(ABS_PATH + code + ".jpg"));
		} catch (IOException ex) {
			// TODO: handle exception
		}
		
		
	}
	
	
	

}


