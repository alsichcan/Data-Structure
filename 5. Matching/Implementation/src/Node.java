public class Node<T> {
    // copy from 문병로 교수 "2018-1" 자료구조 2번째 과제 <MovieDatabase>의 뼈대코드 중 Node.java
    // insertNext()와 removeNext()는 본인이 작성했었음

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
        // insertNext()의 대상이 되는 노드가 마지막 노드인지에 따라 다름
        if (this.getNext() == null){
            this.setNext(newNode);
        } //end if
        else{
            newNode.setNext(this.getNext());
            this.setNext(newNode);
        } //end else
    } //end InsertNext

    public final void removeNext() {
        // 다음 노드가 존재하지 않는다면 아무 것도 이루어지지 않음.
        if(this.getNext() == null){return;}

        // 다음 노드가 마지막 노드인지에 따라 실행 명령이 다름.
        if(this.getNext().getNext()  == null){this.setNext(null);} //end if
        else{this.setNext((this.getNext().getNext()));} //end else
    } //end removeNext
}