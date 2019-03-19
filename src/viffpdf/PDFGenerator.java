package viffpdf;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.*;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;



public class PDFGenerator {
	String destPath;
	int venueFontSize;
	Color dColor;
	Color bColor;
	Color vColor;
	int masterFont;
	PdfFont font;

	ArrayList<PageTable> pageList = new ArrayList<PageTable>();
	ArrayList<VenueTable> venueList = new ArrayList<VenueTable>();
	ArrayList<VenueDateTable> screenTimeList = new ArrayList<VenueDateTable>();
	ArrayList<DayTable> dayList = new ArrayList<DayTable>();
	ArrayList<Date> dateList = new ArrayList<Date>();
	private static int dayCounter = 0;
	// Used to specify the amount of cells in a row and the weight of each cell
	private float[] tableCellNumbers;

	// Used to state the starting hour for screening cells to populate
	private float startingHour, endingHour;

	// For the top and bottom margin spacing of the document
	private final int DOCUMENT_MARGIN = 25;

	// margin spaces between tables on the document
	private final int TABLE_MARGIN = 5;

	private final int number_of_columns = 1080;

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

	public PDFGenerator(String dest, AllTable table, Configuration config) throws IOException {
		setDest(dest);
		File file = new File(dest);
		file.getParentFile().mkdirs();
		pageList = table.PList;
		dayList = table.DList;
		venueList = table.VTList;
		screenTimeList = table.VDTList;
		dateList = table.dateList;
		venueFontSize = config.venue_font_Size;
		dColor = config.dColor;
		bColor = config.bColor;
		vColor = config.vColor;
		switch (config.masterFont) {
		case 0:
			font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
			// http://itextsupport.com/apidocs/itext7/7.1.1/com/itextpdf/io/font/constants/StandardFonts.html
			// this shit is black magic
			break;
		case 1:
			font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
			break;
		case 2:
			font = PdfFontFactory.createFont(StandardFonts.COURIER);
			break;

		}
		generate();
	}

	public void setDest(String dest) {
		if (!dest.endsWith(".pdf")) {
			dest += ".pdf";
		}
		destPath = dest;
	}

	public void generate() throws FileNotFoundException {

		PdfWriter writer = new PdfWriter(destPath);
		PdfDocument pdf = new PdfDocument(writer);
		pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageBackgroundsEventHandler());
		Document document = new Document(pdf, new PageSize(PAGE_WIDTH, PAGE_HEIGHT).rotate());
		document.setFontProvider(document.getFontProvider());

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
		tableCellNumbers = new float[number_of_columns];
		Arrays.fill(tableCellNumbers, 1.0f);

		for (PageTable pt : pageList) {
			int heightCounter = 0;
			for (int i = 0; i < pt.numOfDays; i++) {
				Table dayTable = createSchedule(pt.dayList.get(i), fmt.format(pt.dayList.get(i).dayDate));
				document.add(dayTable);
				heightCounter += pt.thisHeight;
			}
			if (heightCounter <= PAGE_HEIGHT) {
				Table rect = new Table(1);
				rect.useAllAvailableWidth().setHeight(PAGE_HEIGHT - heightCounter);
				document.add(rect);
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
//			canvas.saveState().setFillColor(WebColors.getRGBColor("Black"))
//					.rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight()).fillStroke()
//					.restoreState();
		}
	}

