package dm.bl;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * CustomArrayList is an implementation of the DynamicArray interface.
 * It provides functionality to store and manipulate elements in an array-based list.
 *
 * @param <E> the type of elements stored in the list
 * @see DynamicArray
 */
public class CustomArrayList<E extends Comparable<E>> implements DynamicArray<E> {

    public static final int DEFAULT_SIZE = 10;
    public static final String ILLEGAL_INDEX_ERR_MSG = "The index must not be negative or outside the scope of the collection";
    /**
     * The array storing the elements of the list.
     */
    private Object[] data;

    /**
     * The number of elements currently stored in the list.
     */
    private int size;

    /**
     * Constructs an empty CustomArrayList with initial capacity of 10.
     */
    public CustomArrayList() {
        this.data = new Object[DEFAULT_SIZE];
        this.size = 0;
    }


    @Override
    public void add(E element) {
        if (size == data.length){
            increasingArray();
        }
        data[size++] = element;
    }

    /**
     * Increases the capacity of the internal array to accommodate more elements.
     *
     * <p>The method doubles the current capacity of the array.</p>
     */
    private void increasingArray() {
        int newCapacity = size * 2;
        data = Arrays.copyOf(data, newCapacity);
    }

    @Override
    public void add(E element, int index) {
        checkCorrectIndex(index);
        if (size == data.length){
            increasingArray();
        }
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    /**
     * Checks if the provided index is within the valid range of the collection.
     *
     * <p>Throws an IllegalArgumentException if the index is negative or outside the valid range.</p>
     *
     * @param index the index to be checked
     * @throws IllegalArgumentException if the index is negative or outside the valid range
     */
    private void checkCorrectIndex(int index) {
        if (index > size || index < 0){
            throw new IllegalArgumentException(ILLEGAL_INDEX_ERR_MSG);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        checkCorrectIndex(index);
        return (E) data[index];
    }

    @Override
    public void removeFirst() {
        if (size == 0){
            throw new NoSuchElementException();
        }
        System.arraycopy(data, 1, data, 0, size -1);
        size--;
    }

    @Override
    public void clear() {
        Arrays.fill(data, null);
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void set(int index, E element) {
        checkCorrectIndex(index);
        data[index] = element;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CustomArrayList<?> other)) {
            return false;
        }

        if (size != other.size) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (!Objects.equals(data[i], other.data[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sorts the elements of the list using the QuickSort algorithm.
     */
    public void quickSort() {
        quickSort(0, size - 1);
    }


    /**
     * This is a helper method that recursively sorts the elements of a list between specified low and high indices using the QuickSort algorithm.
     *
     * @param low the lowest index of the sublist to be sorted
     * @param high the highest index of the sublist to be sorted
     */
    private void quickSort(int low, int high) {
        if (low < high) {
            int pivotIndex = partition(low, high);
            quickSort(low, pivotIndex - 1);
            quickSort(pivotIndex + 1, high);
        }
    }

    /**
     * Partitions the sublist between the specified low and high indices and returns the index of the pivot element.
     *
     * @param low the lowest index of the sublist
     * @param high the highest index of the sublist
     * @return the index of the pivot element after partitioning
     */
    private int partition(int low, int high) {
        E pivot = get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (get(j).compareTo(pivot) <= 0) {
                i++;
                swap(i, j);
            }
        }

        swap(i + 1, high);
        return i + 1;
    }

    /**
     * Swaps the elements at the specified indices in the list.
     *
     * @param i the index of the first element to be swapped
     * @param j the index of the second element to be swapped
     */
    private void swap(int i, int j) {
        E temp = get(i);
        set(i, get(j));
        set(j, temp);
    }
}