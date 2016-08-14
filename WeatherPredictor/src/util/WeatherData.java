package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherData {

	private ArrayList<Feature> features;
	private ArrayList<String> featureNames;
	public WeatherData() {
		features = new ArrayList<Feature>();
		featureNames =new ArrayList<String>();
	}

	public void addFeatureNames(String names) {
		String[] tempNames= names.split(",");
		for(String name: tempNames) {
			featureNames.add(name);
		}
	}
	
	public ArrayList<Object> featureValue(String featureName) {
		for(int i=0;i<this.features.size();i++) {
			if(this.features.get(i).getName().contains(featureName)) {
				ArrayList<Object> temp = new ArrayList<Object>();
				temp.addAll(this.features.get(i).getValues());
				return  temp;
			}
		}
		return null;
	}
	
	public List getYValue() {
		int i=0;
		for(String category: this.getFeatureNames() )
		{
			if(category.contains("Events"))
				return this.getFeatures().get(i).getValues();
		}
		return null;
	}
	
	public ArrayList<Feature> getFeatures() {
		return this.features;
	}

	public ArrayList<String> getFeatureNames() {
		return this.featureNames;
	}

	public void addFeatures(String data) {
		String[] featureValues= data.split(",");
		int i=0;
		for(String feature: featureValues) {
			if(featureNames.get(i).contains("EDT")) {
				List<Date> date = new ArrayList<Date>();
				date.add(dateConversion(feature));
				features.add(new Feature<Date> (date,featureNames.get(i) ));
			}
			else if (featureNames.get(i).contains("Events")){
				List<String> events = new ArrayList<String>();
				if(feature.isEmpty()) {
					String event = "Normal";
					events.add(event);
					features.add(new Feature<String> (events,featureNames.get(i) ));
				} else {
					events = new ArrayList<String>();
					String[] eventSplit = feature.split("-");
					//System.out.println("---------");
					for(int j=0;j<eventSplit.length;j++) {
						//System.out.println(eventSplit[j]);
						events.add(eventSplit[j]);
					}
					features.add(new Feature<String> (events,featureNames.get(i) ));
				}
			}
			else 
				if(featureNames.get(i).contains("PrecipitationIn")) {
					Object featureObj = feature;
					List<Object> precp = new ArrayList<Object>();
					precp.add(feature);
					features.add(new Feature<Object> (precp,featureNames.get(i) ));
				}
				else {
					Object featureObj = feature;
					List<Object> val = new ArrayList<Object>();
					val.add(feature);
					features.add(new Feature<Object> (val,featureNames.get(i) ));
				}
			i++;
		}
	}
	public void addFeaturesToXData(String data) {
		String[] featureValues= data.split(",");
		int i=0;
		for(String feature: featureValues) 
		{
			if (featureNames.get(i).contains("Events"))
			{
				continue;
			}
			else if(featureNames.get(i).contains("EST")) {
				List<Date> date = new ArrayList<Date>();
				date.add(dateConversion(feature));
				features.add(new Feature<Date> (date,featureNames.get(i) ));
			}
			else 
			{
				if(featureNames.get(i).contains("PrecipitationIn")) {
					Object featureObj = feature;
					List<Object> precp = new ArrayList<Object>();
					precp.add(feature);
					features.add(new Feature<Object> (precp,featureNames.get(i) ));
				}
				else {
					Object featureObj = feature;
					List<Object> val = new ArrayList<Object>();
					val.add(feature);
					features.add(new Feature<Object> (val,featureNames.get(i) ));
				}			
		    }
			i++;
		}
	}
	
	public void addFeaturesToYData(String data) {
		for(int i=0;i<this.features.size();i++) {
			if(this.features.get(i).getName().contains("Events")) {
				if(this.features.get(i).getValues().size()>0) {
					List values = this.features.get(i).getValues(); 
					values.add((Object) data);
					this.features.get(i).setValues(values);
					return;
				}
			}
		}
		String[] featureValues= data.split(",");
		int i=0;
		for(String feature: featureValues) 
		{
			if(!featureNames.get(i).contains("Events")) {
				i++;
				continue;
			}
			else
			{
				List<String> events = new ArrayList<String>();
				if(feature.isEmpty()) {
					String event = "Normal";
					events.add(event);
					features.add(new Feature<String> (events,featureNames.get(i) ));
				} else {
					events = new ArrayList<String>();
					String[] eventSplit = feature.split("-");
					//System.out.println("---------");
					for(int j=0;j<eventSplit.length;j++) {
						//System.out.println(eventSplit[j]);
						events.add(eventSplit[j]);
					}
					features.add(new Feature<String> (events,featureNames.get(i) ));
					
				}
			}
			i++;
		}

	}
	@SuppressWarnings("deprecation")
	public Date dateConversion(String value) {
		Date date = new Date();
		String dateSplit[] = value.split("-");
		date.setYear(Integer.parseInt(dateSplit[0]));
		date.setMonth(Integer.parseInt(dateSplit[1]));
		date.setDate(Integer.parseInt(dateSplit[2]));

		return date;
	}

	public static boolean isNumeric(String str)	{
		for (char c : str.toCharArray())
		{
			if (!Character.isDigit(c)) return false;
		}
		return true;
	}
}