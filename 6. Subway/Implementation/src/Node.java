public class Node<T> {

    private T item;
    private Node<T> next;

    public Node(T obj) {
        this.item = obj;
        this.next = null;
    }

    public Node(T obj, Node<T> next) {
        this.item = obj;
        this.next = next;
    }

    public final T getItem() {
        return item;
    }

    public final void setItem(T item) {
        this.item = item;
    }

    public final void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getNext() {
        return this.next;
    }

    public final void insertNext(T obj) {
        Node<T> newNode = new Node<>(obj);
        if (this.getNext() == null){
            this.setNext(newNode);
        } //end if
        else{
            newNode.setNext(this.getNext());
            this.setNext(newNode);
        } //end else
    } //end InsertNext

    public final void removeNext() {
        if(this.getNext() == null){return;}

        if(this.getNext().getNext()  == null){this.setNext(null);} //end if
        else{this.setNext((this.getNext().getNext()));} //end else
    } //end removeNext
}