package viffpdf;

import java.util.ArrayList;
import java.util.Date;

public class AllTable {
	ArrayList<VenueTable> VTList = new ArrayList<VenueTable>();
	ArrayList<VenueDateTable> VDTList = new ArrayList<VenueDateTable>();
	ArrayList<DayTable> DList = new ArrayList<DayTable>();
	ArrayList<PageTable> PList = new ArrayList<PageTable>();
	ArrayList<Date> dateList = new ArrayList<Date>();
	public AllTable(ArrayList<VenueTable> v, ArrayList<VenueDateTable> vd, ArrayList<DayTable> d, ArrayList<PageTable> p, ArrayList<Date> date) {
		VTList = v;
		VDTList = vd;
		DList = d;
		PList = p;
		dateList = date;
	}
}
