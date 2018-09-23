import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {
    // MovieDB는 노드의 아이템으로 Genre 클래스의 객체를 갖고 있는 MyLinkedList 형태의 객체 Database로 관리된다.
    // DatabaseIT는 Database의 iterator이다.
    private MyLinkedList<Genre> Database;
    private MyLinkedListIterator<Genre> DatabaseIT;


    public MovieDB() {
        Database = new MyLinkedList<>();
    }


    public void insert(MovieDBItem item) {
        int answer; // 추가할 때의 순서에 관한 정보를 담는다.
        DatabaseIT = (MyLinkedListIterator<Genre>) Database.iterator();
        Genre objGenre = new Genre(item.getGenre()); // input에 대한 Genre 객체를 생성한다.

        // 맨 끝의 노드에 다다를 때까지 새롭게 추가하려는 영화의 장르와 기존에 있는 영화의 장르를 비교한다.
        while (DatabaseIT.hasNext()){
            // 참고: Genre 객체의 compareTo()는 영화 장르의 이름을 사전순으로 비교한다
            answer = DatabaseIT.getNext().compareTo(objGenre);

            // 새롭게 추가되는 영화 장르와 기존에 있는 영화 장르를 비교한 순서 정보에 따라 다음과 같이 진행한다.

            // answer < 0 : 새롭게 추가하려는 영화의 장르가 비교 대상인 기존 영화의 장르보다 사전순으로 후순위이다.
            // 다음 영화의 장르와 비교하기 위해 다음 노드를 부르고 루프를 다시 돌린다.
            if (answer < 0) {DatabaseIT.moveNext();} //end if

            // answer == 0 : 새롭게 추가하려는 영화의 장르가 비교 대상인 기존 영화의 장르와 같다.
            // 해당 장르의 MovieList에 영화 제목을 추가하고 메소드를 종료한다.
            else if (answer == 0){ DatabaseIT.getNext().addMovie(item.getTitle()); return; } // end else if

            // answer > 0 : 새롭게 추가하려는 영화의 장르가 비교 대상인 기존 영화의 장르보다 사전순으로 전순위이다.
            // 비교 대상이 되는 노드 앞에 새로운 노드를 추가하고, 추가된 노드의 MovieList에 새로운 영화 제목을 추가한다.
            // 메소드를 종료한다.
            else {
                DatabaseIT.addCurr(objGenre);
                objGenre.addMovie(item.getTitle());
                return;
            } //end else
        } //end while

        // 새롭게 추가하려는 영화의 장르가 비교대상이 되었던 모든 기존 영화의 장르의 후순위에 위치해야하거나
        // (즉, 위의 DatabaseIT.hasNext()가 False가 될 때까지 return되지 않고 루프를 진행함)
        // 아예 비교대상이 되는 노드가 없었던 경우 (즉, 위의 DatabaseIT.hasNext()가 처음부터 False여서 루프가 돌지 않은 경우)
        // 현재 노드 (head 또는 가장 마지막 노드) 다음에 새로운 노드를 추가하고,
        // 추가된 노드의 MovieList에 새로운 영화 제목을 추가한 뒤, 종료한다.
        Database.add(objGenre);
        objGenre.addMovie(item.getTitle());
    }

    public void delete(MovieDBItem item) {
        int answer; // 추가할 때의 순서에 관한 정보를 담는다.
        DatabaseIT = (MyLinkedListIterator<Genre>) Database.iterator();
        Genre objGenre = new Genre(item.getGenre());
        // 맨 끝의 노드에 다다를 때까지 삭제하려는 영화의 장르와 기존에 있는 영화의 장르를 비교한다.
        while (DatabaseIT.hasNext()) {
            // 참고: Genre 객체의 compareTo()는 영화 장르의 이름을 사전순으로 비교한다
            answer = DatabaseIT.getNext().compareTo(objGenre);

            // 삭제하려는 영화의 장르와 기존에 있는 영화의 장르를 비교한 순서 정보에 따라 다음과 같이 진행한다.

            // answer < 0 : 삭제하려는 영화의 장르가 비교 대상인 기존 영화의 장르보다 사전순으로 후순위이다.
            // 다음 영화의 장르와 비교하기 위해 다음 노드를 부르고 루프를 다시 돌린다.
            if (answer < 0) {DatabaseIT.moveNext();}

            // answer > 0 : 삭제하려는 영화의 장르가 비교 대상인 기존 영화의 장르보다 사전순으로 전순위이다.
            // 즉, 삭제하려는 영화의 장르가 애초에 존재하지 않는다는 의미이므로, 메소드를 종료한다.
            else if (answer > 0) {return;}

            // answer == 0 : 삭제하려는 영화의 장르와 비교 대상인 기존 영화의 장르가 같다.
            // 이제, 해당 영화 장르의 MovieList의 영화 제목들과 비교하여야한다.
            else {
                Node<String> title = DatabaseIT.next().getList().head;

                // 맨 끝의 노드에 다다를 때까지 삭제하려는 영화의 제목와 기존에 있는 영화의 제목을 비교한다.
                 while (title.getNext() != null) {
                    answer = title.getNext().getItem().compareTo(item.getTitle());
                     // 삭제하려는 영화의 제목와 기존에 있는 영화의 제목를 비교한 순서 정보에 따라 다음과 같이 진행한다.

                     // answer < 0 : 삭제하려는 영화의 제목이 비교 대상인 기존 영화의 제목보다 사전순으로 후순위이다.
                     // 다음 영화의 제목과 비교하기 위해 다음 노드를 부르고 루프를 다시 돌린다.
                    if (answer < 0) { title = title.getNext();}

                    // answer > 0 : 삭제하려는 영화의 제목이 비교 대상인 기존 영화의 제목보다 사전순으로 전순위이다.
                    // 즉, 삭제하려는 영화의 제목이 애초에 존재하지 않는다는 의미이므로, 메소드를 종료한다.
                    else if (answer > 0) {return;}

                    // answer == 0 : 삭제하려는 영화의 제목이 비교 대상인 기존 영화의 제목과 같다.
                    // 이제, 해당 영화 제목을 MovieList에서 삭제하고, 루프를 종료한다.
                    else {title.removeNext(); break;}
                } // end while

                // 만약 삭제하려는 영화를 삭제한 뒤 해당 장르의 영화가 존재하지 않는다면,
                // 해당 장르를 상징하는 Genre 객체를 삭제한다.
                if (DatabaseIT.getCurr().isEmptyList()){
                    DatabaseIT.remove();
                } // end if
            } // end else
        } //end while
    }

    // 먼저 결과를 출력할 MovieDBItem을 담을 MyLinkedList 클래스의 객체 searchList를 생성한다.
    // 이후 링크드리스트 Database의 노드의 아이템(Genre 클래스의 객체) 각각에 대해서
    // search하려는 term을 포함하는 영화 제목이 MovieList에 있는지 조사한다.
    // 조건을 만족하는 경우, 그 Genre와 Title을 갖는 MovieDBItem을 생성하여
    // searchList에 하나의 노드로서 추가한다.
    public MyLinkedList<MovieDBItem> search(String term) {
        MyLinkedList<MovieDBItem> searchList = new MyLinkedList<>();
        for (Genre genre : Database){
            for (String movie : genre.getList()){
                if (movie.contains(term)){
                    searchList.add(new MovieDBItem(genre.getName(), movie));
                } //end if
            } //end for
        } //end for
        return searchList;
    }

    // 먼저 결과를 출력할 MovieDBItem을 담을 MyLinkedList 클래스의 객체 itemsList를 생성한다.
    // 이후 링크드리스트 Database의 노드의 아이템(Genre 클래스의 객체) 각각에 대해서
    // 각각의 genreName과 MovieList의 genreML의 노드들의 아이템인 Title을 갖는 MovieDBItem을 생성하여
    // itemsList에 하나의 노드로서 추가한다.
    public MyLinkedList<MovieDBItem> items() {
        MyLinkedList<MovieDBItem> itemsList = new MyLinkedList<>();
        for (Genre genre : Database){
            for (String movie : genre.getList()){
                itemsList.add(new MovieDBItem(genre.getName(), movie));
            }//end for
        } //end for
        return itemsList;
    }
}

