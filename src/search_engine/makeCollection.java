package search_engine;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeCollection {

	public makeCollection(String directory) {

		// 디렉터리 위치 설정
		String dirLocation = ".\\..\\src\\" + directory;
		File dir = new File(dirLocation);

		if (dir.isDirectory()) {
			File[] files = dir.listFiles();

			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();

				// docs element
				Element docsE = doc.createElement("docs");
				doc.appendChild(docsE);

				
				int idNum = 0;
				for (File file : files) {
					if (file.isDirectory()) { // 디렉토리일 경우
						// System.out.println(file.getName() + "디렉토리 있음.");
					}

					else { // 파일일 경우
						
						// if (file.getName().endsWith(".html")) { //확장자 html일 때만 추가
						//System.out.println(file.getName() + " 추가.");
						
						// 파싱 & doc id 생성
						org.jsoup.nodes.Document readDoc = Jsoup.parse(file, "UTF-8");

						// title 추출
						Elements tmpTitle = readDoc.select("title");

						// body 추출
						Elements tmpBody = readDoc.select("body");

						// doc element
						Element docE = doc.createElement("doc");
						docsE.appendChild(docE);
						docE.setAttribute("id", String.valueOf(idNum));

						// title element
						Element titleE = doc.createElement("title");
						titleE.appendChild(doc.createTextNode(tmpTitle.text()));
						docE.appendChild(titleE);

						// body element
						Element bodyE = doc.createElement("body");
						bodyE.appendChild(doc.createTextNode(tmpBody.text()));
						docE.appendChild(bodyE);
						
						idNum++;
					}
				}
				
				// xml 파일 생성 밑작업
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

				// 파일 만들어질 때 들여쓰기
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");

				// xml 파일 생성
				String fileName = "collection.xml";
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new FileOutputStream(new File(fileName)));
				transformer.transform(source, result);
				System.out.println(fileName + " 생성 완료.");

			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			System.out.println("디렉토리 이름을 제대로 입력해 주세요.");
		}

	}

}
