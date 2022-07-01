package DataStructure;

public interface LinkedList<E> {

    int size();
    void addFirst(E e);
    void addLast(E e);
    E first();
    E last();
    E removeFirst();
}