class Genre implements Comparable<Genre> {
    // Genre 객체는 두 가지 데이타 필드를 갖는다.
    // genreName은 장르의 이름이며, genreML은 해당 장르의 영화를 사전순으로 정렬한 LinkedList 타입의 MovieList이다.
    private String genreName;
    private MovieList genreML;

    // String 타입의 input을 받아 장르의 이름으로 설정하고, 해당 장르의 영화를 담을 MovieList 타입의 객체를 생성한다.
    public Genre(String name) {
		genreName = name;
		genreML = new MovieList();
	}

	// 장르의 이름을 리턴하는 메소드
	public String getName() { return genreName; }

	// 장르의 영화를 담고 있는 MovieList를 리턴하는 메소드
	public MovieList getList(){
        return genreML;
    }

    // 장르의 영화를 담고 있는 MovieList에 새로운 영화 제목을 추가하는 메소드
    public void addMovie(String title) {
        this.getList().add(title);
    }

    // 장르의 영화를 담고 있는 MovieList가 비어있는지 (즉 해당 장르의 영화가 없는지) 확인하는 메소드
    public boolean isEmptyList() { return this.getList().isEmpty(); }

    // Genre 객체의 비교는 장르의 이름을 사전순으로 비교해서 이루어진다.
	@Override
	public int compareTo(Genre o) {
		return this.getName().compareTo(o.getName());
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getName() == null) ? 0 : this.getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Genre other = (Genre) obj;
        if (this.getName() == null) {
            if (other.getName() != null)
                return false;
        } else if (!this.getName().equals(other.getName()))
            return false;
        return true;
    }
}

