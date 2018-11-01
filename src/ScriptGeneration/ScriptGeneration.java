package ScriptGeneration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ScriptGeneration.ScriptGenerationEnum.AlgoNameLibMap;
import Utility.Globals;
import Utility.Utility;
import Utility.XMLUtility.XMLUtility;

public class ScriptGeneration {
	public JSONArray jsonObject = null;
	public ScriptGeneration(String jsonPath){
		JSONParser parser = new JSONParser();
		Object jsonObj = null;
		try {
			jsonObj = parser
					.parse(new FileReader(jsonPath));
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jsonObject = (JSONArray) jsonObj;
	}

	public  String generateScriptForMissingDataAndTraining(String subOpr, int treatmentMode, String columns,
			String algoName, String toPredict, String independentVariables) {

		String script = null;
		String library = null;
		String algorithm = null;
		
		StringBuilder strIVs = new StringBuilder();
		String[] ivArr = independentVariables.split(",");
		for (int i = 0; i < ivArr.length; i++) {
			strIVs.append(ivArr[i]);
			strIVs.append("+");
		}
		strIVs = strIVs.replace(strIVs.lastIndexOf("+"), strIVs.length(),"");
		String mode = getStringTreatmentMode(treatmentMode);
		
		String[] libraryAndAlgorithmName = getQualifiedLibraryAndAlgorithmName(algoName);
		
		for (Object jsonObj : jsonObject) {
			String oprJson = (String) ((JSONObject) jsonObj).keySet().toArray()[0];
			if (oprJson.equalsIgnoreCase("MissingDataTreatmentAndTraining")) {
				JSONArray missingDataObjects = (JSONArray) ((JSONObject) jsonObj).get("MissingDataTreatmentAndTraining");
				for (Object missingDataObject : missingDataObjects) {

					String treatModeJson = (String) ((JSONObject) missingDataObject).keySet().toArray()[0];
					if (treatModeJson.equalsIgnoreCase(mode)) {
						JSONArray missingDataModeObjects = (JSONArray) ((JSONObject) missingDataObject).get(mode);
						for (Object missingDataModeObject : missingDataModeObjects) {
							String subOprJson = (String) ((JSONObject) missingDataModeObject).get("subOperation");
							if ("All".equalsIgnoreCase(subOpr) && subOpr.equalsIgnoreCase(subOprJson)) {
								 library = libraryAndAlgorithmName[0];
								 algorithm = libraryAndAlgorithmName[1];
								script = (String) ((JSONObject) missingDataModeObject).get("Script");
								script = script.replace("<%library%>", library).replace("<%algorithm%>", algorithm).replace("<%toPredict%>", toPredict).replace("<%independentVariables%>", strIVs);
								return script;
							} else if ("SpecifiedColumns".equalsIgnoreCase(subOpr)
									&& subOpr.equalsIgnoreCase(subOprJson)) {
								script = (String) ((JSONObject) missingDataModeObject).get("Script");
								String[] colArr = columns.split(",");
								StringBuilder strColumns = new StringBuilder();
								for (int i = 0; i < colArr.length; i++) {
									strColumns.append("\'").append(colArr[i]).append("\'");
									strColumns.append(",");
								}
								strColumns = strColumns.replace(strColumns.lastIndexOf(","), strColumns.length(),
										"");
								library = libraryAndAlgorithmName[0];
								 algorithm = libraryAndAlgorithmName[1];
								script = (String) ((JSONObject) missingDataModeObject).get("Script");
								script = script.replace("<%library%>", library).replace("<%algorithm%>", algorithm).replace("<%toPredict%>", toPredict).replace("<%independentVariables%>", strIVs);
								script = script.replace("<%CommaSeperatedListOfColumns%>", strColumns);
								System.out.println(script);
								return script;
							}
						}

					}

				}
			}
		}
		return "";

	
		
	}

	public  String[] getQualifiedLibraryAndAlgorithmName(String algoName) {
		
		
		File file = new File(Globals.CONFIG_FOLDER_PATH + File.separator + ScriptGenerationEnum.ALGONAMELIBMAPFILE);
		
		Document doc = XMLUtility.getDocumentFromFile(file);
		
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nodeList = doc.getElementsByTagName(AlgoNameLibMap.ALGORITHM);
        String libAlgo[] = new String[2];
        for (int i = 0; i < nodeList.getLength(); i++) {
			NamedNodeMap attrs = nodeList.item(i).getAttributes();
			String name = attrs.getNamedItem(AlgoNameLibMap.NAME).getTextContent();
			if(!Utility.isNullOrBlank(name) && name.equalsIgnoreCase(algoName)){
				libAlgo[0] = attrs.getNamedItem(AlgoNameLibMap.LIBRARY).getTextContent();
				libAlgo[1] = attrs.getNamedItem(AlgoNameLibMap.ALGO).getTextContent();
				break;
			}
        }
		return libAlgo;
	}

	public  void generateScriptForMissingData(String subOpr, int treatmentMode, String columns) {

		
		String mode = getStringTreatmentMode(treatmentMode);
		for (Object jsonObj : jsonObject) {
			String oprJson = (String) ((JSONObject) jsonObj).keySet().toArray()[0];
			if (oprJson.equalsIgnoreCase("MissingDataTreatment")) {
				JSONArray missingDataObjects = (JSONArray) ((JSONObject) jsonObj).get("MissingDataTreatment");
				for (Object missingDataObject : missingDataObjects) {

					String treatModeJson = (String) ((JSONObject) missingDataObject).keySet().toArray()[0];
					if (treatModeJson.equalsIgnoreCase(mode)) {
						JSONArray missingDataModeObjects = (JSONArray) ((JSONObject) missingDataObject).get(mode);
						for (Object missingDataModeObject : missingDataModeObjects) {
							String subOprJson = (String) ((JSONObject) missingDataModeObject).get("subOperation");
							if ("All".equalsIgnoreCase(subOpr) && subOpr.equalsIgnoreCase(subOprJson)) {
								System.out.println(((JSONObject) missingDataModeObject).get("Script"));
							} else if ("SpecifiedColumns".equalsIgnoreCase(subOpr)
									&& subOpr.equalsIgnoreCase(subOprJson)) {
								String script = (String) ((JSONObject) missingDataModeObject).get("Script");
								String[] colArr = columns.split(",");
								StringBuilder strColumns = new StringBuilder();
								for (int i = 0; i < colArr.length; i++) {
									strColumns.append("\'").append(colArr[i]).append("\'");
									strColumns.append(",");
								}
								strColumns = strColumns.replace(strColumns.lastIndexOf(","), strColumns.length(),
										"");
								script = script.replace("<%CommaSeperatedListOfColumns%>", strColumns);
								System.out.println(script);
							}
						}

					}

				}
			}
		}

	}

	private static String getStringTreatmentMode(int treatmentMode) {
		switch (treatmentMode) {
		case 1:
			return "Mean";
		case 2:
			return "Median";
		}
		return "";
	}

	public String generateScriptForTraining(String algoName, String toPredict,
			String independentVariables) {
		String[] libraryAndAlgorithmName = getQualifiedLibraryAndAlgorithmName(algoName);
		String script = null;
		String library = null;
		String algorithm = null;
		StringBuilder strIVs = new StringBuilder();
		String[] ivArr = independentVariables.split(",");
		for (int i = 0; i < ivArr.length; i++) {
			strIVs.append(ivArr[i]);
			strIVs.append("+");
		}
		strIVs = strIVs.replace(strIVs.lastIndexOf("+"), strIVs.length(), "");
		for (Object jsonObj : jsonObject) {
			String oprJson = (String) ((JSONObject) jsonObj).keySet().toArray()[0];
			if (oprJson.equalsIgnoreCase("Training")) {
				JSONObject trainingObject = (JSONObject) ((JSONObject) jsonObj).get("Training");
				script = (String) ((JSONObject) trainingObject).get("Script");
				library = libraryAndAlgorithmName[0];
				algorithm = libraryAndAlgorithmName[1];
				script = script.replace("<%library%>", library).replace("<%algorithm%>", algorithm)
						.replace("<%toPredict%>", toPredict).replace("<%independentVariables%>", strIVs);

				return script;
			}
		}
		return "";

	}
}
