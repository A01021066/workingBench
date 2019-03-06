package viffpdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Venue Tab Parser.
 * 
 * @author Sean Lee
 *
 */
public class VenueParser extends Parser
{
	// Contains all venues listed in the file given.
	private HashMap<String, VenueData> venueMap = new HashMap<String, VenueData>();

	/**
	 * Default constructor for VenueTab. Constructs a VenueTab object in an
	 * invalid state.
	 */
	public VenueParser()
	{
		super("Venues", false);
	}

	/**
	 * Constructs a VenueTab object by parsing data from a .tab file.
	 * 
	 * @param file
	 *            the .tab file to be parsed.
	 * @throws FileNotFoundException
	 *             File must exist.
	 * @throws IllegalArgumentException
	 *             File must contain valid data.
	 */
	public VenueParser(File file) throws FileNotFoundException, IllegalArgumentException
	{
		super("Venues", false);
		setData(file);
	}

	/**
	 * Parses data from a file.
	 * 
	 * @param file
	 *            the .tab file to be parsed.
	 * @throws FileNotFoundException
	 *             File must exist.
	 * @throws IllegalArgumentException
	 *             File must contain valid data.
	 */
	public String setData(File file) throws FileNotFoundException
	{
		String message = "";
		ArrayList<ArrayList<String>> venueStrings = parse(file, "\t");

		// Ensures that the user can switch to a different file without issue.
		venueMap.clear();
		int row = 1;
		
		// Creates Venues to add to venueMap
		for (ArrayList<String> venueData : venueStrings)
		{
			try
			{
				VenueData venue = new VenueData(venueData);
				if (venueMap.containsKey(venue.getCode()))
					throw new IllegalArgumentException("Key \"" + venue.getCode() + "\" repeated.  Meaning ambiguous.");

				venueMap.put(venue.getCode(), venue);
			} catch (IllegalArgumentException e)
			{
				setValidState(false);
				throw new IllegalArgumentException("Row #" + row + ":\n\t" + e.getMessage());
			}

			row++;
		}

		setValidState(true);
		return message;
	}

	/**
	 * Gets venueList - a list of venue data.
	 * 
	 * @return venueList.
	 */
	public HashMap<String, VenueData> getVenueList()
	{
		return venueMap;
	}
}

