package search_engine;

public class kuir {
	public static void main(String[] args) {
		
		
		if(args.length >= 2) {
			if(args[0].equals("-c")) {	
				makeCollection trigger = new makeCollection(args[1]);
			}
			else if(args[0].equals("-k")) {
				makeKeyword trigger = new makeKeyword(args[1]);
			}
			else if(args[0].equals("-i")) {
				indexer trigger = new indexer(args[1]);
			}
			else if(args[0].equals("-s")) {
				searcher trigger = new searcher(args[3], args[1]);
			}
			else {
				System.out.println("\"" + args[0] + "\"는 없는 명령어입니다.");
			}
			
		}
		else {
			System.out.println("인자를 제대로 입력하세요.");
		}

		
		
		//searcher q = new searcher("라면에는 면, 분말 스프가 있다.", "index.post");
	}	
}
