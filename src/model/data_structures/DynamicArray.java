/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * University of the Andes
 * Department of Systems Engineering
 * Licensed under Academic Free License version 2.1
 * Project 3: Graphs
 * Author: Andy Ortiz
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package model.data_structures;

/**
 * Class representing a dynamic array data structure.
 *
 * @param <T> Generic type.
 */
@SuppressWarnings({"unchecked"})
public class DynamicArray<T> implements IDynamicArray<T> {
    // -------------------------------------------------------------
    // Attributes
    // -------------------------------------------------------------

    /**
     * Maximum array capacity.
     */
    private int maxSize;

    /**
     * Number of items present in the array (in compact form from position 0).
     */
    private int size;

    /**
     * Array of items.
     */
    private T[] items;

    /**
     * Default size of the array.
     */
    private static final int DEFAULT_SIZE = 7;

    // -------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------

    /**
     * Constructs an array with a default max size.
     */
    public DynamicArray() {
        items = (T[]) new Object[DEFAULT_SIZE];
        maxSize = DEFAULT_SIZE;
        size = 0;
    }

    /**
     * Constructs an array from the initial maximum size.
     *
     * @param pMax Initial maximum size
     */
    public DynamicArray(int pMax) {
        items = (T[]) new Object[pMax];
        maxSize = pMax;
        size = 0;
    }

    // -------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------

    /**
     * Returns the current array size.
     *
     * @return Current array size.
     */
    public int size() {
        return size;
    }

    /**
     * Returns the max array size.
     *
     * @return Max array size.
     */
    public int getMaxsize() {
        return maxSize;
    }

    /**
     * Returns the item at the ith index.
     *
     * @param pIndex Position of the item
     * @return Element at index i.
     */
    public T get(int pIndex) {
        return items[pIndex];
    }

    /**
     * Adds an item to the array.
     *
     * @param pItem New item to be added to the list
     */
    public void add(T pItem) {

        // Case when the array is at max size
        if (size == maxSize) {
            ensureCapacity();
        }

        // Assign the item to the index
        items[size] = pItem;

        // Increments the list size. Next item will be placed at the index of this size
        size++;
    }

    /**
     * Removes every items from the array.
     */
    public void clear() {

        // Do nothing if list is already empty
        if (isEmpty())
            return;

        for (int i = 0; i < size; i++) {
            items[i] = null;
        }
        size = 0;
    }

    /**
     * Deletes an item from the array matching the value given by the parameter.
     *
     * @param pIndex Index of item to be deleted.
     */
    public void remove(int pIndex) {
        if (pIndex < 0 || pIndex > size)
            throw new IndexOutOfBoundsException(pIndex + " is out of bounds!");

        if (pIndex == size)
            items[pIndex] = null;
        else {

            for (int i = pIndex; i < size - 1; i++) {
                items[i] = items[i + 1];
            }

            items[size - 1] = null;
        }
        size--;
    }

    /**
     * Checks if the array is empty.
     *
     * @return True if empty, false if otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Sets the value of the array at the index.
     *
     * @param pIndex Index of the array.
     * @param pItem  Item the array will hold at the index.
     */
    public void set(int pIndex, T pItem) {
        // Throw an exception if the index is out of bounds
        if (pIndex < 0 || pIndex >= maxSize)
            throw new ArrayIndexOutOfBoundsException();

        if (items[pIndex] == null)
            size++;

        items[pIndex] = pItem;
    }

    /**
     * Dynamically increases the array size if the max size has been exceeded. Allocates more
     * space in the array.
     */
    private void ensureCapacity() {
        maxSize = (maxSize * 3) / 2 + 1;  // Allocate more space for the new array
        T[] copy = items;
        items = (T[]) new Object[maxSize];

        // Copy the array to items
        for (int i = 0; i < copy.length; i++) {
            items[i] = copy[i];
        }
    }

}