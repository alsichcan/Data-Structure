import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Subway {

    // 두 개의 HashMap을 이용한 Double Hashing 기법으로 DB를 관리한다.
    private static HashMap<String, Station> NameMap;
    private static HashMap<String, Station> NumMap;

    // 경로 탐색에 필요한 자료구조
    private static ArrayList<adjacentST> path;
    private static ArrayList<String> visited;
    private static HashMap<String, Integer> distance;
    private static DistanceList distanceList;

    public static void main(String args[]) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // 데이터 등록
        try {
            register(args[0]);
        } catch (Exception e) {
            System.out.println("잘못된 데이터 명입니다: " + e.toString());
        }

        // 명령 수행
        while (true) {
            try {
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0) {
                    break;
                }
                command(input);
            } catch (Exception e) {
                System.out.println("입력이 잘못되었습니다. 오류 :" + e.toString());
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void register(String data) throws Exception {
        FileInputStream fis = new FileInputStream(new File(data));
        InputStreamReader isr = new InputStreamReader(fis, "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String line;

        // Key : 역 이름 , Value: Station 객체
        NameMap = new HashMap<>();

        // Key : 역 번호, Value: Station 객체
        NumMap = new HashMap<>();

        // 역 정보 등록
        while ((line = br.readLine()).length() > 0) {
            // 입력받은 인풋을 ["역 번호", "역 이름", "역 노선"]으로 나눔
            String[] lineData = line.split("\\s");

            if (NameMap.containsKey(lineData[1])) {
                //  이미 입력되어있는 역 이름인 경우 (새로운 노선을 추가하는 경우)

                // 새로운 노선 추가
                NameMap.get(lineData[1]).addLine(lineData[1], lineData[0]);

                // Key: 추가되는 역 번호, Value: 해당 역 Station 객체 추가
                NumMap.put(lineData[0], NameMap.get(lineData[1]));

            } else {
                // 새롭게 입력되는 역 이름인 경우 새로운 Station 객체 생성
                Station newST = new Station(lineData[1], lineData[0]);

                // 두 개의 HashMap에 put
                NameMap.put(lineData[1], newST);
                NumMap.put(lineData[0], newST);
            }
        }

        // 역 간 이동시간 정보 입력
        while ((line = br.readLine()) != null) {
            // 입력받은 인풋을 ["출발역", "도착역", "이동 시간"]으로 나눔
            String[] lineData = line.split(" ");

            // 출발역 변호를 기준으로 해당하는 역을 찾은 뒤 인접 역 정보를 추가한다.
            NumMap.get(lineData[0]).getLine(lineData[0]).add_adjacent(new adjacentST(lineData[0], lineData[1], Integer.parseInt(lineData[2])));
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void command(String input) {
        // 입력받은 인풋을 ["출발지 역 명", "목적지 역 명"]으로 나눔
        String[] lineData = input.split("\\s");

        // (False) 최단시간경로를 구하는 것인지, (True) 최단환승경로를 구하는 것인지 나타내는 boolean
        boolean minTrans = false;

        if (lineData.length == 2) {
            //최단 경로 탐색
            minTrans = false;
        } else if (lineData.length == 3 && lineData[2].equals("!")) {
            // 최소 환승경로 탐색
            minTrans = true;
        } else {
            System.out.println("잘못된 입력입니다.");
        }

        // 경로를 탐색한다.
        search(lineData[0], lineData[1], minTrans);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void search(String start, String dest, Boolean minTrans) {
        path = new ArrayList<>(); // 방문된 역까지 이동한 edge를 포함하는 집합
        visited = new ArrayList<>(); // 해당 역이 방문되었는지를 판단하는 집합
        distance = new HashMap<>(); // 해당 역까지의 거리
        distanceList = new DistanceList();

        // 최소환승경로를 구하는 문제인 경우 환승시간을 역 간 최대 이동 시간(1억) + 1으로 설정한다.
        int TRANSFER = 5;
        if(minTrans) { TRANSFER = 100000001; }

        ////////////////////////////////////////////////////////////////////////////////////
        // 시작 역의 임의의 노선 하나를 시작점으로 설정한다.
        stLine stl = NameMap.get(start).getLine(0);



        // 거리 update 및 방문 표시
        distance.put(stl.getNum(), 0);

        visited.add(stl.getNum());


        // 해당 역이 환승역일 경우, 해당 역에서의 환승비용을 0으로 처리한다.
        for(adjacentST tr : stl.getTransferList()){
            update(stl,tr, 0);
        }

        // 시작 역과 인접한 역의 거리를 update
        for(adjacentST ad : stl.getAdjacentList()){
            update(stl, ad, -1);
        }
       // System.out.println("출발");
        ////////////////////////////////////////////////////////////////////////////////////////////

        while(!NumMap.get(distanceList.peek().getDest()).getName().equals(dest)){
            // 현재 상태에서 최단 시간에 도달할 수 있는 역이 목적지와 같을 때까지

            // 현재 접근할 수 있는 역 중 가장 가까운 역까지의 edge를 선택한다.
            adjacentST temp = distanceList.poll();
          //  System.out.println("찾음");

            // 최단 경로로 도달할 수 있는 역이 이미 방문된 역이라면 무시한다.
            if(visited.contains(temp.getDest())){ continue; }

            // 새롭게 추가된 역까지의 edge를 추가하고, 방문 표시
            path.add(temp);
            visited.add(temp.getDest());

            // 새롭게 추가한 역을 stn 변수로 표시
            stLine stn = NumMap.get(temp.getDest()).getLine(temp.getDest());

            // 새로운 역에서의 환승역까지의 거리를 update하고 update될 경우 추가한다.
            for(adjacentST tr : stn.getTransferList()){
                update(stn, tr, TRANSFER);
            }
         //   System.out.println("환승 성공");
            // 새로운 역에서의 인접역까지의 거리를 update하고 update될 경우 추가한다.
            for(adjacentST ad : stn.getAdjacentList()){
                update(stn, ad, -1);
            }
          //  System.out.println("인접 성공");

        }
        //////////////////////////////////////////////////////////////////////////////////////////
        // 목적지에 도착하는 edge
        adjacentST last = distanceList.poll();
        path.add(last);

        // 목적지까지의 최소 경로 시간
        int time = distance.get(last.getDest());

       // System.out.println("길 찾음");

        // 경로를 출력하기 위한 String 생성
        StringBuilder answer = new StringBuilder();
        for(String pth : getPath(start, last.getDest(), path)){
            answer.append(pth + " ");

            // 최소환승경로를 구하는 경우라면 환승 시간을 다시 알맞게 조정한다
            if(minTrans){
                if(pth.contains("[") && pth.contains("]")){
                    time -= (TRANSFER -5);
                }
            }
        }

        // 경로 출력e
        System.out.println(answer.substring(0, answer.length()-1));
        // 시간 출력
        System.out.println(time);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////
    private static void update(stLine stn, adjacentST ad, int time){
        int t;
        if(time == -1){
            t = ad.getTime();
        } else{
            t = time;
        }
        if(!visited.contains(ad.getDest())){
            // 새로운 역의 인접역이 아직 방문되지 않은 경우
            if(!distance.containsKey(ad.getDest())){
                // 해당 역까지의 거리가 한 번도 계산된 적이 없는 경우
                distance.put(ad.getDest(), distance.get(stn.getNum()) + t);
                distanceList.add(new adjacentST(ad.getStart(), ad.getDest(), distance.get(ad.getDest())));

            } else if (distance.get(ad.getDest()) > distance.get(stn.getNum()) + t){
                // 해당 역까지의 거리가 relaxation이 가능한 경우
                distanceList.delete(new adjacentST(ad.getStart(), ad.getDest(), distance.get(ad.getDest())));
                distance.replace(ad.getDest(), distance.get(stn.getNum()) + t);
                distanceList.add(new adjacentST(ad.getStart(), ad.getDest(), distance.get(ad.getDest())));
            } else if (distance.get(ad.getDest()) == distance.get(stn.getNum()) + t){
                // 해당 역까지의 거리가 동일한 경우
                distanceList.add(new adjacentST(ad.getStart(), ad.getDest(), distance.get(ad.getDest())));
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////
    private static ArrayList<String> getPath(String start, String dest, ArrayList<adjacentST> path){
        ArrayList<String> result = new ArrayList<>();
        String temp = dest;
        while(!NumMap.get(temp).getName().equals(start)){
            for(adjacentST st : path){
                if(st.getDest().equals(temp)){
                    String via = NumMap.get(temp).getName();

                    if(!result.isEmpty() && result.get(0).equals(via)){
                        // 환승역인 경우 [역 명]으로 표기
                        result.remove(0);
                        result.add(0,"[" + via + "]" );
                    } else{
                        result.add(0, via);
                    }

                    temp = st.getStart();
                    break;
                } // end if
            } //end for
        }// end while
        result.add(0, start);

        return result;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

}