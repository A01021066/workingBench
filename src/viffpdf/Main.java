package viffpdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import com.itextpdf.kernel.colors.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	static Rectangle dayHeaderColor = new Rectangle(20, 20);
	static Rectangle backgroundColor = new Rectangle(20, 20);
	static Rectangle venueColor = new Rectangle(20, 20);
	static Rectangle screenTimeColor = new Rectangle(20, 20);
	static Rectangle dayHeaderTextColor = new Rectangle(20, 20);
	static Rectangle fontColor = new Rectangle(20, 20);
	static TextField dayHeaderCode = new TextField();
	static TextField backgroundCode = new TextField();
	static TextField venueCode = new TextField();
	static TextField screenTimeCode = new TextField();
	static TextField fontCode = new TextField();
	static TextField dayHeaderTextCode = new TextField();
	static Configuration config = null;
	static fontLib fontLib = new fontLib();
	static javafx.scene.paint.Color dColor;
	static javafx.scene.paint.Color bColor;
	static javafx.scene.paint.Color vColor;
	static javafx.scene.paint.Color sColor;
	static javafx.scene.paint.Color hColor;
	static javafx.scene.paint.Color fColor;
	static com.itextpdf.kernel.colors.Color dColorConfig = new com.itextpdf.kernel.colors.DeviceRgb(255, 165, 0);
	static com.itextpdf.kernel.colors.Color bColorConfig = new com.itextpdf.kernel.colors.DeviceRgb(0, 0, 0);
	static com.itextpdf.kernel.colors.Color vColorConfig = new com.itextpdf.kernel.colors.DeviceRgb(255, 165, 0);
	static com.itextpdf.kernel.colors.Color sColorConfig = new com.itextpdf.kernel.colors.DeviceRgb(255, 255, 255);
	static com.itextpdf.kernel.colors.Color fColorConfig = new com.itextpdf.kernel.colors.DeviceRgb(0, 0, 0);
	static com.itextpdf.kernel.colors.Color hColorConfig = new com.itextpdf.kernel.colors.DeviceRgb(255, 255, 255);

	static TextField rowHeightConfigInput = new TextField("13.1");
	static Text colorStat;
	static Text sectionStat;
	static Text venueStat;
	static Text screenTimeStat;
	static TextArea logArea;
	static RadioButton checkEmpty;
	static int masterFont;
	static File colorTab;
	static File sectionTab;
	static File venueTab;
	static File screenTimeTab;
	static int[] pageLayoutSetting = { 0, 0, 0, 0, 0, 0, 0, 0 };
	static boolean[] emptyRowSetting = { false, false, false, false, false, false, false, false };
	ArrayList<VenueTable> VTList = new ArrayList<VenueTable>();
	ArrayList<VenueDateTable> VDTList = new ArrayList<VenueDateTable>();
	ArrayList<DayTable> DList = new ArrayList<DayTable>();
	ArrayList<PageTable> PList = new ArrayList<PageTable>();
	ArrayList<Date> dateList = new ArrayList<Date>();
	HashMap<String, ColorData> colorList;
	TreeMap<String, SectionData> sectionList;
	private ArrayList<Button> fileTypes = new ArrayList<Button>(4);
	private ArrayList<Parser> parsers = new ArrayList<Parser>(4);
	private ArrayList<Text> fileStats = new ArrayList<Text>(4);
	private static final int SECTIONS = 0;
	private static final int COLORS = 1;
	private static final int VENUES = 2;
	private static final int SCREEN_TIMES = 3;
	

	/**
	 * Opens a file and passes it to the corresponding parser.
	 */
	public void loaderButton(ActionEvent event, Stage primaryStage) {
		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {

			// Opens a file for parsing
			int fileType = fileTypes.indexOf(event.getSource());

			Status.print("Opening: " + file.getAbsolutePath());

			try {
				Parser parser = parsers.get(fileType);
				Status.print("Loading: " + parser.getFileType());
				Status.print(parser.setData(file) + parser.getFileType() + " file opened properly!");
				fileStats.get(fileType).setText("Passed");
			} catch (FileNotFoundException e) {
				Status.print("File Not Found.  Details:");
				Status.print(e.getMessage());
				fileStats.get(fileType).setText("Failed");
			} catch (IllegalArgumentException e) {
				Status.print("File Not Valid.  Details:");
				Status.print(e.getMessage());
				fileStats.get(fileType).setText("Failed");
			} finally {
				// fOpenPns.get(fileType).setText(file.getName());
			}

			// Cancels file opening.
		} else {
			Status.print("File Open Cancelled.");
		}

	}

	public static void pageSettingUI(Stage primaryStage) {
		GridPane pageSettingLayOut = new GridPane();
		pageSettingLayOut.setHgap(10);
		pageSettingLayOut.setVgap(10);
		Scene pageSettingScene = new Scene(pageSettingLayOut, 400, 400);
		Text pageNumberText = new Text("Page#");
		pageNumberText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		Text daysPerPageText = new Text("Days/Page");
		daysPerPageText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		Text emptyRowText = new Text("Clear Empty Venue");
		emptyRowText.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
		pageSettingLayOut.add(emptyRowText, 8, 1);
		pageSettingLayOut.add(daysPerPageText, 6, 1);
		pageSettingLayOut.add(pageNumberText, 1, 1);
		for (int i = 2; i < 10; i++) {
			Text text = new Text("Page " + (i - 1));
			text.setTextAlignment(TextAlignment.CENTER);
			pageSettingLayOut.add(text, 1, i);
		}
		ArrayList<NumField> daysPerPageInput = new ArrayList<NumField>();
		String[] varNames = { "page1", "page2", "page3", "page4", "page5", "page6", "page7", "page8" };
		for (int n = 2; n < 10; n++) {
			NumField textField = new NumField();
			String defTxt = (n<10)? "4": "0";
			textField.setText(defTxt);
			textField.setId(varNames[n - 2]);
			pageSettingLayOut.add(textField, 6, n);
			daysPerPageInput.add(textField);
		}

		ArrayList<RadioButton> emptyRows = new ArrayList<RadioButton>();
		String[] radioNames = { "r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8" };
		for (int n = 2; n < 10; n++) {
			RadioButton radio = new RadioButton();
			radio.setId(radioNames[n - 2]);
			pageSettingLayOut.add(radio, 8, n);
			emptyRows.add(radio);
		}

		Button applyPageSetting = new Button("Apply");
		applyPageSetting.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Status.print("Applying days/page setting...");
				for (NumField n : daysPerPageInput) {
					if (n.getText().isEmpty()) {
						n.setText("0");
					}
					pageLayoutSetting[daysPerPageInput.indexOf(n)] = Integer.parseInt(n.getText());
				}

				for (RadioButton r : emptyRows) {
					emptyRowSetting[emptyRows.indexOf(r)] = r.isSelected();
				}
				Status.print("Days/page setting successfully applied.");
				int counter = 1;
				for (int num : pageLayoutSetting) {
					if (num > 0) {
						Status.print("Days on page " + counter + ": " + num);
					}
					counter++;
				}
				Stage stage = (Stage) applyPageSetting.getScene().getWindow();
				stage.hide();
			}
		});
		pageSettingLayOut.add(applyPageSetting, 6, 11);

		Stage pageSettingWindow = new Stage();
		pageSettingWindow.setTitle("Page Setting");
		pageSettingWindow.setResizable(false);
		pageSettingWindow.setScene(pageSettingScene);
		pageSettingWindow.setX(primaryStage.getX() + 50);
		pageSettingWindow.setY(primaryStage.getY() + 10);
		pageSettingWindow.initModality(Modality.APPLICATION_MODAL);
		pageSettingWindow.show();

	}

	public void start(Stage primaryStage) {

		parsers.add(SECTIONS, new SectionParser());
		parsers.add(COLORS, new ColorParser());
		parsers.add(VENUES, new VenueParser());
		parsers.add(SCREEN_TIMES, new ScreenTimeParser());
		// ---Loaders
		//
		GridPane loaderGroup = new GridPane();
		loaderGroup.setHgap(10);
		loaderGroup.setVgap(10);

		Button loadColor = new Button("Colors");
		loadColor.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				loaderButton(e, primaryStage);
			}
		});

		Button loadSection = new Button("Sections");
		loadSection.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				loaderButton(e, primaryStage);
			}
		});

		Button loadVenue = new Button("Venues");
		loadVenue.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				loaderButton(e, primaryStage);
			}
		});

		Button loadScreenTime = new Button("Screen Times");
		loadScreenTime.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				loaderButton(e, primaryStage);

			}
		});

		fileTypes.add(loadSection);
		fileTypes.add(loadColor);
		fileTypes.add(loadVenue);
		fileTypes.add(loadScreenTime);

		Text loaderTitle;
		loaderTitle = new Text("Input:");
		loaderTitle.setStyle("-fx-font-weight: bold");
		colorStat = new Text("N/A");
		sectionStat = new Text("N/A");
		venueStat = new Text("N/A");
		screenTimeStat = new Text("N/A");
		fileStats.add(SECTIONS, sectionStat);
		fileStats.add(COLORS, colorStat);
		fileStats.add(VENUES, venueStat);
		fileStats.add(SCREEN_TIMES, screenTimeStat);

		loaderGroup.add(loaderTitle, 1, 0);
		loaderGroup.add(loadColor, 1, 1);
		loaderGroup.add(colorStat, 1, 2);
		loaderGroup.add(loadSection, 2, 1);
		loaderGroup.add(sectionStat, 2, 2);
		loaderGroup.add(loadVenue, 3, 1);
		loaderGroup.add(venueStat, 3, 2);
		loaderGroup.add(loadScreenTime, 4, 1);
		loaderGroup.add(screenTimeStat, 4, 2);

		GridPane.setHalignment(colorStat, HPos.CENTER);
		GridPane.setHalignment(sectionStat, HPos.CENTER);
		GridPane.setHalignment(venueStat, HPos.CENTER);
		GridPane.setHalignment(screenTimeStat, HPos.CENTER);

		// ---Color configurations
		// ---
		GridPane theme = new GridPane();
		theme.setVgap(10);
		theme.setHgap(5);

		Text themeTitle = new Text("Color Config:");
		themeTitle.setStyle("-fx-font-weight: bold");
		Text dayHeader = new Text("Date Bar");
		Text background = new Text("Time Bar");
		Text venue = new Text("Venue Bar");
		Text screenTime = new Text("Movie Block");
		Text dayHeaderText = new Text("Date Text");
		Text font = new Text("Movie Text");

		// -- Color preview algorithm
		// cmyk to rgb
		Button dayHeaderCheck = new Button("?");
		dayHeaderCheck.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				String[] code = dayHeaderCode.getText().split("\\s+");
				double c = Double.parseDouble(code[0]);
				double m = Double.parseDouble(code[1]);
				double y = Double.parseDouble(code[2]);
				double k = Double.parseDouble(code[3]);
				double red = 255 * (1 - c) * (1 - k);
				double green = 255 * (1 - m) * (1 - k);
				double blue = 255 * (1 - y) * (1 - k);
				int r = (int) red;
				int g = (int) green;
				int b = (int) blue;
				dColor = Color.rgb(r, g, b);
				dColorConfig = new DeviceRgb(r, g, b);
				dayHeaderColor.setFill(dColor);
			}
		});
		
		Button backgroundCheck = new Button("?");
		backgroundCheck.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				String[] code = backgroundCode.getText().split("\\s+");
				double c = Double.parseDouble(code[0]);
				double m = Double.parseDouble(code[1]);
				double y = Double.parseDouble(code[2]);
				double k = Double.parseDouble(code[3]);
				double red = 255 * (1 - c) * (1 - k);
				double green = 255 * (1 - m) * (1 - k);
				double blue = 255 * (1 - y) * (1 - k);
				int r = (int) red;
				int g = (int) green;
				int b = (int) blue;
				bColor = Color.rgb(r, g, b);
				bColorConfig = new DeviceRgb(r, g, b);
				backgroundColor.setFill(bColor);
			}
		});
		
		Button venueCheck = new Button("?");
		venueCheck.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				String[] code = venueCode.getText().split("\\s+");
				double c = Double.parseDouble(code[0]);
				double m = Double.parseDouble(code[1]);
				double y = Double.parseDouble(code[2]);
				double k = Double.parseDouble(code[3]);
				double red = 255 * (1 - c) * (1 - k);
				double green = 255 * (1 - m) * (1 - k);
				double blue = 255 * (1 - y) * (1 - k);
				int r = (int) red;
				int g = (int) green;
				int b = (int) blue;
				vColor = Color.rgb(r, g, b);
				vColorConfig = new DeviceRgb(r, g, b);
				venueColor.setFill(vColor);
			}
		});
		
		Button sctCheck = new Button("?");
		sctCheck.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				String[] code = screenTimeCode.getText().split("\\s+");
				double c = Double.parseDouble(code[0]);
				double m = Double.parseDouble(code[1]);
				double y = Double.parseDouble(code[2]);
				double k = Double.parseDouble(code[3]);
				double red = 255 * (1 - c) * (1 - k);
				double green = 255 * (1 - m) * (1 - k);
				double blue = 255 * (1 - y) * (1 - k);
				int r = (int) red;
				int g = (int) green;
				int b = (int) blue;
				sColor = Color.rgb(r, g, b);
				sColorConfig = new DeviceRgb(r, g, b);
				screenTimeColor.setFill(sColor);
			}
		});
		
		Button dayHeaderTextCheck = new Button("?");
		dayHeaderTextCheck.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				String[] code = dayHeaderTextCode.getText().split("\\s+");
				double c = Double.parseDouble(code[0]);
				double m = Double.parseDouble(code[1]);
				double y = Double.parseDouble(code[2]);
				double k = Double.parseDouble(code[3]);
				double red = 255 * (1 - c) * (1 - k);
				double green = 255 * (1 - m) * (1 - k);
				double blue = 255 * (1 - y) * (1 - k);
				int r = (int) red;
				int g = (int) green;
				int b = (int) blue;
				hColor = Color.rgb(r, g, b);
				hColorConfig = new DeviceRgb(r, g, b);
				dayHeaderTextColor.setFill(hColor);
			}
		});
		
		Button fontCheck = new Button("?");
		fontCheck.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				String[] code = fontCode.getText().split("\\s+");
				double c = Double.parseDouble(code[0]);
				double m = Double.parseDouble(code[1]);
				double y = Double.parseDouble(code[2]);
				double k = Double.parseDouble(code[3]);
				double red = 255 * (1 - c) * (1 - k);
				double green = 255 * (1 - m) * (1 - k);
				double blue = 255 * (1 - y) * (1 - k);
				int r = (int) red;
				int g = (int) green;
				int b = (int) blue;
				fColor = Color.rgb(r, g, b);
				fColorConfig = new DeviceRgb(r, g, b);
				fontColor.setFill(fColor);
			}
		});

		dColor = Color.rgb(255, 165, 0);
		dayHeaderColor.setFill(dColor);// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/paint/Color.html
		bColor = Color.rgb(0, 0, 0); // https://www.rapidtables.com/convert/color/rgb-to-cmyk.html
		backgroundColor.setFill(bColor);
		vColor = Color.rgb(255, 165, 0); // https://www.rapidtables.com/convert/color/cmyk-to-rgb.html
		venueColor.setFill(vColor);
		sColor = Color.rgb(255, 255, 255);
		screenTimeColor.setFill(sColor);
		hColor = Color.rgb(255, 255, 255);
		dayHeaderTextColor.setFill(hColor);
		fColor = Color.rgb(0, 0, 0);
		fontColor.setFill(fColor);
		


		theme.add(themeTitle, 0, 0);
		theme.add(dayHeader, 0, 1);
		theme.add(background, 0, 2);
		theme.add(venue, 0, 3);
		theme.add(screenTime, 0, 4);
		theme.add(dayHeaderText, 0, 5);
		theme.add(font, 0, 6);
		theme.add(dayHeaderCode, 1, 1);
		theme.add(backgroundCode, 1, 2);
		theme.add(venueCode, 1, 3);
		theme.add(screenTimeCode, 1, 4);
		theme.add(dayHeaderTextCode, 1, 5);
		theme.add(fontCode, 1, 6);
		theme.add(dayHeaderCheck, 2, 1);
		theme.add(backgroundCheck, 2, 2);
		theme.add(venueCheck, 2, 3);
		theme.add(sctCheck, 2, 4);
		theme.add(dayHeaderTextCheck, 2, 5);
		theme.add(fontCheck, 2, 6);
		theme.add(dayHeaderColor, 3, 1);
		theme.add(backgroundColor, 3, 2);
		theme.add(venueColor, 3, 3);
		theme.add(screenTimeColor, 3, 4);
		theme.add(dayHeaderTextColor, 3, 5);
		theme.add(fontColor, 3, 6);
		

		// ---
		GridPane log = new GridPane();
		log.setVgap(10);
		log.setHgap(10);

		Text logTitle = new Text("Status: ");
		logTitle.setStyle("-fx-font-weight: bold");

		logArea = new TextArea();
		logArea.setEditable(false);
		logArea.setPrefHeight(355);
		logArea.setPrefWidth(450);

		log.add(logTitle, 1, 0);
		log.add(logArea, 1, 1);

		// ---Font configurations
		// ---
		GridPane fontConfig = new GridPane();
		fontConfig.setHgap(10);
		fontConfig.setVgap(10);

		Text fontTitle = new Text("Font Config:");
		fontTitle.setStyle("-fx-font-weight: bold");

		Text fontFace = new Text("Font");
		ObservableList<String> fonts = FXCollections.observableArrayList("Helvetica",			
				"NeueHaas", "Calibri", "Arial", "Garamond", "Geneva", "Verdana", "AvantGarde");
		final ComboBox<String> fontBox = new ComboBox<String>(fonts);
		fontBox.getSelectionModel().selectFirst();


		Text fontSize = new Text("Size");
		ObservableList<Integer> sizes = FXCollections.observableArrayList();
		for (int i = 2; i < 33; i++) {
			sizes.add(i);
		}
		final ComboBox<Integer> sizeBox = new ComboBox<Integer>(sizes); // only applying to venue font sizes yet.
		sizeBox.getSelectionModel().select(5);

		sizeBox.setPrefWidth(100);
		fontConfig.add(fontTitle, 0, 0);
		fontConfig.add(fontFace, 0, 1);
		fontConfig.add(fontBox, 1, 1);
		fontConfig.add(fontSize, 0, 2);
		fontConfig.add(sizeBox, 1, 2);

		//

		// ---Output buttons
		// ---
		GridPane outPutGroup = new GridPane();
		Button save = new Button("Save");
		Button load = new Button("Load");
		Button export = new Button("Generate");
		export.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				/**
				 * Bound all of these to the generate button or buffer button These are all test
				 * cases.
				 */

				// get the Venue datas from the VenueParser.
				HashMap<String, VenueData> venueList = ((VenueParser) parsers.get(2)).getVenueList();
				colorList = ((ColorParser) parsers.get(1)).getColorMap();
				Iterator<Map.Entry<String, ColorData>> itt = colorList.entrySet().iterator();
					while (itt.hasNext()) {
					Map.Entry<String, ColorData> e = (Map.Entry<String, ColorData>) itt.next();
					System.out.println(e.getValue().getColor());
				}
				sectionList = ((SectionParser) parsers.get(0)).getSectionList();
				// we need the iterator.
				Iterator<Map.Entry<String, VenueData>> it = venueList.entrySet().iterator();

				while (it.hasNext()) {
					// get each specific venue data entry
					Map.Entry<String, VenueData> entry = (Map.Entry<String, VenueData>) it.next();
					// get the venue object
					VenueData venue = entry.getValue();
					// arrange each venue object with their screen times.
					VenueTable tableEntry = new VenueTable(venue, (ScreenTimeParser) parsers.get(3));
					// add these venue+sct object into the list.
					VTList.add(tableEntry);
				}

				// create a date list for our references.
				for (VenueTable v : VTList) {
					for (ScreenTimeData s : v.thisTimeBlocks) {
						if (!dateList.contains(s.getDate())) {
							dateList.add(s.getDate());
						}
					}
				}
				// sort the dates
				Collections.sort(dateList);

				// for each venue we have, for each day we have.
				for (VenueTable vt : VTList) {
					for (Date d : dateList) {
						// create an VDT object that contains:
						// a specific venue(unique to object)
						// a specific date(unique to object)
						// an arraylist of sct data(all of the movies shown on this venue at this date)
						
						VenueDateTable vdtEntry = new VenueDateTable(vt, d, Float.parseFloat(rowHeightConfigInput.getText()));
						if (checkEmpty.isSelected()) {
							if (!vdtEntry.thisVDT.isEmpty()) {
								VDTList.add(vdtEntry);
							}
						} else {
							VDTList.add(vdtEntry);
						}

					}
				}

				// for each day
				for (Date day : dateList) {
					// create a dayTable object that contains:
					// a date(unique to object)
					// an arrayList of VDTs
					DayTable temp = new DayTable(VDTList, day);
					DList.add(temp);
				}
				int c = 1;
				int dayCounter = 0;
				for (int i : pageLayoutSetting) {
					if (dayCounter < DList.size()) {
						PageTable pTable = new PageTable(DList, i, c++);
						PList.add(pTable);
						dayCounter += pTable.numOfDays;
						Status.print(dayCounter + " days successfully allocated.");

					}
				}
				if (dayCounter <= dateList.size()) {
					int leftOver = dateList.size() - dayCounter;
					Status.print("After formatting, you have " + leftOver + " days left unallocated.");
//					int leftOverHeight = 0;
//					for (int l = DList.size() - leftOver; l < DList.size(); l++) {
//						leftOverHeight += DList.get(l).thisHeight;
//						Status.print(DList.get(l).dayDate + "");
//						Status.print(leftOverHeight + "");
//					}
//					try {
//						int leftOverPage = (int) Math.ceil(leftOverHeight / PList.get(0).maxHeight);
//						Status.print("It will take approximately " + leftOverPage
//								+ " more pages to fill in all of the days.");
//					} catch (IndexOutOfBoundsException empty) {
//						Status.print("Please update your page setting.");
//					}
//					super spaghetti, not priority, fix later				

				}

				AllTable table = new AllTable(VTList, VDTList, DList, PList, dateList, colorList, sectionList);
				masterFont = fonts.indexOf(fontBox.getValue());
				if (config == null) {
				config = new Configuration(sizeBox.getValue(), dColorConfig, bColorConfig, vColorConfig, sColorConfig, hColorConfig, fColorConfig, masterFont);
				}
				try {
					PDFGenerator generator = new PDFGenerator(System.getProperty("user.dir").toString() + "/viffpdf", table, config);
				} catch (IOException e) {
					e.printStackTrace();
				}

				PageTable.dayCount = 0;
				PageTable.pageCount = 1;
				DayTable.count = 0;
				VTList = new ArrayList<VenueTable>();
				VDTList = new ArrayList<VenueDateTable>();
				DList = new ArrayList<DayTable>();
				PList = new ArrayList<PageTable>();
				dateList = new ArrayList<Date>();
			}
		});
		
		save.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				config = new Configuration(sizeBox.getValue(), dColorConfig, bColorConfig, vColorConfig, sColorConfig, hColorConfig, fColorConfig, masterFont);
				try {
					FileOutputStream file = new FileOutputStream("save.ser");
					ObjectOutputStream out = new ObjectOutputStream(file);
					out.writeObject(config);
					out.close();
					file.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});

		load.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
				FileInputStream file = new FileInputStream("save.ser");
				ObjectInputStream in = new ObjectInputStream(file);
					config = (Configuration)in.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		//// Page Setting UI
		Button pageSetting = new Button("Page Setting");
		pageSetting.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				pageSettingUI(primaryStage);
			}
		});
		/////
		outPutGroup.add(export, 0, 0);
		outPutGroup.add(pageSetting, 1, 0);
		outPutGroup.add(save, 2, 0);
		outPutGroup.add(load, 3, 0);

		// ---Time block configurations
		// ---
		GridPane timeBlockConfig = new GridPane();
		timeBlockConfig.setHgap(10);
		timeBlockConfig.setVgap(10);
		Text rowHeightConfig = new Text("Max Row Height: ");

		rowHeightConfigInput.setPrefWidth(150);
		Text timeBlockTitle = new Text("Block Config:");
		timeBlockTitle.setStyle("-fx-font-weight: bold");
		checkEmpty = new RadioButton("Clear Empty Rows");
		timeBlockConfig.add(checkEmpty, 0, 2);
		timeBlockConfig.add(rowHeightConfig, 0, 3);
		timeBlockConfig.add(rowHeightConfigInput, 1, 3);
		timeBlockConfig.add(timeBlockTitle, 0, 0);
		

		// ---
		try {
			Group root = new Group();

			root.getChildren().add(loaderGroup);
			root.getChildren().add(theme);
			root.getChildren().add(log);
			root.getChildren().add(fontConfig);
			root.getChildren().add(outPutGroup);
			root.getChildren().add(timeBlockConfig);

			Scene scene = new Scene(root, 1000, 500);
			loaderGroup.setLayoutX(10);
			loaderGroup.setLayoutY(10);
			log.setLayoutX(10);
			log.setLayoutY(scene.getHeight() - 400);
			theme.setLayoutX(scene.getWidth() - 400);
			theme.setLayoutY(10);
			fontConfig.setLayoutX(scene.getWidth() - 400);
			fontConfig.setLayoutY(scene.getHeight() - 250);
			outPutGroup.setHgap(10);
			outPutGroup.setLayoutX(scene.getWidth() - 300);
			outPutGroup.setLayoutY(scene.getHeight() - 40);
			timeBlockConfig.setLayoutX(scene.getWidth() - 400);
			timeBlockConfig.setLayoutY(scene.getHeight() - 160);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.setTitle("VIFF-PDF Generator");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
