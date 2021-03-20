package search_engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class makeKeyword {

	public makeKeyword(String readFileName) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// docs element
			Element docsE = doc.createElement("docs");
			doc.appendChild(docsE);

			Path filePath = Paths.get(readFileName);
			InputStream inputStream = Files.newInputStream(filePath);
			org.jsoup.nodes.Document readDoc = Jsoup.parse(inputStream, "UTF-8", "", Parser.xmlParser());

			// title 추출
			Elements tmpTitle = readDoc.select("title");

			// body 추출
			Elements tmpBody = readDoc.select("body");

			Elements tmpDoc = readDoc.select("doc");
			for (int i = 0; i < tmpDoc.size(); i++) {
				// doc element
				Element docE = doc.createElement("doc");
				docsE.appendChild(docE);
				docE.setAttribute("id", String.valueOf(i));
				// title element
				Element titleE = doc.createElement("title");
				titleE.appendChild(doc.createTextNode(tmpTitle.get(i).text()));
				docE.appendChild(titleE);

				// body element
				String docBody = "";
				String bodyText = tmpBody.get(i).text();
				KeywordExtractor ke = new KeywordExtractor();
				KeywordList kl = ke.extractKeyword(bodyText, true);
				for (int j = 0; j < kl.size(); j++) {
					if (j != 0)
						docBody += "#";
					Keyword kwrd = kl.get(j);
					docBody += kwrd.getString() + ":" + kwrd.getCnt();
				}

				Element bodyE = doc.createElement("body");
				bodyE.appendChild(doc.createTextNode(docBody));
				docE.appendChild(bodyE);
			}

			// xml 파일 생성 밑작업
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			// 파일 만들어질 때 들여쓰기
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// xml 파일 생성
			String fileName = "index.xml";
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new FileOutputStream(new File(fileName)));
			transformer.transform(source, result);
			System.out.println(fileName + " 생성 완료.");

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
