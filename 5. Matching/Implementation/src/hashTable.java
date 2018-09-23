import java.lang.reflect.Array;
import java.lang.reflect.Constructor;

public class hashTable<T extends AVLTree, M extends substring>{

    private T[] table;
    private final Constructor<? extends T> cnst;

    public hashTable(Class<? extends T> tClass) throws Exception {
        // 크기가 100인 T[]을 선언한다.
        table = (T[]) Array.newInstance(tClass, 100);
        this.cnst = tClass.getConstructor();
        for(int i=0; i < 100 ; i++){
            table[i] = cnst.newInstance();
        }
    }

    public void insert(M item){
        // 해시테이블에 새로운 아이템을 추가한다
        table[item.hashCode()].insert(item);
    }

    public T retrieve(int index){
        // 해시테이블의 index번째 slot의 아이템을 반환한다
        return table[index];
    }

    public T find(M item){
        // 해시테이블에 item이 속할 slot의 아이템을 반환한다.
        return table[item.hashCode()];
    }

}