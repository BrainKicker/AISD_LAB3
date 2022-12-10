package containers;

import algo.Sorts;

import java.util.Comparator;

public class ArrayList<T> implements List<T> {


    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    private static final double CAPACITY_MULTIPLIER = 1.73205080757;


    private Object[] arr;
    private int size = 0;


    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
    }

    private void ensureCapacity() {
        ensureCapacity(size + 1);
    }

    // PUBLIC
    public void ensureCapacity(int minCapacity) {
        if (capacity() >= minCapacity)
            return;
        setCapacity(Math.max((int) (capacity() * CAPACITY_MULTIPLIER), minCapacity));
    }

    private void setCapacity(int newCapacity) {
        if (newCapacity < size)
            throw new IllegalArgumentException();
        if (arr == null) {
            arr = new Object[newCapacity];
            return;
        }
        if (newCapacity == capacity())
            return;
        Object[] newArr = new Object[newCapacity];
        System.arraycopy(arr, 0, newArr, 0, size);
        arr = newArr;
    }


    public ArrayList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayList(int initialCapacity) {
        arr = new Object[initialCapacity];
    }

    public ArrayList(T[] objs) {
        setData(objs);
    }

    public ArrayList(ArrayList<T> other) {
        arr = new Object[other.arr.length];
        System.arraycopy(other.arr, 0, arr, 0, other.arr.length);
        size = arr.length;
    }


    @Override
    public void add(T t) {
        ensureCapacity();
        arr[size] = t;
        size++;
    }

    @Override
    public void set(int index, T t) {
        checkIndex(index);
        arr[index] = t;
    }

    @Override
    public void insert(int index, T t) {
        if (index < 0 || index > size) // > size, not >= size
            throw new IndexOutOfBoundsException();
        ensureCapacity();
        System.arraycopy(arr, index, arr, index + 1, size - index);
        arr[index] = t;
        size++;
    }

    @Override
    public void remove(int index) {
        checkIndex(index);
        System.arraycopy(arr, index + 1, arr, index, size - index - 1);
        arr[size()-1] = null;
        size--;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) arr[index];
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++)
            arr[i] = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    public int capacity() {
        return arr.length;
    }


    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void sort(int start, int end, Comparator<? super T> comparator, Sorts.SortingType sortingType) {
        Sorts.sort(arr, start, end, (Comparator) comparator, sortingType);
    }


    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(arr, 0, array, 0, size);
        return array;
    }

    @Override
    public final void setData(Object[] objs) {
        arr = new Object[objs.length];
        System.arraycopy(objs, 0, arr, 0, objs.length);
        size = arr.length;
    }


    public void minimizeCapacity() {
        setCapacity(size);
    }


    public void resize(int size) {
        if (size < size()) {
            do removeLast(); while (size < size());
        } else {
            ensureCapacity(size);
            this.size = size;
        }
    }


    @Override
    public String toString() {
        if (empty())
            return "{ }";
        StringBuilder sb = new StringBuilder("{ ");
        for (int i = 0;;) {
            sb.append(get(i).toString());
            if (++i == size)
                break;
            sb.append(", ");
        }
        return sb.append(" }").toString();
    }
}