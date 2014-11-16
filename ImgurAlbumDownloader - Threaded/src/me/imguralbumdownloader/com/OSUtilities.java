package me.imguralbumdownloader.com;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Micah Lee
 * OSUtilities is used in determining OS specific actions. This class will have some built in methods, however
 * if needed, user can always call execute with their command, and arguments.
 */
public class OSUtilities {
	public static final int UNKNOWN = -1;
	public static final int WINDOWS = 0;
	public static final int MAC = 1;
	public static final int LINUX = 2;
	
	/**
	 * getOS will return the current OS
	 * @return - int - the respective operating system
	 */
	public static int getOS(){
		//Get the name of the OS
		String os = System.getProperty("os.name").toLowerCase();
		//Match the OS to the specific name
		if(os.contains("win")){
			return WINDOWS;
		}
		else if(os.contains("mac")){
			return MAC;
		}
		else if(os.contains("linux") || os.contains("unix") || os.contains("nux")){
			return LINUX;
		}
		else{
			return UNKNOWN;
		}
	}
	
	/**
	 * @param cmd - String - the command to execute
	 * @param args - String - the arguments for the command separated by spaces
	 * buildCMD will build an array of String for executing
	 * @return - String[] - a complete array with the command and arguments
	 */
	private static String[] buildCMD(String cmd, String args){
		ArrayList<String> cmds = new ArrayList<String>();
		cmds.add(cmd.trim());
		cmds.addAll(Arrays.asList(args.split(" ")));
		String[] ret = new String[cmds.size()];
		for(int i = 0; i < cmds.size(); i++){
			ret[i] = cmds.get(i);
		}
		return ret;
	}
	
	/**
	 * @param cmd - String - the command to execute
	 * @param args - String - the arguments for the command separated by spaces
	 * execute will perform a command using Runtime.exec(String[])
	 * @return - boolean - this will return true if the process completed successfully i.e. not null
	 */
	public static boolean execute(String cmd, String args){
		String[] toExec = buildCMD(cmd, args);
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(toExec);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p != null ? true: false;
	}
	
	/**
	 * @param path - String - the path to open
	 * openDir will open up a directory based on the operating system
	 * @return - whether or not this method completed successfully
	 * @throws IOException
	 */
	public static boolean openDir(String path) throws IOException{
		//Switch based on the operating system
		switch(getOS()){
		//Execute according to the operating system
		case WINDOWS:
			return execute("explorer", path);
		case MAC:
			return execute("open", path);
		case LINUX:
			if(execute("gnome-open", path)){
				return true;
			}
			else if(execute("gvfs-open", path)){
				return true;
			}
			else if(execute("nautilus", path)){
				return true;
			}
			else if(execute("xdg-open", path)){
				return true;
			}
			else if(execute("kde-open", path)){
				return true;
			}
			break;
		default:
			Desktop.getDesktop().open(new File(path));
			return true;
		}
		return false;
	}
}
