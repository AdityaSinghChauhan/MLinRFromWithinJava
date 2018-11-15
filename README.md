# MLinRFromWitinJava
This library empowers java developers/users to garner the capabilities of R for performing machine learning tasks without actually having to write R scripts. The library provides an abstract way to generate model, perform training and perform prediction without having to worry about the compliant R syntaxes. 

Example, 
A company wants to predict prospective Loan Amount based on an applicant's salary and age. They have historical data of the same but the data is not clean. It contains some missing values in the Age and Salary columns.

Tasks : 
1. Clean the data by replacing missing values of Age,Salary by the median value of respective column. 
2. Train the data using a particular algorithm like RandomForest
3. Predict for custom age and salary. 

Code :: 

  GenerateModel model = new GenerateModel("Path/MLScriptsinRFromWithinJava/dump", "Path/MLScriptsinRFromWithinJava/dataset/loanAmount.csv", operation, subOpr, "SpecifiedColumns", treatmentMode, columns, algorithm, toPredict, independentVariables);
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
