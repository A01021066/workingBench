package viffpdf;

import java.io.File;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	static Rectangle dayHeaderColor = new Rectangle(20, 20);
	static Rectangle backgroundColor = new Rectangle(20, 20);
	static Rectangle venueColor = new Rectangle(20, 20);
	static TextField minPerPxlField;
	static TextField dayHeaderCode = new TextField();
	static TextField backgroundCode = new TextField();
	static TextField venueCode = new TextField();
	static Color dColor;
	static Color bColor;
	static Color vColor;
	static Text colourStat;
	static Text sectionStat;
	static Text venueStat;
	static Text screenTimeStat;
	static TextArea logArea;
	
	static File colourTab;
	static File sectionTab;
	static File venueTab;
	static File screenTimeTab;
	
	public void start(Stage primaryStage) {
		final FileChooser fileChooser = new FileChooser();
		//---
		GridPane loaderGroup = new GridPane();
		loaderGroup.setHgap(10);
		loaderGroup.setVgap(10);
		
		Button loadColour = new Button("Colours");
		loadColour.setOnAction(
	            new EventHandler<ActionEvent>() {
	                @Override
	                public void handle(final ActionEvent e) {
	                    File file = fileChooser.showOpenDialog(primaryStage);
	                    if (file != null) {
	                        colourTab = file;
	                        status.print("Loading " + colourTab.toString() + "...");
	                        colourStat.setText("Loaded!");
	                        status.print("Successful!");
	                    }
	                }
	            });
		Button loadSection = new Button("Sections");
		loadSection.setOnAction(
	            new EventHandler<ActionEvent>() {
	                @Override
	                public void handle(final ActionEvent e) {
	                    File file = fileChooser.showOpenDialog(primaryStage);
	                    if (file != null) {
	                        sectionTab = file;
	                        status.print("Loading " + sectionTab.toString() + "...");
	                        sectionStat.setText("Loaded!");
	                        status.print("Successful!");
	                    }
	                }
	            });
		Button loadVenue = new Button("Venues");
		loadVenue.setOnAction(
	            new EventHandler<ActionEvent>() {
	                @Override
	                public void handle(final ActionEvent e) {
	                    File file = fileChooser.showOpenDialog(primaryStage);
	                    if (file != null) {
	                        venueTab = file;
	                        status.print("Loading " + venueTab.toString() + "...");
	                        venueStat.setText("Loaded!");
	                        status.print("Successful!");
	                    }
	                }
	            });
		Button loadScreenTime = new Button("Screen Times");
		loadScreenTime.setOnAction(
	            new EventHandler<ActionEvent>() {
	                @Override
	                public void handle(final ActionEvent e) {
	                    File file = fileChooser.showOpenDialog(primaryStage);
	                    if (file != null) {
	                        screenTimeTab = file;
	                        status.print("Loading " + screenTimeTab.toString() + "...");
	                        screenTimeStat.setText("Loaded!");
	                        status.print("Successful!");
	                    }
	                }
	            });
		
		
		Text loaderTitle = new Text("Input:");
		loaderTitle.setStyle("-fx-font-weight: bold");
		colourStat = new Text("N/A");
		sectionStat = new Text("N/A");
		venueStat = new Text("N/A");
		screenTimeStat = new Text("N/A");
		
		loaderGroup.add(loaderTitle, 1, 0);
		loaderGroup.add(loadColour, 1, 1);
		loaderGroup.add(colourStat, 1, 2);
		loaderGroup.add(loadSection, 2, 1);
		loaderGroup.add(sectionStat, 2, 2);
		loaderGroup.add(loadVenue, 3, 1);
		loaderGroup.add(venueStat, 3, 2);
		loaderGroup.add(loadScreenTime, 4, 1);
		loaderGroup.add(screenTimeStat, 4, 2);
		
		GridPane.setHalignment(colourStat, HPos.CENTER);
		GridPane.setHalignment(sectionStat, HPos.CENTER);
		GridPane.setHalignment(venueStat, HPos.CENTER);
		GridPane.setHalignment(screenTimeStat, HPos.CENTER);
		
		
		//---
		GridPane theme = new GridPane();
		theme.setVgap(10);
		theme.setHgap(5);
		
		Text themeTitle = new Text("Color Config:");
		themeTitle.setStyle("-fx-font-weight: bold");
		Text dayHeader = new Text("Day Header");
		Text background = new Text("Background");
		Text venue = new Text("Venue");
		

		Button dayHeaderCheck = new Button("?");
		Button backgroundCheck = new Button("?");
		Button venueCheck = new Button("?");
		

		dColor = Color.rgb(255,165,0);
		dayHeaderColor.setFill(dColor);// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/paint/Color.html
		bColor = Color.rgb(0,0,0); // https://www.rapidtables.com/convert/color/rgb-to-cmyk.html
		backgroundColor.setFill(bColor);
		vColor = Color.rgb(255,165,0); //https://www.rapidtables.com/convert/color/cmyk-to-rgb.html
		venueColor.setFill(vColor);

		theme.add(themeTitle, 0, 0);
		theme.add(dayHeader, 0, 1);
		theme.add(background, 0, 2);
		theme.add(venue, 0, 3);
		theme.add(dayHeaderCode, 1, 1);
		theme.add(backgroundCode, 1, 2);
		theme.add(venueCode, 1, 3);
		theme.add(dayHeaderCheck, 2, 1);
		theme.add(backgroundCheck, 2, 2);
		theme.add(venueCheck, 2, 3);
		theme.add(dayHeaderColor, 3, 1);
		theme.add(backgroundColor, 3, 2);
		theme.add(venueColor, 3, 3);
		
		
		//---
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
		
		
		//---
		GridPane fontConfig = new GridPane();
		fontConfig.setHgap(10);
		fontConfig.setVgap(10);
		
		Text fontTitle = new Text("Font Config:");
		fontTitle.setStyle("-fx-font-weight: bold");
		
		Text fontFace = new Text("Font");
		ObservableList<String> fonts = FXCollections.observableArrayList(
			        "Times New Roman",
			        "Comic Sans",
			        "Fonts"
			    );
		final ComboBox<String> fontBox = new ComboBox<String>(fonts);
		
		Text fontSize = new Text("Size");
		ObservableList<Integer> sizes = FXCollections.observableArrayList(
			        6,
			        8,
			        10
			    );
		final ComboBox<Integer> sizeBox = new ComboBox<Integer>(sizes); //only applies to the screen time blocks
		//other font sizes are fixed for printing purpose.
		
		sizeBox.setPrefWidth(50);
		fontConfig.add(fontTitle, 0, 0);
		fontConfig.add(fontFace, 0, 1);
		fontConfig.add(fontBox, 1, 1);
		fontConfig.add(fontSize, 0, 2);
		fontConfig.add(sizeBox, 1, 2);
		
		
		//
		
		//---
		GridPane outPutGroup = new GridPane();
		Button export = new Button("Generate");
		outPutGroup.add(export, 0, 0);
		
		
		//---
		GridPane timeBlockConfig = new GridPane();
		timeBlockConfig.setHgap(10);
		timeBlockConfig.setVgap(10);
		Text timeBlockTitle = new Text("Block Config:");
		timeBlockTitle.setStyle("-fx-font-weight: bold");
		Text minPerPxl = new Text("Mins/Pixel");
		minPerPxlField = new TextField();
		minPerPxlField.setPrefWidth(50);
		timeBlockConfig.add(timeBlockTitle, 0, 0);
		timeBlockConfig.add(minPerPxl, 0, 1);
		timeBlockConfig.add(minPerPxlField, 1, 1);
		
		//---
		try {
			Group root = new Group();

			root.getChildren().add(loaderGroup);
			root.getChildren().add(theme);
			root.getChildren().add(log);
			root.getChildren().add(fontConfig);
			root.getChildren().add(outPutGroup);
			root.getChildren().add(timeBlockConfig);
			
			Scene scene = new Scene(root, 1000,500);
			loaderGroup.setLayoutX(10);
			loaderGroup.setLayoutY(10);
			log.setLayoutX(10);
			log.setLayoutY(scene.getHeight() - 400);
			theme.setLayoutX(scene.getWidth() - 400);
			theme.setLayoutY(10);
			fontConfig.setLayoutX(scene.getWidth() - 400);
			fontConfig.setLayoutY(scene.getHeight() - 350);
			outPutGroup.setLayoutX(scene.getWidth() - 170);
			outPutGroup.setLayoutY(scene.getHeight() - 40);
			timeBlockConfig.setLayoutX(scene.getWidth() - 400);
			timeBlockConfig.setLayoutY(scene.getHeight() - 230);
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
