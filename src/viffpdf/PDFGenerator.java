package viffpdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

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
import com.itextpdf.layout.property.FontKerning;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
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
	float rowHeight;
	PdfFont font;

	ArrayList<PageTable> pageList = new ArrayList<PageTable>();
	ArrayList<VenueTable> venueList = new ArrayList<VenueTable>();
	ArrayList<VenueDateTable> screenTimeList = new ArrayList<VenueDateTable>();
	ArrayList<DayTable> dayList = new ArrayList<DayTable>();
	ArrayList<Date> dateList = new ArrayList<Date>();
	// Used to specify the amount of cells in a row and the weight of each cell

	// margin spaces between tables on the document
	private final int TABLE_MARGIN = 3;

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
		rowHeight = screenTimeList.get(0).thisHeight;
		switch (config.masterFont) {
		case 0:
			font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
			// http://itextsupport.com/apidocs/itext7/7.1.1/com/itextpdf/io/font/constants/StandardFonts.html
			// black magic
			break;
		case 1:
			font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
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

		// set up the file
		PdfWriter writer = new PdfWriter(destPath);
		PdfDocument pdf = new PdfDocument(writer);
		pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageBackgroundsEventHandler());
		Document document = new Document(pdf, new PageSize(PAGE_WIDTH, PAGE_HEIGHT).rotate());
		document.setFontProvider(document.getFontProvider());
		document.setMargins(0, 5, 0, 5);
		SimpleDateFormat fmt = new SimpleDateFormat("EEEEEEE, MMMMMMMM dd", Locale.US);

		// draw each page we have in the data.
		for (PageTable pt : pageList) {
			int heightCounter = 0;
			for (int i = 0; i < pt.numOfDays; i++) {
				Table dayTable = createSchedule(pt.dayList.get(i), fmt.format(pt.dayList.get(i).dayDate));
				document.add(dayTable);
				heightCounter += pt.thisHeight;

			}
			// if the page's content isn't large enough to make the whole page.
			if (pt.thisHeight <= PAGE_HEIGHT) {
				Table rect = new Table(number_of_columns);
				rect.useAllAvailableWidth().setHeight(PAGE_HEIGHT - pt.thisHeight);
				rect.setBackgroundColor(ColorConstants.WHITE);
				document.add(rect);
			}
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
			canvas.saveState().setFillColor(WebColors.getRGBColor("Black"))
					.rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight()).fillStroke()
					.restoreState();
		}
	}

	private Table createSchedule(DayTable table, String date) {
		//System.out.println("Creating table for Day " + table.dayDate);
		// Counters
		SolidBorder border = new SolidBorder(1.0f);
		Border evenRightGrid = new DottedBorder(ColorConstants.GRAY, 0.5f, 1.0f);
		Border evenLeftGrid = new DottedBorder(ColorConstants.GRAY, 0.85f, 1.0f);
		Border oddRightGrid = new DottedBorder(ColorConstants.DARK_GRAY, 0.5f, 1.0f);
		Border oddLeftGrid = new DottedBorder(ColorConstants.DARK_GRAY, 0.85f, 1.0f);

		// Size of table in columns
		int number_of_columns = 1080;
		int max_table_width = 960;

		// list of times
		String[] times = { "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00",
				"20:00", "21:00", "22:00", "23:00", "00:00", "01:00" };

		// Initialize table with 1080 cells across
		Table schedule_table = new Table(number_of_columns);
		// prevent table from spliting.
		schedule_table.setKeepTogether(true);
		
		schedule_table.useAllAvailableWidth().setTextAlignment(TextAlignment.CENTER)
				.setHorizontalAlignment(HorizontalAlignment.CENTER).setMarginBottom(TABLE_MARGIN);
		schedule_table.setBorder(Border.NO_BORDER);
		// adding date at the top
		schedule_table.addHeaderCell(createDateCell(number_of_columns, date));
		Cell cell;

		// adding the blank space left on the time gird row
		cell = new Cell(1, HOUR * 2).setBackgroundColor(bColor).setPadding(0).setBorder(Border.NO_BORDER);
		schedule_table.addHeaderCell(cell);

		// adding the time grid
		for (int i = 0; i < times.length; i++) {
			schedule_table.addHeaderCell(createTimeCell(times[i]));
		}

		// adding each venue + screentime row
		for (VenueDateTable vdt : table.venueSCTList) {

			// adding venue cell, same length as the blank space for time grid
			Cell vdtCell = new Cell(1, HOUR * 2);;
			vdtCell.setKeepTogether(true);
			vdtCell.setNextRenderer(new FoldedBorderCellRenderer(vdtCell));
			vdtCell.add(new Paragraph(vdt.thisVenue.getNameShort()).setWidth(schedule_table.getColumnWidth(0))
					.setFontSize(venueFontSize).setFont(font).setTextAlignment(TextAlignment.CENTER).setBold()
					.setFontColor(ColorConstants.BLACK)).setPadding(0).setMargin(0);
			vdtCell.setTextAlignment(TextAlignment.CENTER)/* .setBackgroundColor(vColor) */;
			vdtCell.setHorizontalAlignment(HorizontalAlignment.CENTER);
			vdtCell.setVerticalAlignment(VerticalAlignment.MIDDLE);

			// TODO do we go with height setting, or font size setting?
			vdtCell.setHeight(rowHeight).setBorder(Border.NO_BORDER);
			vdtCell.setBorderBottom(Border.NO_BORDER);

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
						Cell emptyCell = new Cell(1, 15 - minCounter % 15).setPadding(0).setMargin(-5f)
								.setBorder(Border.NO_BORDER).setBorderTop(border).setBorderBottom(border);
						emptyCell.setHeight(rowHeight);
						emptyCell.setMargin(0);
						emptyCell.setKeepTogether(true);

						if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
							emptyCell.setBackgroundColor(ColorConstants.GRAY);
						} else {
							emptyCell.setBackgroundColor(ColorConstants.DARK_GRAY);

						}

						if (minCounter == 30 || (minCounter - 30) % HOUR == 0) {
							if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
								emptyCell.setBorderLeft(evenLeftGrid);
								emptyCell.setBorderRight(evenRightGrid);
							} else {
								emptyCell.setBorderLeft(oddLeftGrid);
								emptyCell.setBorderRight(oddRightGrid);
							}
						} else {
							if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
								emptyCell.setBorderRight(evenRightGrid);
							} else {
								emptyCell.setBorderRight(oddRightGrid);
							}
						}
						minCounter += QUARTER_HOUR - (minCounter % 15);
						schedule_table.addCell(emptyCell);
					}

					Cell emptyCell = new Cell(1, 15).setPadding(0).setMargin(0);
					emptyCell.setMargin(0);
					emptyCell.setKeepTogether(true);


					if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
						emptyCell.setBackgroundColor(ColorConstants.GRAY);
					} else {
						emptyCell.setBackgroundColor(ColorConstants.DARK_GRAY);

					}
					emptyCell.setHeight(rowHeight);
					emptyCell.setBorderTop(border).setBorderBottom(border).setBorderLeft(Border.NO_BORDER)
							.setBorderRight(Border.NO_BORDER);
					emptyCell.setBorder(Border.NO_BORDER);
					if (minCounter == 30 || (minCounter - 30) % HOUR == 0) {
						if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
							emptyCell.setBorderLeft(evenLeftGrid);
							emptyCell.setBorderRight(evenRightGrid);
						} else {
							emptyCell.setBorderLeft(oddLeftGrid);
							emptyCell.setBorderRight(oddRightGrid);
						}
					} else {
						if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
							emptyCell.setBorderRight(evenRightGrid);
						} else {
							emptyCell.setBorderRight(oddRightGrid);
						}

					}
					emptyCell.setMargin(0);
					schedule_table.addCell(emptyCell);
					minCounter += 15;
					// alternate between gray and dark gray TODO change this to be user defined
					// later

					// increment the min counter
					// minCounter += (movieStartBlock - minCounter);
				}

				// after the initial blank space. Add in the movie show time block.
				Cell sctCell = new Cell(1, sct.getLengthMin());
				sctCell.setHeight(rowHeight).setPadding(0).setMargin(0).setMaxWidth(0); // the setMaxWidth(0) fixed
																						// the streching issue.
																						// Black magic.
				sctCell.setKeepTogether(true);
				sctCell.setBorderTop(border).setBorderBottom(border).setBorderLeft(Border.NO_BORDER)
						.setBorderRight(Border.NO_BORDER);

				Paragraph p = new Paragraph();
				p.setPadding(0).setMargin(0);
				
				Text name = new Text(sct.getMovieName());
				name.setFontSize(venueFontSize - 2).setFont(font)
				.setFontColor(ColorConstants.BLACK).setTextAlignment(TextAlignment.CENTER).setFontKerning(FontKerning.NO).setWordSpacing(0.0f);
				
				Text time = new Text( "\n" + sct.getTimeDetails());
				time.setFontSize(venueFontSize - 3f).setFont(font)
				.setFontColor(ColorConstants.DARK_GRAY).setTextAlignment(TextAlignment.CENTER).setFontKerning(FontKerning.NO).setWordSpacing(0.0f);
				p.add(name);
				p.add(time);
				
				
				sctCell.add(p);
				
				if (minCounter == 30 || (minCounter - 30) % HOUR == 0) {
					if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
						sctCell.setBorderLeft(evenLeftGrid);
						//sctCell.setBorderRight(evenRightGrid);
					} else {
						sctCell.setBorderLeft(oddLeftGrid);
						//sctCell.setBorderRight(oddRightGrid);
					}
				} else if ((minCounter + sct.getLengthMin()) % 15 == 0) {
					if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
						sctCell.setBorderRight(evenRightGrid);
					} else {
						sctCell.setBorderRight(oddRightGrid);
					}
				}
				sctCell.setBackgroundColor(dColor).setPadding(0).setMargin(0);
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
							.setBorder(Border.NO_BORDER).setBorderTop(border).setBorderBottom(border);
					fillCell.setHeight(rowHeight);
					fillCell.setKeepTogether(true);

					if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
						fillCell.setBackgroundColor(ColorConstants.GRAY);
					} else {
						fillCell.setBackgroundColor(ColorConstants.DARK_GRAY);

					}

					if (minCounter == 30 || (minCounter - 30) % HOUR == 0) {
						if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
							fillCell.setBorderLeft(evenLeftGrid);
							fillCell.setBorderRight(evenRightGrid);
						} else {
							fillCell.setBorderLeft(oddLeftGrid);
							fillCell.setBorderRight(oddRightGrid);
						}
					} else {
						if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
							fillCell.setBorderRight(evenRightGrid);
						} else {
							fillCell.setBorderRight(oddRightGrid);
						}
					}
					minCounter += QUARTER_HOUR - (minCounter % 15);
					schedule_table.addCell(fillCell);
				}

				Cell fillCell = new Cell(1, 15).setPadding(0).setMargin(0);
				fillCell.setHeight(rowHeight);
				fillCell.setBorderTop(border).setBorderBottom(border).setBorderLeft(Border.NO_BORDER)
						.setBorderRight(Border.NO_BORDER);
				fillCell.setBorder(Border.NO_BORDER);
				fillCell.setKeepTogether(true);
				if (minCounter == 30 || (minCounter - 30) % HOUR == 0) {
					if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
						fillCell.setBorderLeft(evenLeftGrid);
						fillCell.setBorderRight(evenRightGrid);
					} else {
						fillCell.setBorderLeft(oddLeftGrid);
						fillCell.setBorderRight(oddRightGrid);
					}
				} else {
					if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
						fillCell.setBorderRight(evenRightGrid);
					} else {
						fillCell.setBorderRight(oddRightGrid);
					}
				}
				minCounter += 15;
				if (table.venueSCTList.indexOf(vdt) % 2 == 0) {
					fillCell.setBackgroundColor(ColorConstants.GRAY);
				} else {
					fillCell.setBackgroundColor(ColorConstants.DARK_GRAY);
				}
				// TODO use % 15 to draw dotted line for time grid.
				schedule_table.addCell(fillCell);
			}
			// start new row for next VDTable;
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
		cell.setHeight(rowHeight);
		cell.setKeepTogether(true);
		cell.setBorder(Border.NO_BORDER);
		cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		cell.add(new Paragraph(date).setFontSize(venueFontSize).setBold().setFontColor(ColorConstants.WHITE)
				.setPadding(0).setMargin(0)); // ColorConstants
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
		cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
		cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		cell.add(new Paragraph(time)).setFontSize(venueFontSize).setPaddingLeft(0).setBold().setPadding(0).setMargin(0)
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
}
