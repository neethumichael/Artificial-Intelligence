package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import util.Feature;
import util.WeatherData;

public class SelectFeature {
	
	private Feature chosen;
	private HashMap<String, ArrayList<WeatherData>> subset;
	private double infoGain;
	private double threshold;
	
	/**
	 * Constructor: initialize fields
	 * @param target
	 * @param attributes
	 * @param instances
	 * @throws IOException
	 */
	public SelectFeature(Feature target, ArrayList<Feature> attributes, 
			ArrayList<WeatherData> instances) throws IOException {
		
		// Initialize variables
		chosen = null;
		infoGain = -1;
		subset = null;
		
		// Iterate to find the attribute with the largest information gain
		for (Feature currAttribute : attributes) {
			double currInfoGain = 0;
			HashMap<String, ArrayList<WeatherData>> currSubset = null;
			InformationRatio ir = new InformationRatio();
			double gainr = ir.calcInformationRatio(XtrainData,YtrainData,currAttribute);
				InfoGainContinuous contigous = new InfoGainContinuous(currAttribute, target, instances);
				currInfoGain = contigous.getInfoGain();
				currSubset = contigous.getSubset();
				threshold = contigous.getThreshold();
			 
			if (currInfoGain > infoGain) {
				infoGain = currInfoGain;
				chosen = currAttribute;
				subset = currSubset;
			}
		}
	}
	
	public Feature getChosen() {
		return chosen;
	}
	
	public double getInfoGain() {
		return infoGain;
	}
	
	public HashMap<String, ArrayList<WeatherData>> getSubset() {
		return subset;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	
}