package viffpdf;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

//functions for updating status console

public class Status { 
    static void print(String text) {
        Text t = new Text(text + "\n");
        t.setFill(Color.BLACK);
        Main.logArea.getChildren().add(t); 
    }
    
	static void printError(String text) {
	    Text t = new Text(text + "\n");
	    t.setFill(Color.RED);
	    Main.logArea.getChildren().add(t);
	}
	
	static void printWarning(String text) {
       Text t = new Text(text); //parser warnings contain newline char at the end
        t.setFill(Color.BLUE);
        Main.logArea.getChildren().add(t);
	}
}
