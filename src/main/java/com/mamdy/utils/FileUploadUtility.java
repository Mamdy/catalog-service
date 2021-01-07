package com.mamdy.utils;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


@UtilityClass
public class FileUploadUtility {
	@Autowired
	ServletContext context;
	private static final String ABS_PATH = "D:\\_DVT\\workspace\\catalog-service\\src\\main\\webapp\\assets\\images\\";
	private static String REAL_PATH = "";
	private static final Logger logger = LoggerFactory.getLogger(FileUploadUtility.class);

	public static void uplaodFile(HttpServletRequest request, MultipartFile file, String code) {

		//Recuperation du chemin réel
		REAL_PATH = request.getSession().getServletContext().getRealPath("assets/images/");
		logger.info(REAL_PATH);

		// s'asurer que ces repertoires existe, sinon les créers
		if (!new File(ABS_PATH).exists()) {
			//creation du repertoir
			new File(ABS_PATH).mkdir();
		}

		if (!new File(REAL_PATH).exists()) {
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
	// compress the image bytes before storing it in the database
	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}
		logger.info("Compressed Image Byte Size - " + outputStream.toByteArray().length);

		return outputStream.toByteArray();
	}

	// uncompress the image bytes before returning it to the angular application
	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException | DataFormatException ioe) {
		}
		return outputStream.toByteArray();
	}



}