	private Table createSchedule(DayTable table, String date) {
		// Counters
		int column_counter;
		int table_height_counter = 0;
		SolidBorder border = new SolidBorder(1.0f);

		// Size of table in columns
		int number_of_columns = 1080;
		int max_table_width = 960;

		// For truncating strings that are too long for the cell
		int charsPerCell = 3;

		// list of times
		String[] times = { "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00",
				"20:00", "21:00", "22:00", "23:00", "00:00", "01:00" };

		// Initialize table with 1080 cells across
		Table schedule_table = new Table(number_of_columns);
		

		schedule_table.useAllAvailableWidth().setTextAlignment(TextAlignment.CENTER)
				.setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(WebColors.getRGBColor("WHITE"))
				.setMarginTop(TABLE_MARGIN);
		schedule_table.setBorder(border);
		schedule_table.addHeaderCell(createDateCell(number_of_columns, date).setBorder(border));
		Cell cell;

		cell = new Cell(1, HOUR * 2).setBackgroundColor(bColor).setPadding(0);
		schedule_table.addHeaderCell(cell);

		for (int i = 0; i < times.length; i++) {
			schedule_table.addHeaderCell(createTimeCell(times[i]));
		}

		for (VenueDateTable vdt : table.venueSCTList) {

			Cell vdtCell = new Cell(1, HOUR * 2);
			
			vdtCell.add(new Paragraph(vdt.thisVenue.getNameShort()).setWidth(schedule_table.getColumnWidth(0)).setFontSize(venueFontSize).setFont(font)
					.setTextAlignment(TextAlignment.CENTER).setBold().setFontColor(ColorConstants.BLACK));
			vdtCell.setTextAlignment(TextAlignment.CENTER)/*.setBackgroundColor(vColor)*/;
			vdtCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
			vdtCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			// TODO do we go with height setting, or font size setting?
			vdtCell.setHeight(vdt.thisHeight);
			//vdtCell.setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER);
			vdtCell.setNextRenderer(new FoldedBorderCellRenderer(vdtCell));
			

			schedule_table.addCell(vdtCell);
			int minCounter = 0;

			for (ScreenTimeData sct : vdt.thisVDT) {
				int movieStartBlock = sct.getStartBlock();
//				System.out.println("Block: " + sct.getStartBlock());
//				System.out.println("Time: " + sct.getStartTime());
				if (movieStartBlock > minCounter) {
					Cell emptyCell = new Cell(1, movieStartBlock - minCounter).setPadding(0).setMargin(0)
							/*.setBorder(null)*/;
					emptyCell.setHeight(vdt.thisHeight);
					emptyCell.setBorderTop(border).setBorderBottom(border).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
					schedule_table.addCell(emptyCell);
					if (table.venueSCTList.indexOf(vdt) %2 == 0) {
						emptyCell.setBackgroundColor(ColorConstants.GRAY);
					} else {
						emptyCell.setBackgroundColor(ColorConstants.DARK_GRAY);
					}
					minCounter+= (movieStartBlock - minCounter);
				}

				Cell sctCell = new Cell(1, sct.getLengthMin());
				sctCell.setHeight(vdt.thisHeight).setPadding(0).setMargin(0).setMaxWidth(0);
				sctCell.setBorderTop(border).setBorderBottom(border).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
				String name = sct.getMovieName();
				
				AffineTransform affinetransform = new AffineTransform();     
				FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
				Font fontSample = new Font("Tahoma", Font.PLAIN, venueFontSize);
				int textwidth = (int)(fontSample.getStringBounds(name, frc).getWidth());
				while (textwidth > 100) {
					int nameLength = name.length();
					name = name.substring(0, nameLength - 1);
					textwidth = (int)(fontSample.getStringBounds(name, frc).getWidth());
				}
				sctCell.add(new Paragraph(sct.getMovieName()).setFontSize(venueFontSize).setFont(font)
						.setFontColor(ColorConstants.BLACK).setTextAlignment(TextAlignment.CENTER));
				sctCell.setBackgroundColor(dColor).setPadding(0);
				sctCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
				sctCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
				minCounter+= sct.getLengthMin();

				schedule_table.addCell(sctCell);
			}
			if (minCounter < max_table_width) {
				Cell fill = new Cell(1, max_table_width - minCounter).setPadding(0).setMargin(0);
				fill.setHeight(vdt.thisHeight);
				fill.setBorderTop(border).setBorderBottom(border).setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
				if (table.venueSCTList.indexOf(vdt) %2 == 0) {
					fill.setBackgroundColor(ColorConstants.GRAY);
				} else {
					fill.setBackgroundColor(ColorConstants.DARK_GRAY);
				}
				schedule_table.addCell(fill);
				
			}
			schedule_table.startNewRow();
		}
		
		return schedule_table;
	}

