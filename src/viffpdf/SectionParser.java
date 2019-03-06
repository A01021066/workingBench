package viffpdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Section Tab Parser.
 * 
 * @author Sean Lee
 *
 */
public class SectionParser extends Parser
{
	// Contains all sections listed in the file given
	private TreeMap<String, SectionData> sectionMap = new TreeMap<String, SectionData>();

	/**
	 * Default constructor for SectionTab. Constructs a SectionTab object in an
	 * invalid state.
	 */
	public SectionParser()
	{
		super("Sections", false);
	}

	/**
	 * Constructs a SectionTab object by parsing data from a .tab file.
	 * 
	 * @param file
	 *            the .tab file to be parsed.
	 * @throws FileNotFoundException
	 *             File must exist.
	 * @throws IllegalArgumentException
	 *             File must contain valid data.
	 */
	public SectionParser(File file) throws FileNotFoundException, IllegalArgumentException
	{
		super("Sections", false);
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
		ArrayList<ArrayList<String>> sectionStrings = parse(file, "\t");

		// Ensures that the user can switch to a different file without issue.
		sectionMap.clear();
		int row = 1;

		// Creates Sections to add to sectionMap
		for (ArrayList<String> sectionData : sectionStrings)
		{
			try
			{
				SectionData section = new SectionData(sectionData);
				if (sectionMap.containsKey(section.getCode()))
					throw new IllegalArgumentException(
							"Key \"" + section.getCode() + "\" repeated.  Meaning ambiguous.");

				sectionMap.put(section.getCode(), section);
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
	 * Gets sectionList - a list of section data.
	 * 
	 * @return sectionList.
	 */
	public TreeMap<String, SectionData> getSectionList()
	{
		return sectionMap;
	}
}
