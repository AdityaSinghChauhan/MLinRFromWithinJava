import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPList;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;

import ModelCreation.GenerateModel;
import ScriptGeneration.ScriptGeneration;
import ScriptGeneration.ScriptGenerationEnum;
import Utility.Globals;

public class Main {

	public static void main(String args[]) {
		Globals.init("Path/MLScriptsinRFromWithinJava/Config");
		Scanner scan = new Scanner(System.in);
		System.out.println("Please enter the operation you wish to perform : ");
		System.out.println("1 for Missing Data Treatment\n2 for Training\n3 for Missing Data Treatment and Training");
		int operation = Integer.parseInt(scan.nextLine());
		int treatmentMode = 0;
		int subOpr = 0;
		String algorithm = "";
		String[] libraryAndAlgorithmName = null;
		String toPredict = null;
		String independentVariables = null;
		ScriptGeneration scrGen = new ScriptGeneration("Path/MLScriptsinRFromWithinJava\\Scripts\\Scripts.json");
		//scrGen.getQualifiedLibraryAndAlgorithmName(ScriptGenerationEnum.RANDOMFOREST);
		String script = "";
		GenerateModel model = null;
		HashMap<String, ArrayList<Double>> values = null;
		ArrayList<Double> temp= null;
		switch (operation) {
		case 1:

			System.out.println(
					"Do you want all the columns to be treated for missing values or some specified columns ?");
			System.out.println("Press 1 for treating ALL columns\nPress 2 for treating specified columns");
			 subOpr = Integer.parseInt(scan.nextLine());
			
			switch (subOpr) {
			case 1:
				System.out.println(
						"We can treat the missing data values by either replacing them with Mean value of the column set or Median value of the column set");
				System.out.println(
						"Press 1 for treating missing values with Mean\nPress 2 for treating missing values with Median");
				treatmentMode = Integer.parseInt(scan.nextLine());
				scrGen.generateScriptForMissingData("All", treatmentMode, null);
				break;
			case 2:
				System.out.println("Please enter comma seperated column names to be treated");
				String columns = scan.nextLine();
				System.out.println(
						"We can treat the missing data values by either replacing them with Mean value of the column set or Median value of the column set");
				System.out.println(
						"Press 1 for treating missing values with Mean\nPress 2 for treating missing values with Median");
				treatmentMode = Integer.parseInt(scan.nextLine());
				scrGen.generateScriptForMissingData("SpecifiedColumns", treatmentMode, columns);
				break;
			}
			break;
		case 2:
			System.out.println("Please enter the algorithm you want to perform the training on :");
			algorithm = scan.nextLine();
			System.out.println("What do you want to predict ? Enter column name");
			toPredict = scan.nextLine();
			System.out.println("Enter comma seperated list of predictors ?");
			independentVariables = scan.nextLine();
			script = scrGen.generateScriptForTraining(algorithm, toPredict, independentVariables);
			model = new GenerateModel("Path/MLScriptsinRFromWithinJava/dump", "Path/MLScriptsinRFromWithinJava/dataset/loanAmount.csv", script);
			model.performTraining();
			values = new HashMap<String, ArrayList<Double>>();
			temp = new ArrayList<Double>();
			temp.add(20.0);
			temp.add(25.0);
			temp.add(28.0);
			values.put("Age", (ArrayList<Double>) temp.clone());
			temp.clear();
			temp.add(623000.0);
			temp.add(515000.0);
			temp.add(611000.0);
			values.put("Salary", (ArrayList<Double>) temp.clone());
			REXP result = model.predict(values);
			
				try {
					double[] vals = result.asDoubles();
					int i = 0;
					for(Double val : vals){
						System.out.println("Prediction results :");
						System.out.println("Age : "+values.get("Age").get(i)+" Salary : "+values.get("Salary").get(i)+ " Predicted Loan Amount :"+val.toString());
						i++;
					}
				} catch (REXPMismatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;
		case 3:

			System.out.println("Do you want all the columns to be treated for missing values or some specified columns ?");
			System.out.println("Press 1 for treating ALL columns\nPress 2 for treating specified columns");
			 subOpr = Integer.parseInt(scan.nextLine());
			 treatmentMode = 0;
			switch (subOpr) {
			case 1:
				System.out.println(
						"We can treat the missing data values by either replacing them with Mean value of the column set or Median value of the column set");
				System.out.println(
						"Press 1 for treating missing values with Mean\nPress 2 for treating missing values with Median");
				treatmentMode = Integer.parseInt(scan.nextLine());
				System.out.println("Please enter the algorithm you want to learn the training on :");
				algorithm = scan.nextLine();
				System.out.println("What do you want to predict ? Enter column name");
				toPredict = scan.nextLine();
				System.out.println("Enter comma seperated list of predictors ?");
				independentVariables = scan.nextLine();
				 script = scrGen.generateScriptForMissingDataAndTraining("All", treatmentMode, null, algorithm, toPredict, independentVariables);
				 model = new GenerateModel("Path/MLScriptsinRFromWithinJava/dump", "Path/MLScriptsinRFromWithinJava/dataset/loanAmount.csv", script);
				model.performTraining();
				values = new HashMap<String, ArrayList<Double>>();
				temp = new ArrayList<Double>();
				temp.add(20.0);
				temp.add(25.0);
				temp.add(28.0);
				values.put("Age", (ArrayList<Double>) temp.clone());
				temp.clear();
				temp.add(623000.0);
				temp.add(515000.0);
				temp.add(611000.0);
				values.put("Salary", (ArrayList<Double>) temp.clone());
				 result = model.predict(values);
				
					try {
						double[] vals = result.asDoubles();
						int i = 0;
						for(Double val : vals){
							System.out.println("Prediction results :");
							System.out.println("Age : "+values.get("Age").get(i)+" Salary : "+values.get("Salary").get(i)+ " Predicted Loan Amount :"+val.toString());
							i++;
						}
					} catch (REXPMismatchException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				break;
			case 2:
				System.out.println("Please enter comma seperated column names to be treated");
				String columns = scan.nextLine();
				System.out.println(
						"We can treat the missing data values by either replacing them with Mean value of the column set or Median value of the column set");
				System.out.println(
						"Press 1 for treating missing values with Mean\nPress 2 for treating missing values with Median");
				treatmentMode = Integer.parseInt(scan.nextLine());
				System.out.println("Please enter the algorithm you want to learn the training on :");
				algorithm = scan.nextLine();
				System.out.println("What do you want to predict ? Enter column name");
				toPredict = scan.nextLine();
				System.out.println("Enter comma seperated list of predictors ?");
				independentVariables = scan.nextLine();
				script = scrGen.generateScriptForMissingDataAndTraining("SpecifiedColumns", treatmentMode, columns, algorithm, toPredict, independentVariables);
				model = new GenerateModel("Path/MLScriptsinRFromWithinJava/dump", "Path/MLScriptsinRFromWithinJava/dataset/loanAmount.csv", script);
				model.performTraining();
				values = new HashMap<String, ArrayList<Double>>();
				temp = new ArrayList<Double>();
				temp.add(20.0);
				temp.add(25.0);
				temp.add(28.0);
				values.put("Age", (ArrayList<Double>) temp.clone());
				temp.clear();
				temp.add(623000.0);
				temp.add(515000.0);
				temp.add(611000.0);
				values.put("Salary", (ArrayList<Double>) temp.clone());
				 result = model.predict(values);
				
					try {
						double[] vals = result.asDoubles();
						int i = 0;
						for(Double val : vals){
							System.out.println("Prediction results :");
							System.out.println("Age : "+values.get("Age").get(i)+" Salary : "+values.get("Salary").get(i)+ " Predicted Loan Amount :"+val.toString());
							i++;
						}
					} catch (REXPMismatchException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				break;
			}

			break;
		}

	}
}
