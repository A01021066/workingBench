package viffpdf;

import java.util.ArrayList;
import java.util.Date;

public class AllTable {
	ArrayList<VenueTable> VTList = new ArrayList<VenueTable>();
	ArrayList<VenueDateTable> VDTList = new ArrayList<VenueDateTable>();
	ArrayList<DayTable> DList = new ArrayList<DayTable>();
	ArrayList<PageTable> PList = new ArrayList<PageTable>();
	ArrayList<Date> dateList = new ArrayList<Date>();
	public AllTable(Main obj) {
		VTList = obj.VTList;
		VDTList = obj.VDTList;
		DList = obj.DList;
		PList = obj.PList;
		dateList = obj.dateList;
	}
}
