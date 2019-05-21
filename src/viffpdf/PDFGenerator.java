package viffpdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.TreeMap;

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
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.BorderRadius;
import com.itextpdf.layout.property.FontKerning;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TableRenderer;

public class PDFGenerator {
	String destPath;
	int venueFontSize;
	Color dColor;
	Color bColor;
	Color vColor;
	Color sColor;
	Color hColor;
	Color fColor;
	Color oColor;
	Color eColor;
	int masterFont;
	float rowHeight;
	PdfFont font;

	ArrayList<PageTable> pageList = new ArrayList<PageTable>();
	ArrayList<VenueTable> venueList = new ArrayList<VenueTable>();
	ArrayList<VenueDateTable> screenTimeList = new ArrayList<VenueDateTable>();
	ArrayList<DayTable> dayList = new ArrayList<DayTable>();
	ArrayList<Date> dateList = new ArrayList<Date>();
	HashMap<String, ColorData> colorList;
	TreeMap<String, SectionData> sectionList;
	// Used to specify the amount of cells in a row and the weight of each cell

	// margin spaces between tables on the document
	private final int TABLE_MARGIN = 3;

	private final int number_of_columns = 1080;

	/**
	 * Constant for the value of the PDF's page width.
	 */
	private static final float PAGE_WIDTH = 651;
	/**
	 * Constant for the value of the PDF's page height.
	 */
	private static final float PAGE_HEIGHT = 736;

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
		sColor = config.sColor;
		hColor = config.hColor;
		fColor = config.fColor;
		oColor = config.oColor;
		eColor = config.eColor;
		rowHeight = screenTimeList.get(0).thisHeight;
		colorList = table.colorList;
		sectionList = table.sectionList;
		fontLib fontLib = new fontLib();
		ArrayList<PdfFont> fonts = fontLib.fonts;
		font = fonts.get(config.masterFont);
		generate();
	}

	public void setDest(String dest) {
		if (!dest.endsWith(".pdf")) {
			dest += ".pdf";
		}
		destPath = dest;
	}

	public void generate() throws FileNotFoundException {

		// set up the file
		PdfWriter writer = new PdfWriter(destPath);
		PdfDocument pdf = new PdfDocument(writer);
		pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageBackgroundsEventHandler());
		Document document = new Document(pdf, new PageSize(PAGE_WIDTH, PAGE_HEIGHT).rotate());
		document.setFontProvider(document.getFontProvider());
		document.setMargins(5, 5, 0, 5);
		SimpleDateFormat fmt = new SimpleDateFormat("EEEEEEE, MMMMMMMM dd", Locale.US);
		PageTable.pageCount = 1;
		PageTable.dayCount = 0;
		// draw each page we have in the data.
		for (PageTable pt : pageList) {
			float heightCounter = 0;
			for (int i = 0; i < pt.numOfDays; i++) {
				Table dayTable = createSchedule(pt.dayList.get(i), fmt.format(pt.dayList.get(i).dayDate));

				document.add(dayTable);
				heightCounter += pt.thisHeight;

			}
			// if the page's content isn't large enough to make the whole page.
			if (pt.thisHeight <= PAGE_WIDTH) {
				Table rect = new Table(number_of_columns);
				rect.useAllAvailableWidth().setHeight(PAGE_WIDTH - pt.thisHeight);
				rect.setBackgroundColor(ColorConstants.BLACK);
				heightCounter += (PAGE_WIDTH - pt.thisHeight);
				document.add(rect);
			}

			System.out.println("Page height counter: " + pt.thisHeight);
			System.out.println(PAGE_WIDTH);
		}

		document.close();
	}

	protected class PageBackgroundsEventHandler implements IEventHandler {

		@Override
		public void handleEvent(Event event) {
			PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
			PdfPage page = docEvent.getPage();
			PdfCanvas canvas = new PdfCanvas(page);
			Rectangle rect = page.getPageSize();
			canvas.saveState().setFillColor(ColorConstants.BLACK)
					.rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight()).fillStroke()
					.restoreState();
		}
	}

	private Table createSchedule(DayTable table, String date) {
		// System.out.println("Creating table for Day " + table.dayDate);

		float heightCounter = 0;
		Border gridMin = new DashedBorder(0.5f);
		Border gridHour = new DashedBorder(0.75f);

		// Size of table in columns
		int number_of_columns = 1080;
		int max_table_width = 960;

		// list of times
		String[] times = { "10:00AM", "11:00AM", "12:00PM", "1:00PM", "2:00PM", "3:00PM", "4:00PM", "5:00PM", "6:00PM",
				"7:00PM", "8:00PM", "9:00PM", "10:00PM", "11:00PM", "12:00AM", "1:00AM" };

		// Initialize table with 1080 cells across
		Table schedule_table = new Table(number_of_columns);

		// prevent table from spliting.
		schedule_table.setKeepTogether(true);

		schedule_table.useAllAvailableWidth().setTextAlignment(TextAlignment.CENTER)
				.setHorizontalAlignment(HorizontalAlignment.CENTER).setMarginBottom(TABLE_MARGIN);
		schedule_table.setBorder(Border.NO_BORDER);

		// adding date at the top
		Cell dateCell = createDateCell(number_of_columns, date);

		schedule_table.addHeaderCell(dateCell);
		heightCounter += rowHeight;
		Cell cell;

		// adding the blank space left on the time gird row
		cell = new Cell(1, HOUR * 2).setBackgroundColor(bColor).setPadding(0).setBorder(Border.NO_BORDER);
		schedule_table.addHeaderCell(cell);
		heightCounter += rowHeight;

		// adding the time grid
		for (int i = 0; i < times.length; i++) {
			schedule_table.addHeaderCell(createTimeCell(times[i]));
		}

		// adding each venue + screentime row
		for (VenueDateTable vdt : table.venueSCTList) {
			heightCounter += rowHeight;
			// adding venue cell, same length as the blank space for time grid
			Cell vdtCell = new Cell(1, HOUR * 2);
			vdtCell.setKeepTogether(true);
			vdtCell.setNextRenderer(new FoldedBorderCellRenderer(vdtCell));
			vdtCell.add(new Paragraph(vdt.thisVenue.getNameShort()).setWidth(schedule_table.getColumnWidth(0))
					.setFontSize(venueFontSize).setFont(font).setTextAlignment(TextAlignment.CENTER).setBold()
					.setFontColor(ColorConstants.BLACK)).setPadding(0).setMargin(0);
			vdtCell.setTextAlignment(TextAlignment.CENTER)/* .setBackgroundColor(vColor) */;
			vdtCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
			vdtCell.setVerticalAlignment(VerticalAlignment.MIDDLE);

			// do we go with height setting, or font size setting?
			vdtCell.setHeight(rowHeight).setBorder(Border.NO_BORDER);
			vdtCell.setBorderBottom(Border.NO_BORDER);
			// System.out.println("Row Height: " + vdtCell.getHeight());

			// adding screen times for this row.
			schedule_table.addCell(vdtCell);
			// needed to track the time elapse
			int minCounter = 0;

			for (ScreenTimeData sct : vdt.thisVDT) {
				// the column # of which the movie starts at.
				int movieStartBlock = sct.getStartBlock();
				// if the movie doesn't start at column 120 (9:30AM, or the 570th minutes of the
				// day)
				while (movieStartBlock > minCounter) {
					// create an empty cell to fill in the blank before the first movie starts
					if (minCounter % QUARTER_HOUR != 0) {
						Cell emptyCell = new Cell(1, 15 - minCounter % 15).setPadding(0).setMargin(0)
								.setBorderTop(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER)
								.setBorderRight(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER);
						emptyCell.setHeight(rowHeight);
						// emptyCell.setWidth(0);
						emptyCell.setMargin(0);
						emptyCell.setPadding(0);
						emptyCell.setKeepTogether(true);

						if ((minCounter - 30 + (15 - minCounter % 15)) % 60 == 0) {
							emptyCell.setBorderRight(gridHour);
						} else {
							emptyCell.setBorderRight(gridMin);
						}

						if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
							emptyCell.setBackgroundColor(eColor);
						} else {
							emptyCell.setBackgroundColor(oColor);
						}
						minCounter += QUARTER_HOUR - (minCounter % 15);
						schedule_table.addCell(emptyCell);
					}

					Cell emptyCell = new Cell(1, 15).setPadding(0).setMargin(0);

					emptyCell.setKeepTogether(true);
					emptyCell.setHeight(rowHeight);
					emptyCell.setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
							.setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
					if ((minCounter - 30 + (15)) % 60 == 0) {
						emptyCell.setBorderRight(gridHour);
					} else {
						emptyCell.setBorderRight(gridMin);
					}

					if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
						emptyCell.setBackgroundColor(eColor);
					} else {
						emptyCell.setBackgroundColor(oColor);
					}
					schedule_table.addCell(emptyCell);
					minCounter += 15;
				}

//				// after the initial blank space. Add in the movie show time block.
				Cell sctCell = new Cell(1, sct.getLengthMin());
				System.out.println("Minutes:" + sct.getLengthMin());
				// sctCell.setWidth(10);

				sctCell.setKeepTogether(true);
				sctCell.setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
						.setBorderRight(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER);

				sctCell.setBorderLeft(new SolidBorder(0.5f));
				sctCell.setBorderTop(new SolidBorder(0.25f));
				sctCell.setBorderBottom(new SolidBorder(0.25f));
				// sctCell.setBorderRight(new SolidBorder(0.25f));

				if (colorList.containsKey(sct.getSectionCode())) {
					sctCell.setNextRenderer(
							new ColoredCellRenderer(sctCell, colorList.get(sct.getSectionCode()).getColor()));
				} else {
					sctCell.setBackgroundColor(sColor);

				}

				Paragraph p = new Paragraph();
				p.setPadding(0).setMargin(0);
				Text name = new Text(sct.getMovieName());
				name.setFontSize(venueFontSize - 2f).setFont(font).setFontColor(fColor)
						.setTextAlignment(TextAlignment.CENTER).setFontKerning(FontKerning.NO).setWordSpacing(0.0f);

				Text time = new Text("\n" + sct.getTimeDetails());
				time.setFontSize(venueFontSize - 3f).setFont(font)
						.setTextAlignment(TextAlignment.CENTER).setFontKerning(FontKerning.NO).setWordSpacing(0.0f)
						.setFontColor(ColorConstants.GRAY);
				p.setMultipliedLeading(0.9f);
				p.add(name);
				p.add(time);

				sctCell.add(p);
				sctCell.setHeight(rowHeight);
				sctCell.setPadding(0).setMargin(0).setMaxWidth(0); // the setMaxWidth(0) fixed
				// the streching issue.
				// Black magic.
				sctCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
				sctCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
				minCounter += sct.getLengthMin();
				schedule_table.addCell(sctCell);
			}

			// if the last movie finishes before the day finishes, add the filling cell to
			// finish off the day.
			while (minCounter < max_table_width) {
				if (minCounter % QUARTER_HOUR != 0) {
					Cell fillCell = new Cell(1, 15 - minCounter % 15).setPadding(0).setMargin(0)
							.setBorder(Border.NO_BORDER).setBorderTop(Border.NO_BORDER)
							.setBorderBottom(Border.NO_BORDER);
					fillCell.setHeight(rowHeight);
					fillCell.setKeepTogether(true);

					if ((minCounter - 30 + (15 - minCounter % 15)) % 60 == 0) {
						fillCell.setBorderRight(gridHour);
					} else {
						fillCell.setBorderRight(gridMin);
					}

					if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
						fillCell.setBackgroundColor(eColor);
					} else {
						fillCell.setBackgroundColor(oColor);
					}
					minCounter += QUARTER_HOUR - (minCounter % 15);
					schedule_table.addCell(fillCell);
				}

				Cell fillCell = new Cell(1, 15).setPadding(0).setMargin(0);
				fillCell.setHeight(rowHeight);
				fillCell.setBorderTop(Border.NO_BORDER).setBorderBottom(Border.NO_BORDER)
						.setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER);
				fillCell.setBorder(Border.NO_BORDER);
				if (minCounter + 15 < HOUR * 16) {
					if ((minCounter - 30 + (15)) % 60 == 0) {
						fillCell.setBorderRight(gridHour);
					} else {
						fillCell.setBorderRight(gridMin);
					}
				}

				fillCell.setKeepTogether(true);
				minCounter += 15;
				if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
					fillCell.setBackgroundColor(eColor);
				} else {
					fillCell.setBackgroundColor(oColor);
				}
				schedule_table.addCell(fillCell);
			}
			// start new row for next VDTable;
			schedule_table.startNewRow();
		}

		System.out.println("Day Height: " + heightCounter);
		schedule_table.setNextRenderer(new tableBackgroundRenderer(schedule_table, table));
		return schedule_table;
	}

	/**
	 * Creates a type of cell that contains the date
	 * 
	 * @author Steven Ma
	 */
	private Cell createDateCell(int cellWidth, String date) {
		Cell cell = new Cell(1, cellWidth);
		cell.setHeight(rowHeight);
		cell.setKeepTogether(true);
		cell.setPadding(0);
		cell.setMargin(0);
		cell.setBorder(Border.NO_BORDER);
		cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		cell.add(new Paragraph(date).setFontSize(venueFontSize).setBold().setFontColor(hColor).setPadding(0)
				.setMargin(0));
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
		cell.setKeepTogether(true);
		cell.setBorder(Border.NO_BORDER);
		cell.setHeight(rowHeight);
		cell.setPadding(0);
		cell.setMargin(0);
		cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
		cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		cell.add(new Paragraph(time)).setFontSize(venueFontSize).setBold().setPadding(0).setMargin(0)
				.setFontColor(ColorConstants.WHITE).setBackgroundColor(bColor);
		return cell;
	}

	// triangle.
	private class FoldedBorderCellRenderer extends CellRenderer {
		public FoldedBorderCellRenderer(Cell modelElement) {
			super(modelElement);
		}

		@Override
		public void draw(DrawContext drawContext) {
			PdfCanvas canvas = drawContext.getCanvas();
			canvas.saveState().setFillColor(vColor);
			Rectangle cellRect = getOccupiedAreaBBox();
			// Draw the custom Cell
			canvas.moveTo(cellRect.getX(), cellRect.getY() + cellRect.getHeight());
			canvas.lineTo(cellRect.getX() + cellRect.getWidth(), cellRect.getY() + cellRect.getHeight());
			canvas.lineTo(cellRect.getX() + cellRect.getWidth(), cellRect.getY());
			canvas.lineTo(cellRect.getX() + 5, cellRect.getY());
			canvas.lineTo(cellRect.getX(), cellRect.getY() + 5);
			canvas.closePathFillStroke().restoreState();
			// Fill the undrawn areas with black
			canvas.saveState().setFillColor(ColorConstants.BLACK);
			canvas.moveTo(cellRect.getX(), cellRect.getY());
			canvas.lineTo(cellRect.getX() + 5, cellRect.getY());
			canvas.lineTo(cellRect.getX(), cellRect.getY() + 5);
			canvas.closePathFillStroke().restoreState();
			super.draw(drawContext);
		}
	}

	// precious colored bar
	private class ColoredCellRenderer extends CellRenderer {
		private Color color;

		public ColoredCellRenderer(Cell modelElement, Color color) {
			super(modelElement);
			this.color = color;
		}

		@Override
		public void draw(DrawContext drawContext) {
			PdfCanvas canvas = drawContext.getCanvas();
			Rectangle cellRect = getOccupiedAreaBBox();

			canvas.saveState().setFillColor(sColor).setStrokeColor(sColor).setLineWidth(0.5f);
			float borderMargin = 0.25f;
			// move cursor to top left corner
			canvas.moveTo(cellRect.getX(), cellRect.getY());

			// line to top right corner
			canvas.lineTo(cellRect.getX() + cellRect.getWidth(), cellRect.getY());

			// line to bottom right corner
			canvas.lineTo(cellRect.getX() + cellRect.getWidth(), cellRect.getY() + cellRect.getHeight());

			// line to bottom left corner
			canvas.lineTo(cellRect.getX(), cellRect.getY() + cellRect.getHeight());

			// line to top left corner
			canvas.lineTo(cellRect.getX(), cellRect.getY());
			canvas.closePathFillStroke().restoreState();

			canvas.saveState().setFillColor(color).setStrokeColor(color);
			canvas.rectangle(cellRect.getX() + cellRect.getWidth() - 2, cellRect.getY() + borderMargin, 1.8,
					cellRect.getHeight() - 2 * (borderMargin));
			canvas.fillStroke().restoreState();

			super.draw(drawContext);
		}
	}

	// time grid
	// TODO
	private class tableBackgroundRenderer extends TableRenderer {

		DayTable day = null;

		public tableBackgroundRenderer(Table modelElement, DayTable day) {
			super(modelElement);
			this.day = day;
		}

		@Override
		public void drawBackground(DrawContext drawContext) {
			PdfCanvas canvas = drawContext.getCanvas();
			Rectangle cellRect = getOccupiedAreaBBox();
			double heightCounter = 3.85;
			double rowHeight1 = rowHeight + 0.15;
			int rowCounter = 0;
			for (VenueDateTable vdt : day.venueSCTList) {
				if (rowCounter % 2 == 0) {
					canvas.saveState().setFillColor(eColor).setStrokeColor(eColor);
					canvas.rectangle(cellRect.getX(), cellRect.getY() + heightCounter, cellRect.getWidth() - 2,
							rowHeight1);
					canvas.fillStroke().restoreState();
				} else if (rowCounter % 2 != 0) {
					canvas.saveState().setFillColor(oColor).setStrokeColor(oColor);
					canvas.rectangle(cellRect.getX(), cellRect.getY() + heightCounter, cellRect.getWidth() - 2,
							rowHeight1);
					canvas.fillStroke().restoreState();
				}
				heightCounter += rowHeight1;
				rowCounter++;
			}
		}
	}
}
