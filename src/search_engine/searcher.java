package search_engine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class searcher {

	public int id;
	public Float calcsim;
	public String title;

	public searcher(int id, Float calcsim, String title) {
		this.id = id;
		this.calcsim = calcsim;
		this.title = title;
	}

	public searcher(String query, String postFile) {

		String postFileLocation = ".\\..\\src\\" + postFile;
		searcher[] top3List = CalcSim(query, postFileLocation);
		System.out.println("<Rank>\t<title>\t<id>\t<CalcSim>");
		
		int rank = 1;
		for(searcher top : top3List) {
			if(top == null) {
				break;
			}
			else {
				System.out.println(" " + (rank++) +".\t" + top.title+"\t[" + top.id +"]\t " + top.calcsim);
			}
		}
	}
	
	public static searcher[] CalcSim(String query, String postFile) {
		searcher[] top3Data = InnerProduct(query, postFile);
		return top3Data;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static searcher[] InnerProduct(String query, String postFile) {
		// collection.xml 경로
		String readFileName = ".\\..\\src\\collection.xml";
		ArrayList<String> inputKeyword = new ArrayList<>();
		ArrayList<Float> inputWeight = new ArrayList<>();
		ArrayList<searcher> totalData = new ArrayList<>();

		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		for (Keyword kwrd : kl) {
			inputKeyword.add(kwrd.getString());
			inputWeight.add((float) 1);
			// System.out.println(kwrd.getString());
		}

		try {
			FileInputStream fileStream = new FileInputStream(postFile);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);

			Object object = objectInputStream.readObject();

			HashMap<String, ArrayList<indexer>> hashMap = (HashMap) object;

			Path filePath = Paths.get(readFileName);
			InputStream inputStream = Files.newInputStream(filePath);
			Document readDoc = Jsoup.parse(inputStream, "UTF-8", "", Parser.xmlParser());

			// title 추출
			Elements everyTitle = readDoc.select("title");

			for (int i = 0; i < everyTitle.size(); i++) {
				float calcsim = 0;
				for (int j = 0; j < inputKeyword.size(); j++) {
					if (hashMap.containsKey(inputKeyword.get(j))) {
						for (int z = 0; z < hashMap.get(inputKeyword.get(j)).size(); z++) {
							if (Integer.toString(i).equals(hashMap.get(inputKeyword.get(j)).get(z).id)) {
								calcsim += inputWeight.get(j) * hashMap.get(inputKeyword.get(j)).get(z).TF_IDF;
							}
						}
					}
				}
				if(calcsim != 0) {
					totalData.add(new searcher(i, (float) (Math.round(calcsim * 100) / 100.0), everyTitle.get(i).text()));
				}
			}
			objectInputStream.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("[querry.java] FileOutputStream 오류입니다.");
		} catch (ClassNotFoundException e) {
			System.out.println("[querry.java] Class를 찾을 수 없습니다.");
		}

		searcher[] top3Data = new searcher[3];
		
		Collections.sort(totalData, new Comparator() {

			@Override
			public int compare(Object s1, Object s2) {
				float a = (float)((searcher)s1).calcsim;
				float b = (float)((searcher)s2).calcsim;
				int check = 0;
				if(a == b){
					check = 0;
				}
				else if(a < b) {
					check = 1;
				}
				else {
					check = -1;
				}
				return check;
			}

		});
		
		for(int i = 0, size = totalData.size()>top3Data.length?top3Data.length:totalData.size(); i < size; i++) {
			top3Data[i] = totalData.get(i);
		}
		return top3Data;
	}

}