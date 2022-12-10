package containers;

import algo.Sorts;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;

public interface List<T> {


    default void add(T t) {
        insert(size(), t);
    }

    default void addFirst(T t) {
        insert(0, t);
    }

    default void addLast(T t) {
        add(t);
    }


    void set(int index, T t);


    void insert(int index, T t);


    void remove(int index);

    default void removeFirst() {
        remove(0);
    }

    default void removeLast() {
        remove(size() - 1);
    }


    T get(int index);

    default T getFirst() {
        return get(0);
    }

    default T getLast() {
        return get(size() - 1);
    }


    default T popFirst() {
        T t = getFirst();
        removeFirst();
        return t;
    }

    default T popLast() {
        T t = getLast();
        removeLast();
        return t;
    }


    default boolean contains(T t) {
        for (int i = 0; i < size(); i++)
            if (Objects.equals(get(i), t))
                return true;
        return false;
    }


    default void forEach(Consumer<T> action) {
        for (int i = 0; i < size(); i++)
            action.accept(get(i));
    }


    void clear();


    int size();

    default boolean empty() {
        return size() == 0;
    }


    default void sort(Comparator<? super T> comparator) {
        sort(0, size(), comparator, Sorts.SortingType.DEFAULT);
    }

    default void sort(int start, int end, Comparator<? super T> comparator) {
        sort(start, end, comparator, Sorts.SortingType.DEFAULT);
    }

    default void sort(Comparator<? super T> comparator, Sorts.SortingType sortingType) {
        sort(0, size(), comparator, sortingType);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default void sort(int start, int end, Comparator<? super T> comparator, Sorts.SortingType sortingType) {
        Object[] array = toArray();
        Sorts.sort(array, start, end, (Comparator) comparator, sortingType);
        setData(array);
    }


    Object[] toArray();

    void setData(Object[] objs);
}