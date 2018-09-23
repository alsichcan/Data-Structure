import java.util.Iterator;
import java.util.NoSuchElementException;

public class StationList<T> implements ListInterface<T>{

    // dummy head
    Node<T> head;
    int numItems;

    public StationList() {
        head = new Node<>(null);
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
        head.insertNext(item);
        numItems += 1;
    }

    @Override
    public void removeAll() {
        head.setNext(null);
    }
}

class ItemListIterator<T> implements Iterator<T> {
    private StationList<T> list;
    private Node<T> curr;
    private Node<T> prev;

    public ItemListIterator(StationList<T> list) {
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