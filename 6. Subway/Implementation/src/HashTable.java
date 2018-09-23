import java.lang.reflect.Array;
import java.lang.reflect.Constructor;

public class HashTable<T extends AVLTree, M extends Station>{

    private T[] table;
    private final Constructor<? extends T> cnst;

    public HashTable(Class<? extends T> tClass) throws Exception {
        table = (T[]) Array.newInstance(tClass, 10000);
        this.cnst = tClass.getConstructor();
        for(int i=0; i < 10000 ; i++){
            table[i] = cnst.newInstance();
        }
    }

    public void insert(M item){
        table[item.hashCode()].insert(item);
    }


    public AVLTreeNode<M> retrieve(String item){
        return table[Math.abs(item.hashCode()) % 10000].search(item);
    }

    /*
    public T retrieve(int index){

        return table[index];
    }

    public T find(M item){

        return table[item.hashCode()];
    }
    */

}