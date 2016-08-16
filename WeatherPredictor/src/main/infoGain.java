package main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import util.Feature;
import util.WeatherData;
public class infoGain {
	
	private Feature attribute;
	private double threshold;
	private double infoGain = -1;
	private HashMap<String, ArrayList<WeatherData>> subset;
	
	/**
	 * Constructor: initialize fields. This class is for calculating the information gain
	 * of continuous attribute. 
	 * Use one cut to binary method. 
	 * @param attribute
	 * @param target
	 * @param instances
	 * @throws IOException
	 */
	public infoGain(Feature attribute, Feature target, 
			ArrayList<WeatherData> instances) throws IOException {
		
		this.attribute = attribute;
		
		// Initialize threshold and infoGain
		// (1) Get the name of the attribute to be calculated
		final String attributeName = attribute.getName();
				
		// (2) Sort instances according to the attribute
		Comparator<WeatherData> comparator = new Comparator<WeatherData>() {
			@Override
			public int compare(WeatherData x, WeatherData y) {
				HashMap<String, String> xPair = x.getAttributeValuePairs();
				String xValue = xPair.get(attributeName);
						
				HashMap<String, String> yPair = y.getAttributeValuePairs();
				String yValue = yPair.get(attributeName);
				if (Double.parseDouble(xValue) - Double.parseDouble(yValue) > 0) return 1;
				else if (Double.parseDouble(xValue) - Double.parseDouble(yValue) < 0) return -1;
				else return 0;
			}
		};
		Collections.sort(instances, comparator);
		
		/*
		 (3) Get each position that target value change,
			then calculate information gain of each position
		    find the maximum position value to be the threshold 		
		 */		 
		int thresholdPos = 0;
		for (int i = 0; i < instances.size() - 1; i++) {
			HashMap<String, String> instancePair = instances.get(i).getAttributeValuePairs();
			String instanceValue = instancePair.get(attributeName);
			HashMap<String, String> instancePair2 = instances.get(i + 1).getAttributeValuePairs();
			String instanceValue2 = instancePair2.get(attributeName);
					
			if (!instanceValue.equals(instanceValue2)) {
				double currInfoGain = calculateConti(attribute, target, instances, i);
				if (currInfoGain - infoGain > 0) {
					infoGain = currInfoGain;
					thresholdPos = i;
				}
			}
		}	
		
		// (4) Calculate threshold
		HashMap<String, String> a = instances.get(thresholdPos).getAttributeValuePairs();
		String aValue = a.get(attributeName);
		HashMap<String, String> b = instances.get(thresholdPos).getAttributeValuePairs();
		String bValue = b.get(attributeName);			
		threshold = (Double.parseDouble(aValue) + Double.parseDouble(bValue)) / 2;	
		
		// Initialize subset
		subset = new HashMap<String, ArrayList<WeatherData>>();
		ArrayList<WeatherData> left = new ArrayList<WeatherData>();
		ArrayList<WeatherData> right = new ArrayList<WeatherData>();
		for (int i = 0; i < thresholdPos; i++) {
			left.add(instances.get(i));
		}
		for (int i = thresholdPos + 1; i < instances.size(); i++) {
			right.add(instances.get(i));
		}
		String leftName = "less" + threshold;
		String rightName = "more" + threshold;
		subset.put(leftName, left);
		subset.put(rightName, right);
	}
	
	public static double calculateConti(Feature attribute, Feature target, 
			ArrayList<WeatherData> instances, int index) throws IOException {
		
		int totalN = instances.size();
		double infoGain = Entropy.calculate(target, instances);
		int subL = index + 1;
		int subR = instances.size() - index - 1;
		double subResL = ((double) subL) / ((double) totalN) * 
				Entropy.calculateConti(target, instances, 0, index);
		double subResR = ((double) subR) / ((double) totalN) * 
				Entropy.calculateConti(target, instances, index + 1, totalN - 1);
		infoGain -= (subResL + subResR);
		return infoGain;
	}
	
	public Feature getAttribute() {
		return attribute;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	public double getInfoGain() {
		return infoGain;
	}
	
	public HashMap<String, ArrayList<WeatherData>> getSubset() {
		return subset;
	}
	
	public String toString() {
		return "Attribute: " + attribute.getName() + "\n" + "Threshold: " + threshold + "\n" 
				+ "InfoGain: " + infoGain + "\n" + "Subset: " + subset;
	}
}
