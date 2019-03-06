package viffpdf;

import java.util.ArrayList;

/**
 * Section. Stores data on a section.
 * 
 * @author Andrew Busto
 *
 */
public class SectionData extends RowData
{
	/*
	 * name - the name of the section. code - the code/key which refers to the
	 * section globally.
	 */
	private String name, code;

	/**
	 * Constructs a section based on an array of relevant data.
	 * 
	 * @param data
	 *            The data to be stored in Section form. Note - Must be in
	 *            format {name, code}
	 * @throws IllegalArgumentException
	 *             Data given Must be in format {name, code}.
	 */
	public SectionData(ArrayList<String> data) throws IllegalArgumentException
	{
		setData(data);
	}



	/**
	 * Updates all data on the section.
	 * 
	 * @param data
	 *            The data to be stored in Section form. Note - Must be in
	 *            format {name, code}
	 * @throws IllegalArgumentException
	 *             Data given Must be in format {name, code}.
	 */
	public void setData(ArrayList<String> data) throws IllegalArgumentException
	{
		if (data.size() != 2)
			throw new IllegalArgumentException(
					"" + data.size() + " data elements given. Must be 2 data elements per row.");

		super.setData(data);
		name = data.get(0);
		code = data.get(1);
	}

	/**
	 * Gets name - this section's name.
	 * 
	 * @return name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets name - this section's name.
	 * 
	 * @param name
	 *            The value to set name to.
	 */
	public void setName(String name)
	{
		this.name = name;
		data.set(0, name);
	}

	/**
	 * Gets code - the code that represents this section globally.
	 * 
	 * @return code.
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * Sets code - the code that represents this section globally.
	 * 
	 * @param code
	 *            The value to set code to.
	 */
	public void setCode(String code)
	{
		this.code = code;
		data.set(1, code);
	}
}
