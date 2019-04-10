package com.pruebaTess4j.OcrTess;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import org.wso2.msf4j.formparam.FormDataParam;

import net.minidev.json.JSONObject;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Path("/ocr")
public class OCRService {

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/readDocument")
	public Response readDocument(@FormDataParam("file") File file) {
		String base641 = cropImage(file, 58, 23, 180, 60);
		String base642 = cropImage(file, 36, 141, 160, 60);
		String base643 = cropImage(file, 69, 264, 160, 60);
		String base644 = cropImage(file, 531, 30, 160, 40);
		
		String base645 = cropImage(file, 96, 408, 300, 40);
		String base646 = cropImage(file, 493, 400, 260, 40);
	
		
		ITesseract it = new Tesseract();
		it.setLanguage("spa");
		JSONObject json = new JSONObject();
		try {
			json.put("caja1", it.doOCR(base64ToFile(base641)));
			json.put("caja2", it.doOCR(base64ToFile(base642)));
			json.put("caja3", it.doOCR(base64ToFile(base643)));
			json.put("caja4", it.doOCR(base64ToFile(base644)));
			json.put("cajaprueba1", it.doOCR(base64ToFile(base645)));
			json.put("cajaprueba2", it.doOCR(base64ToFile(base646)));
			
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().entity(json).build();

	}

	// Convierte el base64 de la imágen recortada a un File
	public static File base64ToFile(String base64) throws IOException {
		String tempPath = Paths.get(System.getProperty("java.io.tmpdir")).toString();
		File ocr = File.createTempFile("tmp", ".png");
		System.out.println(base64);
		byte dearr[] = Base64.getDecoder().decode(base64);

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(ocr);
			fos.write(dearr);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ocr;
	}

	// Recorta una imágen con los parámetros indicados y lo devuelve en base64
	public static String cropImage(File image, int startX, int startY, int widthX, int widthY) {
		BufferedImage in = null;
		try {
			in = ImageIO.read(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedImage img = in.getSubimage(startX, startY, widthX, widthY);
		BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = copyOfImage.createGraphics();
		g.drawImage(img, 0, 0, null);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(copyOfImage, "png", baos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String data = DatatypeConverter.printBase64Binary(baos.toByteArray());
		// String imageString = "data:image/png;base64," + data;

		return data;
	}
}
