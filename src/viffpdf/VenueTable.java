package viffpdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class VenueTable {

	ArrayList<ScreenTimeData> thisTimeBlocks = new ArrayList<ScreenTimeData>();
	VenueData thisVenue;
	boolean ready = false;

	public VenueTable(VenueData venue, ScreenTimeParser screenTime) {
		if (venue != null && screenTime.isValidState()) {
			thisVenue = venue;
			ArrayList<ScreenTimeData> STDList = screenTime.getScreenTimeList();

			for (ScreenTimeData std : STDList) {

				if (std.getVenueCode().equals(venue.getCode())) {
					thisTimeBlocks.add(std);
				}
			}
		}
	}

	public String toString() {
		String temp = thisVenue.getNameLong() + ": \n";
		if (!thisTimeBlocks.isEmpty()) {
			for (ScreenTimeData e : thisTimeBlocks) {
				temp += "\t" + e.getMovieName() + " " + e.getDate() + "\n";
			}
		}
		return temp;
	}

	public ArrayList<ScreenTimeData> getVTList(){
		return thisTimeBlocks;
	}
}
