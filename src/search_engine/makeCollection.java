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

		// ���͸� ��ġ ����
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
					if (file.isDirectory()) { // ���丮�� ���
						// System.out.println(file.getName() + "���丮 ����.");
					}

					else { // ������ ���
						
						// if (file.getName().endsWith(".html")) { //Ȯ���� html�� ���� �߰�
						//System.out.println(file.getName() + " �߰�.");
						
						// �Ľ� & doc id ����
						org.jsoup.nodes.Document readDoc = Jsoup.parse(file, "UTF-8");

						// title ����
						Elements tmpTitle = readDoc.select("title");

						// body ����
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
				
				// xml ���� ���� ���۾�
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

				// ���� ������� �� �鿩����
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");

				// xml ���� ����
				String fileName = "collection.xml";
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new FileOutputStream(new File(fileName)));
				transformer.transform(source, result);
				System.out.println(fileName + " ���� �Ϸ�.");

			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			System.out.println("���丮 �̸��� ����� �Է��� �ּ���.");
		}

	}

}
