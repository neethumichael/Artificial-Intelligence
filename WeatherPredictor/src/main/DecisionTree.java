package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import util.Feature;
import util.WeatherData;

public class DecisionTree {
	static ArrayList<String> targets = new ArrayList<String>();
	
	public static void main(String args[]) {
		// read data
		updateTarget();
		ArrayList<String> features = new ArrayList<String>();
		ArrayList<WeatherData> weatherData = new ArrayList<WeatherData>();
		ArrayList<WeatherData> weatherXData = new ArrayList<WeatherData>();
		HashSet<WeatherData> XtrainData = readData("weatherDataTrain.txt",true);
		HashSet<WeatherData> YtrainData = readData("weatherDataTrain.txt",false);
		HashSet<WeatherData> XtestData  = readData("weatherDataTest.txt",true);
		HashSet<WeatherData> YtestData  = readData("weatherDataTest.txt",false);
		HashSet<WeatherData> testData  = readData2("weatherDataTest.txt");
		for(WeatherData data: XtrainData) {
			features = data.getFeatureNames();
		}
		// moving to arraylist
		for(WeatherData data: testData) {
			weatherData.add(data);
		}
		for(WeatherData data: XtestData) {
			weatherXData.add(data);
		}
		for(String target: targets){
         Validate v = new Validate();
         ArrayList<WeatherData> res = v.validate(weatherData, features, target, weatherXData);
         weatherXData = res;
         targets.remove(0);
         //System.out.println("Score of "+target+" is "+score);
		}
		/*	InformationRatio ir = new InformationRatio();
		for(WeatherData wd : XtrainData)
		{
			//System.out.println("the inf ratio is");
			ArrayList<Feature> frs = wd.getFeatures();
		    for(Feature f : frs)
		    {
		    	if(f.getName().equals("Mean TemperatureF"))
		    	{
		    		System.out.println("the feature is"+f.getName());
		    		double gainr = ir.calcInformationRatio(XtrainData,YtrainData,f);
		    		System.out.println("the inf ratio is"+gainr);

		    	}
		    }
		    break;
			//ir.calcInformationRatio(trainDataX,trainDataY,)
		}*/

		/*for(WeatherData wd : YtrainData)
		{
			for(Feature fd: wd.getFeatures())
			{
				System.out.println(fd.getName());
				System.out.println(fd.getValues());
			}

		}*/
	}

	private static HashSet<WeatherData> readData2(String filename) {
		HashSet<WeatherData> data = new HashSet<WeatherData>();
		Scanner scanner = null;
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

			newData.addFeatures(line);



			data.add(newData);
		}
		return data;
	}	


	private static HashSet<WeatherData> readData(String filename,boolean isX) {
		HashSet<WeatherData> data = new HashSet<WeatherData>();
		Scanner scanner = null;
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
			data.add(newData);
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
