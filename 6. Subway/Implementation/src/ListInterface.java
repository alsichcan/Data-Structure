public interface ListInterface<T> extends Iterable<T> {
    // copy from 문병로 교수 "2018-1" 자료구조 2번째 과제 <MovieDatabase>의 뼈대코드 중 ListInterface.java

    public boolean isEmpty();

    public int size();

    public void add(T item);

    public T first();

    public void removeAll();
}