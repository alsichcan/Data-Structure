import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Subway {
    private static HashMap<String, String> StationMap;
    private static HashTable<AVLTree, Station> StationTable;

    public static void main(String args[])
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


        try {
            register(args[0]);
        }
        catch (Exception e){
            System.out.println("오류: " + e.toString());
        }

        while (true)
        {
            try
            {
                String input = br.readLine();
                long t = System.currentTimeMillis();
                if (input.compareTo("QUIT") == 0){
                    break;
                }
                command(input);
                System.out.println((System.currentTimeMillis() - t) + " ms");
            }
            catch (Exception e)
            {
                System.out.println("입력이 잘못되었습니다. 오류 :"+e.toString());
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void register(String data) throws Exception{
        FileInputStream fis =  new FileInputStream(new File(data));
        InputStreamReader isr = new InputStreamReader(fis, "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String line;
        StationMap = new HashMap<>();
        StationTable = new HashTable<>(AVLTree.class);

        // 역 정보 등록
        while((line = br.readLine()).length() > 0) {
            String[] lineData = line.split("\\s");
            Station station = new Station(lineData[1], lineData[0], lineData[2]);

            StationMap.put(station.getStNum(), station.getName());
            // Station 객체를 데이터베이스에 삽입
            StationTable.insert(station);
        }

        // 역 간 이동시간 정보 입력
        while((line = br.readLine()) != null){
            String[] lineData = line.split(" ");
            adjacentST adST = new adjacentST(lineData[0], lineData[1], Integer.parseInt(lineData[2]));
            String name = StationMap.get(lineData[0]);

            for(Object st : StationTable.retrieve(name).getList()){
                if(((Station) st).getStNum().compareTo(adST.getStart()) == 0){
                    ((Station) st).add_adjancent(adST);
                } // end if
            } // end for
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void command(String input) throws Exception{

        String[] lineData = input.split("\\s");
        boolean minTrans = false;
        if(lineData.length == 2){
            //최단 경로 탐색
            minTrans = false;
        } else if (lineData.length == 3 && lineData[2].equals("!")){
            // 최소 환승경로 탐색
            minTrans = true;
        } else{
            System.out.println("잘못된 입력입니다.");
        }

        minTime(lineData[0], lineData[1], minTrans);

/*
        AVLTreeNode station =  StationTable.retrieve(input);
        System.out.println(station.getName());

        for(Object st : station.getList()){
            Station stn = (Station) st;

            System.out.println("역 이름: " + stn.getName() + "역 번호: " +   stn.getStNum() + " 역 노선: " +  stn.getLine());
            for(adjacentST ad : stn.getAdjacentList()){
                System.out.println("이웃 역 명: " + StationMap.get(ad.getDest()) + " 이웃 역 번호: " +  ad.getDest() + " 이동시간: " +  ad.getTime());
            }
            for(adjacentST ad : stn.getTransferList()){
                System.out.println("환승 역 명: " + StationMap.get(ad.getDest())+ " 환승 역 번호: " +  ad.getDest() + " 이동시간: " + ad.getTime());
            }
        }
*/
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    private static void minTime(String start, String destination, Boolean minTrans) {
        ArrayList<adjacentST> path = new ArrayList<>(); // 집합 S
        ArrayList<String> visited = new ArrayList<>(); // 해당 역이 집합 S에 속하는지 판단하는 집합
        HashMap<String, Integer> distance = new HashMap<>(); // 해당 역까지의 거리
        PriorityQueue<adjacentST> adList = new PriorityQueue<>();
        adjacentST temp;
        int TRANSFER = 5;
        if(minTrans) { TRANSFER = 100000001; }


        ///////////////////////////////////////////////////////////////////////////////////////////
        // 시작 역의 임의의 노선 하나를 시작점으로 설정한다.
        Station st = StationTable.retrieve(start).getList().first();


        distance.put(st.getStNum(), 0);
        visited.add(st.getStNum()); //방문했다고 표시
        for(adjacentST tr : st.getTransferList()){ // 해당 역이 환승역일 경우, 해당 역에서의 환승비용을 0으로 처리한다.
            distance.put(tr.getDest(), 0);
            adList.add(new adjacentST(tr.getStart(), tr.getDest(), 0));

          // System.out.println("환승역: " + StationMap.get(tr.getDest())+ " " + tr.getDest());
        }

        for(adjacentST ad : st.getAdjacentList()){ // 시작 역과 인접한 역의 거리를 update
            distance.put(ad.getDest(), ad.getTime());
            adList.add(new adjacentST(ad.getStart(), ad.getDest(), ad.getTime()));
         // System.out.println("인접역: " + StationMap.get(ad.getDest())+ " " + ad.getDest());
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        while(!StationMap.get(adList.peek().getDest()).equals(destination)){
            // 현재 상태에서 최단 시간에 도달할 수 있는 역이 목적지와 같을 때까지

            temp = adList.poll();


     //       System.out.println("@@@@@" + "출발지: " + StationMap.get(temp.getStart()) + " >>>>>>> " +  "도착지: " + StationMap.get(temp.getDest()) + "||||| 소요시간: " + temp.getTime());

            // 최단 경로로 도달할 수 있는 이미 방문된 곳이라면 무시한다.
            if(visited.contains(temp.getDest())){
     //           System.out.println("@@@@@@@@@@@@@@@@@@@무시@@@@@@@@@@@@@@@@@");
                continue;
            }

            path.add(temp);
            visited.add(temp.getDest());
            Station stn = null;

            // 새롭게 추가된 역에 해당하는 Station 객체를 찾아서 stn 변수에 넣는다
            for(Station station : StationTable.retrieve(StationMap.get(temp.getDest())).getList()){
                if(station.getStNum().equals(temp.getDest())){
                    stn = station;
                }
            }

           // System.out.println("역 명: " + stn.getName() + "역 번호: " + stn.getStNum());

            // 새로운 역에서의 환승역까지의 거리를 update하고 update될 경우 추가한다.
            for(adjacentST tr : stn.getTransferList()){
                if(!visited.contains(tr.getDest())){
                    if(!distance.containsKey(tr.getDest())){
                        distance.put(tr.getDest(), distance.get(stn.getStNum())+ TRANSFER);
                        adList.add(new adjacentST(tr.getStart(), tr.getDest(), distance.get(tr.getDest())));
                       // System.out.println("환승역: " + StationMap.get(tr.getDest())+ " " + tr.getDest());
                    } else if (distance.get(tr.getDest()) > distance.get(stn.getStNum()) + TRANSFER){
                        distance.replace(tr.getDest(), distance.get(stn.getStNum()) + TRANSFER);
                        adList.add(new adjacentST(tr.getStart(), tr.getDest(), distance.get(tr.getDest())));
                       // System.out.println("환승역: " + StationMap.get(tr.getDest())+ " " + tr.getDest());
                    } else if (distance.get(tr.getDest()) == distance.get(stn.getStNum()) + TRANSFER){
                        adList.add(new adjacentST(tr.getStart(), tr.getDest(), distance.get(tr.getDest())));
                    }
                }

            }

            //System.out.println("환승거리 update 성공");


            // 새로운 역에서의 인접역까지의 거리를 update하고 update될 경우 추가한다.
            for(adjacentST ad : stn.getAdjacentList()){
                if(!visited.contains(ad.getDest())){
                    if(!distance.containsKey(ad.getDest())){
                        //System.out.println("도착역: " + ad.getDest() + " 소요시간: " + ad.getTime());
                        distance.put(ad.getDest(), distance.get(stn.getStNum()) + ad.getTime());
                        adList.add(new adjacentST(ad.getStart(), ad.getDest(), distance.get(ad.getDest())));
                 //       System.out.println("인접역: " + StationMap.get(ad.getDest())+ " " + ad.getDest());
                    } else if (distance.get(ad.getDest()) > distance.get(stn.getStNum()) + ad.getTime()){
                        distance.replace(ad.getDest(), distance.get(stn.getStNum()) + ad.getTime());
                        adList.add(new adjacentST(ad.getStart(), ad.getDest(), distance.get(ad.getDest())));
                      //  System.out.println("도착역: " + ad.getDest() + " 소요시간: " + ad.getTime());
                 //       System.out.println("인접역: " + StationMap.get(ad.getDest())+ " " + ad.getDest());
                    } else if (distance.get(ad.getDest()) == distance.get(stn.getStNum()) + ad.getTime()){
                        adList.add(new adjacentST(ad.getStart(), ad.getDest(), distance.get(ad.getDest())));
                       // System.out.println("도착역: " + ad.getDest() + " 소요시간: " + ad.getTime());
                    }
                }

            }
            //System.out.println("인접거리 update 성공");
        }
        //////////////////////////////////////////////////////////////////////////////////////////
        adjacentST last = adList.poll();
        path.add(last);
        int time = distance.get(last.getDest());

        //System.out.println("@@@@@" + "출발지: " + StationMap.get(last.getStart()) + " >>>>>>> " +  "도착지: " + StationMap.get(last.getDest()) + "||||| 소요시간: " + last.getTime());
       // System.out.println("도착역: " + StationMap.get(last.getDest()) + " " + last.getDest());

        //System.out.println("길은 다 찾음!");

        StringBuilder answer = new StringBuilder();
        for(String pth : getPath(start, last.getDest(), path)){
            if(minTrans){
                if(pth.contains("[") && pth.contains("]")){
                    time -= (TRANSFER -5);

                }
            }
            answer.append(pth + " ");
        }

        System.out.println(answer.substring(0, answer.length()-1));
        System.out.println(time);

    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    private static ArrayList<String> getPath(String start, String destination, ArrayList<adjacentST> path){
        ArrayList<String> result = new ArrayList<>();
        String temp = destination;
        while(!StationMap.get(temp).equals(start)){
            for(adjacentST st : path){
                if(st.getDest().equals(temp)){
                    result.add(0, StationMap.get(temp));
                    temp = st.getStart();
                    break;
                } // end if
            } //end for
        }// end while

        result.add(0, StationMap.get(temp));

        for(int i=1; i < result.size()-1; i++){
            if(result.get(i).equals(result.get(i+1))){
                result.remove(i+1);
                result.add(i, "[" + result.get(i) + "]");
                result.remove(i+1);
            }
        } // end for

        return result;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

}
