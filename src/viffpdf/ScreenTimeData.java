package viffpdf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * ScreenTime. Stores data on a screen-time.
 * 
 * @author Andrew Busto
 *
 */
public class ScreenTimeData extends RowData
{
	/*
	 * date - The date on which the screening will occur. movieName - The name
	 * of the movie. movieId - The universal identifier for a movie. sectionCode
	 * - The code representing the section of the movie. venueCode - The code
	 * representing the venue showing the movie.
	 */
	private String movieName, movieId, sectionCode, venueCode, pageNum;

	// date - The date on which the screening will occur.
	private Date date;

	/*
	 * lengthMin - The length of the movie in minutes. lengthHrs - The length of
	 * the movie in HHMM. startTime - The start time of the movie in HHMM.
	 */
	private int lengthMin, lengthHrs, startTime, startBlock;

	/**
	 * Constructs a ScreenTime based on an array of relevant data.
	 * 
	 * @param data
	 *            The data to be stored in ScreenTime form. Note - Must be in
	 *            format {date, movieName, movieId, length(minutes),
	 *            length(HH:MM:SS), sectionCode, startTime(HH:MM:SS),
	 *            venueCode, pageNum}.
	 * @throws IllegalArgumentException
	 *             Data given Must be in format {date, movieName, movieId,
	 *             length(minutes), length(HH:MM:SS), sectionCode,
	 *             startTime(HH:MM:SS), venueCode}.
	 */
	public ScreenTimeData(ArrayList<String> data) throws IllegalArgumentException
	{
		setData(data);
	}


