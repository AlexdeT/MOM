package com.onmobile.mom.database;

/**
 * This interface defines the methods needed in order to manage the database.
 *
 * @author adetalouet
 */
public interface IDatabase {

    /**
     * Create the data set
     */
    public void createDataSet();

    /**
     * Delete all the item in the data set
     */
    public void deleteAll();

    /**
     * Get the number of item in a data set
     *
     * @return - the number of item
     */
    public int getCount();

}
