package ModelCreation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;

import RServe.RServeConnection;
import ScriptGeneration.ScriptGeneration;
import Utility.Globals;

public class GenerateModel {
	
	String script = null;
	String modelLocation = null;
	String randomGUID = null;
	String outputLocation = null;
	String datasetLocation = null;
	String operation = null;
	String subOperation = null;
	 String modelDirectory = null;
	 ScriptGeneration scriptGeneration = null;
	 RServeConnection rServeConnection = null;
	
	
	public GenerateModel(String outputLocation, String datasetLocation, String script) {
		this.outputLocation = outputLocation;
		this.datasetLocation = datasetLocation;
		this.script = script;
		rServeConnection = new RServeConnection("127.0.0.1", 6311);
		scriptGeneration = new ScriptGeneration(Globals.SCRIPT_FILE);
		
	}


	public GenerateModel(String outputlocation, String datasetlocation, int operation, int subOpr, String columnFlag, int treatmentMode, String columns,
			String algoName, String toPredict, String independentVariables) {
		this.outputLocation = outputlocation;
		this.datasetLocation = datasetlocation;
		rServeConnection = new RServeConnection("127.0.0.1", 6311);
		scriptGeneration = new ScriptGeneration(Globals.SCRIPT_FILE);
		if(operation == 1){
			
		}else if(operation == 2){
			this.script = scriptGeneration.generateScriptForTraining(algoName, toPredict, independentVariables);
		}else if(operation == 3){
			
				script = scriptGeneration.generateScriptForMissingDataAndTraining(columnFlag,  treatmentMode,  columns,
						 algoName,  toPredict,  independentVariables);
		}
	}


	public String getScript() {
		return script;
	}


	public void setScript(String script) {
		this.script = script;
	}


	public String getModelLocation() {
		return modelLocation;
	}


	public void setModelLocation(String modelLocation) {
		this.modelLocation = modelLocation;
	}


	public String getRandomGUID() {
		return randomGUID;
	}


	public void setRandomGUID(String randomGUID) {
		this.randomGUID = randomGUID;
	}


	public String getOutputLocation() {
		return outputLocation;
	}


	public void setOutputLocation(String outputLocation) {
		this.outputLocation = outputLocation;
	}


	public String getDatasetLocation() {
		return datasetLocation;
	}


	public void setDatasetLocation(String datasetLocation) {
		this.datasetLocation = datasetLocation;
	}


	public String getOperation() {
		return operation;
	}


	public void setOperation(String operation) {
		this.operation = operation;
	}


	public String getSubOperation() {
		return subOperation;
	}


	public void setSubOperation(String subOperation) {
		this.subOperation = subOperation;
	}


	public void performTraining() {
		this.treatScriptForPaths();
		rServeConnection.executeTrainingScript(this.script);
		
	}


	private void treatScriptForPaths() {
		this.randomGUID = UUID.randomUUID().toString();
		/**
		 * Create a sub directory to place models that shall be used 
		 */
		String qualifiedOutputlocation = this.outputLocation + File.separator + this.randomGUID;
		File subDir = new File(qualifiedOutputlocation);
		if(!subDir.exists()){
			subDir.mkdirs();
		}
		this.modelDirectory  = qualifiedOutputlocation + File.separator + "myModel.rds";
		this.script = this.script.replace("<%ThisControl.ReadData%>", this.datasetLocation).replace("<%ModelDirectory%>", this.modelDirectory);	
	}
	
	public String predictScript(HashMap<String, ArrayList<Double>> values){
		StringBuilder testScript = new StringBuilder();
		testScript.append("model = readRDS(\""+this.modelDirectory+"\")\n");
		testScript.append("test <- data.frame(");
		StringBuilder str = new StringBuilder();
		for(String column : values.keySet()){
			
			str.append(column+"= c(");
			ArrayList<Double> valList = values.get(column);
			for(Double val:valList){
				str.append(val+",");
			}
			str = str.replace(str.lastIndexOf(","),str.lastIndexOf(",")+1, "");
			str.append("),");
			
		}
		str = str.replace(str.lastIndexOf(","),str.lastIndexOf(",")+1, "");
		str.append(")\n");
		str.append("pred <- predict(model, newdata = test)");
		testScript.append(str);
		
		return testScript.toString();
	}
	
	public REXP predict(HashMap<String, ArrayList<Double>> values){
		String testScript = this.predictScript(values);
		REXP result = rServeConnection.executePredictionScript(testScript);
		return result;
	}
	
	public void clearModelDir() {
	String qualifiedOutputlocation = this.outputLocation + File.separator + this.randomGUID;
	File subDir = new File(qualifiedOutputlocation);
	if(subDir.exists()){
		Utility.deleteDir(qualifiedOutputlocation);
	}

}
