package main;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import util.Feature;
import util.WeatherData;

public class SelectFeature 
{
   private String feature;
   private HashMap<Integer,WeatherData>xTrainLeftPart;
   private HashMap<Integer,WeatherData>xTrainRightPart;
   private HashMap<Integer,WeatherData>yTrainLeftPart;
   private HashMap<Integer,WeatherData>yTrainRightPart;
   private double gainRatio;
   private double maxGainRatioFeatureValue;
   HashMap<String,HashMap<Integer,WeatherData>> ysubset;
HashMap<String,HashMap<Integer,WeatherData>> xsubset;
   
   public SelectFeature(String target_val,ArrayList<String> features,HashMap<Integer,WeatherData> XDataInTrain,
		   HashMap<Integer,WeatherData> YDataInTrain) 
		   {
	             feature = null;
	             gainRatio = Double.MIN_VALUE;
	             //System.out.println("the min double"+gainRatio);
	             xTrainLeftPart = null;
	             xTrainRightPart = null;
	             yTrainLeftPart = null;
	             yTrainRightPart = null;
	             HashMap<String,Double> gainRatioMap = new HashMap<String,Double>();
	  
	             //Loop through the feature list to find the feature with maximum Gain Ratio
	            // System.out.println("feature "+features);
	             for(String ftr : features)
	             {
	            	 //System.out.println("next feature is "+ftr);
	            	 double currentGainRatio = Double.MIN_VALUE;
	            	 HashMap<Integer,WeatherData> xTrainLeftPartCurrent = null;
	            	 HashMap<Integer,WeatherData> xTrainRightPartCurrent = null;
	            	 HashMap<Integer,WeatherData> yTrainLeftPartCurrent = null;
	            	 HashMap<Integer,WeatherData> yTrainRightPartCurrent = null;
	            	 HashMap<String,HashMap<Integer,WeatherData>> ysubsetCurrent = null;
	     			HashMap<String,HashMap<Integer,WeatherData>> xsubsetCurrent = null;
	     		
	            	 GainRatio gr = new GainRatio(XDataInTrain,YDataInTrain,ftr,target_val);
	            
	            	 currentGainRatio = gr.getGainRatio();
	            	 xTrainLeftPartCurrent = gr.getXTrainLeftPart();
	            	 xTrainRightPartCurrent = gr.getXTrainRightPart();
	            	 yTrainLeftPartCurrent = gr.getYTrainLeftPart();
	            	 yTrainRightPartCurrent = gr.getYTrainRightPart();
	            	 maxGainRatioFeatureValue = gr.getMaxGainRatioFeatureValue();
	            	 ysubsetCurrent = gr.getYSubset();
	            	 xsubsetCurrent = gr.getXSubset();
	            	 gainRatioMap.put(ftr, currentGainRatio);
	             
	                if(currentGainRatio > gainRatio)
	                 {
		            	 gainRatio = currentGainRatio;
		            	 xTrainLeftPart = xTrainLeftPartCurrent;
		            	 xTrainRightPart = xTrainRightPartCurrent;
		            	 yTrainLeftPart = yTrainLeftPartCurrent;
		            	 yTrainRightPart = yTrainRightPartCurrent;
		            	 ysubset = ysubsetCurrent;
		 				 xsubset = xsubsetCurrent;
		            	 feature = ftr;
	                 }
	             
	             }
	             int count =0;
	             for(Map.Entry<String, Double> data : gainRatioMap.entrySet())
	     		{
	             String key = data.getKey();
	             double gain = data.getValue();
	             if(gain==0.0)
	            	 count++;
	             
	     		}

	             if(count==gainRatioMap.size()) {
	            	 String key=null;
	            	 for(Map.Entry<String, Double> data : gainRatioMap.entrySet())
	 	     		{
	 	             key = data.getKey();
	 	             break;
	 	     		}
	            	 GainRatio gr = new GainRatio(XDataInTrain,YDataInTrain,key,target_val);
	            	 gainRatio = gr.getGainRatio();
	            	 xTrainLeftPart = gr.getXTrainLeftPart();
	            	 xTrainRightPart = gr.getXTrainRightPart();
	            	 yTrainLeftPart = gr.getYTrainLeftPart();
	            	 yTrainRightPart = gr.getYTrainRightPart();
	            	 ysubset = gr.getYSubset();
	 				 xsubset = gr.getXSubset();
	            	 feature = gr.getFeature();
	            	 
	             }
	             
		   }
   public String getFeature()
   {
   	return feature;
   }
   public double getMaxGainRatioFeatureValue()
   {
 	  return maxGainRatioFeatureValue; 
   }
   public double getGainRatio()
   {
 	  return gainRatio;
   }
   public HashMap<Integer,WeatherData> getXTrainLeftPart()
   {
 	  return xTrainLeftPart;
   }
   public HashMap<Integer,WeatherData> getXTrainRightPart()
   {
 	  return xTrainRightPart;
   }
   public HashMap<Integer,WeatherData> getYTrainLeftPart()
   {
 	  return yTrainLeftPart;
   }
   public HashMap<Integer,WeatherData> getYTrainRightPart()
   {
 	  return yTrainRightPart;
   }
   
   public HashMap<String,HashMap<Integer,WeatherData>> getYSubset() {
	   for(Map.Entry<String,HashMap<Integer,WeatherData>> data : ysubset.entrySet())
		{
		   String key = data.getKey();
		  
		   break;
		}
		
	   
		return ysubset;
	}

	public HashMap<String,HashMap<Integer,WeatherData>> getXSubset() {
		return xsubset;
	}
   
}
