package viffpdf;

import java.util.ArrayList;

public class PageTable {
	static int dayCount = 0;
	static int pageCount = 1;
	int numOfDays = 0;
	final int maxHeight = 736 * 4;
	int thisHeight = 0;
	int pageNum;
	ArrayList<DayTable> dayList = new ArrayList<DayTable>();

	public PageTable(ArrayList<DayTable> list, int days, int page) {
		int upperBound = dayCount + days;
		pageNum = pageCount++;

		Status.print("Trying to allocate " + days + " days into page " + page + "...");
		int heightCounter = 0;
		if (upperBound > list.size()) {
			upperBound = list.size();
		}

		for (int n = dayCount; n < upperBound; n++) {
			heightCounter += list.get(n).thisHeight;
			if (heightCounter + list.get(n).thisHeight >= maxHeight) {
				upperBound = n;
				Status.print("Too many days within one page. Emitting days on ***PAGE " + pageNum + "***." );
				break;
			}
		}

		//System.out.println(upperBound);

		for (int i = dayCount; i < upperBound; i++) {
				dayList.add(list.get(i));
				dayCount++;
				numOfDays++;
		}
		thisHeight = heightCounter;
	}
	
	public void addDay(DayTable day) {
			Status.print("Adding day #" + (dayCount+1) + " to page# " + pageCount + ".\n");
			dayList.add(day);
			dayCount++;
	}
	
	public boolean isFull(DayTable day) {
		return thisHeight + day.thisHeight > maxHeight;
	}
	
	

	public String toString() {
		String temp = "Page " + pageNum + "\n" + "Height: " + thisHeight + "\n";
		for (DayTable d : dayList) {
			temp += "\t" + d;
		}
		return temp;
	}
}
