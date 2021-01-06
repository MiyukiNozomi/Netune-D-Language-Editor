package net.netune.run;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NetuneRuntime {
	
	public static String location;
	public static String exec_batch;
	
	public static void execute(){
		try{
			File f = new File(location + "/.netune/");
			
			if(!f.exists())
				f.mkdir();
			
			String final_batch = "@echo off\ncd " + location + "\n" + exec_batch;
			
			Files.write(Paths.get(location + "/.netune/build.bat"),final_batch.getBytes());
			
			Runtime.getRuntime().exec("cmd /c start " + location + "/.netune/build.bat");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}