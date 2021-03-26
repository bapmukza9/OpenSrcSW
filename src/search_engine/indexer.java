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

	// 아웃풋 경로 수정
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

			// body 추출
			Elements everyBody = readDoc.select("body");
			int id = 0;
			// body 개수만큼 반복
			for (Element body : everyBody) {
				String textBody = body.text();
				// '#' 기준으로 분리
				String[] keyvalues = textBody.split("#");
				for (String keyvalue : keyvalues) {
					// ':' 기준을 분리
					String[] tmpKeyvalue = keyvalue.split(":");
					// 키워드
					String key = tmpKeyvalue[0];
					// 한 body에 단어가 등장한 횟수(tf값)
					Float value = Float.parseFloat(tmpKeyvalue[1]);

					// 키워드가 같은지 체크
					if (KeywordMap.containsKey(key)) {
			
						// 동일한 id인지 체크
						int tmpID = KeywordMap.get(key).size() - 1;
						// id가 같으면 tf값 합치기
						if (KeywordMap.get(key).get(tmpID).id.equals(Integer.toString(id))) {
							float savedTF = KeywordMap.get(key).get(tmpID).TF_IDF;
							KeywordMap.get(key).get(tmpID).id = Integer.toString(id);
							KeywordMap.get(key).get(tmpID).TF_IDF = savedTF + value;
						}
						// 다른 id일 때
						else {
							KeywordMap.get(key).add(new indexer(Integer.toString(id), value));

						}
					}
					// 키워드가 다를 때(새로운 키워드 추가)
					else {
						ArrayList<indexer> arrValue = new ArrayList<indexer>();
						indexer Indexer = new indexer(Integer.toString(id), value);
						arrValue.add(Indexer);
						KeywordMap.put(key, arrValue);
					}
				}
				id++;
			}

			// TF·IDF 지정
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
			
			System.out.println(new File(outputFileName).getCanonicalPath() + " 생성 완료.");
			objectOutputStream.writeObject(KeywordMap);
			objectOutputStream.close();

		} 
		catch(NoSuchFileException e) {
			System.out.println("[indexer.java] " + readFileName + " 파일이 없습니다. 다시 확인해주세요.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("[indexer.java] FileOutputStream 오류입니다.");
		}

	}
}
