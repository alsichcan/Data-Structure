import java.util.ArrayList;
import java.io.*;

public class AVLTree<T extends Station>{
    public AVLTreeNode<T> root;

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void insert(T newItem) {
        root = insertItem(root, newItem);
    }

    private AVLTreeNode<T> insertItem(AVLTreeNode<T> AVLNode, T newItem) {

        //System.out.println("newitem: " + newItem.getStNum() + " hashCode: " + Math.abs(newItem.hashCode()) % 10000 + " name: " + newItem.getName());
        if (AVLNode == null) {
            AVLNode = new AVLTreeNode<>(newItem, null, null);
           // System.out.println("create");

        } else if (AVLNode.getName().compareTo(newItem.getName()) == 0) {
            AVLNode.insert(newItem);
          //  System.out.println("same");
            return AVLNode;
        } else if (AVLNode.getName().compareTo(newItem.getName()) > 0) {
          //  System.out.println("left");
            AVLNode.setLeft(insertItem(AVLNode.getLeft(), newItem));
        } else {
          //  System.out.println("right");
            AVLNode.setRight(insertItem(AVLNode.getRight(), newItem));
        }

        updateHeight(AVLNode);
        updateBalance(AVLNode);

        if (AVLNode.getHeightDiff() > 1 && AVLNode.getLeft().getName().compareTo(newItem.getName()) > 0){
            AVLNode = rightRotate(AVLNode);

        } else if (AVLNode.getHeightDiff() > 1 && AVLNode.getLeft().getName().compareTo(newItem.getName()) < 0) {
            AVLNode.setLeft(leftRotate(AVLNode.getLeft()));
            AVLNode = rightRotate(AVLNode);

        } else if (AVLNode.getHeightDiff() < -1 && AVLNode.getRight().getName().compareTo(newItem.getName()) > 0) {
            AVLNode.setRight(rightRotate(AVLNode.getRight()));
            AVLNode = leftRotate(AVLNode);

        } else if (AVLNode.getHeightDiff() < -1 && AVLNode.getRight().getName().compareTo(newItem.getName()) < 0) {
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

    ///////////////////////////////////////////////////////////////////////////////////

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
    //////////////////////////////////////////////////////////////////////////////////////

    public AVLTreeNode<T> search(String searchItem){
        return searchItem(root, searchItem);
    }

    private AVLTreeNode<T> searchItem(AVLTreeNode<T> root, String searchItem) {
        //System.out.println("searchItem: " + searchItem);
        AVLTreeNode<T> result = null;
        if (root != null) {
           // System.out.println("root: " + root.retrieve().getStNum());
            if (root.getName().compareTo(searchItem) == 0){
              //  System.out.println("found");
                result = root;
            }else if (root.getName().compareTo(searchItem) > 0){
                //System.out.println("left");
                result = searchItem(root.getLeft(), searchItem);
            }else{
               // System.out.println("right");
                result = searchItem(root.getRight(), searchItem);
            } // end if
        }
        return result;
    }




}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////


class AVLTreeNode<T extends Station>{
    private String name;
    private ArrayList<String> lineList;
    private StationList<T> stList;
    private AVLTreeNode<T> leftChild;
    private AVLTreeNode<T> rightChild;
    private int height;
    private int heightDiff;

    //////////////////////////////////////////////////////////////////////////
    public AVLTreeNode(T newItem, AVLTreeNode<T> left, AVLTreeNode<T> right) {
        this.name = newItem.getName();

        stList = new StationList<>();
        stList.add(newItem);

        lineList = new ArrayList<>();
        lineList.add(newItem.getStNum());

        leftChild = left;
        rightChild = right;
        height = 1;
        heightDiff = 0;
    }
    /////////////////////////////////////////////////////////////////////////////

    public String getName(){ return this.name; }
    public ArrayList<String> getLine(){ return lineList; }

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
        // 새로 추가되는 역에 대한 환승 정보를 추가
        for(String line : lineList){  // 추가되는 역의 환승List에 사전에 입력되어있던 역을 추가
            newItem.add_transfer(new adjacentST(newItem.getStNum(), line, 5));
        }
        for(Station st : stList){  // 사전에 입력되어있던 역에 추가되는 역에 대한 환승정보를 환승List에 추가
            st.add_transfer(new adjacentST(st.getStNum(), newItem.getStNum(), 5));
        }
        // lineList에 새롭게 추가되는 역을 추가
        lineList.add(newItem.getStNum());
        // newItem을 stList의 맨 처음에 추가
        stList.add(newItem);
    }

    public StationList<T> getList(){
        // 해당 노드가 담고 있는 stList를 반환한다
        return stList;
    }
}





