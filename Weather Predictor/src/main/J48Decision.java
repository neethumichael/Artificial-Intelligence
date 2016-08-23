package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import util.WeatherData;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.OneR;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.core.FastVector;
import weka.core.Instances;

public class J48Decision {
	static ArrayList<String> targets = null;
	HashMap<Integer,WeatherData> YtestDataMap = null;
	public J48Decision(ArrayList<String> targets,HashMap<Integer,WeatherData> YtestDataMap){
		this.targets = targets;
		this.YtestDataMap = YtestDataMap;
	}

	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} 
		catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
		return inputReader;
	}

	public double evaluate() {
		double startTime =0.0;
		HashMap<String,ArrayList<String>> predictedOutput = new HashMap<String,ArrayList<String>>();
		HashMap<Integer,String> comp = new HashMap<Integer,String>();
		for(String target: targets){
			int i=0;
			HashMap<Integer,String> individualPred = new HashMap<Integer,String>();
			BufferedReader datafile = null;
			BufferedReader testfile = null;
			try {
				if(target.contains("Fog")){
					datafile = readDataFile("fogTrain.arff");
					testfile = readDataFile("fogTest.arff"); 
				}
				else if(target.contains("Snow")){
					datafile = readDataFile("snowTrain.arff");
					testfile = readDataFile("snowTest.arff"); 
				}
				else if(target.contains("Thunder")) {
					datafile = readDataFile("thunderTrain.arff");
					testfile = readDataFile("thunderTest.arff");
				}
				else if(target.contains("Rain")) {
					datafile = readDataFile("rainTrain.arff");
					testfile = readDataFile("rainTest.arff");
				}

				Instances data = new Instances(datafile);
				Instances test = new Instances(testfile);
				data.setClassIndex(data.numAttributes() - 1);
				test.setClassIndex(data.numAttributes()-1);
				startTime = System.currentTimeMillis();
				Classifier cls = new J48();

				cls.buildClassifier(data);
				for (int j = 0; j < test.numInstances(); j++) {
					double clsLabel = cls.classifyInstance(test.instance(j));
					if(clsLabel == 1.0) {
						individualPred.put(j,target);
					}
					else {
						individualPred.put(j,"");
					}
				}
				for(Map.Entry<Integer, String> result : individualPred.entrySet()) {
					String wd = result.getValue();
					if(comp.containsKey(i)){
						if(!wd.equals("")) {
							String s = comp.get(i);
							s +="-"+wd;
							comp.put(i,s);
						}
					}
					else {
						if(!wd.equals("")){

							comp.put(i, wd);
						}
					}
					i++;
				}
			}
			catch(Exception e) {
			}
		}
		double endTime = System.currentTimeMillis();
		System.out.println("Time for training and testing Weka J48 "+(endTime-startTime)/(1000*60));
		main.Evaluation eval = new main.Evaluation(YtestDataMap,targets,comp);
		return eval.computeAccuracy();	
	}
}
