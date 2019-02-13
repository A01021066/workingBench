package viffpdf;


import java.util.ArrayList;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceCmyk;

/**
 * ColourValue. Stores data on a colour.
 * 
 * @author Andrew Busto
 *
 */
public class ColorData extends RowData
{
	// The code of a section which this ColourValue is associated with.
	private String sectionCode;

	// cyan - The amount of cyan in this colour (%)
	// magenta - The amount of magenta in this colour (%)
	// yellow - The amount of yellow in this colour (%)
	// black - The amount of black in this colour (%)
	// red - The amount of red in this colour
	// green - The amount of green in this colour
	// blue - The amount of blue in this colour
	private int cyan, magenta, yellow, black, red, green, blue;

	// color - The Color generated from the values of red, green, and blue.
	private Color color;

	/**
	 * Constructs a section based on an array of relevant data.
	 * 
	 * @param data
	 *            The data to be stored in ColourValue form. Note - Must be in
	 *            format {sectionCode, cyan, magenta, yellow, black}
	 * @throws IllegalArgumentException
	 *             Data given Must be in format {sectionCode, cyan, magenta,
	 *             yellow, black}
	 */
	public ColorData(ArrayList<String> data) throws IllegalArgumentException
	{
		setData(data);
	}

	/**
	 * Updates all data on the colour.
	 * 
	 * @param data
	 *            The data to be stored in ColourValue form. Note - Must be in
	 *            format {sectionCode, cyan, magenta, yellow, black}
	 * @throws IllegalArgumentException
	 *             Data given Must be in format {sectionCode, cyan, magenta,
	 *             yellow, black}
	 */
	public void setData(ArrayList<String> data) throws IllegalArgumentException
	{
		if (data.size() != 5)
			throw new IllegalArgumentException(
					"" + data.size() + " data elements given. Must be 5 data elements per row.");

		super.setData(data);

		try
		{
			// parsing raw input data here
			sectionCode = data.get(0).replace(" ", "");
			cyan = Integer.parseInt(data.get(1).replace(" ", ""));
			magenta = Integer.parseInt(data.get(2).replace(" ", ""));
			yellow = Integer.parseInt(data.get(3).replace(" ", ""));
			black = Integer.parseInt(data.get(4).replace(" ", ""));
			/*
			 * //converting raw input data to useable RGB format here red = 255
			 * * ( 1 - cyan / 100 ) * ( 1 - black / 100 ); green = 255 * ( 1 -
			 * magenta / 100 ) * ( 1 - black / 100 ); blue = 255 * ( 1 - yellow
			 * / 100 ) * ( 1 - black / 100 );
			 */

			color = new DeviceCmyk(cyan, magenta, yellow, black);
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"Rows must be in format " + "{sectionCode, cyan, magenta, yellow, black}");
		}
	}

	/**
	 * Gets color - The Color generated from the values of red, green, and blue.
	 * 
	 * @return color.
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Sets color - The Color generated from the values of red, green, and blue.
	 * 
	 * @param color
	 *            The Color to set color to.
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 * Gets red - The amount of red in this colour.
	 * 
	 * @return red.
	 */
	public int getRed()
	{
		return red;
	}

	/**
	 * Sets red - The amount of red in this colour.
	 * 
	 * @param red
	 *            The value to set red to.
	 */
	public void setRed(int red)
	{
		this.red = red;
	}

	/**
	 * Gets green - The amount of green in this colour.
	 * 
	 * @return green.
	 */
	public int getGreen()
	{
		return green;
	}

	/**
	 * Sets green - The amount of green in this colour.
	 * 
	 * @param green
	 *            The value to set green to.
	 */
	public void setGreen(int green)
	{
		this.green = green;
	}

	/**
	 * Gets blue - The amount of blue in this colour.
	 * 
	 * @return blue.
	 */
	public int getBlue()
	{
		return blue;
	}

	/**
	 * Sets blue - The amount of blue in this colour.
	 * 
	 * @param blue
	 *            The value to set blue to.
	 */
	public void setBlue(int blue)
	{
		this.blue = blue;
	}

	/**
	 * Gets sectionCode - The code of a section which this ColourValue is
	 * associated with.
	 * 
	 * @return sectionCode.
	 */
	public String getSectionCode()
	{
		return sectionCode;
	}

	/**
	 * Sets sectionCode - The code of a section which this ColourValue is
	 * associated with.
	 * 
	 * @param sectionCode
	 *            the value to set sectionCode to.
	 */
	public void setSectionCode(String sectionCode)
	{
		this.sectionCode = sectionCode;
		data.set(0, sectionCode);
	}

	/**
	 * Gets cyan - The amount of cyan in this colour (in %).
	 * 
	 * @return cyan.
	 */
	public int getCyan()
	{
		return cyan;
	}

	/**
	 * Sets cyan - The amount of cyan in this colour (in %).
	 * 
	 * @param cyan
	 *            the value to set cyan to.
	 */
	public void setCyan(int cyan)
	{
		this.cyan = cyan;
		data.set(1, "" + cyan);
	}

	/**
	 * Gets magenta - The amount of magenta in this colour (in %).
	 * 
	 * @return magenta.
	 */
	public int getMagenta()
	{
		return magenta;
	}

	/**
	 * Sets magenta - The amount of magenta in this colour (in %).
	 * 
	 * @param magenta
	 *            the value to set magenta to.
	 */
	public void setMagenta(int magenta)
	{
		this.magenta = magenta;
		data.set(2, "" + magenta);
	}

	/**
	 * Gets yellow - The amount of yellow in this colour (in %).
	 * 
	 * @return yellow.
	 */
	public int getYellow()
	{
		return yellow;
	}

	/**
	 * Sets yellow - The amount of yellow in this colour (in %).
	 * 
	 * @param yellow
	 *            the value to set yellow to.
	 */
	public void setYellow(int yellow)
	{
		this.yellow = yellow;
		data.set(3, "" + yellow);
	}

	/**
	 * Gets black - The amount of black in this colour (in %).
	 * 
	 * @return black.
	 */
	public int getBlack()
	{
		return black;
	}

	/**
	 * Sets black - The opacity of this colour (in %).
	 * 
	 * @param black
	 *            the value to set black to.
	 */
	public void setBlack(int black)
	{
		this.black = black;
		data.set(4, "" + black);
	}
}

