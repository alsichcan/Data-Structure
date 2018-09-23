import java.util.ArrayList;

public class AVLTree<T extends substring> {
    private AVLTreeNode<T> root;

    ///////////////////////////////////////////////////////////////////////////////////////////////

    // 문병로 교수의 자료구조 수업의 강의노트 중 Binary Search Tree의 insert()메소드를 참고하여 변용함
    public void insert(T newItem) {
        root = insertItem(root, newItem);
    }

    private AVLTreeNode<T> insertItem(AVLTreeNode<T> AVLNode, T newItem) {
        if (AVLNode == null) {
            AVLNode = new AVLTreeNode<T>(newItem, null, null);
        } else if (AVLNode.retrieve().compareTo(newItem) == 0) {
            AVLNode.insert(newItem);
            return AVLNode;
        } else if (AVLNode.retrieve().compareTo(newItem) > 0) {
            AVLNode.setLeft(insertItem(AVLNode.getLeft(), newItem));
        } else {
            AVLNode.setRight(insertItem(AVLNode.getRight(), newItem));
        }

        updateHeight(AVLNode);
        updateBalance(AVLNode);

        if (AVLNode.getHeightDiff() > 1 && AVLNode.getLeft().retrieve().compareTo(newItem) > 0){
            // 기준 노드의 왼쪽 자식노드의 왼쪽 서브트리에 새 노드가 삽입된 경우
            // 기준 노드의 왼쪽 자식노드를 중심으로 우회전 1회 실행
            AVLNode = rightRotate(AVLNode);

        } else if (AVLNode.getHeightDiff() > 1 && AVLNode.getLeft().retrieve().compareTo(newItem) < 0) {
            // 기준 노드의 왼쪽 자식노드의 오른쪽 서브트리에 새 노드가 삽입된 경우
            // 기준 노드의 왼쪽 자식노드의 오른쪽 자식노드를 기준으로 자회전 1회 실행하고
            // 기준 노드의 왼쪽 자식노드를 중심으로 우회전을 1회 실행
            AVLNode.setLeft(leftRotate(AVLNode.getLeft()));
            AVLNode = rightRotate(AVLNode);

        } else if (AVLNode.getHeightDiff() < -1 && AVLNode.getRight().retrieve().compareTo(newItem) > 0) {
            // 기준 노드의 오른쪽 자식노드의 왼쪽 서브트리에 새 노드가 삽입된 경우
            // 기준 노드의 오른쪽 자식노드의 왼쪽 자식노드를 기준으로 우회전 1회 실행하고
            // 기준 노드의 오른쪽 자식노드를 중심으로 좌회전을 1회 실행
            AVLNode.setRight(rightRotate(AVLNode.getRight()));
            AVLNode = leftRotate(AVLNode);

        } else if (AVLNode.getHeightDiff() < -1 && AVLNode.getRight().retrieve().compareTo(newItem) < 0) {
            //기준 노드의 오른쪽 자식노드의 오른쪽 서브트리에 새 노드가 삽입된 경우
            //기준 노드의 오른쪽 자식노드를 기준으로 자회전을 1회 실행
            AVLNode = leftRotate(AVLNode);
        }
        return AVLNode;
    }

    private AVLTreeNode<T> leftRotate(AVLTreeNode<T> AVLNode){
        AVLTreeNode<T> temp = AVLNode;

        AVLNode = AVLNode.getRight();
        temp.setRight(AVLNode.getLeft());
        AVLNode.setLeft(temp);

        updateHeight(temp);
        updateHeight(AVLNode);

        return AVLNode;
    }

    private AVLTreeNode<T> rightRotate(AVLTreeNode<T> AVLNode){
        AVLTreeNode<T> temp = AVLNode;
        AVLNode = AVLNode.getLeft();
        temp.setLeft(AVLNode.getRight());
        AVLNode.setRight(temp);

        updateHeight(temp);
        updateHeight(AVLNode);

        return AVLNode;
    }

    /////////////////////////////////////////////////////////////////////////////////

    private int getHeight(AVLTreeNode<T> AVLNode){
        if (AVLNode == null){
            return 0;
        }else{
            return AVLNode.getHeight();
        }
    }