	/**
	 * Creates a type of cell that contains the date
	 * 
	 * @author Steven Ma
	 */
	private Cell createDateCell(int cellWidth, String date) {
		Cell cell = new Cell(1, cellWidth);
		cell.add(new Paragraph(date).setFontSize(DATE_FONT_SIZE).setBold().setFontColor(ColorConstants.WHITE)); // ColorConstants
		cell.setTextAlignment(TextAlignment.LEFT).setBackgroundColor(dColor).setPadding(0).setPaddingLeft(10);
		return cell;
	}

	/**
	 * Creates a type of cell that contains the schedule times
	 * 
	 * @author Steven Ma 3
	 */
	private Cell createTimeCell(String time) {
		Cell cell = new Cell(1, HOUR);
		cell.setBorder(Border.NO_BORDER);
		cell.add(new Paragraph(time)).setFontSize(TIME_FONT_SIZE).setPadding(5).setBold()
				.setFontColor(ColorConstants.WHITE).setBackgroundColor(bColor);
		return cell;
	}

	private class FoldedBorderCellRenderer extends CellRenderer {

		
	    public FoldedBorderCellRenderer(Cell modelElement) {
	        super(modelElement);
	    }
	 
	    @Override
	    public void draw(DrawContext drawContext) {
	    	PdfCanvas canvas = drawContext.getCanvas();
	    	canvas.saveState().setFillColor(vColor);
	    	Rectangle cellRect = getOccupiedAreaBBox();
	    	System.out.println("Drawiing");
	    	//Draw the custom Cell
	    	canvas.moveTo(cellRect.getX(), cellRect.getY() +  cellRect.getHeight());
	    	canvas.lineTo(cellRect.getX() + cellRect.getWidth(), cellRect.getY() +  cellRect.getHeight());
	    	canvas.lineTo(cellRect.getX() + cellRect.getWidth(), cellRect.getY());
	    	canvas.lineTo(cellRect.getX() + 5, cellRect.getY());
	    	canvas.lineTo(cellRect.getX(), cellRect.getY() + 5);
	    	canvas.closePathFillStroke().restoreState();
	    	//Fill the undrawn areas with black
	    	canvas.saveState().setFillColor(ColorConstants.BLACK);
	    	canvas.moveTo(cellRect.getX(), cellRect.getY());
	    	canvas.lineTo(cellRect.getX() + 5, cellRect.getY());
	    	canvas.lineTo(cellRect.getX(), cellRect.getY() + 5);
	    	canvas.closePathFillStroke().restoreState();
	        super.draw(drawContext);
	    }
	}
	
//	private Cell createVenueCell(String name, Table table) {
//		Cell cell = new Cell(1, HOUR * 2);
//		cell.add(new Paragraph(name).setWidth(table.getColumnWidth(0)).setFontSize(12)
//				.setTextAlignment(TextAlignment.CENTER).setBold().setFontColor(ColorConstants.BLACK));
//		cell.setTextAlignment(TextAlignment.CENTER).setBackgroundColor(vColor);
//		cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
//		cell.setWidth(table.getColumnWidth(0));
//		return cell;
//	}

//	private Table createVDTRow(VenueDateTable vdt, int index) {
//		Table vdtRow = new Table(number_of_columns);
//		vdtRow.useAllAvailableWidth().setHeight(vdt.thisHeight);
//		Cell venue = createVenueCell(vdt.thisVenue.getNameShort(), vdtRow);
//		venue.setHeight(vdt.thisHeight);
//		vdtRow.addCell(venue);
//		vdtRow.setHeight(vdt.thisHeight);
//
//		for (int i = 0; i < 16; i++) {
//			Cell screenTime = new Cell(1, 1);
//			if (i % 2 == 0) {
//				screenTime.setBackgroundColor(ColorConstants.GRAY);
//			} else {
//				screenTime.setBackgroundColor(ColorConstants.DARK_GRAY);
//			}
//			vdtRow.addCell(screenTime);
//		}
//		return vdtRow;
//	}
}
