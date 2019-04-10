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

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Path("/ocr")
public class OCRService {

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/readDocument")
	public Response readDocument(@FormDataParam("file") File file) {
		StringBuilder response = new StringBuilder();

		String base641 = cropImage(file, 58, 23, 180, 60);
		String base642 = cropImage(file, 36, 141, 160, 60);
		String base643 = cropImage(file, 69, 264, 160, 60);
		String base644 = cropImage(file, 531, 30, 160, 40);
		
		String base645 = cropImage(file, 96, 408, 300, 40);
		String base646 = cropImage(file, 493, 400, 260, 40);
		
		String base647 = cropImage(file, 582, 143, 140, 50);
		String base648 = cropImage(file, 544, 263, 150, 60);


		
		ITesseract it = new Tesseract();
		//it.setDatapath("../tessdata");
		it.setLanguage("spa");
		String ocrRes = "";
		try {
			ocrRes = "Caja 1 :: " + it.doOCR(base64ToFile(base641));
			ocrRes += "\nCaja2 :: "  + it.doOCR(base64ToFile(base642));
			ocrRes += "\nCaja3 :: "  + it.doOCR(base64ToFile(base643));
			ocrRes += "\nCaja4 :: "  + it.doOCR(base64ToFile(base644));
			ocrRes += "\nCajaPrueba :: "  + it.doOCR(base64ToFile(base645));
			ocrRes += "\nCajaPrueba1 :: "  + it.doOCR(base64ToFile(base646));
			ocrRes += "\nCaja 5 :: "  + it.doOCR(base64ToFile(base647));
			ocrRes += "\nCaja 6 :: "  + it.doOCR(base64ToFile(base648));

		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().entity(ocrRes).build();

	}

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
		String imageString = "data:image/png;base64," + data;

		return data;
	}
}
