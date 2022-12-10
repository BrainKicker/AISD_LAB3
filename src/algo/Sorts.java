package algo;

import containers.ArrayList;
import containers.Pair;

import java.util.Comparator;

public final class Sorts {

    private Sorts() {
        new Error("Forbidden instance of class " + Sorts.class).printStackTrace();
        System.exit(1);
    }


    public enum SortingType {

        DEFAULT,
        SELECTION_SORT,
        BUBBLE_SORT,
        INSERTION_SORT,
        MERGE_SORT,
        QUICK_SORT,
        HEAP_SORT,
        TIMSORT,
        MYSORT;

        private static SortingType currentDefault = TIMSORT;

        public static SortingType getCurrentDefault() {
            return currentDefault;
        }

        public static void setCurrentDefault(SortingType currentDefault) {
            if (currentDefault == DEFAULT)
                throw new RuntimeException();
            SortingType.currentDefault = currentDefault;
        }
    }


    public static <T> void sort(T[] array, Comparator<? super T> comparator) {
        sort(array, 0, array.length, comparator);
    }

    public static <T> void sort(T[] array, int start, int end, Comparator<? super T> comparator) {
        sort(array, start, end, comparator, SortingType.DEFAULT);
    }

    public static <T> void sort(T[] array, Comparator<? super T> comparator, SortingType sortingType) {
        sort(array, 0, array.length, comparator, sortingType);
    }

    public static <T> void sort(T[] array, int start, int end, Comparator<? super T> comparator, SortingType sortingType) {
        if (sortingType == SortingType.DEFAULT)
            sortingType = SortingType.getCurrentDefault();
        switch (sortingType) {
            case MYSORT -> mySort(array, start, end, comparator);
            case TIMSORT -> timsort(array, start, end, comparator);
            case QUICK_SORT -> quickSort(array, start, end, comparator);
            case HEAP_SORT -> heapSort(array, start, end, comparator);
            case MERGE_SORT -> mergeSort(array, start, end, comparator);
            case INSERTION_SORT -> insertionSort(array, start, end, comparator);
            case SELECTION_SORT -> selectionSort(array, start, end, comparator);
            case BUBBLE_SORT -> bubbleSort(array, start, end, comparator);
            case DEFAULT -> throw new RuntimeException();
        }
    }


    public static <T> void selectionSort(T[] array, Comparator<? super T> comparator) {
        selectionSort(array, 0, array.length, comparator);
    }

    public static <T> void selectionSort(T[] array, int start, int end, Comparator<? super T> comparator) {
        for (int i = start; i < end; i++) {
            int minIndex = i;
            T min = array[i];
            for (int j = i + 1; j < end; j++) {
                T cur = array[j];
                if (comparator.compare(cur, min) < 0) {
                    minIndex = j;
                    min = cur;
                }
            }
            T tmp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = tmp;
        }
    }


    public static <T> void bubbleSort(T[] array, Comparator<? super T> comparator) {
        bubbleSort(array, 0, array.length, comparator);
    }

    public static <T> void bubbleSort(T[] array, int start, int end, Comparator<? super T> comparator) {
        for (int i = end - 1; i > start; i--) {
            for (int j = start; j < i; j++) {
                if (comparator.compare(array[j], array[j+1]) > 0) {
                    T tmp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = tmp;
                }
            }
        }
    }


    public static <T> void insertionSort(T[] array, Comparator<? super T> comparator) {
        insertionSort(array, 0, array.length, comparator);
    }

    public static <T> void insertionSort(T[] array, int start, int end, Comparator<? super T> comparator) {
        for (int i = start + 1; i < end; i++) {
            T cur = array[i];
            int indexToPaste = i;
            while (indexToPaste > start && comparator.compare(cur, array[indexToPaste-1]) < 0)
                --indexToPaste;
            System.arraycopy(array, indexToPaste, array, indexToPaste + 1, i - indexToPaste);
            array[indexToPaste] = cur;
        }
    }


    public static <T> void mergeSort(T[] array, Comparator<? super T> comparator) {
        mergeSort(array, 0, array.length, comparator);
    }

