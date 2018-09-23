
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<T> implements ListInterface<T> {
	// dummy head
	Node<T> head;
	int numItems;


	public MyLinkedList() {
		head = new Node<T>(null);
	}

	public MyLinkedList(T obj) { head = new Node<T>(obj); }
    /**
     * {@code Iterable<T>}를 구현하여 iterator() 메소드를 제공하는 클래스의 인스턴스는
     * 다음과 같은 자바 for-each 문법의 혜택을 볼 수 있다.
     * 
     * <pre>
     *  for (T item: iterable) {
     *  	item.someMethod();
     *  }
     * </pre>
     * 
     * @see PrintCmd#apply(MovieDB)
     * @see SearchCmd#apply(MovieDB)
     * @see java.lang.Iterable#iterator()
     */
    public final Iterator<T> iterator() {
    	return new MyLinkedListIterator<T>(this);
    }

    public T getHead() {return head.getItem();}

	@Override
	public boolean isEmpty() {
		return head.getNext() == null;
	}

	@Override
	public int size() {
		return numItems;
	}

	@Override
	public T first() {
		return head.getNext().getItem();
	}

	// add 메소드는 MyLinkedList에 새로운 Node를 추가하고자할 때,
    // 스펙에서 요구되는 정렬 조건을 만족시키기 위한 방법을 정의하고 있다.
    @Override
    public void add(T item) {
        int answer = 0;  // 추가할 때의 순서에 관한 정보를 담는다.
        MyLinkedListIterator<T> IT = (MyLinkedListIterator<T>) iterator();
        while (IT.hasNext()) {
            if (item instanceof String){
                // 노드의 아이템으로서 추가하려는 자료형이 String인 경우,
                // 기존에 존재하는 노드의 아이템과 item을
                // compareTo()메소드를 통해서 비교한 값을 answer에 대입한다.
                answer = item.toString().compareTo(IT.next().toString());
            }
            else if (item instanceof MyLinkedList){
                // 노드의 아이템으로서 추가하려는 자료형이 MyLinkedList인 경우,
                // 기존에 존재하는 노드의 아이템 (MyLinkedList)의 head 값과 item (MyLinkedList)의 head 값을
                // compareTo()메소드를 통해서 비교한 값을 answer에 대입한다.
                MyLinkedList thisList = (MyLinkedList) item;
                MyLinkedList nextList = (MyLinkedList) IT.next();
                answer = thisList.head.getItem().toString().compareTo(nextList.head.getItem().toString());
            }
            else if (item instanceof MovieDBItem){
                // 노드의 아이템으로서 추가하려는 자료형이 MovieDBItem인 경우,
                // 기존에 존재하는 노드의 아이템 (MovieDBItem)과 item을
                // MoovieDBItem 클래스에서 override한 compareTo()메소드를 통해서 비교한 값을 answer에 대입한다.
                answer = ((MovieDBItem) item).compareTo(((MovieDBItem) IT.next()));
            }
            else {System.err.println("should not reach here");}
                // 본 문제에서 이 이외의 아이템을 추가하는 경우가 없다.

            // 새롭게 추가되는 Node와 기존에 있는 Node를 비교한 순서 정보에 따라 다음과 같이 진행한다.

            // answer > 0 : 새롭게 추가하려는 Node가 지금 비교대상이 되는 Node보다 후순위에 와야하므로
            // 다음 노드와 비교하기 위해 루프를 더 돌린다.
            if (answer > 0) {continue;}

            // answer < 0 : 새롭게 추가하려는 Node가 지금 비교대상이 되는 Node보다 바로 앞에 와야하므로
            // 지금 비교대상이 되는 Node의 앞에 새로운 노드를 추가하고 종료한다.
            else if (answer < 0) {IT.addPrev(item); return;}

            // answer = 0 : 새롭게 추가하려는 Node가 지금 비교대상이 되는 Node와 같으므로
            // 아무런 행동을 하지 않고 종료한다.
            else {return;}
        }//end while

        // 새롭게 추가하려는 Node가 비교대상이 되었던 모든 노드의 후순위에 위치해야하거나
        // (즉, 위의 IT.hasNext()가 False가 될 때까지 return되지 않고 루프를 진행함)
        // 아예 비교대상이 되는 노드가 없었던 경우 (즉, 위의 IT.hasNext()가 처음부터 False여서 루프가 돌지 않은 경우)
        // 현재 위치에 새로운 노드를 추가하고 종료한다.
        IT.addCurr(item);
    } //end add

	@Override
	public void removeAll() {
		head.setNext(null);
	}
} //end MyLinkedList

class MyLinkedListIterator<T> implements Iterator<T> {
	// FIXME implement this
	// Implement the iterator for MyLinkedList.
	// You have to maintain the current position of the iterator.
	private MyLinkedList<T> list;
	private Node<T> curr;
	private Node<T> prev;

	public MyLinkedListIterator(MyLinkedList<T> list) {
		this.list = list;
		this.curr = list.head;
		this.prev = null;
	}

	@Override
	public boolean hasNext() {
		return curr.getNext() != null;
	}

	//get Items of the next Node and move to the Nextnode
	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();
		prev = curr;
		curr = curr.getNext();
		return curr.getItem();
	}

	// move to the Nextnode
    public void moveNext(){
        if (!hasNext()){ㅊ
            throw new NoSuchElementException();
        }
        prev = curr;
        curr = curr.getNext();
    }

    // remove the current Node
	@Override
	public void remove() {
		if (prev == null)
			throw new IllegalStateException("next() should be called first");
		if (curr == null)
			throw new NoSuchElementException();
		prev.removeNext();
		this.list.numItems -= 1;
		System.out.println("Done");
		curr = prev;
		prev = null;
	}

	// get the item of the current Node
	public T getCurr(){return curr.getItem();}

	// add the Node that contains T obj as an item before the Current Node
	public void addPrev(T obj){prev.insertNext(obj); this.list.numItems += 1;}

	// add the Node that contains T obj as an item after the Current Node
    public void addCurr(T obj){curr.insertNext(obj); this.list.numItems += 1;}
}