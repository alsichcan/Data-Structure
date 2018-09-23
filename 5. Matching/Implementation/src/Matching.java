import java.io.*;
import java.util.ArrayList;

public class Matching
{
	// 전체적으로 자료를 관리할 hashTable 객체를 선언한다.
    private static hashTable<AVLTree, substring> dataTable;

	public static void main(String args[]) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command (String input) throws Exception
	{
		// 입력된 command에 따라 적절한 기능을 수행한다.

        // 명렁어 구분자를 제외한 명령어
	    String order = input.substring(2);

	    // 명령어 구분자에 따라 명령을 수행한다.
		if(input.charAt(0) =='<'){
			dataTable = register(order);
		} else if(input.charAt(0) == '@') {
			printData(dataTable, Integer.parseInt(order));
		} else if(input.charAt(0) == '?'){
			search(dataTable, order);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////
	private static hashTable<AVLTree, substring> register(String input) throws Exception{
	    // 새로운 텍스트 파일을 입력한다.

		// 해시테이블 객체 생성
        hashTable<AVLTree, substring> dataTable = new hashTable<>(AVLTree.class);

		// 파일 객체 생성
		File file = new File(input);
		//입력 버퍼 생성
		BufferedReader bf = new BufferedReader(new FileReader(file));

		String line; // 읽어드린 문장을 담을 String 객체
		int lineNum = 0; // 몇 번쨰 문장인지에 대한 정보를 담은 객체

		while((line = bf.readLine()) != null){
			lineNum++;
			for(int i = 0 ; i < line.length() - 5 ; i++){
			    // 읽어드린 한 줄을 6글자 단위로 쪼개서 substring 객체를 생성하여 추가한다.
				dataTable.insert(new substring(line.substring(i, i+6), lineNum, i+1));
			}
		}

		return dataTable;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	private static void printData(hashTable dataTable, int index){
	    //dataTable의 index번째 slot에 속하는 AVLTree의 노드들을 출력한다
		dataTable.retrieve(index).print();
	}

	//////////////////////////////////////////////////////////////////////////////////////
	private static void search(hashTable<AVLTree, substring> dataTable, String pattern){
	    // 패턴의 시작 6글자와 마지막 6글자를 각각 substring 객체를 생성한다.
        substring start = new substring(pattern.substring(0, 6));
        substring end = new substring(pattern.substring(pattern.length()-6));

        ArrayList<substring> startList, endList; // 조건에 맞는 substring을 담을 ArrayList객체
        StringBuilder result = new StringBuilder(""); // 결과를 담을 StringBuilder

        // 패턴의 시작 6글자와 마지막 6글자 각각에 해당하는 dataTable의 slot을 찾고
        // 그 slot의 AVLTree에서 각 substring과 일치하는 노드를 찾아서
        // 해당 노드에 있는 substring들을 ArrayList에 담는다.
        startList = dataTable.find(start).search(start);

        endList = dataTable.find(end).search(end);


        if(startList != null && endList != null) {
            for (substring first : startList) {
                for (substring last : endList) {
                    if (first.inPattern(last, pattern.length())) {
                        // 패턴의 시작 6글자와 일치하는 substring의 ArrayList인 startList
                        // 패턴의 마지막 6글자와 일치하는 substring의 ArrayList인 endList
                        // 각 리스트에서 하나씩 뽑아서 같은 부분에서 나온 것인지 확인하여 추가
                        result.append(first.getKey() + " ");
                    }
                }
            }
        }

        // 결과를 출력한다.
        if (result.toString().equals("")) {
            System.out.println("(0, 0)");
        } else {
            System.out.println(result.toString().substring(0, result.length()-1));
        }
	}
}