    public static <T> void mergeSort(T[] array, int start, int end, Comparator<? super T> comparator) {
        if (end - start <= 1)
            return;
        if (end - start == 2) {
            if (comparator.compare(array[start], array[start+1]) > 0) {
                T tmp = array[start];
                array[start] = array[start+1];
                array[start+1] = tmp;
            }
        } else {
            int mid = start + ((end - start) >> 1);
            mergeSort(array, start, mid, comparator);
            mergeSort(array, mid, end, comparator);
            merge(array, start, mid, end, comparator);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void merge(T[] array, int start, int mid, int end, Comparator<? super T> comparator) {

        Object[] tmpArr = new Object[mid - start];

        System.arraycopy(array, start, tmpArr, 0, tmpArr.length);

        int index = 0;

        while (start < mid && mid < end)
            if (comparator.compare((T) tmpArr[index], array[mid]) <= 0)
                array[start++] = (T) tmpArr[index++];
            else
                array[start++] = array[mid++];

        while (start < mid)
            array[start++] = (T) tmpArr[index++];
    }

    @SuppressWarnings("unchecked")
    private static <T> void mergeWithGallop(
            T[] array, int start, int mid, int end, Comparator<? super T> comparator, final int maxGallopCount) {

        Object[] tmpArr = new Object[mid - start];

        System.arraycopy(array, start, tmpArr, 0, tmpArr.length);

        int index = 0;

        int[] currentGallopCount = { 0, 0 };

        while (start < mid && mid < end) {

            if (currentGallopCount[0] == maxGallopCount || currentGallopCount[1] == maxGallopCount) {
                if (currentGallopCount[0] == maxGallopCount) {
                    currentGallopCount[0] = 0;
                    int maxIndex = binaryFindMinGreater(tmpArr, array[mid], index, tmpArr.length, comparator);
                    while (index < maxIndex)
                        array[start++] = (T) tmpArr[index++];
                } else {
                    currentGallopCount[1] = 0;
                    int maxIndex = binaryFindMinGreater(array, (T) tmpArr[index], mid, end, comparator);
                    while (mid < maxIndex)
                        array[start++] = array[mid++];
                }
            } else {
                if (comparator.compare((T) tmpArr[index], array[mid]) <= 0) {
                    array[start] = (T) tmpArr[index];
                    ++index;
                    currentGallopCount[0] += 1;
                    currentGallopCount[1] = 0;
                } else {
                    array[start] = array[mid];
                    ++mid;
                    currentGallopCount[0] = 0;
                    currentGallopCount[1] += 1;
                }
                ++start;
            }
        }

        while (start < mid)
            array[start++] = (T) tmpArr[index++];
    }

    @SuppressWarnings("unchecked")
    private static <E, T> int binaryFindMinGreater(E[] arr, T sample, int left, int right, Comparator<? super T> comparator) {
        if (left >= right)
            throw new IllegalArgumentException();
        while (right - left > 1) {
            int mid = left + ((right - left) >> 1);
            int diff = comparator.compare((T) arr[mid], sample);
            if (diff <= 0)
                left = mid;
            else
                right = mid;
        }
        return comparator.compare((T) arr[left], sample) > 0 ? left : right;
    }


    public static <T> void quickSort(T[] array, Comparator<? super T> comparator) {
        quickSort(array, 0, array.length, comparator);
    }

    public static <T> void quickSort(T[] array, int start, int end, Comparator<? super T> comparator) {

        if (end - start <= 1)
            return;

        int mid = start + ((end - start) >> 1);

        T pivot = array[mid];

        // swapping pivot with last
        array[mid] =  array[end-1];
        array[end-1] = pivot;

        int lastMin = start - 1;
        for (int i = start; i < end - 1; i++) {
            if (comparator.compare(array[i], pivot) < 0) {
                ++lastMin;
                T tmp = array[lastMin];
                array[lastMin] = array[i];
                array[i] = tmp;
            }
        }
        ++lastMin;
        // swapping pivot with first greater
        array[end-1] = array[lastMin];
        array[lastMin] = pivot;

        // recursion
        quickSort(array, start, lastMin, comparator);
        quickSort(array, lastMin + 1, end, comparator);
    }


    public static <T> void heapSort(T[] array, Comparator<? super T> comparator) {
        heapSort(array, 0, array.length, comparator);
    }

    public static <T> void heapSort(T[] array, int start, int end, Comparator<? super T> comparator) {

        class HeapSortClass {

            int size = end - start;

            T get(int index) {
                return array[start+index];
            }

            void set(int index, T t) {
                array[start+index] = t;
            }

            void correctDown(int i) {
                int ch1 = 2 * i + 1;
                if (ch1 >= size)
                    return;
                int ch2 = ch1 + 1;
                if (ch2 == size)
                    ch2 = ch1;
                if (comparator.compare(get(ch1), get(i)) > 0
                        || comparator.compare(get(ch2), get(i)) > 0) {
                    int maxChild = comparator.compare(get(ch1), get(ch2)) > 0 ? ch1 : ch2;
                    T tmp = get(i);
                    set(i, get(maxChild));
                    set(maxChild, tmp);
                    correctDown(maxChild);
                }
            }

            void correctUp(int i) {
                if (i <= 0)
                    return;
                int p = (i - 1) >> 1;
                if (comparator.compare(get(i), get(p)) > 0) {
                    T tmp = get(i);
                    set(i, get(p));
                    set(p, tmp);
                    correctUp(p);
                }
            }

            void sort() {
                for (int i = size - 1; i > 0; i--)
                    correctUp(i);

                while (size-- > 0) {
                    T tmp = get(0);
                    set(0, get(size));
                    set(size, tmp);
                    correctDown(0);
                }
            }
        }

        new HeapSortClass().sort();
    }


    /**
     * <a href="https://en.wikipedia.org/wiki/Timsort">Timsort</a> for an array.
     */
    public static <T> void timsort(T[] array, Comparator<? super T> comparator) {
        timsort(array, 0, array.length, comparator);
    }

    /**
     * <a href="https://en.wikipedia.org/wiki/Timsort">Timsort</a> for an array from start inclusive to end exclusive: [start, end).
     */
    @SuppressWarnings("unchecked")
    public static <T> void timsort(T[] array, int start, int end, Comparator<? super T> comparator) {

        if (end - start <= 1) // (*)
            return;

        class TimsortClass {

            final static int MAX_MINRUN = 64;

            final static int MAX_GALLOP_COUNT = 7;

            int calculateMinrun(int n) {
                int r = 0;
                while (n >= MAX_MINRUN) {
                    r |= n & 1;
                    n >>= 1;
                }
                return n + r;
            }
        }

        TimsortClass timsortClass = new TimsortClass();

        final int minrun = timsortClass.calculateMinrun(end - start);

        ArrayList<Pair<Integer,Integer>> indexes = new ArrayList<>();

        for(int curRunStart = start, curRunEnd = curRunStart + 2;;) {

            // next element (curRunStart + 1) exists (*)
            boolean increasing = comparator.compare(array[curRunStart], array[curRunStart + 1]) <= 0;

            Comparator<T> comp
                    = increasing
                    ? (Comparator<T>) comparator
                    : (o1, o2) -> {
                return /*minus here:*/-comparator.compare(o1, o2) + 1;
            };

            while (curRunEnd < end && comp.compare(array[curRunEnd - 1], array[curRunEnd]) <= 0)
                ++curRunEnd;

            // 'a0 > a1 > ... > an' TO 'an <= an-1 <= ...  <= a0'
            if (!increasing) {
                int mid = (curRunEnd - curRunStart) >> 1;
                for (int i = 0; i < mid; i++) {
                    T tmp = array[curRunStart + i];
                    array[curRunStart+i] = array[curRunEnd-i-1];
                    array[curRunEnd-i-1] = tmp;
                }
            }

            if (curRunEnd - curRunStart < minrun)
                curRunEnd = Math.min(curRunStart + minrun, end);

            // next element existing required at next iteration
            if (curRunEnd + 1 == end) // (*) (**)
                ++curRunEnd;

            insertionSort(array, curRunStart, curRunEnd, comparator);

            indexes.add(new Pair<>(curRunStart, curRunEnd - curRunStart));

            while (indexes.size() >= 3) {
                Pair<Integer,Integer> Z = indexes.getLast();
                Pair<Integer,Integer> Y = indexes.get(indexes.size() - 2);
                Pair<Integer,Integer> X = indexes.get(indexes.size() - 3);
                if (Z.second <= Y.second + X.second || Y.second <= X.second) {
                    if (Z.second > X.second) {
                        mergeWithGallop(array, X.first, Y.first, Y.first + Y.second, comparator, TimsortClass.MAX_GALLOP_COUNT);
                        indexes.set(indexes.size() - 3, new Pair<>(X.first, X.second + Y.second));
                        indexes.remove(indexes.size() - 2);
                    } else {
                        mergeWithGallop(array, Y.first, Z.first, Z.first + Z.second, comparator, TimsortClass.MAX_GALLOP_COUNT);
                        indexes.set(indexes.size() - 2, new Pair<>(Y.first, Y.second + Z.second));
                        indexes.removeLast();
                    }
                }
            }

            if (curRunEnd == end)
                break;

            curRunStart = curRunEnd;
            curRunEnd = curRunStart + 2; // <= end because (**)
        }

        while (true) {
            if (indexes.size() < 2)
                break;
            if (indexes.size() == 2) {
                mergeWithGallop(array, start, indexes.getLast().first, end, comparator, TimsortClass.MAX_GALLOP_COUNT);
                break;
            }
            Pair<Integer,Integer> Z = indexes.getLast();
            Pair<Integer,Integer> Y = indexes.get(indexes.size() - 2);
            Pair<Integer,Integer> X = indexes.get(indexes.size() - 3);
            if (Z.second > X.second) {
                mergeWithGallop(array, X.first, Y.first, Y.first + Y.second, comparator, TimsortClass.MAX_GALLOP_COUNT);
                indexes.set(indexes.size() - 3, new Pair<>(X.first, X.second + Y.second));
                indexes.remove(indexes.size() - 2);
            } else {
                mergeWithGallop(array, Y.first, Z.first, Z.first + Z.second, comparator, TimsortClass.MAX_GALLOP_COUNT);
                indexes.set(indexes.size() - 2, new Pair<>(Y.first, Y.second + Z.second));
                indexes.removeLast();
            }
        }
    }


    public static <T> void mySort(T[] array, Comparator<? super T> comparator) {
        mySort(array, 0, array.length, comparator);
    }

    public static <T> void mySort(T[] array, int start, int end, Comparator<? super T> comparator) {

        final int MIN_LENGTH = 32;

        if (end - start <= MIN_LENGTH) {
            insertionSort(array, start, end, comparator);
            return;
        }

        int mid = start + ((end - start) >> 1);
        mySort(array, start, mid, comparator);
        mySort(array, mid, end, comparator);
        merge(array, start, mid, end, comparator);
    }
}