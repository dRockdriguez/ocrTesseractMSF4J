package com.pruebaTess4j.OcrTess;

import org.wso2.msf4j.MicroservicesRunner;
/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) {
		new MicroservicesRunner().deploy(new OCRService()).start();
	}
}
