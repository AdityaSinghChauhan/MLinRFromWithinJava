# MLinRFromWitinJava
This library empowers java developers/users to garner the capabilities of R for performing machine learning tasks without actually having to write R scripts. The library provides an abstract way to generate model, perform training and perform prediction without having to worry about the compliant R syntaxes. 

Example, 
A company wants to predict prospective Loan Amount based on an applicant's salary and age. They have historical data of the same but the data is not clean. It contains some missing values in the Age and Salary columns.

Tasks : 
1. Clean the data by replacing missing values of Age,Salary by the median value of respective column. 
2. Train the data using a particular algorithm like RandomForest
3. Predict for custom age and salary. 

Code :: 

  GenerateModel model = new GenerateModel("Path/MLScriptsinRFromWithinJava/dump", "Path/MLScriptsinRFromWithinJava/dataset/loanAmount.csv", operation, subOpr, "SpecifiedColumns", treatmentMode, columns, algorithm, toPredict, independentVariables);<BR>
  model.performTraining();<BR>
  values = new HashMap<String, ArrayList<Double>>();<BR>
  temp = new ArrayList<Double>();<BR>
  temp.add(20.0);<BR>
  temp.add(25.0);<BR>
  temp.add(28.0);<BR>
  values.put("Age", (ArrayList<Double>) temp.clone());<BR>
  temp.clear();<BR>
  temp.add(623000.0);<BR>
  temp.add(515000.0);<BR>
  temp.add(611000.0);<BR>
  values.put("Salary", (ArrayList<Double>) temp.clone());<BR>
  result = model.predict(values);<BR>
    try {<BR>
      double[] vals = result.asDoubles();<BR>
      int i = 0;<BR>
      for(Double val : vals){<BR>
        System.out.println("Prediction results :");<BR>
        System.out.println("Age : "+values.get("Age").get(i)+" Salary : "+values.get("Salary").get(i)+ " Predicted Loan Amount :"+val.toString());<BR>
        i++;<BR>
      }<BR>
    } catch (REXPMismatchException e) {<BR>
      e.printStackTrace();<BR>
    }<BR>

Structure :

Root-->Config :: Stores property file(properties.json), script template file(Scripts.json), algorithm library map file (algonamelibmap.xml). Refer description of each file for more details. 