    private void updateHeight(AVLTreeNode<T> AVLNode){
        AVLNode.setHeight(Math.max(getHeight(AVLNode.getLeft()), getHeight(AVLNode.getRight())) + 1);
    }

    private void updateBalance(AVLTreeNode<T> AVLNode){
        AVLNode.setHeightDiff(getHeight(AVLNode.getLeft()) - getHeight(AVLNode.getRight()));
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    public void print() {
        StringBuilder result = new StringBuilder(""); // 반환할 결과를 담을 StringBuilder 객체
        System.out.println(preOrder(root, result));
    }

    private String preOrder(AVLTreeNode<T> root, StringBuilder result) {
        // 문병로 교수의 자료구조 수업의 강의 노트 중 preorder traversal에 관한 메소드를 참고하여 변형함
        if (root != null) {
            // root 노드가 나타내는 String 값을 StringBuilder에 추가한다.
            // String 값은 노드가 담고있는 itemList의 첫번째 substring 객체의 value값이다.
            result.append(root.retrieve().getValue() + " ");

            // 재귀 반복
            preOrder(root.getLeft(), result);
            preOrder(root.getRight(), result);
        }

        if (result.toString().equals("")) {
            return "EMPTY";
        } else {
            return result.toString().substring(0, result.length()-1);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<T> search(T pattern){
        ArrayList<ItemList<T>> box = new ArrayList<ItemList<T>>(); // 반환할 결과를 담은 ArrayList 객체
        ArrayList<ItemList<T>> list = preSearch(root, pattern, box);

        if(list.size() == 0){
            return null;
        }else{
            ArrayList<T> answer = new ArrayList<T>();

            // box에 담겨있는 itemList의 item들을 ArrayList에 추가한다.
            for(T t : list.get(0)){
                answer.add(t);
            }
            return answer;
        }
    }

    private ArrayList<ItemList<T>> preSearch(AVLTreeNode<T> root, T pattern, ArrayList<ItemList<T>> box) {
        // 문병로 교수의 자료구조 수업의 강의 노트 중 preorder traversal에 관한 메소드를 참고하여 변형함
        if (root != null) {
            if (pattern.compareTo(root.retrieve()) == 0){
                // root 노드가 나타내는 String 값과 pattern이 같으면
                // 해당 노드가 담고 있는 itemList를 box에 추가한다.
                box.add(root.getList());
            }

            // 재귀 반복
            preSearch(root.getLeft(), pattern, box);
            preSearch(root.getRight(), pattern, box);
        }
        return box;
    }
}
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


class AVLTreeNode<T>{
    private ItemList<T> itemList; // 같은 String 값을 갖는 substring 객체들을 담는 LinkedList 타입의 객체
    private AVLTreeNode<T> leftChild;
    private AVLTreeNode<T> rightChild;
    private int height;
    private int heightDiff;

    //////////////////////////////////////////////////////////////////////////
    public AVLTreeNode(T newItem, AVLTreeNode<T> left, AVLTreeNode<T> right) {
        itemList = new ItemList<T>();
        itemList.add(newItem);
        leftChild = left;
        rightChild = right;
        height = 1;
        heightDiff = 0;
    }
    /////////////////////////////////////////////////////////////////////////////

    public void setLeft(AVLTreeNode<T> left){
        leftChild = left;
    }
    public void setRight(AVLTreeNode<T> right){
        rightChild = right;
    }
    public AVLTreeNode<T> getLeft(){
        return leftChild;
    }
    public AVLTreeNode<T> getRight(){
        return rightChild;
    }

    public void setHeight(int height){
        this.height = height;
    }
    public void setHeightDiff(int heightDiff){
        this.heightDiff = heightDiff;
    }
    public int getHeight(){ return this.height; }
    public int getHeightDiff(){
        return this.heightDiff;
    }

    /////////////////////////////////////////////////////////////////////////////////
    public void insert(T newItem){
        // newItem을 itemList의 맨 마지막에 추가
        itemList.add(newItem);
    }


    public T retrieve(){
        // 해당 노드가 대표하는 String값을 알기 위해
        // itemList의 첫번째 substring을 반환한다
        return itemList.first();
    }

    public ItemList<T> getList(){
        // 해당 노드가 담고 있는 itemList를 반환한다
        return itemList;
    }



}





