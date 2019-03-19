package viffpdf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class VenueDateTable {
	VenueData thisVenue;
	float thisHeight;
	Date thisDate;
	ArrayList<ScreenTimeData> thisVDT = new ArrayList<ScreenTimeData>();

	public VenueDateTable(VenueTable venue, Date date, float height) {
		if (venue != null) {
			thisVenue = venue.thisVenue;
			thisDate = date;
			thisHeight = height;

			for (ScreenTimeData e : venue.getVTList()) {
				if (e.getDate().equals(date)) {
					thisVDT.add(e); 
				}
			}
		}
	}

	public String toString() {
		String temp = thisVenue.getNameLong() + "\n" + "\t" + thisDate + "\n";

		if (!thisVDT.isEmpty()) {
			for (ScreenTimeData e : thisVDT) {
				temp += "\t\t" + e.getMovieName() + "\n";
			}
		}
		return temp;
	}
	
	public float getHeight() {
		return thisHeight;
	}
}
