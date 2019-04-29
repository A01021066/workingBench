README

VIFFPDF Application

VIFFPDF is a PDF schedule generator for the Vancouver International Film Festival.
It generates the screen times of movies and events for each day of the festival.
Colors, fonts, and other display features can be modified before creating the PDF file.
 
Starting the application via Eclipse:
	Ensure that Eclipse is updated and Java SDK is at least version 9.
	1. Open Eclipse.
 	2. Expand the "viffpdf" directory in the Package Explorer (to the left of the window).
 	3. Expand the following directories inside the viffpdf directory: src > viffpdf. Various .java files should appear.
 	4. Search for a file named "Main.java", right-click the file and select Run As > Java Application.
 	
Creating a runnable jar file with Eclipse:
 	1. Open Eclipse.
 	2. Find the "viffpdf" directory in the Package Explorer.
 	3. Right-click the directory and click Export.
 	4. Expand the Java directory. 
 	5. Select "Runnable JAR File" and click Next.
 	6. Select the Launch Configuration: Make sure "Main - viffpdf" is selected.
 	7. Enter the Export Destination or select "Browse..." to choose where the JAR file will be exported. 
 		Make sure a name for the jar file is included in the Export Destination (i.e. "viffpdf.jar")
 	8. Click Finish. The exported JAR file will be in chosen directory.
 	
Running the application:
	Four tab files are required to create a PDF schedule, each corresponding to components of the schedule:
	 - Colors.tab: Color values of screen-time color strips.
	 - Sections.tab: Section code of each screen-time/event.
	 - Venues.tab:	Venues of the film festival.
	 - Screentime.tab: Various data of each film/event in the festival. Includes film duration, start times, and end times.
	 
	1. Once the application window appears, upload each of the four files by clicking each button in the Input section.
		Each button opens a window to select the correct file for each button. Users will be notified if the selected file is valid.
		The appropriate files are entered if "Passed" appears below all buttons. 	
	2. Click the "Page Settings" button at the bottom right. A new window will appear to configure the how many days will appear in each page.
		A "Day" is the schedule of all screen times of all venues for that particular day.
	3. Click on any radio buttons to clear any empty venues for that particular page (venues without screen-times for that day).
	4. Click Apply to apply these page settings to the PDF.
	6. Configure any other settings for PDF (Color Config, Font Config, etc.).
	7. Click Generate to create the PDF. The generated PDF should be within the same directory as the JAR file. 
		If it was created via Eclipse, it will be located inside the viffpdf directory.
		
	NOTE: Steps 1-4 are the minimum steps required to create a PDF with a default style. 
		
		