import javax.xml.crypto.Data;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 *
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를
 * 유지하는 데이터베이스이다.
 */
public class MovieDB {
    // MovieDB는 장르명을 헤드에, 영화 제목을 노드의 아이템으로 갖고 있는 링크드리스트 MyLinkedList<String> 각각을
    // 노드의 아이템으로서 갖고 있는 링크드리스트 MyLinkedList<MyLinkedList<String>>으로 구성되어있다.
    public MyLinkedList<MyLinkedList<String>> Database;
    public MyLinkedListIterator<MyLinkedList<String>> DatabaseIT;


    public MovieDB() {
        Database = new MyLinkedList<>();
    }

    // Database 상에 input의 Genre가 존재하는지 확인하고 해당 위치를 리턴한다.
    // 존재한다면 MovieDB상에서 해당 Genre가 사전순으로 몇 번째에 위치하는지를 리턴한다. (1번째~)
    // 존재하지 않는다면 0의 값을 리턴한다.
    public int checkGenre(String item){
        DatabaseIT = (MyLinkedListIterator) Database.iterator();
        int index = 0;
        while (DatabaseIT.hasNext()){
            index += 1;
            if (DatabaseIT.next().getHead().equals(item)){
                return index;
            }//end if
        } //end while
        return 0;
    } //end checkExist

    // Param으로 받은 MyLinkedList<String>에 Param의 Title이 존재하는지 확인하고 해당 위치를 리턴한다.
    // 존재한다면 MyLinkedList상에서 해당 Title이 사전순으로 몇 번째에 위치하는지를 리턴한다. (1번째~)
    // 존재하지 않는다면 0의 값을 리턴한다.
    public int checkTitle(MyLinkedList<String> ML, String item){
        MyLinkedListIterator<String> MLIT = (MyLinkedListIterator) ML.iterator();
        int index = 0;
        while (MLIT.hasNext()){
            index += 1;
            if (MLIT.next().equals(item)){
                return index;
            }//end if
        }//end while
        return 0;
    } //end checkTitle

    // Param으로 받은 index 값에 해당하는 순번에 위치한 MyLinkedList을 리턴받는다.
    // 일반적으로 checkGenre()를 통해서 리턴받은 integer값을 input으로 활용한다.
    public MyLinkedList<String> getList(int index){
        DatabaseIT =  (MyLinkedListIterator) Database.iterator();
        for (int i = 0 ; i < index ; i++){
            DatabaseIT.moveNext();
        } // end for
        return DatabaseIT.getCurr();
    }

    // param으로 받은 item의 Genre를 자신의 헤드에 담고 있는 링크드리스트를 아이템으로 갖는 노드가 Database에 있는지 확인한다.
    // genreIdx == 0 이면, (즉, Database 상에 존재하지 않으면) item의 Genre를 자신의 헤드에 담고 있는 링크드리스트를 새롭게 생성하고,
    // 이렇게 생성한 링크드리스트에 item의 Title을 추가한 뒤, 링크드리스트를 Database에 추가한다.
    // genreIdx != 0 이면, (즉, Database 상에 이미 존재한다면) item의 Genre를 자신의 헤드에 담고 있느 링크드리스트를 call해서
    // 해당 링크드리스트에 item의 Title을 추가한다.
    public void insert(MovieDBItem item) {
        int genreIdx = checkGenre(item.getGenre());
        if (genreIdx == 0){
            MyLinkedList ML = new MyLinkedList(item.getGenre());
            ML.add(item.getTitle());
            Database.add(ML);
        } //end if
        else {
            MyLinkedList ML = getList(genreIdx);
            ML.add(item.getTitle());
        } //end else
        System.out.println(Database.size());
    } // end insert

    // item이 Database에 이미 있는지 checkGenre와 checkTitle를 통해 확인한다.
    // 존재하지 않는다면, 아무 동작도 하지 않고 중단한다.
    // 이미 존재한다면, item의 Genre를 자신의 헤드에 담고 있는 링크드리스트를 call해서
    // 해당 링크드리스트에서 item의 Title을 담고 있는 노드로 이동한 뒤 제거한다.
    public void delete(MovieDBItem item) {
        int genreIdx = checkGenre(item.getGenre());
        int titleIdx = checkTitle(getList(genreIdx), item.getTitle());
        if (genreIdx == 0 || titleIdx == 0){ return;}
        else {
            MyLinkedListIterator<String> MLIT = (MyLinkedListIterator) getList(genreIdx).iterator();
            for (int i = 0; i < titleIdx; i++) {
                MLIT.moveNext();
            } //end for
            MLIT.remove();
            if (ML.)
            System.out.println(Database.size());
        } // end else
        System.out.println(Database.size());
    } //end delete

    // 먼저 결과를 출력할 MovieDBItem을 담을 MyLinkedList 클래스의 객체 searchList를 생성한다.
    // 이후 링크드리스트 Database의 노드의 아이템(MyLinkedList 클래스의 객체) 각각에 대해서
    // param으로 받은 term을 포함하는 String형의 자료를 아이템으로 갖고 있는 노드를 갖고 있는지 조사한다.
    // 조건을 만족하는 노드가 있을 경우, 그 Genre와 Title을 갖고 있는 MovieDBItem을 생성하여
    // searchList에 하나의 노드로서 추가한다.
    public MyLinkedList<MovieDBItem> search(String term) {
        MyLinkedList<MovieDBItem> searchList = new MyLinkedList();
        for (MyLinkedList ML : Database){
            MyLinkedListIterator MLIT = (MyLinkedListIterator) ML.iterator();
            while (MLIT.hasNext()){
                if (MLIT.next().toString().contains(term)){
                    searchList.add(new MovieDBItem(ML.getHead().toString(), MLIT.getCurr().toString()));
                } // end if
            } //end while
        } //end for
        return searchList;
    } //end search

    // 먼저 결과를 출력할 MovieDBItem을 담을 MyLinkedList 클래스의 객체 itemsList를 생성한다.
    // 이후 링크드리스트 Database의 노드의 아이템(MyLinkedList 클래스의 객체) 각각에 대해서
    // 각 노드의 head에 위치한 Genre와 노드의 노드의 아이템인 Title을 갖고 있는 MovieDBItem을 생성하여
    // itemsList에 하나의 노드로서 추가한다.
    public MyLinkedList<MovieDBItem> items() {
        MyLinkedList<MovieDBItem> itemsList = new MyLinkedList();
        for (MyLinkedList ML : Database){
            MyLinkedListIterator MLIT = (MyLinkedListIterator) ML.iterator();
            while (MLIT.hasNext()){
                itemsList.add(new MovieDBItem(ML.getHead().toString(), MLIT.next().toString()));
            } // end while
        } //end for
        return itemsList;
    }
}