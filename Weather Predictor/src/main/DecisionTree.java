package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import util.Feature;
import util.Node;
import util.WeatherData;

public class DecisionTree {

	static ArrayList<String> targets = new ArrayList<String>();
	static private ArrayList<String> featureSet = null;
	static HashMap<Integer,String> comp = new HashMap<Integer,String>();

	public static void main(String args[]) throws Exception {
		// add all predictions to targets ArrayList (Rain,Snow,Fog,Thunderstorm)
		updateTarget();

		ArrayList<Feature> features = new ArrayList<Feature>();
		HashMap<Integer,WeatherData> XtrainDataMap = new LinkedHashMap<Integer,WeatherData>();
		HashMap<Integer,WeatherData> YtrainDataMap= new LinkedHashMap<Integer,WeatherData>();
		HashMap<Integer,WeatherData> XtestDataMap = new LinkedHashMap<Integer,WeatherData>();
		HashMap<Integer,WeatherData> YtestDataMap = new LinkedHashMap<Integer,WeatherData>();
		// read train and test data
		XtrainDataMap = readData("weatherDataTrain.txt",true);
		YtrainDataMap = readData("weatherDataTrain.txt",false);
		XtestDataMap = readData("weatherDataTest.txt",true);
		YtestDataMap = readData("weatherDataTest.txt",false);
		// create a decision tree model of each class and classify test data
		double startTime = System.currentTimeMillis();
		for (String target : targets) {
			try {
				int i = 0;
				// construct tree for current target class
				GrowTree tree = new GrowTree(featureSet, target, XtrainDataMap,
						YtrainDataMap);
				System.out.println("calling tree construct for "+target);
				Node root = tree.construct();
				System.out.println("tree created successfully for "+target);

				ValidateWithPruning vwp = new ValidateWithPruning(root,
						featureSet, target, XtrainDataMap,YtrainDataMap,XtestDataMap, YtestDataMap);
				HashMap<Integer, String> res = vwp.validateAfterPrune();
				formatResult(res,i);

			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		double endTime = System.currentTimeMillis();

		// Evaluation of the result
		Evaluation eval = new Evaluation(YtestDataMap,targets,comp);
		double accuracy = eval.computeAccuracy();
				
		// J48 CLASSIFIER
		J48Decision  J48= new J48Decision(targets,YtestDataMap);
		double J48_accuracy = J48.evaluate();
		System.out.println("Accuracy of Weka J48 Implementation = "+J48_accuracy);
		System.out.println("Accuracy of C4.5 Implementation = "+accuracy);
		System.out.println("Time for training and testing C4.5 Implementation= "+(endTime-startTime)/(1000*60)+" minutes");
		
	}

	// combines the classification result for each class, from their respective model into
	// a format consistent with the input file. For example, the format is Fog-Rain for a data which
	// was classified to classes Fog and Rain respectively.
	private static void formatResult(HashMap<Integer,String> res,int i) {
		for(Map.Entry<Integer, String> result : res.entrySet()) {
			String wd = result.getValue();
			if(comp.containsKey(i)){
				if(!wd.equals("")) {
					String s = comp.get(i);
					s +="-"+wd;
					comp.put(i,s);
				}
			}
			else {
				if(!wd.equals(""))
					comp.put(i, wd);
			}
			i++;
		}
	}

	// reads the input file
	// if boolean isX is true, then updates X values (feature values)
	//                is false, then updates Y value (class)
	private static HashMap<Integer,WeatherData> readData(String filename,boolean isX) {
		HashMap<Integer,WeatherData> data = new LinkedHashMap<Integer,WeatherData>();
		Scanner scanner = null;
		int key=0;
		try {
			scanner = new Scanner(new File(filename));
		} catch (FileNotFoundException exception) {
			System.out.println("Error: File not found");
			System.exit(1);
		}
		String line;
		String firstLine = scanner.nextLine(); 
		if(featureSet == null) {
			addFeatureNames(firstLine);
		}
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			WeatherData newData = new WeatherData();
			newData.addFeatureNames(firstLine);
			if(isX)
			{
				newData.addFeaturesToXData(line);
			}
			else
			{
				newData.addFeaturesToYData(line);
			}
			data.put(key, newData);
			key++;
		}
		return data;
	}	

	// adds all the feature name to featureSet
	public static void addFeatureNames(String names) {
		featureSet = new ArrayList<String>();
		String[] tempNames= names.split(",");
		for(String name: tempNames) {
			if(!name.contains("EST")&&!name.contains("Events"))
				featureSet.add(name);
		}
	}

	public static void updateTarget() {
		targets.add("Fog");
		targets.add("Rain");
		targets.add("Snow");
		targets.add("Thunderstorm");
	}
}