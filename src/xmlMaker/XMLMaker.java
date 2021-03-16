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

	// dir ���� ����, �̸� üũ
	public static ArrayList<String> dirCheck(File dir) {

		ArrayList<String> fileArray = new ArrayList<>();
		if (dir.isDirectory()) {

			File[] files = dir.listFiles();

			for (File file : files) {
				if (file.isDirectory()) { // ���丮�� ���
					//System.out.println(file.getName() + "���丮 ����.");
				}

				else { // ������ ���
					// if (file.getName().endsWith(".html")) { //Ȯ���� html�� ���� �߰�

					fileArray.add(file.getName());
					//System.out.println(file.getName() + " �߰�.");
				}
			}
		}

		return fileArray;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("������ �ִ� ���丮 �̸��� �Է��ϼ��� : ");
		String dirName = sc.next();

		if (!(new File(dirName + "\\")).isDirectory())
			System.out.println("���丮 �̸��� �ٽ� Ȯ�����ּ���.");

		else {
			ArrayList<String> fileArray = dirCheck(new File(dirName + "\\"));

			try {

				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();
				
				// docs element
				Element docsE = doc.createElement("docs");
				doc.appendChild(docsE);
				
				// �Ľ� & doc id ����
				for (int i = 0; i < fileArray.size(); i++) {
					File file = new File("dir\\" + fileArray.get(i));
					org.jsoup.nodes.Document readDoc = Jsoup.parse(file, "UTF-8");

					// title ����
					Elements tmpTitle = readDoc.select("title");

					// body ����
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
		
				// xml ���� ����
				TransformerFactory transformerFactory = TransformerFactory.newInstance();

				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

				// ���� ������� �� �鿩����
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");

				String xmlFileName = "collection.xml";
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new FileOutputStream(new File(xmlFileName)));
				transformer.transform(source, result);
				System.out.println(xmlFileName + " ���� �Ϸ�.");

			} catch (FileNotFoundException e) {
				// TODO: handle exception
			} catch (Exception e) {
				System.out.println(e);
			}

		}
	}
}
