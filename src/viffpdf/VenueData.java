package viffpdf;


import java.util.ArrayList;
import java.util.Scanner;

/**
 * Venue. Stores data on a venue.
 * 
 * @author Andrew Busto
 *
 */
public class VenueData extends RowData implements Comparable<VenueData>
{
	/*
	 * nameLong - the long form of a venue's name. nameShort- the short form of
	 * a venue's name. code - the key associated with a venue.
	 */
	private String nameLong, nameShort, code;

	/**
	 * Constructs a Venue object from an array of relevant data.
	 * 
	 * @param data
	 *            The data to be stored in Venue form. Note - Must be in format
	 *            {nameLong, nameShort, code}
	 * @throws IllegalArgumentException
	 *             Data given Must be in format {nameLong, nameShort, code}.
	 */
	public VenueData(ArrayList<String> data) throws IllegalArgumentException
	{
		setData(data);
	}



	/**
	 * Updates all data on the venue.
	 * 
	 * @param data
	 *            The data to be stored in Venue form. Note - Must be in format
	 *            {nameLong, nameShort, code}.
	 * @throws IllegalArgumentException
	 *             Data given Must be in format {nameLong, nameShort, code}.
	 */
	public void setData(ArrayList<String> data) throws IllegalArgumentException
	{
		if (data.size() != 3)
			throw new IllegalArgumentException(
					"" + data.size() + " data elements given. Must be 3 data elements per row.");

		super.setData(data);
		nameLong = data.get(0);
		nameShort = data.get(1);
		code = data.get(2);
	}

	/**
	 * Gets nameLong - The venue's name in long form.
	 * 
	 * @return nameLong.
	 */
	public String getNameLong()
	{
		return nameLong;
	}

	/**
	 * Sets nameLong - The venue's name in long form.
	 * 
	 * @param nameLong
	 *            The value to set nameLong to.
	 */
	public void setNameLong(String nameLong)
	{
		this.nameLong = nameLong;
		data.set(0, nameLong);
	}

	/**
	 * Gets nameShort - The venue's name in short form.
	 * 
	 * @return nameShort.
	 */
	public String getNameShort()
	{
		return nameShort;
	}

	/**
	 * Sets nameShort - The venue's name in short form.
	 * 
	 * @param nameShort
	 *            The value to set nameShort to.
	 */
	public void setNameShort(String nameShort)
	{
		this.nameShort = nameShort;
		data.set(1, nameShort);
	}

	/**
	 * Gets code - The code that universally represents the venue.
	 * 
	 * @return code.
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * Sets code - The code that universally represents this venue.
	 * 
	 * @param code
	 *            The value to set code to.
	 */
	public void setCode(String code)
	{
		this.code = code;
		data.set(2, code);
	}

	/**
	 * Creates a String that represents this Venue.
	 * 
	 * @return nameShort.
	 */
	@Override
	public String toString()
	{
		return this.nameShort;
	}

	/**
	 * Compares this Venue to another Venue. Comparison is performed on each
	 * Venue's nameShort variable.
	 * 
	 * @param that
	 *            The Venue this Venue is being compared to.
	 */
	public int compareTo(VenueData that)
	{
		Scanner scanThis = new Scanner(this.nameShort);
		Scanner scanThat = new Scanner(that.nameShort);

		// Compares the strings word by word
		while (scanThis.hasNext())
		{
			if (!scanThat.hasNext())
			{
				scanThis.close();
				scanThat.close();
				return -1;
			}

			int result;
			// Checks if the next thing should be compared as a word or a number
			if (scanThis.hasNextInt() && scanThat.hasNextInt())
			{
				result = scanThis.nextInt() - scanThat.nextInt();
			} else
			{
				result = scanThis.next().compareTo(scanThat.next());
			}

			// Returns a value as soon as differences are found.
			if (result != 0)
			{
				scanThis.close();
				scanThat.close();
				return result;
			}
		}

		// Checks if that is longer than this
		if (scanThat.hasNext())
		{
			scanThis.close();
			scanThat.close();
			return 1;
		}

		scanThis.close();
		scanThat.close();
		return 0;
	}
}

