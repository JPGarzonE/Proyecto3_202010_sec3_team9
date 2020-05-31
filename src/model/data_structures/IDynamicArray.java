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
 * Represents a generic dynamic array.
 *
 * @param <T> Generic type.
 */
public interface IDynamicArray<T>{

    /**
     * Returns the current size of the array.
     *
     * @return The size of the array.
     */
    int size();

    /**
     * Returns the item at the ith position in the array.
     *
     * @param pIndex Index in the array.
     * @return Item at ith position.
     */
    T get(int pIndex);

    /**
     * Adds an item to the end of the array.
     *
     * @param pItem Data to be added to the array.
     */
    void add(T pItem);

    /**
     * Removes every items from the array.
     */
    void clear();

    /**
     * Removes an item at the index given by the parameter.
     *
     * @param pIndex Index in the array.
     */
    void remove(int pIndex);

    /**
     * Checks if the array is empty.
     *
     * @return True if empty, false if otherwise.
     */
    boolean isEmpty();

    /**
     * Sets the value of the array at the index.
     *
     * @param pIndex Index of the array.
     * @param pValue Value the array will hold at the index.
     */
    void set(int pIndex, T pValue);
    
}