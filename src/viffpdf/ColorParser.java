package viffpdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class ColorParser extends Parser {
	private HashMap<String, ColorData> colorMap = new HashMap<String, ColorData>();

	/**
	 * Default constructor for colorTab. Constructs a colorTab object in an
	 * invalid state.
	 */
	public ColorParser() {
		super("colors", false);
	}
	
	public ColorParser(File file) throws FileNotFoundException {
		super("Color", false);
	}

	@Override
	public String setData(File file) throws FileNotFoundException {
		String message = "";
		ArrayList<ArrayList<String>> colorStrings = parse(file, "=|%");

		// Ensures that the user can switch to a different file without issue.
		colorMap.clear();
		int row = 1;

		// Creates ColorValues to add to colouMap.
		for (ArrayList<String> colorData : colorStrings)
		{
			try
			{
				ColorData color = new ColorData(colorData);
				if (colorMap.containsKey(color.getSectionCode()))
					throw new IllegalArgumentException(
							"Key \"" + color.getSectionCode() + "\" repeated.  Meaning ambiguous.");

				colorMap.put(color.getSectionCode(), color);
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

}
