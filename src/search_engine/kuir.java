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
				System.out.println("\"" + args[0] + "\"�� ���� ��ɾ��Դϴ�.");
			}
			
		}
		else {
			System.out.println("���ڸ� ����� �Է��ϼ���.");
		}

		
		
		//searcher q = new searcher("��鿡�� ��, �и� ������ �ִ�.", "index.post");
	}	
}
