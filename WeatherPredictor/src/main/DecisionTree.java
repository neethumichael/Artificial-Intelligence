package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import util.Feature;
import util.WeatherData;

public class DecisionTree {
	static ArrayList<String> targets = new ArrayList<String>();

	public static void main(String args[]) {
		// read data
		// add all predictions to targets ArrayList (Rain,Snow,Fog,Thunderstorm)
		updateTarget();

		ArrayList<Feature> features = new ArrayList<Feature>();
		HashMap<Integer,WeatherData> XtrainDataMap = new LinkedHashMap<Integer,WeatherData>();
		HashMap<Integer,WeatherData> YtrainDataMap= new LinkedHashMap<Integer,WeatherData>();
		HashMap<Integer,WeatherData> XtestDataMap = new LinkedHashMap<Integer,WeatherData>();
		HashMap<Integer,WeatherData> YtestDataMap = new LinkedHashMap<Integer,WeatherData>();

		XtrainDataMap = readData("weatherDataTrain.txt",true);
		YtrainDataMap = readData("weatherDataTrain.txt",false);
		XtestDataMap = readData("weatherDataTest.txt",true);
		YtestDataMap = readData("weatherDataTest.txt",false);

		for(Map.Entry<Integer, WeatherData> key_weatherData : XtrainDataMap.entrySet())
		{
			WeatherData wd = key_weatherData.getValue();
			ArrayList<Feature> allFeatures = wd.getFeatures();
			for(Feature f : allFeatures)
			{
				if((f.getName().contains("EST")) || (f.getName().contains("PrecipitationIn"))){
					continue;
				}else{
					features.add(f);
				}					
			}
			break;
		}

		for(String target: targets){
			Validate v = new Validate(features, target, XtrainDataMap, YtrainDataMap, XtestDataMap,
					YtestDataMap);
			HashMap<Integer,WeatherData> res = v.validate();
		}		
	}

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

	public static void updateTarget() {
		targets.add("Fog");
		targets.add("Rain");
		targets.add("Snow");
		targets.add("Thunderstorm");
	}
}
