/**@author Louis Liu
 *
 */

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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.scene.control.ColorPicker;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	static Configuration config = null;
	static configSave saveFile = new configSave();
	static javafx.scene.paint.Color dColor = Color.rgb(255, 165, 0);
	static javafx.scene.paint.Color bColor = Color.rgb(0, 0, 0);
	static javafx.scene.paint.Color vColor = Color.rgb(255, 165, 0);
	static javafx.scene.paint.Color sColor = Color.rgb(255, 255, 255);
	static javafx.scene.paint.Color hColor = Color.rgb(255, 255, 255);
	static javafx.scene.paint.Color fColor = Color.rgb(0, 0, 0);
	static javafx.scene.paint.Color oColor = Color.rgb(211, 211, 211);
	static javafx.scene.paint.Color eColor = Color.rgb(128, 128, 128);
	static com.itextpdf.kernel.colors.Color dColorConfig = new com.itextpdf.kernel.colors.DeviceRgb(255, 165, 0);
	static com.itextpdf.kernel.colors.Color bColorConfig = new com.itextpdf.kernel.colors.DeviceRgb(0, 0, 0);
	static com.itextpdf.kernel.colors.Color vColorConfig = new com.itextpdf.kernel.colors.DeviceRgb(255, 165, 0);
	static com.itextpdf.kernel.colors.Color sColorConfig = new com.itextpdf.kernel.colors.DeviceRgb(255, 255, 255);
	static com.itextpdf.kernel.colors.Color fColorConfig = new com.itextpdf.kernel.colors.DeviceRgb(0, 0, 0);
	static com.itextpdf.kernel.colors.Color hColorConfig = new com.itextpdf.kernel.colors.DeviceRgb(255, 255, 255);
	static com.itextpdf.kernel.colors.Color oColorConfig = new com.itextpdf.kernel.colors.DeviceRgb(211, 211, 211);
	static com.itextpdf.kernel.colors.Color eColorConfig = new com.itextpdf.kernel.colors.DeviceRgb(128, 128, 128);
	static ArrayList<NumField> daysPerPageInput = new ArrayList<NumField>();
	


	static TextField rowHeightConfigInput = new TextField("13");
	static Text colorStat;
	static Text sectionStat;
	static Text venueStat;
	static Text screenTimeStat;
	static TextFlow logArea;
	static RadioButton checkEmpty;
	static int masterFont;
	static File colorTab;
	static File sectionTab;
	static File venueTab;
	static File screenTimeTab;
	static int[] pageLayoutSetting = { 4, 4, 4, 4, 4, 4, 4, 4 };
	static boolean[] emptyRowSetting = { false, false, false, false, false, false, false, false };
