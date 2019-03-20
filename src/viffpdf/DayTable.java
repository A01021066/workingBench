package viffpdf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class DayTable {

	static int count = 0;
	float thisHeight = 0;
	Date dayDate;
	ArrayList<VenueDateTable> venueSCTList = new ArrayList<VenueDateTable>();
	
	public DayTable(ArrayList<VenueDateTable> list, Date date) {
		dayDate = date;
//		Status.print("Generating Day Table for: " + dayDate);
		for(VenueDateTable vdtEntry : list) {
			if (vdtEntry.thisDate.equals(date)) {
				venueSCTList.add(vdtEntry);
			}
		}
		
		Collections.sort(venueSCTList, new Comparator<VenueDateTable>(){
			@Override
			public int compare(VenueDateTable a, VenueDateTable b) {
				String aName = a.thisVenue.getCode();
				String bName = b.thisVenue.getCode();
				return aName.compareTo(bName);		
			}
		});

		thisHeight = (venueSCTList.size() + 2) * venueSCTList.get(0).getHeight() + 3;
	}
	

	
	public String toString() {
		String temp = "Day " + (++count) + ": " + dayDate.toString() + "\n";
		temp += "\tHeight: " + thisHeight + "\n";
		for (VenueDateTable e : venueSCTList) {
			temp += "\t" + e + "\n";
		}
		return temp;
	}
	
}
