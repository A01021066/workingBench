package viffpdf;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

public class fontLib implements Serializable {
	ArrayList<PdfFont> fonts = new ArrayList<PdfFont>();
	public fontLib() {
		try {
			
			//the path here is for the exported .jar file
			//when run from eclipse, this path likely wont work
			PdfFont Helvetica = PdfFontFactory.createFont(StandardFonts.HELVETICA);
			fonts.add(Helvetica);
			PdfFont NeueHaas = PdfFontFactory.createFont("fonts/NeueHaas.otf");
			fonts.add(NeueHaas);
			PdfFont Calibri = PdfFontFactory.createFont("fonts/Calibri.ttf");
			fonts.add(Calibri);
			PdfFont Arial = PdfFontFactory.createFont("fonts/Arial.ttf");
			fonts.add(Arial);
			PdfFont Garamond = PdfFontFactory.createFont("fonts/Garamond.ttf");
			fonts.add(Garamond);
			PdfFont Geneva = PdfFontFactory.createFont("fonts/Geneva.ttf");
			fonts.add(Geneva);
			PdfFont Verdana = PdfFontFactory.createFont("fonts/Verdana.ttf");
			fonts.add(Verdana);
			PdfFont AvantGarde = PdfFontFactory.createFont("fonts/AvantGarde.ttf");
			fonts.add(AvantGarde);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<PdfFont> getFontLib(){
		return fonts;
	}
}
