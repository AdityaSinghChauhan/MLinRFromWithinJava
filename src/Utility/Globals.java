package Utility;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ScriptGeneration.ScriptGenerationEnum;
import Utility.Utility;


public class Globals {
	public static String CONFIG_FOLDER_PATH ="";
	public static String DUMP_FOLDER_PATH ="";
	public static String SCRIPT_FILE = "";
	public static void init(String configPath) {
		CONFIG_FOLDER_PATH = configPath;
		String propetiesFilePath = configPath + File.separator + ScriptGenerationEnum.PropertiesFile.PROPERTYFILE;
		propetiesFilePath = propetiesFilePath.replace("\\", "\\\\");
		JSONParser parser = new JSONParser();
		Object jsonObj = null;
		try {
			jsonObj = parser
					.parse(new FileReader(propetiesFilePath));
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 JSONObject jsonObject = (JSONObject) jsonObj;
		 String dumpPath = (String) jsonObject.get(ScriptGenerationEnum.PropertiesFile.DUMPFOLDER);
		 if(!Utility.isNullOrBlank(dumpPath)){
			 DUMP_FOLDER_PATH = dumpPath;
		 }
		 String scriptFilePath = (String) jsonObject.get(ScriptGenerationEnum.PropertiesFile.SCRIPTFILE);
		 if(!Utility.isNullOrBlank(scriptFilePath)){
			 SCRIPT_FILE = scriptFilePath;
		 }
	
	}
	
}