//	ArrayList<VenueTable> VTList = new ArrayList<VenueTable>();
//	ArrayList<VenueDateTable> VDTList = new ArrayList<VenueDateTable>();
//	ArrayList<DayTable> DList = new ArrayList<DayTable>();
//	ArrayList<PageTable> PList = new ArrayList<PageTable>();
//	ArrayList<Date> dateList = new ArrayList<Date>();
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
				Status.printWarning(parser.setData(file)); //prints any warnings
				Status.print(parser.getFileType() + " file opened properly!");
				fileStats.get(fileType).setText("Passed");
			} catch (FileNotFoundException e) {
				Status.printError("File Not Found.  Details:");
				Status.printError(e.getMessage());
				fileStats.get(fileType).setText("Failed");
			} catch (IllegalArgumentException e) {
				Status.printError("File Not Valid.  Details:");
				Status.printError(e.getMessage());
				fileStats.get(fileType).setText("Failed");
			} finally {
				// fOpenPns.get(fileType).setText(file.getName());
			}

			// Cancels file opening.
		} else {
			Status.printError("File Open Cancelled.");
		}

	}

	public static void pageSettingUI(Stage primaryStage) {
		GridPane pageSettingLayOut = new GridPane();
		pageSettingLayOut.setHgap(10);
		pageSettingLayOut.setVgap(10);
		Scene pageSettingScene = new Scene(pageSettingLayOut, 300, 400);
		Text pageNumberText = new Text("Page#");
		pageNumberText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		Text daysPerPageText = new Text("Days/Page");
		daysPerPageText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		pageSettingLayOut.add(daysPerPageText, 6, 1);
		pageSettingLayOut.add(pageNumberText, 1, 1);
		for (int i = 2; i < 10; i++) {
			Text text = new Text("Page " + (i - 1));
			text.setTextAlignment(TextAlignment.CENTER);
			pageSettingLayOut.add(text, 1, i);
		}
		daysPerPageInput = new ArrayList<NumField>();
		String[] varNames = { "page1", "page2", "page3", "page4", "page5", "page6", "page7", "page8" };
		for (int n = 2; n < 10; n++) {
			NumField textField = new NumField();
			String defTxt = (n < 10) ? "4" : "0";
			textField.setText(defTxt);
			textField.setId(varNames[n - 2]);
			pageSettingLayOut.add(textField, 6, n);
			daysPerPageInput.add(textField);
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

	@SuppressWarnings("rawtypes")
	public void start(Stage primaryStage) {


		parsers.add(SECTIONS, new SectionParser());
		parsers.add(COLORS, new ColorParser());
		parsers.add(VENUES, new VenueParser());
		parsers.add(SCREEN_TIMES, new ScreenTimeParser());
		rowHeightConfigInput.setMaxWidth(50);
		saveFile.set('d', rgbToCmyk(dColor));
		saveFile.set('b', rgbToCmyk(bColor));
		saveFile.set('v', rgbToCmyk(vColor));
		saveFile.set('s', rgbToCmyk(sColor));
		saveFile.set('h', rgbToCmyk(hColor));
		saveFile.set('f', rgbToCmyk(fColor));
		saveFile.set('o', rgbToCmyk(oColor));
		saveFile.set('e', rgbToCmyk(eColor));

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
		Text oddRow = new Text("Odd Row");
		Text evenRow = new Text("Even Row");

		ColorPicker dayPicker = new ColorPicker();
		dayPicker.setMaxWidth(40);
		dayPicker.setValue(dColor);
        dayPicker.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				ArrayList<Double> list = rgbToCmyk(dayPicker.getValue());
				float c1 = list.get(0).floatValue();
				float m1 = list.get(1).floatValue();
				float y1 = list.get(2).floatValue();
				float k1 = list.get(3).floatValue();
				saveFile.set('d', list);
				dColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
			}
        });
        
		ColorPicker backPicker = new ColorPicker();
		backPicker.setMaxWidth(40);
		backPicker.setValue(bColor);
        backPicker.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				ArrayList<Double> list = rgbToCmyk(backPicker.getValue());
				float c1 = list.get(0).floatValue();
				float m1 = list.get(1).floatValue();
				float y1 = list.get(2).floatValue();
				float k1 = list.get(3).floatValue();
				saveFile.set('b', list);
				bColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
			}
        });
		
		ColorPicker venuePicker = new ColorPicker();
		venuePicker.setMaxWidth(40);
		venuePicker.setValue(vColor);
        venuePicker.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				ArrayList<Double> list = rgbToCmyk(venuePicker.getValue());
				float c1 = list.get(0).floatValue();
				float m1 = list.get(1).floatValue();
				float y1 = list.get(2).floatValue();
				float k1 = list.get(3).floatValue();
				saveFile.set('v', list);
				vColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
			}
        });
		
		ColorPicker screenPicker = new ColorPicker();
		screenPicker.setMaxWidth(40);
		screenPicker.setValue(sColor);
        screenPicker.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				ArrayList<Double> list = rgbToCmyk(screenPicker.getValue());
				float c1 = list.get(0).floatValue();
				float m1 = list.get(1).floatValue();
				float y1 = list.get(2).floatValue();
				float k1 = list.get(3).floatValue();
				saveFile.set('s', list);
				sColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
			}
        });
		
		ColorPicker dayTextPicker = new ColorPicker();
		dayTextPicker.setMaxWidth(40);
		dayTextPicker.setValue(hColor);
        dayTextPicker.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				ArrayList<Double> list = rgbToCmyk(dayTextPicker.getValue());
				float c1 = list.get(0).floatValue();
				float m1 = list.get(1).floatValue();
				float y1 = list.get(2).floatValue();
				float k1 = list.get(3).floatValue();
				saveFile.set('h', list);
				hColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
			}
        });
		
		ColorPicker fontPicker = new ColorPicker();
		fontPicker.setMaxWidth(40);
		fontPicker.setValue(fColor);
        fontPicker.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				ArrayList<Double> list = rgbToCmyk(fontPicker.getValue());
				float c1 = list.get(0).floatValue();
				float m1 = list.get(1).floatValue();
				float y1 = list.get(2).floatValue();
				float k1 = list.get(3).floatValue();
				saveFile.set('f', list);
				fColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
			}
        });
		
		ColorPicker oddPicker = new ColorPicker();
		oddPicker.setMaxWidth(40);
		oddPicker.setValue(oColor);
        oddPicker.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				ArrayList<Double> list = rgbToCmyk(oddPicker.getValue());
				float c1 = list.get(0).floatValue();
				float m1 = list.get(1).floatValue();
				float y1 = list.get(2).floatValue();
				float k1 = list.get(3).floatValue();
				saveFile.set('o', list);
				oColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
			}
        });
		
		ColorPicker evenPicker = new ColorPicker();
		evenPicker.setMaxWidth(40);
		evenPicker.setValue(eColor);
        evenPicker.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				ArrayList<Double> list = rgbToCmyk(evenPicker.getValue());
				float c1 = list.get(0).floatValue();
				float m1 = list.get(1).floatValue();
				float y1 = list.get(2).floatValue();
				float k1 = list.get(3).floatValue();
				saveFile.set('e', list);
				eColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
			}
        });

		ArrayList<TextField> colorInputs = new ArrayList<TextField>();

		TextField dc = new TextField();
		dc.setMaxWidth(50);
		colorInputs.add(dc);

		TextField dm = new TextField();
		dm.setMaxWidth(50);
		colorInputs.add(dm);

		TextField dy = new TextField();
		dy.setMaxWidth(50);
		colorInputs.add(dy);

		TextField dk = new TextField();
		dk.setMaxWidth(50);
		colorInputs.add(dk);

		TextField bc = new TextField();
		bc.setMaxWidth(50);
		colorInputs.add(bc);

		TextField bm = new TextField();
		bm.setMaxWidth(50);
		colorInputs.add(bm);

		TextField by = new TextField();
		by.setMaxWidth(50);
		colorInputs.add(by);

		TextField bk = new TextField();
		bk.setMaxWidth(50);
		colorInputs.add(bk);

		TextField vc = new TextField();
		vc.setMaxWidth(50);
		colorInputs.add(vc);

		TextField vm = new TextField();
		vm.setMaxWidth(50);
		colorInputs.add(vm);

		TextField vy = new TextField();
		vy.setMaxWidth(50);
		colorInputs.add(vy);

		TextField vk = new TextField();
		vk.setMaxWidth(50);
		colorInputs.add(vk);

		TextField sc = new TextField();
		sc.setMaxWidth(50);
		colorInputs.add(sc);

		TextField sm = new TextField();
		sm.setMaxWidth(50);
		colorInputs.add(sm);

		TextField sy = new TextField();
		sy.setMaxWidth(50);
		colorInputs.add(sy);

		TextField sk = new TextField();
		sk.setMaxWidth(50);
		colorInputs.add(sk);

		TextField fc = new TextField();
		fc.setMaxWidth(50);
		colorInputs.add(fc);

		TextField fm = new TextField();
		fm.setMaxWidth(50);
		colorInputs.add(fm);

		TextField fy = new TextField();
		fy.setMaxWidth(50);
		colorInputs.add(fy);

		TextField fk = new TextField();
		fk.setMaxWidth(50);
		colorInputs.add(fk);

		TextField oc = new TextField();
		oc.setMaxWidth(50);
		colorInputs.add(oc);

		TextField om = new TextField();
		om.setMaxWidth(50);
		colorInputs.add(om);

		TextField oy = new TextField();
		oy.setMaxWidth(50);
		colorInputs.add(oy);

		TextField ok = new TextField();
		ok.setMaxWidth(50);
		colorInputs.add(ok);

		TextField ec = new TextField();
		ec.setMaxWidth(50);
		colorInputs.add(ec);

		TextField em = new TextField();
		em.setMaxWidth(50);
		colorInputs.add(em);

		TextField ey = new TextField();
		ey.setMaxWidth(50);
		colorInputs.add(ey);

		TextField ek = new TextField();
		ek.setMaxWidth(50);
		colorInputs.add(ek);

		TextField hc = new TextField();
		hc.setMaxWidth(50);
		colorInputs.add(hc);

		TextField hm = new TextField();
		hm.setMaxWidth(50);
		colorInputs.add(hm);

		TextField hy = new TextField();
		hy.setMaxWidth(50);
		colorInputs.add(hy);

		TextField hk = new TextField();
		hk.setMaxWidth(50);
		colorInputs.add(hk);

		/*
		 * Special Thanks to: Johnny Fam
		 */
		Button dB = new Button("↵");
		/*
		 * Special Thanks ends here
		 * 
		 */

		dB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color test = null;
				try {
					double c = Double.parseDouble(dc.getText())/100;
					double m = Double.parseDouble(dm.getText())/100;
					double y = Double.parseDouble(dy.getText())/100;
					double k = Double.parseDouble(dk.getText())/100;
					test = cmykToRgb(c, m, y, k);
				} catch (NumberFormatException e) {
				    Status.printError("Please fill in all of the fields for cmyk.");
					return;
				}
				if (test != null) {
					ArrayList<Double> l = new ArrayList<Double>();
					l.add(Double.parseDouble(dc.getText())/100);
					l.add(Double.parseDouble(dm.getText())/100);
					l.add(Double.parseDouble(dy.getText())/100);
					l.add(Double.parseDouble(dk.getText())/100);
					float c1 = l.get(0).floatValue();
					float m1 = l.get(1).floatValue();
					float y1 = l.get(2).floatValue();
					float k1 = l.get(3).floatValue();
					dColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('d', l);
				}
				dayPicker.setValue(test);
			}
		});

		Button bB = new Button("↵");
		bB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color test = null;
				try {
					double c = Double.parseDouble(bc.getText())/100;
					double m = Double.parseDouble(bm.getText())/100;
					double y = Double.parseDouble(by.getText())/100;
					double k = Double.parseDouble(bk.getText())/100;
					test = cmykToRgb(c, m, y, k);
				} catch (NumberFormatException e) {
                    Status.printError("Please fill in all of the fields for cmyk.");
                    return;
                }
				if (test != null) {
					ArrayList<Double> l = new ArrayList<Double>();
					l.add(Double.parseDouble(bc.getText())/100);
					l.add(Double.parseDouble(bm.getText())/100);
					l.add(Double.parseDouble(by.getText())/100);
					l.add(Double.parseDouble(bk.getText())/100);
					float c1 = l.get(0).floatValue();
					float m1 = l.get(1).floatValue();
					float y1 = l.get(2).floatValue();
					float k1 = l.get(3).floatValue();
					bColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('b', l);
				}
				backPicker.setValue(test);
			}
		});

		Button vB = new Button("↵");
		vB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color test = null;
				try {
					double c = Double.parseDouble(vc.getText())/100;
					double m = Double.parseDouble(vm.getText())/100;
					double y = Double.parseDouble(vy.getText())/100;
					double k = Double.parseDouble(vk.getText())/100;
					test = cmykToRgb(c, m, y, k);
				} catch (NumberFormatException e) {
                    Status.printError("Please fill in all of the fields for cmyk.");
                    return;
                }
				if (test != null) {
					ArrayList<Double> l = new ArrayList<Double>();
					l.add(Double.parseDouble(vc.getText())/100);
					l.add(Double.parseDouble(vm.getText())/100);
					l.add(Double.parseDouble(vy.getText())/100);
					l.add(Double.parseDouble(vk.getText())/100);
					float c1 = l.get(0).floatValue();
					float m1 = l.get(1).floatValue();
					float y1 = l.get(2).floatValue();
					float k1 = l.get(3).floatValue();
					vColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('v', l);
				}
				venuePicker.setValue(test);
			}
		});

		Button sB = new Button("↵");
		sB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color test = null;
				try {
					double c = Double.parseDouble(sc.getText())/100;
					double m = Double.parseDouble(sm.getText())/100;
					double y = Double.parseDouble(sy.getText())/100;
					double k = Double.parseDouble(sk.getText())/100;
					test = cmykToRgb(c, m, y, k);
				} catch (NumberFormatException e) {
                    Status.printError("Please fill in all of the fields for cmyk.");
                    return;
                }
				if (test != null) {
					ArrayList<Double> l = new ArrayList<Double>();
					l.add(Double.parseDouble(sc.getText())/100);
					l.add(Double.parseDouble(sm.getText())/100);
					l.add(Double.parseDouble(sy.getText())/100);
					l.add(Double.parseDouble(sk.getText())/100);
					float c1 = l.get(0).floatValue();
					float m1 = l.get(1).floatValue();
					float y1 = l.get(2).floatValue();
					float k1 = l.get(3).floatValue();
					sColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('s', l);
				}
				screenPicker.setValue(test);
			}
		});

		Button hB = new Button("↵");
		hB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color test = null;
				try {
					double c = Double.parseDouble(hc.getText())/100;
					double m = Double.parseDouble(hm.getText())/100;
					double y = Double.parseDouble(hy.getText())/100;
					double k = Double.parseDouble(hk.getText())/100;
					test = cmykToRgb(c, m, y, k);
				} catch (NumberFormatException e) {
                    Status.printError("Please fill in all of the fields for cmyk.");
                    return;
                }
				if (test != null) {
					ArrayList<Double> l = new ArrayList<Double>();
					l.add(Double.parseDouble(hc.getText())/100);
					l.add(Double.parseDouble(hm.getText())/100);
					l.add(Double.parseDouble(hy.getText())/100);
					l.add(Double.parseDouble(hk.getText())/100);
					float c1 = l.get(0).floatValue();
					float m1 = l.get(1).floatValue();
					float y1 = l.get(2).floatValue();
					float k1 = l.get(3).floatValue();
					hColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('h', l);
				}
				dayTextPicker.setValue(test);
			}
		});

		Button fB = new Button("↵");
		fB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color test = null;
				try {
					double c = Double.parseDouble(fc.getText())/100;
					double m = Double.parseDouble(fm.getText())/100;
					double y = Double.parseDouble(fy.getText())/100;
					double k = Double.parseDouble(fk.getText())/100;
					test = cmykToRgb(c, m, y, k);
				} catch (NumberFormatException e) {
                    Status.printError("Please fill in all of the fields for cmyk.");
                    return;
                }
				if (test != null) {
					ArrayList<Double> l = new ArrayList<Double>();
					l.add(Double.parseDouble(fc.getText())/100);
					l.add(Double.parseDouble(fm.getText())/100);
					l.add(Double.parseDouble(fy.getText())/100);
					l.add(Double.parseDouble(fk.getText())/100);
					float c1 = l.get(0).floatValue();
					float m1 = l.get(1).floatValue();
					float y1 = l.get(2).floatValue();
					float k1 = l.get(3).floatValue();
					fColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('f', l);
				}
				fontPicker.setValue(test);
			}
		});

		Button oB = new Button("↵");
		oB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color test = null;
				try {
				double c = Double.parseDouble(oc.getText())/100;
				double m = Double.parseDouble(om.getText())/100;
				double y = Double.parseDouble(oy.getText())/100;
				double k = Double.parseDouble(ok.getText())/100;
				test = cmykToRgb(c, m, y, k);
				} catch (NumberFormatException e) {
                    Status.printError("Please fill in all of the fields for cmyk.");
                    return;
                }
				if (test != null) {
					ArrayList<Double> l = new ArrayList<Double>();
					l.add(Double.parseDouble(oc.getText())/100);
					l.add(Double.parseDouble(om.getText())/100);
					l.add(Double.parseDouble(oy.getText())/100);
					l.add(Double.parseDouble(ok.getText())/100);
					float c1 = l.get(0).floatValue();
					float m1 = l.get(1).floatValue();
					float y1 = l.get(2).floatValue();
					float k1 = l.get(3).floatValue();
					oColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('o', l);
				}
				oddPicker.setValue(test);
			}
		});

		Button eB = new Button("↵");
		eB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color test = null;
				try {
				double c = Double.parseDouble(ec.getText())/100;
				double m = Double.parseDouble(em.getText())/100;
				double y = Double.parseDouble(ey.getText())/100;
				double k = Double.parseDouble(ek.getText())/100;
				test = cmykToRgb(c, m, y, k);
				} catch (NumberFormatException e) {
                    Status.printError("Please fill in all of the fields for cmyk.");
                    return;
                }
				if (test != null) {
					ArrayList<Double> l = new ArrayList<Double>();
					l.add(Double.parseDouble(ec.getText())/100);
					l.add(Double.parseDouble(em.getText())/100);
					l.add(Double.parseDouble(ey.getText())/100);
					l.add(Double.parseDouble(ek.getText())/100);
					float c1 = l.get(0).floatValue();
					float m1 = l.get(1).floatValue();
					float y1 = l.get(2).floatValue();
					float k1 = l.get(3).floatValue();
					eColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('e', l);
				}
				evenPicker.setValue(test);
			}
		});

		theme.add(themeTitle, 0, 0);
		Text cLabel = new Text("      c");
		cLabel.setStyle("-fx-font-weight: bold");
		theme.add(cLabel, 2, 0);
		
		Text mLabel = new Text("      m");
		mLabel.setStyle("-fx-font-weight: bold");
		theme.add(mLabel, 3, 0);
		
		Text yLabel = new Text("      y");
		yLabel.setStyle("-fx-font-weight: bold");
		theme.add(yLabel, 4, 0);
		
		Text kLabel = new Text("      k");
		kLabel.setStyle("-fx-font-weight: bold");
		theme.add(kLabel, 5, 0);

		theme.add(dayHeader, 0, 1);
		theme.add(dayPicker, 1, 1);
		theme.add(dc, 2, 1);
		theme.add(dm, 3, 1);
		theme.add(dy, 4, 1);
		theme.add(dk, 5, 1);
		theme.add(dB, 6, 1);

		theme.add(background, 0, 2);
		theme.add(backPicker, 1, 2);
		theme.add(bc, 2, 2);
		theme.add(bm, 3, 2);
		theme.add(by, 4, 2);
		theme.add(bk, 5, 2);
		theme.add(bB, 6, 2);

		theme.add(venue, 0, 3);
		theme.add(venuePicker, 1, 3);
		theme.add(vc, 2, 3);
		theme.add(vm, 3, 3);
		theme.add(vy, 4, 3);
		theme.add(vk, 5, 3);
		theme.add(vB, 6, 3);

		theme.add(screenTime, 0, 4);
		theme.add(screenPicker, 1, 4);
		theme.add(sc, 2, 4);
		theme.add(sm, 3, 4);
		theme.add(sy, 4, 4);
		theme.add(sk, 5, 4);
		theme.add(sB, 6, 4);

		theme.add(dayHeaderText, 0, 5);
		theme.add(dayTextPicker, 1, 5);
		theme.add(hc, 2, 5);
		theme.add(hm, 3, 5);
		theme.add(hy, 4, 5);
		theme.add(hk, 5, 5);
		theme.add(hB, 6, 5);

		theme.add(font, 0, 6);
		theme.add(fontPicker, 1, 6);
		theme.add(fc, 2, 6);
		theme.add(fm, 3, 6);
		theme.add(fy, 4, 6);
		theme.add(fk, 5, 6);
		theme.add(fB, 6, 6);

		theme.add(oddRow, 0, 7);
		theme.add(oddPicker, 1, 7);
		theme.add(oc, 2, 7);
		theme.add(om, 3, 7);
		theme.add(oy, 4, 7);
		theme.add(ok, 5, 7);
		theme.add(oB, 6, 7);

		theme.add(evenRow, 0, 8);
		theme.add(evenPicker, 1, 8);
		theme.add(ec, 2, 8);
		theme.add(em, 3, 8);
		theme.add(ey, 4, 8);
		theme.add(ek, 5, 8);
		theme.add(eB, 6, 8);

		// ---
		FlowPane log = new FlowPane();
		
		ScrollPane logWrapper = new ScrollPane();

		Text logTitle = new Text("Status: ");
		logTitle.setStyle("-fx-font-weight: bold");

		logArea = new TextFlow();
		logArea.setPrefHeight(355);
		logArea.setPrefWidth(450);
		
		
		logWrapper.setContent(logArea);
	    logWrapper.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, 
	                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		log.getChildren().add(logTitle);
		log.getChildren().add(logWrapper);

		// ---Font configurations
		// ---
		GridPane fontConfig = new GridPane();
		fontConfig.setHgap(10);
		fontConfig.setVgap(10);

		Text fontTitle = new Text("Font:");
		fontTitle.setStyle("-fx-font-weight: bold");

		Text fontFace = new Text("Font");
		ObservableList<String> fonts = FXCollections.observableArrayList("Helvetica", "NeueHaas", "Calibri", "Arial",
				"Garamond", "Geneva", "Verdana", "AvantGarde");
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
		Button save = new Button("Save Setting");
		Button load = new Button("Load Setting");
		Button export = new Button("Create PDF");
		export.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				/**
				 * Bound all of these to the generate button or buffer button These are all test
				 * cases.
				 */

				ArrayList<VenueTable> VTList = new ArrayList<VenueTable>();
				ArrayList<VenueDateTable> VDTList = new ArrayList<VenueDateTable>();
				ArrayList<DayTable> DList = new ArrayList<DayTable>();
				ArrayList<PageTable> PList = new ArrayList<PageTable>();
				ArrayList<Date> dateList = new ArrayList<Date>();
				
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

						VenueDateTable vdtEntry = new VenueDateTable(vt, d,
								Float.parseFloat(rowHeightConfigInput.getText()));
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
				}

				AllTable table = new AllTable(VTList, VDTList, DList, PList, dateList, colorList, sectionList);
				masterFont = fonts.indexOf(fontBox.getValue());
				config = new Configuration(sizeBox.getValue(), dColorConfig, bColorConfig, vColorConfig, sColorConfig,
						hColorConfig, fColorConfig, oColorConfig, eColorConfig, masterFont);
				try {
					FileChooser fileChooser = new FileChooser();
					File dest = fileChooser.showSaveDialog(primaryStage);
					
					PDFGenerator generator = new PDFGenerator(dest.getAbsolutePath(),
							table, config);
				} catch (Exception e) {
					e.printStackTrace();
					Status.print("PDF creation failed. Please close opened PDF file if you're trying to override it.");
				}

				PageTable.dayCount = 0;
				PageTable.pageCount = 1;
				DayTable.count = 0;
			}
		});

		save.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				saveFile.setFont(fonts.indexOf(fontBox.getValue()));
				saveFile.setFontSize(sizeBox.getValue());
				try {
					final FileChooser fileChooser = new FileChooser();
					File dest = fileChooser.showSaveDialog(primaryStage);
					
					FileOutputStream file = new FileOutputStream(dest.getAbsolutePath());
					ObjectOutputStream out = new ObjectOutputStream(file);
					out.writeObject(saveFile);
					out.close();
					file.close();

				} catch (IOException e) {
					//e.printStackTrace();
					return;
				}	catch (NullPointerException e) {
					Status.printError("Saving cancelled.");
					//e.printStackTrace();
					return;
				}
				Status.print("Setting saving successful.");
			}

		});

		load.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					final FileChooser fileChooser = new FileChooser();
					File dest = fileChooser.showOpenDialog(primaryStage);
					FileInputStream file = new FileInputStream(dest.getAbsolutePath());
					ObjectInputStream in = new ObjectInputStream(file);
					configSave temp = (configSave) in.readObject();

					sizeBox.getSelectionModel().select(sizes.indexOf(temp.vfs));
					fontBox.getSelectionModel().select(temp.mf);
					float c1;
					float m1;
					float y1;
					float k1;
					
					ArrayList<Double> d = temp.getD();
					dayPicker.setValue(cmykToRgb(d));
					c1 = d.get(0).floatValue();
					m1 = d.get(1).floatValue();
					y1 = d.get(2).floatValue();
					k1 = d.get(3).floatValue();
					dColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('d', d);

					
					ArrayList<Double> b = temp.getB();
					backPicker.setValue(cmykToRgb(b));
					c1 = b.get(0).floatValue();
					m1 = b.get(1).floatValue();
					y1 = b.get(2).floatValue();
					k1 = b.get(3).floatValue();
					bColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('b', b);
					
					ArrayList<Double> v = temp.getV();
					venuePicker.setValue(cmykToRgb(v));
					c1 = v.get(0).floatValue();
					m1 = v.get(1).floatValue();
					y1 = v.get(2).floatValue();
					k1 = v.get(3).floatValue();
					vColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('v', v);
					
					ArrayList<Double> s = temp.getS();
					screenPicker.setValue(cmykToRgb(s));
					c1 = s.get(0).floatValue();
					m1 = s.get(1).floatValue();
					y1 = s.get(2).floatValue();
					k1 = s.get(3).floatValue();
					sColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('s', s);
					
					ArrayList<Double> h = temp.getH();
					dayTextPicker.setValue(cmykToRgb(h));
					c1 = h.get(0).floatValue();
					m1 = h.get(1).floatValue();
					y1 = h.get(2).floatValue();
					k1 = h.get(3).floatValue();
					hColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('h', h);
					
					ArrayList<Double> f = temp.getF();
					fontPicker.setValue(cmykToRgb(f));
					c1 = f.get(0).floatValue();
					m1 = f.get(1).floatValue();
					y1 = f.get(2).floatValue();
					k1 = f.get(3).floatValue();
					fColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('f', f);
					
					ArrayList<Double> o = temp.getO();
					oddPicker.setValue(cmykToRgb(o));
					c1 = o.get(0).floatValue();
					m1 = o.get(1).floatValue();
					y1 = o.get(2).floatValue();
					k1 = o.get(3).floatValue();
					oColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('o', o);
					
					ArrayList<Double> e = temp.getE();
					evenPicker.setValue(cmykToRgb(e));
					c1 = e.get(0).floatValue();
					m1 = e.get(1).floatValue();
					y1 = e.get(2).floatValue();
					k1 = e.get(3).floatValue();
					eColorConfig = new com.itextpdf.kernel.colors.DeviceCmyk(c1, m1, y1, k1);
					saveFile.set('e', e);



				} catch (ClassNotFoundException e) {
					//e.printStackTrace();
					return;
				} catch (IOException e) {
					//e.printStackTrace();
					return;
				} catch (NullPointerException e) {
					Status.printError("Loading cancelled.");
					//e.printStackTrace();
					return;
				}
				
				Status.print("Setting loading successful.");
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
		Text rowHeightConfig = new Text("Max Row Height:\n(13 recommended) ");

		rowHeightConfigInput.setPrefWidth(150);
		Text timeBlockTitle = new Text("Line:");
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
			primaryStage.show();
			loaderGroup.setLayoutX(primaryStage.getWidth() - 950);
			loaderGroup.setLayoutY(primaryStage.getHeight() - 590);
			log.setLayoutX(primaryStage.getWidth() - 940);
			log.setLayoutY(primaryStage.getHeight() - 500);
			theme.setLayoutX(primaryStage.getWidth() - 400);
			theme.setLayoutY(primaryStage.getHeight() - 590);
			fontConfig.setLayoutX(primaryStage.getWidth() - 400);
			fontConfig.setLayoutY(primaryStage.getHeight() - 280);
			outPutGroup.setHgap(10);
			outPutGroup.setLayoutX(primaryStage.getWidth() - 400);
			outPutGroup.setLayoutY(primaryStage.getHeight() - 150);
			timeBlockConfig.setLayoutX(primaryStage.getWidth() - 200);
			timeBlockConfig.setLayoutY(primaryStage.getHeight() - 280);
			//primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.setTitle("VIFF-PDF Generator");

		} catch (Exception e) {
			e.printStackTrace();

			return;
		}
	}

	static Color cmykToRgb(ArrayList<Double> list) {
		Color output;
		double c = list.get(0);
		double m = list.get(1);
		double y = list.get(2);
		double k = list.get(3);
		double red = 255 * (1 - c) * (1 - k);
		double green = 255 * (1 - m) * (1 - k);
		double blue = 255 * (1 - y) * (1 - k);
		int r = (int) red;
		int g = (int) green;
		int b = (int) blue;
		output = Color.rgb(r, g, b);
		return output;
	}
	
	static Color cmykToRgb(double c, double m, double y, double k) {
		Color output;
		if (c > 1 || m > 1 || y > 1 || k > 1) {
		    Status.printError("Please only use value between 0 and 100 for cmyk.");
		    return null;
		}
		double red = 255 * (1 - c) * (1 - k);
		double green = 255 * (1 - m) * (1 - k);
		double blue = 255 * (1 - y) * (1 - k);
		int r = (int) red;
		int g = (int) green;
		int b = (int) blue;
		output = Color.rgb(r, g, b);
		return output;
	}

	static ArrayList<Double> rgbToCmyk(Color color) {
		ArrayList<Double> list = new ArrayList<Double>();
		double r = color.getRed();
		double g = color.getGreen();
		double b = color.getBlue();
		double max = Math.max(Math.max(r, g), b);
		double k = 1 - max;
		double c = (1 - r - k) / (1 - k);
		double m = (1 - g - k) / (1 - k);
		double y = (1 - b - k) / (1 - k);
		list.add(c);
		list.add(m);
		list.add(y);
		list.add(k);
		return list;
	}

	public static void main(String[] args) {
		launch(args);

	}
}
