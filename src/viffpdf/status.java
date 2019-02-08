package viffpdf;

public class status {
	static void print(String text) {
		StringBuilder string = new StringBuilder(Main.logArea.getText());
		string.append(text + "\n");
		Main.logArea.setText(string.toString());
	}
}
