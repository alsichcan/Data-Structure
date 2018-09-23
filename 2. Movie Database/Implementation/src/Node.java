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
        // insertNext() 의 대상이 되는 노드가 마지막 노드인지에 따라 다름
        if (this.next == null){
            this.setNext(newNode);
        } //end if
        else{
            newNode.setNext(this.getNext());
            this.setNext(newNode);
        } // end else
    } //end InsertNext
    
    public final void removeNext() {
        // removeNext() 의 대상이 되는 노드의 다음 노드가 마지막 노드인지에 따라 다름
        if (this.getNext().next == null){
            this.setNext(null);
        } // end if
        else {
            this.setNext(this.getNext().getNext());
        } //end else
    }  //end removeNext
}