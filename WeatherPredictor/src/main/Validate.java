package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import util.Feature;
import util.Node;
import util.WeatherData;

public class Validate {

	private Node root;
	private double scores;
	private ArrayList<WeatherData> result;
	public ArrayList<WeatherData> validate(ArrayList<WeatherData> testData,ArrayList<String> features,String target,
			ArrayList<WeatherData> XtestData
			) {
		try {
			GrowTree tree = new GrowTree(testData, features, target);
			root = tree.construct();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		result = XtestData;
		int correct = 0;
		ArrayList<WeatherData> res = getResult(testData, features, target);
		
		int i=0;
		for (WeatherData item : res) {	

			String testLabel = (String)item.getYValue().get(0);
			List label = testData.get(i).getYValue();
			for(int j=0;j<label.size();j++) {
				String x = (String)label.get(j);
				if(x.equals(testLabel)) {
				correct++;
				break;
				}
			}
			i++;
		}
		scores = (correct *1.0 / res.size());
		return res;
		//return scores;
	}

	public ArrayList<WeatherData> getResult(ArrayList<WeatherData> XtestData,ArrayList<String> features,String target) {
		mine(XtestData, features, target);
		return result;
	}

	private void mine(ArrayList<WeatherData> testData,ArrayList<String> features,String target) {
		for (int i = 0; i < testData.size(); i++) {
			Node node = root;
			WeatherData currInstance = testData.get(i);
			WeatherData resInstance = result.get(i);
			String value = null;
			while (!node.getType().equals("leaf")) {
				String attributeName = node.getAttribute().getName();
				ArrayList<Feature> attributeValuePairs = currInstance.getFeatures();
				for(Feature f: attributeValuePairs) {
					if(f.getName().contains(attributeName)) {
						value = (String)f.getValues().get(0);
					}
				}
				//String value = attributeValuePairs.get(attributeName);

				HashMap<String, Node> children = node.getChildren();
				String tmp = "";
				for (String s : children.keySet()) {
					String threshold = s.substring(4);
					if (Double.parseDouble(value) < Double.parseDouble(threshold)) {
						tmp = "less";
					} else {
						tmp = "more";
					}
					String curLabel = s.substring(0, 4);
					if (tmp.equals(curLabel)) node = children.get(s);
				}

			}
			resInstance.addFeaturesToYData(node.getleafLabel());
		}
	}

}
