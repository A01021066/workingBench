package viffpdf;

import java.util.ArrayList;

public class PageTable {
	static int dayCount = 0;
	static int pageCount = 1;
	int numOfDays = 0;
	final float maxHeight = 651;
	float thisHeight = 0;
	int pageNum;
	ArrayList<DayTable> dayList = new ArrayList<DayTable>();

	public PageTable(ArrayList<DayTable> list, int days, int page) {
		int upperBound = dayCount + days;
		pageNum = pageCount++;

		Status.print("Trying to allocate " + days + " days into page " + page + "...");
		float heightCounter = 0;
		if (upperBound > list.size()) {
			upperBound = list.size();
		}

		for (int n = dayCount; n < upperBound; n++) {
			heightCounter += list.get(n).thisHeight;
			if (heightCounter >= maxHeight) {
				Status.print("Too many days within one page. Emitting days on ***PAGE " + pageNum + "***.");
				break;
			}

			dayList.add(list.get(n));
			dayCount++;
			numOfDays++;
		}
		thisHeight = heightCounter;
	}

	public void addDay(DayTable day) {
		Status.print("Adding day #" + (dayCount + 1) + " to page# " + pageCount + ".\n");
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
