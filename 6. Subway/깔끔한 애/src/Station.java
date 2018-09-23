import java.util.*;

public class Station {
    private String name; // 역의 이름
    private ArrayList<stLine> lineList; // 해당 역의 이름에 지나가는 여러 노선들을 집합

    public Station(String name, String stNum){
        this.name = name;
        lineList = new ArrayList<>();
        this.addLine(name, stNum);
    }
    //////////////////////////////////////////////////////////////////////////////
    public String getName(){ return this.name; }

    public void addLine(String name, String stNum){
        stLine stl = new stLine(name, stNum);

        // 환승역 정보를 서로 추가한다
        for(stLine other : lineList){
            stl.add_transfer(new adjacentST(stl.getNum(), other.getNum(), 5));
            other.add_transfer(new adjacentST(other.getNum(), stl.getNum(), 5));
        }

        lineList.add(stl);
    }

    public stLine getLine(String stNum){
        for(stLine stl: lineList){
            if(stl.getNum().equals(stNum)){
                return stl;
            }
        }
        return null;
    }

    public stLine getLine(int idx){
        return lineList.get(idx);
    }
}

class stLine{
    // 특정 노선 위의 한 역을 나타내는 객체이다

    String name;
    String stNum;
    private ArrayList<adjacentST> adjacentList; // 이웃한 역으로의 edge의 집합
    private ArrayList<adjacentST> transferList; // 환승 가능한 역으로의 edge의 집합

    public stLine(String name, String stNum){
        this.name = name;
        this.stNum = stNum;
        adjacentList = new ArrayList<>();
        transferList = new ArrayList<>();
    }

    //////////////////////////////////////////////////////
    public String getName(){ return this.name; }
    public String getNum() { return this.stNum; }

    public void add_adjacent(adjacentST newST){
        adjacentList.add(newST);
    }
    public void add_transfer(adjacentST newST){
        transferList.add(newST);
    }
    public ArrayList<adjacentST> getAdjacentList() {
        return adjacentList;
    }
    public ArrayList<adjacentST> getTransferList() {
        return transferList;
    }
}


class adjacentST implements Comparable<adjacentST> {
    // 역 간 edge를 표현하는 객체
    // [출발 역 번호, 도착 역 번호, 이동시간]으로 구성됨

    private String start;
    private String dest;
    private long time;

    public adjacentST(String start, String dest, long time){
        this.start = start;
        this.dest = dest;
        this.time = time;
    }

    public String getStart() {
        return start;
    }
    public String getDest() {
        return dest;
    }
    public long getTime() {
        return time;
    }

    // 이동시간이 낮은 순으로 정렬된다.
    @Override
    public int compareTo(adjacentST other){
        if (this.time > other.time){
            return 1;
        } else if (this.time < other.time){
            return -1;
        } else {
            return 0;
        }
    }

}