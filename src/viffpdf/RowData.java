package viffpdf;

import java.util.ArrayList;

/**
 * FileRowData. Stores a "row" as an ArrayList.
 * 
 * @author Andrew
 *
 */
public abstract class RowData
{
	// The data being stored.
	protected ArrayList<String> data = new ArrayList<String>();

	/**
	 * Default constructor.
	 */
	public RowData()
	{
		// body intentionally blank.
	}

	/**
	 * Constructs a FileRowData object based on an array of data.
	 * 
	 * @param data
	 *            The data to be stored in FileRowData format.
	 */
	public RowData(ArrayList<String> data)
	{
		this.data = data;
	}

	/**
	 * Sets data.
	 * 
	 * @param data
	 *            The value to set data to.
	 */
	public void setData(ArrayList<String> data)
	{
		this.data = data;
	}

	/**
	 * Gets data.
	 * 
	 * @return data.
	 */
	public ArrayList<String> getData()
	{
		return data;
	}
}