	/**
	 * Updates all data on the screen-time.
	 * 
	 * @param data
	 *            The data to be stored in ScreenTime form. Note - Must be in
	 *            format {date, movieName, movieId, length(minutes),
	 *            length(HH:MM:SS), sectionCode, startTime(HH:MM:SS),
	 *            venueCode}.
	 * @throws IllegalArgumentException
	 *             Data given Must be in format {date, movieName, movieId,
	 *             length(minutes), length(HH:MM:SS), sectionCode,
	 *             startTime(HH:MM:SS), venueCode}.
	 */
	public void setData(ArrayList<String> data) throws IllegalArgumentException
	{
		// Ensures a sufficient amount of data is given
		if (data.size() != 9)
			throw new IllegalArgumentException(
					"" + data.size() + " data elements given. Must be 9 data elements per row.");

		super.setData(data);

		// Parses variable's data from the array of data given
		try
		{
			date = new SimpleDateFormat("MM/dd/yyyy").parse(data.get(0));
			movieName = data.get(1);
			movieId = data.get(2);
			lengthMin = Integer.parseInt(data.get(3));
			lengthHrs = Integer.parseInt(data.get(4).replace(":", "")) / 100;
			sectionCode = data.get(5);
			startTime = Integer.parseInt(data.get(6).replace(":", "")) / 100;
			startBlock = this.getStartTime() - 570;
			venueCode = data.get(7);
			pageNum = data.get(8);
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"Rows must be in format " + "{date, movieName, movieId, length(minutes), "
							+ "length(HH:MM:SS), sectionCode, startTime(HH:MM:SS), venueCode}");
		} catch (ParseException e)
		{
			throw new IllegalArgumentException("Date must be in format MM/DD/YY");
		}
	}

	/**
	 * Gets date - The date on which the screening will occur.
	 * 
	 * @return date.
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * Sets date - The date on which the screening will occur.
	 * 
	 * @param date
	 *            The value to set date to.
	 */
	public void setDate(Date date)
	{
		this.date = date;
		data.set(0, "" + date);
	}

	/**
	 * Gets movieName - The name of the movie.
	 * 
	 * @return movieName.
	 */
	public String getMovieName()
	{
		return movieName;
	}

	/**
	 * Sets movieName - The name of the movie.
	 * 
	 * @param movieName
	 *            The value to set movieName to.
	 */
	public void setMovieName(String movieName)
	{
		this.movieName = movieName;
		data.set(1, movieName);
	}

	/**
	 * Gets movieId - The universal identifier for a movie.
	 * 
	 * @return movieId
	 */
	public String getMovieId()
	{
		return movieId;
	}

	/**
	 * Sets movieId - The universal identifier for a movie.
	 * 
	 * @param movieId
	 *            The value to set movieId to.
	 */
	public void setMovieId(String movieId)
	{
		this.movieId = movieId;
		data.set(2, movieId);
	}

	/**
	 * Gets sectionCode - The code representing the section of the movie.
	 * 
	 * @return sectionCode.
	 */
	public String getSectionCode()
	{
		return sectionCode;
	}

	/**
	 * Sets sectionCode - The code representing the section of the movie.
	 * 
	 * @param sectionCode
	 *            The value to set sectionCode to.
	 */
	public void setSectionCode(String sectionCode)
	{
		this.sectionCode = sectionCode;
		data.set(5, sectionCode);
	}

	/**
	 * Gets venueCode - The code representing the venue showing the movie.
	 * 
	 * @return venueCode.
	 */
	public String getVenueCode()
	{
		return venueCode;
	}

	/**
	 * Sets venueCode - The code representing the venue showing the movie.
	 * 
	 * @param venueCode
	 *            The value to set venueCode to.
	 */
	public void setVenueCode(String venueCode)
	{
		this.venueCode = venueCode;
		data.set(7, venueCode);
	}

	/**
	 * Gets lengthMin - The length of the movie in minutes.
	 * 
	 * @return lengthMin.
	 */
	public int getLengthMin()
	{
		return lengthMin;
	}

	/**
	 * Sets lengthMin - The length of the movie in minutes.
	 * 
	 * @param lengthMin
	 *            The value to set lengthMin to.
	 */
	public void setLengthMin(int lengthMin)
	{
		this.lengthMin = lengthMin;
		data.set(3, "" + lengthMin);
	}

	/**
	 * Gets lengthHrs - The length of the movie in HHMM.
	 * 
	 * @return lengthHrs.
	 */
	public int getLengthHrs()
	{
		return lengthHrs;
	}

	/**
	 * Sets lengthHrs - The length of the movie in HHMM.
	 * 
	 * @param lengthHrs
	 *            The value to set lengthHrs to.
	 */
	public void setLengthHrs(int lengthHrs)
	{
		this.lengthHrs = lengthHrs;
		data.set(4, "" + lengthHrs);
	}

	/**
	 * Gets startTime - The start time of the movie in minutes from HHMM format.
	 * 
	 * @return the start time in minutes
	 */
	public int getStartTime()
	{
		int hours = startTime / 100;
		int minutes = startTime % 100 + (hours * 60);
		return minutes;

	}
	
	public int getStartBlock() {
		return startBlock;
	}

	/**
	 * Gets the time details of the screening in XX:XXpm/XXX min format to
	 * display under the Screen title
	 * 
	 * @return the start time in minutes
	 */
	public String getTimeDetails()
	{
		int hours = startTime / 100;
		int minutes = (startTime % 100);
		String stringMin = minutes + "";
		String amPm = "am";
		String pageNumber = this.pageNum;
		if (hours >= 12)
		{
			amPm = "pm";
			if (hours != 12)
				hours -= 12;
		}
		if (minutes < 10)
		{
			stringMin = "0" + minutes;
		}
		String result = hours + ":" + stringMin + amPm + " " + lengthMin + "min p" + pageNum;

		if (pageNumber.equals("NaN") == true)
			result = hours + ":" + stringMin + amPm + " " + lengthMin + "min";
		return result;
	}

	/**
	 * Sets startTime - The start time of the movie in HHMM.
	 * 
	 * @param startTime
	 *            The value to set startTime to.
	 */
	public void setStartTime(int startTime)
	{
		this.startTime = startTime;
		data.set(6, "" + startTime);
	}
	
	public void setPageNum(String pageNumber)
	{
		this.pageNum = pageNumber;
		data.set(8, pageNumber);
	}
	
	public String getPageNum()
	{
		return this.pageNum;
	}

	public String toString()
	{
		return this.movieName;
	}
	
}
