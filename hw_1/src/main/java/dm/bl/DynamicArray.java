package dm.bl;

/**
 * An interface representing a dynamic array data structure.
 * A dynamic array automatically adjusts its size when elements are added or removed.
 *
 * @param <E> the type of elements stored in the dynamic array
 */
public interface DynamicArray<E> {

    /**
     * Adds the specified element to the end of the dynamic array.
     *
     * @param element the element to be added
     */
    void add(E element);

    /**
     * Inserts the specified element at the specified position in the dynamic array.
     * Shifts the element currently at that position (if any) and any subsequent elements to the right.
     *
     * @param element the element to be inserted
     * @param index   the index at which the element should be inserted
     * @throws IllegalArgumentException if the index is out of range or negative
     */
    void add(E element, int index);

    /**
     * Replaces the element at the specified position in the dynamic array with the specified element.
     *
     * @param index   the index of the element to be replaced
     * @param element the new element to be stored at the specified position
     * @throws IllegalArgumentException – if the index is out of range or negative
     */
    void set(int index, E element);

    /**
     * Retrieves the element at the specified position in the dynamic array.
     *
     * @param index the index of the element to be retrieved
     * @return the element at the specified position
     * @throws IllegalArgumentException – – if the index is out of range or negative
     */
    E get(int index);

    /**
     * Removes the first occurrence of the specified element from the dynamic array, if it is present.
     * Shifts any subsequent elements to the left.
     */
    void removeFirst();

    /**
     * Removes all the elements from this list.
     * The list will be empty after this call returns.
     */
    void clear();

    /**
     * Returns the number of elements in this list.  If this list contains
     *
     * @return the number of elements in this list
     */
    int size();
}