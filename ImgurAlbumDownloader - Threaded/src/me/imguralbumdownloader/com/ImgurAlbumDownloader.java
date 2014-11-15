package me.imguralbumdownloader.com;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

/**
 * 
 * @author Micah Lee
 * ImgurAlbumDownloader is a thread which will concurrently download an Imgur Album
 */
public class ImgurAlbumDownloader implements Runnable {
	//Regex's to match for specific file types
	public static final String gifs = "//i\\.imgur\\.com/.*\\.gif";
	public static final String pngs = "//i\\.imgur\\.com/.*\\.png";
	public static final String jpgs = "//i\\.imgur\\.com/.*\\.jpg";
	public static final String webm = "//i\\.imgur\\.com/.*\\.(webm|mp4)";
	public static final String all = "//i\\.imgur\\.com/.*\\..*";
	private final String albumURL;
	private final String path;
	//matchingPattern will hold which of the Regex's to hold, which will be used in determining how to download the images
	private final String matchingPattern;
	private final DownloadingWindow window;
	//continueDownload is used to determine whether or not this download has been cancelled
	public boolean continueDownload = true;
	//Parser used to parse the website's html
	private Parser parser;
	//Array List to hold the image sources
	private final ArrayList<String> imgSrcs;
	private String albumTitle;
	
	/**
	 * Constructor
	 * @param url - URL of the imgur album
	 * @param pth - where to download the imgur album
	 * @param pattern - the pattern to be used for this download (what type of files to download)
	 * @param w - the window responsible for this downloader
	 */
	public ImgurAlbumDownloader(final String url, final String pth, final String pattern, final DownloadingWindow w) {
		//Set the imgurURL
		albumURL = url;
		//Instantiate the image sources
		imgSrcs = new ArrayList<String>();
		//Create a NodeList for <title> elements in the imgur album
		NodeList title = null;
		//Instantiate the parser
		parser = new Parser();
		//Get the title from the website
		try {
			parser.setURL(albumURL);
			title = parser.parse(new TagNameFilter("title"));
		} catch (ParserException e1) {
			JOptionPane.showMessageDialog(null, "Invalid connection. Check the URL, or your internet connection.", "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
		//Reset parse for re-use
		parser.reset();
		//Store the title obtained from the parser
		albumTitle = title.remove(0).toPlainTextString().trim();
		//Set the path, and create a new folder named after the imgur album
		this.path = pth + albumTitle + "\\";
		//Test file to check if directory exists
		File testFile = new File(path.substring(0, path.length() -1));
		//If directory doesn't exist, then make it
		if(!testFile.exists()){
			testFile.mkdir();
		}
		//Set the matching pattern
		matchingPattern = pattern;
		//Set the reference to the window responsible for this downloader
		window = w;
		//Set the window's downloading text to show the album being downloaded
		window.setDownloading("Now Downloading " + albumTitle);
	}
	
	private void getImages(){
		NodeList images = null;
		boolean toBreak = false;
		switch(matchingPattern){
		case jpgs:
			toBreak = true;
		case pngs:
			toBreak = true;
		case gifs:
			toBreak = true;
		case all:
			images = getSources("a");
			addSources(images, "href");
			if(toBreak){
				break;
			}
		case webm:
			images = getSources("source");
			addSources(images, "src");
		}
	}
	
	private NodeList getSources(String filter){
		NodeList ret = null;
		try {
			parser.setURL(albumURL);
			ret = parser.parse(new TagNameFilter(filter));
		} catch (ParserException e1) {
			JOptionPane.showMessageDialog(null, "Invalid connection. Check the URL, or your internet connection.", "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
			return null;
		}
		parser.reset();
		return ret;
	}
	
	/**
	 * 
	 * @param elements - NodeList - List of all the html elements grabbed by the parser
	 * @param tagName - String - The tag used to get the attribute from the html element
	 * 
	 * addSources will iterate through a NodeList, and add any image sources to the imgSrcs ArrayList for downloading
	 */
	private void addSources(NodeList elements, String tagName){
		//Iterate through the html elements
		for(SimpleNodeIterator i = elements.elements(); i.hasMoreNodes();){
			//Keep track of the element
			Tag tag = (Tag) i.nextNode();
			//If the tags attribute exists, then store the source if it matches the pattern
			if(tag.getAttribute(tagName) != null){
				//Get the source value from the html element
				String src = tag.getAttribute(tagName).trim();
				//Match it against the matchingPattern (file types to download)
				Pattern p = Pattern.compile(matchingPattern);
				Matcher m = p.matcher(src);
				//If it matches, then add it to the source list
				if(m.matches()){
					imgSrcs.add(src.split("//")[1].trim());
				}
			}
		}
	}
	
	/**
	 * downloadImages will take the sources from imgSrcs and attempt to download them using a File Output Stream
	 */
	private void downloadImages(){
		//If imgSrcs is empty then there is nothing to download. Show user that no images of this type were found
		if(imgSrcs.isEmpty()){
			JOptionPane.showMessageDialog(null, "No images were found." , "", JOptionPane.INFORMATION_MESSAGE);
		}
		//If imgSrcs has sources then continue with the download
		else{
			//Set the number of files to be downloaded for the progress bar
			window.setFileCount(imgSrcs.size());
			
			//Loop through all the file sources
			for (int i = 0; i < imgSrcs.size(); i++) {
				//Increment the progress bar based on our counter used to loop
				window.incrementProgress(i);
				
				//If the download hasn't been cancelled, continue the download
				if(continueDownload){
					//Create new URL based on the image file
					URL img = null;
					try {
						img = new URL("http://" + imgSrcs.get(i));
					} 
					//If the URL failed to connect, then print error and end download. This will return to run() which will dispose of the window properly
					catch (MalformedURLException e1) {
						JOptionPane.showMessageDialog(null, "Invalid connection. Check the URL, or your internet connection.", "Error", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
						return;
					}
					
					//Split the string to remove i.imgur.com, leaving just the file and file extension
					String fileName = imgSrcs.get(i).split("i\\.imgur\\.com/")[1];
					//Since we have obtained the file name, we can update the window to show which file is currently downloading
					window.setFile("...\\" + albumTitle + "\\" + fileName);
					//Create byte array to hold image
					byte[] b = new byte[4096];
					//Used to keep track of how much of the image has been read
					int length;
					try {
						//Create the input stream based on the image
						InputStream is = img.openStream();
						//Create the output stream, which points to the designated path
						//This will also create the new file based on the name of the file obtained from the website
						FileOutputStream os = new FileOutputStream(path + fileName);
						//While the image reads bytes into the byte array
						while ((length = is.read(b)) != -1) {
							//Write those bytes to the new file
							os.write(b, 0, length);
						}
						//Close the streams when we're done to prevent unwanted connections
						is.close();
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				//If download has been cancelled, end the download. This will return to run, which will dispose of the window properly
				else{
					break;
				}
			}
		}
		//Show the window (this will only be noticable if the user has hidden the window)
		window.setVisible(true);
		//After the window has been shown, show a window that this album has finished downloading. If they click Yes, the explorer will open up the path
		try {
			DoneWindow dialog = new DoneWindow(albumTitle, path);
			dialog.setModal(false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * run is overriding Runnable's abstract values. This is so multiple downloads can happen concurrently
	 */
	public void run() {
		//First get the images
		getImages();
		//Then download the images
		downloadImages();
		//Hide the window
		window.setVisible(false);
		//Disable the window so it will not show up when hidden windows are shown
		window.setEnabled(false);
		//Dispose of the window
		window.dispose();
	}
}
