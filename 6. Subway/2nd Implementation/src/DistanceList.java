import java.util.NoSuchElementException;

class DistanceList{
    Node<Integer> head;
    int numItems;

    public DistanceList() { head = new Node<>(null);}

    public DistanceListIterator iterator() {
        return new DistanceListIterator(this);
    }

    public boolean isEmpty() { return head.getNext() == null; }


    public int size() { return numItems; }

    public void add(adjacentST item) {
        Node<Integer> value = head;
        System.out.println("추가하려는 값: " + item.getTime());
        System.out.println("Bucket 개수: " + numItems);

        while (value.getNext() != null) {
            int bucket = value.getNext().getItem();
            System.out.println("비교대상: " + bucket + "추가하려는 값: " + item.getTime());

            if (bucket < item.getTime()) {
                value = value.getNext();
                System.out.println("여기");
                }

            else if (bucket > item.getTime()) {
                System.out.println("저기");
                value.insertNext(item.getTime());
                value.getNext().add(item);
                numItems ++ ;
                System.out.println("새 bucket 생성");
                return;
            }

            else {
                value.getNext().add(item);
                System.out.println("bucket에 추가");
                return;
            }
        } // end while
        value.insertNext(item.getTime());
        value.getNext().add(item);
        numItems++;
        System.out.println("맨 끝!");
    }

    public void delete(adjacentST item){
        DistanceListIterator DLIT = iterator();
        System.out.println("여기가 잘못이니??");
        while(!DLIT.hasNext()){
            if(DLIT.next() == item.getTime()){
                DLIT.getCurr().delete(item);
                System.out.println("@@@@@@@@@@삭제@@@@@@@@@@@@");
                break;
            }
        }
        if(DLIT.getCurr().getBucketSize() == 0){
            DLIT.remove();
        }

    }

    public adjacentST peek() {
        return head.getNext().peek();
    }

    public adjacentST poll(){
        adjacentST temp = head.getNext().poll();

        if(head.getNext().getBucketSize() == 0){
            head.removeNext();
            numItems--;
        }

        return temp;
    }


    public void removeAll() {
        head.setNext(null);
    }
}


class DistanceListIterator{
    private DistanceList list;
    private Node<Integer> curr;
    private Node<Integer> prev;
    private Node<Integer> head;

    public DistanceListIterator(DistanceList list) {
        this.head = list.head;
        this.list = list;
        this.curr = list.head;
        this.prev = null;
    }

    public boolean hasNext() {
        return curr.getNext() != null;
    }

    public int next() {
        if (!hasNext())
            throw new NoSuchElementException();

        prev = curr;
        curr = curr.getNext();

        return curr.getItem();
    }

    public Node<Integer> getCurr(){
        return curr;
    }

    public void remove() {
        if (prev == null)
            prev = head;
            //throw new IllegalStateException("next() should be called first");
        if (curr == null)
            throw new NoSuchElementException();
        prev.removeNext();
        curr = prev;
        prev = null;
        list.numItems -= 1;
    }
}
