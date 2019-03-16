package viffpdf;

import com.itextpdf.kernel.colors.Color;

public class Configuration {
	int venue_font_Size;
	Color dColor;
	Color bColor;
	Color vColor;
	
	public Configuration(int venueFontSize, Color dColor, Color bColor, Color vColor) {
		this.venue_font_Size = venueFontSize;
		this.dColor = dColor;
		this.bColor = bColor;
		this.vColor = vColor;
	}
	

}
