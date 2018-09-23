import java.util.Iterator;
import java.util.NoSuchElementException;

public class ItemList<T> implements ListInterface<T>{
    // copy from 문병로 교수 "2018-1" 자료구조 2번째 과제 <MovieDatabase>의 뼈대코드 중 MyLinkedList.java

    // dummy head
    Node<T> head;
    int numItems;

    public ItemList() {
        head = new Node<T>(null);
    }

    public final Iterator<T> iterator() {
        return new ItemListIterator<T>(this);
    }

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

    @Override
    public void add(T item) {
        Node<T> last = head;
        while (last.getNext() != null) {
            last = last.getNext();
        }
        last.insertNext(item);
        numItems += 1;
    }

    @Override
    public void removeAll() {
        head.setNext(null);
    }
}

class ItemListIterator<T> implements Iterator<T> {
    // copy from 문병로 교수 "2018-1" 자료구조 2번째 과제 <MovieDatabase>의 뼈대코드 중 MyLinkedList.java
    private ItemList<T> list;
    private Node<T> curr;
    private Node<T> prev;

    public ItemListIterator(ItemList<T> list) {
        this.list = list;
        this.curr = list.head;
        this.prev = null;
    }

    @Override
    public boolean hasNext() {
        return curr.getNext() != null;
    }

    // get Items of the next Node and move to the next Node
    @Override
    public T next() {
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
        list.numItems -= 1;
        curr = prev;
        prev = null;
    }
}