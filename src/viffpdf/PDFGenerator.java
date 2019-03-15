package viffpdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Map.Entry;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.*;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;


public class PDFGenerator {
	String destPath;

	AllTable table;
	private static int dayCounter = 0;
	// Used to specify the amount of cells in a row and the weight of each cell
	private float[] tableCellNumbers;

	// Used to state the starting hour for screening cells to populate
	private float startingHour, endingHour;
	
	// For the top and bottom margin spacing of the document
	private final int DOCUMENT_MARGIN = 25;
	
	// margin spaces between tables on the document
	private final int TABLE_MARGIN = 5;

	/**
	 * Constant for the value of the PDF's page width.
	 */
	private static final int PAGE_WIDTH = 651;
	/**
	 * Constant for the value of the PDF's page height.
	 */
	private static final int PAGE_HEIGHT = 736;

	/**
	 * Constant for 15 minutes.
	 */
	private static final int QUARTER_HOUR = 15;

	/**
	 * Constant for 30 minutes.
	 */
	private static final int HALF_HOUR = 30;

	/**
	 * Constant for 45 minutes.
	 */
	private static final int THREE_QUARTERS_HOUR = 45;

	/**
	 * Constant for 1 hour for 60 minutes.
	 */
	private static final int HOUR = 60;

	/**
	 * Constant for date row font size.
	 */
	private static final int DATE_FONT_SIZE = 9;

	/**
	 * Constant for date row font size.
	 */
	private static final int TIME_FONT_SIZE = 7;

	public PDFGenerator(String dest, AllTable table) throws IOException {
		setDest(dest);
		File file = new File(dest);
		file.getParentFile().mkdirs();
		this.table = table;
		generate();
	}

	public void setDest(String dest) {
		if (!dest.endsWith(".pdf")) {
			dest += ".pdf";
		}
		destPath = dest;
	}

	public void generate() throws FileNotFoundException {
		int number_of_columns = 1080;

		PdfWriter writer = new PdfWriter(destPath);
		PdfDocument pdf = new PdfDocument(writer);
		pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageBackgroundsEventHandler());
		Document document = new Document(pdf, new PageSize(PAGE_WIDTH, PAGE_HEIGHT));

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
		tableCellNumbers = new float[number_of_columns];
		Arrays.fill(tableCellNumbers, 1.0f);
		for (PageTable pt : table.PList) {
			for (int i = 0; i < pt.numOfDays; i++) {
				Table day = createSchedule(pt.dayList.get(i), fmt.format(pt.dayList.get(i).dayDate));
				day.setHeight(pt.dayList.get(i).thisHeight);
				System.out.println(pt.dayList.get(i));
				document.add(day);
			}
		}
		dayCounter = 0;
		document.close();

	}

	protected class PageBackgroundsEventHandler implements IEventHandler {

		@Override
		public void handleEvent(Event event) {
			PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
			PdfPage page = docEvent.getPage();

			int pagenumber = docEvent.getDocument().getNumberOfPages();
			PdfCanvas canvas = new PdfCanvas(page);
			Rectangle rect = page.getPageSize();
			canvas.saveState().setFillColor(WebColors.getRGBColor("Black"))
					.rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight()).fillStroke()
					.restoreState();
		}
	}
	
	private Table createSchedule(DayTable table, String date) {
		// Counters
		int column_counter;
		int table_height_counter = 0;

		// Size of table in columns
		int number_of_columns = 1080;
		int max_table_width = 960;
		
		// For truncating strings that are too long for the cell
		int charsPerCell = 3;

		// Venue name cell width
		int venueCellLength = HOUR * 2;

		// Intialize arraylist of times for the schedule
		ArrayList<String> listOfTimes = new ArrayList<String>();
		listOfTimes.add("10:00");
		listOfTimes.add("11:00");
		listOfTimes.add("12:00");
		listOfTimes.add("13:00");
		listOfTimes.add("14:00");
		listOfTimes.add("15:00");
		listOfTimes.add("16:00");
		listOfTimes.add("17:00");
		listOfTimes.add("18:00");
		listOfTimes.add("19:00");
		listOfTimes.add("20:00");
		listOfTimes.add("21:00");
		listOfTimes.add("22:00");
		listOfTimes.add("23:00");
		listOfTimes.add("00:00");
		listOfTimes.add("01:00");

		// Initialize table with 1080 cells across
		
		Table schedule_table = new Table(tableCellNumbers);

		schedule_table.setWidth(PAGE_WIDTH).setTextAlignment(TextAlignment.LEFT)
				.setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(WebColors.getRGBColor("WHITE"))
				/*.setMarginTop(TABLE_MARGIN)*/;

		//schedule_table.addHeaderCell(createDateCell(number_of_columns, date));
		Cell cell;
//		cell = new Cell(1, number_of_columns).setBackgroundColor(WebColors.getRGBColor("BLACK")).setPadding(0);
//		schedule_table.addHeaderCell(cell);
//		
		cell = new Cell(1, 1).setBackgroundColor(WebColors.getRGBColor("WHITE")).setPadding(0);
		Text text = new Text(date);
		cell.getChildren().add(text);
		schedule_table.addHeaderCell(cell);
//		for (int i = 0; i < listOfTimes.size(); i++)
//		{
//			//schedule_table.addHeaderCell(createTimeCell(listOfTimes.get(i)));
//		}

//		
		return schedule_table;
	}
}
