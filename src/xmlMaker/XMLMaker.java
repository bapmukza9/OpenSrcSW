package xmlMaker;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.*;

public class XMLMaker {

	// dir 파일 개수, 이름 체크
	public static ArrayList<String> dirCheck(File dir) {

		ArrayList<String> fileArray = new ArrayList<>();
		if (dir.isDirectory()) {

			File[] files = dir.listFiles();

			for (File file : files) {
				if (file.isDirectory()) { // 디렉토리일 경우
					//System.out.println(file.getName() + "디렉토리 있음.");
				}

				else { // 파일일 경우
					// if (file.getName().endsWith(".html")) { //확장자 html일 때만 추가

					fileArray.add(file.getName());
					//System.out.println(file.getName() + " 추가.");
				}
			}
		}

		return fileArray;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("파일이 있는 디렉토리 이름을 입력하세요 : ");
		String dirName = sc.next();

		if (!(new File(dirName + "\\")).isDirectory())
			System.out.println("디렉토리 이름을 다시 확인해주세요.");

		else {
			ArrayList<String> fileArray = dirCheck(new File(dirName + "\\"));

			try {

				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();
				
				// docs element
				Element docsE = doc.createElement("docs");
				doc.appendChild(docsE);
				
				// 파싱 & doc id 생성
				for (int i = 0; i < fileArray.size(); i++) {
					File file = new File("dir\\" + fileArray.get(i));
					org.jsoup.nodes.Document readDoc = Jsoup.parse(file, "UTF-8");

					// title 추출
					Elements tmpTitle = readDoc.select("title");

					// body 추출
					Elements tmpBody = readDoc.select("body");

					// doc element
					Element docE = doc.createElement("doc");
					docsE.appendChild(docE);
					docE.setAttribute("id", String.valueOf(i));

					// title element
					Element titleE = doc.createElement("title");
					titleE.appendChild(doc.createTextNode(tmpTitle.text()));
					docE.appendChild(titleE);

					// body element
					Element bodyE = doc.createElement("body");
					bodyE.appendChild(doc.createTextNode(tmpBody.text()));
					docE.appendChild(bodyE);
				}
		
				// xml 파일 생성
				TransformerFactory transformerFactory = TransformerFactory.newInstance();

				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

				// 파일 만들어질 때 들여쓰기
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");

				String xmlFileName = "collection.xml";
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new FileOutputStream(new File(xmlFileName)));
				transformer.transform(source, result);
				System.out.println(xmlFileName + " 생성 완료.");

			} catch (FileNotFoundException e) {
				// TODO: handle exception
			} catch (Exception e) {
				System.out.println(e);
			}

		}
	}
}
