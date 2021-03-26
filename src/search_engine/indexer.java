package search_engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

@SuppressWarnings("serial")
public class indexer implements Serializable {
	public String id;
	public Float TF_IDF;

	public indexer(String id, Float TF_IDF) {
		this.id = id;
		this.TF_IDF = TF_IDF;
	}

	// �ƿ�ǲ ��� ����
	public indexer(String readFileName) {
		String readFilePath = ".\\..\\src\\" + readFileName;
		String outputFileName = ".\\..\\src\\index.post";
		HashMap<String, ArrayList<indexer>> KeywordMap = new HashMap<>();

		try {
			
			FileOutputStream fileStream = new FileOutputStream(outputFileName);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);

			Path filePath = Paths.get(readFilePath);
			InputStream inputStream = Files.newInputStream(filePath);
			Document readDoc = Jsoup.parse(inputStream, "UTF-8", "", Parser.xmlParser());

			// body ����
			Elements everyBody = readDoc.select("body");
			int id = 0;
			// body ������ŭ �ݺ�
			for (Element body : everyBody) {
				String textBody = body.text();
				// '#' �������� �и�
				String[] keyvalues = textBody.split("#");
				for (String keyvalue : keyvalues) {
					// ':' ������ �и�
					String[] tmpKeyvalue = keyvalue.split(":");
					// Ű����
					String key = tmpKeyvalue[0];
					// �� body�� �ܾ ������ Ƚ��(tf��)
					Float value = Float.parseFloat(tmpKeyvalue[1]);

					// Ű���尡 ������ üũ
					if (KeywordMap.containsKey(key)) {
			
						// ������ id���� üũ
						int tmpID = KeywordMap.get(key).size() - 1;
						// id�� ������ tf�� ��ġ��
						if (KeywordMap.get(key).get(tmpID).id.equals(Integer.toString(id))) {
							float savedTF = KeywordMap.get(key).get(tmpID).TF_IDF;
							KeywordMap.get(key).get(tmpID).id = Integer.toString(id);
							KeywordMap.get(key).get(tmpID).TF_IDF = savedTF + value;
						}
						// �ٸ� id�� ��
						else {
							KeywordMap.get(key).add(new indexer(Integer.toString(id), value));

						}
					}
					// Ű���尡 �ٸ� ��(���ο� Ű���� �߰�)
					else {
						ArrayList<indexer> arrValue = new ArrayList<indexer>();
						indexer Indexer = new indexer(Integer.toString(id), value);
						arrValue.add(Indexer);
						KeywordMap.put(key, arrValue);
					}
				}
				id++;
			}

			// TF��IDF ����
			Set<String> keys = KeywordMap.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String key = it.next();
				for (int i = 0; i < KeywordMap.get(key).size(); i++) {
					Float tf = KeywordMap.get(key).get(i).TF_IDF;
					Float TF_IDF = (float) (Math.round( (tf * Math.log((float) everyBody.size() / KeywordMap.get(key).size())) *100)/100.0);
					KeywordMap.get(key).get(i).TF_IDF = TF_IDF;
					//System.out.println("Key:" + key + " ID:" + KeywordMap.get(key).get(i).id + " TF_IDF:" + TF_IDF);
				}
			}
			
			System.out.println(new File(outputFileName).getCanonicalPath() + " ���� �Ϸ�.");
			objectOutputStream.writeObject(KeywordMap);
			objectOutputStream.close();

		} 
		catch(NoSuchFileException e) {
			System.out.println("[indexer.java] " + readFileName + " ������ �����ϴ�. �ٽ� Ȯ�����ּ���.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("[indexer.java] FileOutputStream �����Դϴ�.");
		}

	}
}
