package viffpdf;


import com.itextpdf.kernel.colors.Color;
/** the "save" menu to be developed **/
//TODO put this in a text file. In main or pdfgenerator, add a loading function that construct
//a Configuration object that loads into Main to be used.
public class Configuration {
	int venue_font_Size;
	Color dColor;
	Color bColor;
	Color vColor;
	Color sColor;
	Color hColor;
	Color fColor;
	Color oColor;
	Color eColor;
	int masterFont;
	
	public Configuration(int venueFontSize, Color dColor, Color bColor, Color vColor, Color sColor, Color hColor, Color fColor, Color oColor, Color eColor, int masterFont) {
		this.venue_font_Size = venueFontSize;
		this.dColor = dColor;
		this.bColor = bColor;
		this.vColor = vColor;
		this.sColor = sColor;
		this.hColor = hColor;
		this.fColor = fColor;
		this.oColor = oColor;
		this.eColor = eColor;
		this.masterFont = masterFont;
	}
	
	//might be better if return a collection.
	//tbd.
	public void load() {};
	

}