// 특정 장르에 속하는 영화들을 사전순으로 정렬한 LinkedList이다.
// add() 메소드를 제외하고는 MyLinkedList와 다른 점은 없다.
class MovieList implements ListInterface<String> {
	Node<String> head;
	int numItems;

    public MovieList() { head = new Node<>(null);}

	@Override
	public Iterator<String> iterator() {
		return new MovieListIterator(this);
	}

	@Override
	public boolean isEmpty() { return head.getNext() == null; }

	@Override
	public int size() { return numItems; }

	// MovieList에 새로운 영화를 추가하는 메소드
    // 영화 제목이 사전순으로 알맞은 위치에 추가되도록 한다.
	@Override
	public void add(String item) {
        int answer; // 추가할 때의 순서에 관한 정보를 담는다.

        Node<String> movie = head;

        while (movie.getNext() != null) {
        // 맨 끝의 노드에 다다를 때까지 새롭게 추가하려는 영화 제목(item)과 기존에 있는 영화 제목(movie 노드의 item)을 비교한다.
            answer = movie.getNext().getItem().compareTo(item);

            // 새롭게 추가되는 영화 제목과 기존에 있는 영화제목을 비교한 순서 정보에 따라 다음과 같이 진행한다.

            // answer < 0 : 새롭게 추가하려는 영화 제목이 비교 대상인 기존 영화 제목보다 사전순으로 후순위이다.
            // 다음 영화 제목과 비교하기 위해 다음 노드를 부르고 루프를 다시 돌린다.
            if (answer < 0) { movie = movie.getNext();}

            // answer > 0 : 새롭게 추가하려는 영화 제목이 비교 대상인 기존 영화 제목보다 사전순으로 전순위이다.
            // 비교 대상이 된 노드의 앞에 새로운 노드를 추가한다.
            else if (answer > 0) { movie.insertNext(item); numItems ++ ;return;}

            // answer = 0 : 새롭게 추가하려는 영화 제목이 비교 대상인 기존 영화 제목과 같다.
            // 메소드를 바로 종료한다.
            else {return;}
        } // end while

        // 새롭게 추가하려는 영화 제목이 비교대상이 되었던 모든 기존 영화 제목의 후순위에 위치해야하거나
        // (즉, 위의 movie.getNext()가 null이 될 때까지 return되지 않고 루프를 진행함)
        // 아예 비교대상이 되는 노드가 없었던 경우 (즉, 위의 movie.getNext()가 처음부터 null여서 루프가 돌지 않은 경우)
        // 현재 노드 (head 또는 가장 마지막 노드) 다음에 새로운 노드를 추가하고 종료한다.
        movie.insertNext(item);
        numItems++;
	}

	@Override
	public String first() {
		return head.getNext().getItem();
	}

	@Override
	public void removeAll() {
		head.setNext(null);
	}
}

class MovieListIterator implements Iterator<String> {
    private MovieList list;
    private Node<String> curr;
    private Node<String> prev;

    public MovieListIterator(MovieList list) {
        this.list = list;
        this.curr = list.head;
        this.prev = null;
    }

    @Override
    public boolean hasNext() {
        return curr.getNext() != null;
    }

    @Override
    public String next() {
        if (!hasNext())
            throw new NoSuchElementException();

        prev = curr;
        curr = curr.getNext();

        return curr.getItem();
    }

    @Override
    public void remove() {
        if (prev == null)
            throw new IllegalStateException("next() should be called first");
        if (curr == null)
            throw new NoSuchElementException();
        prev.removeNext();
        curr = prev;
        prev = null;
        list.numItems -= 1;
    }
}