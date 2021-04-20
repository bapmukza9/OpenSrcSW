package search_engine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.w3c.dom.Document;


public class getSnippet {


	public getSnippet(String txt, String keyword) {
		try {
		String fileName = "src\\" + txt;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		
		Path filePath = Paths.get(fileName);
		InputStream inputStream = Files.newInputStream(filePath);
		org.jsoup.nodes.Document readDoc = Jsoup.parse(inputStream, "UTF-8", "", Parser.xmlParser());
		
		String total = readDoc.text();
		
		String data[] = total.split(" ");
		
		System.out.println("<" + txt + " 데이터 split>");
		for(int i =0 ; i < data.length ; i++) {
			System.out.println(data[i]);
		}
		
		System.out.println("\n<사용자 입력 값>");
		System.out.println(keyword + "\n");
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}