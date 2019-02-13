package viffpdf;
//functions for updating status console

public class Status {
	static void print(String text) {
		StringBuilder string = new StringBuilder(Main.logArea.getText());
		string.append(text + "\n");
		Main.logArea.setText(string.toString());
	}
}
