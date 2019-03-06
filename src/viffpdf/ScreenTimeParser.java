package viffpdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Screen-Times Tab Parser.
 * 
 * @author Sean Lee
 *
 */
public class ScreenTimeParser extends Parser
{
	// Contains all screenTimes listed in the file given.
	ArrayList<ScreenTimeData> screenTimeList = new ArrayList<ScreenTimeData>();

	/**
	 * Default constructor for ScreenTimesTab. Constructs a ScreenTimesTab
	 * object in an invalid state.
	 */
	public ScreenTimeParser()
	{
		super("Screen-Times", false);
	}

	/**
	 * Constructs a ScreenTimesTab object by parsing data from a .tab file.
	 * 
	 * @param file
	 *            the .tab file to be parsed.
	 * @throws FileNotFoundException
	 *             File must exist.
	 * @throws IllegalArgumentException
	 *             File must contain valid data.
	 */
	public ScreenTimeParser(File file) throws FileNotFoundException, IllegalArgumentException
	{
		super("Screen-Times", false);
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
		ArrayList<ArrayList<String>> screenTimeStrings = parse(file, "\t");

		// Ensures that the user can switch to a different file without issue.
		screenTimeList.clear();
		int i = 1;

		// Creates ScreenTimes to add to screenTimeList
		for (ArrayList<String> screenTimeData : screenTimeStrings)
		{
			try
			{
				ScreenTimeData st = new ScreenTimeData(screenTimeData);

				if (st.getSectionCode().isEmpty())
					message += "Warning: Section code missing in " + "\n\tTitle:\t" + st.getMovieName() + "\n\tId:\t"
							+ st.getMovieId() + "\n\tRow:\t" + i + "\n";

				screenTimeList.add(new ScreenTimeData(screenTimeData));
			} catch (IllegalArgumentException e)
			{
				setValidState(false);
				throw new IllegalArgumentException("Row #" + i + ":\n\t" + e.getMessage());
			}

			i++;
		}

		setValidState(true);
		return message;
	}

	/**
	 * Gets screenTimeList - a list of screen-time data.
	 * 
	 * @return screenTimeList.
	 */
	public ArrayList<ScreenTimeData> getScreenTimeList()
	{
		return screenTimeList;
	}
}
