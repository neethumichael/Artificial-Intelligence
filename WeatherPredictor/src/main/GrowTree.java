package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import util.Node;
import util.WeatherData;
import util.Feature;

public class GrowTree {
	private ArrayList<String> attributes;
	private ArrayList<WeatherData> instances;
	private String target;
	
	/*public GrowTree(String fileName) throws IOException {
		ProcessInputData input = new ProcessInputData(fileName);
		attributes = input.getAttributeSet();
		instances = input.getInstanceSet();
		target = input.getTargetAttribute();
	}*/
	
	public GrowTree(ArrayList<WeatherData> instances, ArrayList<String> attributes,
			String target) {
		this.instances = instances;
		this.attributes = attributes;
		this.target = target;
	}

	/**
	 * Construct tree
	 * @return TreeNode
	 * @throws IOException
	 */
	public Node construct() throws IOException {
		return constructTree(target, attributes, instances);
	}
	
	/**
	 * Construct tree recursively. First make the root node, then construct its subtrees 
	 * recursively, and finally connect root with subtrees.
	 * @param target
	 * @param attributes
	 * @param instances
	 * @return TreeNode
	 * @throws IOException
	 */
	private Node constructTree(String target, ArrayList<String> attributes, 
			ArrayList<WeatherData> instances) throws IOException {
		
		/*
		 *  Stop when (1) entropy is zero
		 *  (2) no attribute left
		 */
		if (homogenous(target, instances) || attributes.size() == 0) {
			String leafLabel = "";
			if (homogenous(target, instances)) {
				// get 
				instances.get(0).getYValue();
				leafLabel = instances.get(0).getYValue().toString();
			} else {
				leafLabel = getMajorityLabel(target, instances);
			}
			Node leaf = new Node(leafLabel);
			return leaf;
		}
		
		// Choose the root attribute
		SelectFeature selectedFeature = new SelectFeature(target, attributes, instances);
		String rootAttr = selectedFeature.getChosen();
		
		// Remove the chosen attribute from attribute set
		attributes.remove(rootAttr);
		
		// Make a new root
		Node root = new Node(rootAttr);
		
		// Get value subsets of the root attribute to construct branches
		HashMap<String, ArrayList<Feature>> valueSubsets = selectedFeature.getSubset();
		for (String valueName : valueSubsets.keySet()) {
			ArrayList<WeatherData> subset = valueSubsets.get(valueName);
			if (subset.size() == 0) {
				String leafLabel = getMajorityLabel(target, instances);
				Node leaf = new Node(leafLabel);
				root.addChild(valueName, leaf);
			} else {
				Node child = constructTree(target, attributes, subset);
				root.addChild(valueName, child);
			}			
		}		
		
		// Remember to add it again!
		attributes.add(rootAttr);
		
		return root;
	}
	
	public Boolean homogenous(String target, ArrayList<WeatherData> instances) {
		ArrayList<Object> valuesOfTarget = new ArrayList<Object>();
		for(WeatherData wd : instances)
		{
			
			valuesOfTarget.add(wd.featureValue(target)); 		
		}
		String targetName = target;
		HashMap<Object, Integer> countValueOfTarget = new HashMap<Object, Integer>();
		for (Object s : valuesOfTarget) {
			countValueOfTarget.put(s, 0);
		}
		Object valueOfInstanceAtTarget =null;
		ArrayList<WeatherData> data = new ArrayList<WeatherData>();
		for (WeatherData instance : instances) {
			data.add(instance);
		}
		for (int i = 0; i <= instances.size(); i++)  {
			WeatherData instance = data.get(i);
			ArrayList<Feature>  attributeValuePairsOfInstance= instance.getFeatures();
			for(Feature f: attributeValuePairsOfInstance) {
				if(f.getName().equals(targetName))
				{
					valueOfInstanceAtTarget= (Object)f.getValues().get(0);
					break;
				}
			}
			
			countValueOfTarget.put(valueOfInstanceAtTarget, 
					countValueOfTarget.get(valueOfInstanceAtTarget) + 1);
		}
		int totalN = instances.size();
		double count = 0;
		
		for (Object s : valuesOfTarget) {
			int countSingleValue = countValueOfTarget.get(s);
			if (countSingleValue != 0) 
				return false;							
		}
		
	
		return true;
	}
	
	/**
	 * Get the majority target class label from instances
	 * @param target
	 * @param instances
	 * @return String
	 * @throws IOException
	 */
	public String getMajorityLabel(String target, ArrayList<WeatherData> instances) throws IOException {
		ArrayList<Object> valuesOfTarget = new ArrayList<Object>();
		for(WeatherData wd : instances)
		{
			
			valuesOfTarget.add(wd.featureValue(target)); 		
		}
		String targetName = target;
		HashMap<Object, Integer> countValueOfTarget = new HashMap<Object, Integer>();
		HashMap<Object, List<String>> labelOfTarget = new HashMap<Object, List<String>>();
		for (Object s : valuesOfTarget) {
			countValueOfTarget.put(s, 0);
		}
		Object valueOfInstanceAtTarget =null;
		for (WeatherData instance : instances) {
			ArrayList<Feature>  attributeValuePairsOfInstance= instance.getFeatures();
			for(Feature f: attributeValuePairsOfInstance) {
				if(f.getName().equals(targetName))
				{
					valueOfInstanceAtTarget= (Object)f.getValues().get(0);
				}
			}
			
			if (!countValueOfTarget.containsKey(valueOfInstanceAtTarget)) 
				throw new IOException("Invalid input data");
			countValueOfTarget.put(valueOfInstanceAtTarget, 
					countValueOfTarget.get(valueOfInstanceAtTarget) + 1);
			labelOfTarget.put(valueOfInstanceAtTarget, instance.getYValue());
		}
		String maxLabel = "";
		int maxCount = 0;
		for (Object s : countValueOfTarget.keySet()) {
			int currCount = countValueOfTarget.get(s);
			if (currCount > maxCount) {
				maxCount = currCount;
				//maxLabel = labelOfTarget.get(s);
				if(labelOfTarget.containsValue(target)) {
					maxLabel = target;
				}
				else {
					maxLabel = "No "+target;
				}
			}
		}
		return maxLabel;
	}
}
