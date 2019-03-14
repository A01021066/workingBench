package viffpdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PDFGenerator {
	String destPath;
	public PDFGenerator(String dest, ArrayList<PageTable> PList) throws IOException {
		setDest(dest);
		File file = new File(dest);
		file.getParentFile().mkdirs();
	}
	
	
	public void setDest(String dest) {
		if (!dest.endsWith(".pdf"))
		{
			dest += ".pdf";
		}
		destPath = dest;
	}
}
