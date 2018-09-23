import java.util.*;

public class Station implements Comparable<Station> {
    private String name;
    private String stNum;
    private String line;
    private ArrayList<adjacentST> adjacentList;
    private ArrayList<adjacentST> transferList;

    public Station(String name){
        this.name = name;
    }

    public Station(String name, String stNum){
        this.name = name;
        this.stNum = stNum;
    }

    public Station(String name, String stNum, String line){
        this.name = name;
        this.stNum = stNum;
        this.line = line;
        adjacentList = new ArrayList<>();
        transferList = new ArrayList<>();
    }

    //////////////////////////////////////////////////////////////////////////////
    public String getName(){ return this.name; }
    public String getStNum() {
        return this.stNum;
    }
    public String getLine() { return this.line;}


    public void add_adjancent(adjacentST newST){
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
    ///////////////////////////////////////////////////////////////////////////////

    @Override
    public int compareTo(Station other){
        return this.name.compareTo(other.name);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Station other = (Station) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (stNum == null) {
            if (other.stNum != null)
                return false;
        } else if (!stNum.equals(other.stNum))
            return false;
        if (line == null) {
            if (other.line != null)
                return false;
        } else if (!line.equals(other.line))
            return false;
        return true;
    }


    @Override
    public int hashCode() {
        return (Math.abs(name.hashCode()) % 10000);
    }



}

class adjacentST implements Comparable<adjacentST> {
    private String start;
    private String dest;
    private int time;

    public adjacentST(String start, String dest, int time){
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

    public int getTime() {
        return time;
    }

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