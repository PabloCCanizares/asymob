package metrics.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import generator.Entity;
import generator.Intent;
import generator.UserInteraction;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;

public class MetricDataBase {

	HashMap<String, MetricDbSingleEntry> dbMap;
	MetricDbSingleEntry dbCurrentBot;
	Iterator<Entry<String, MetricDbSingleEntry>> it;
	public MetricDataBase()
	{
		dbMap = new HashMap<String, MetricDbSingleEntry>();
		dbCurrentBot = null;
	}
	public void insertNewBot(String strName)
	{
		if(!dbMap.containsKey(strName))
		{
			dbCurrentBot = new MetricDbSingleEntry(strName);
			dbMap.put(strName, dbCurrentBot);
		}
		else
		{
			System.out.println("MetricDataBase - WARNING! The current bot is already included in the db, selecting it as current bot");
			dbCurrentBot = dbMap.get(strName);
		}
	}
	public void addBotMetric(MetricValue metricRes) {

		if(dbCurrentBot != null)
			dbCurrentBot.addBotMetrics(metricRes);
		else
			System.out.println("MetricDataBase - WARNING! The current bot is not assigned");
	}
	
	public void addEntityMetric(Entity enIn, MetricValue metricRes) {
		if(dbCurrentBot != null)
			dbCurrentBot.addEntityMetric(enIn, metricRes);
		
	}
	public void addFlowMetric(UserInteraction flowIn, MetricValue metricRes) {
		
		if(dbCurrentBot != null)
			dbCurrentBot.addFlowMetric(flowIn, metricRes);
		else
			System.out.println("MetricDataBase - WARNING! The current bot is not assigned");
		
	}
	public void addIntentMetric(Intent intentIn, MetricValue metricRes) {
		
		if(dbCurrentBot != null)
			dbCurrentBot.addIntentMetric(intentIn, metricRes);
		else
			System.out.println("MetricDataBase - WARNING! The current bot is not assigned");

	}

	public HashMap<Entity, List<MetricValue>> getEntityMap() {
		return dbCurrentBot.getEntityMap();
	}

	public LinkedList<MetricValue> getBotMetrics() {
		return dbCurrentBot.getBotMetrics();
	}
	public HashMap<UserInteraction, List<MetricValue>> getFlowMap() {
		return dbCurrentBot.getFlowMap();
	}
	public HashMap<Intent, List<MetricValue>> getIntentMap() {
		return dbCurrentBot.getIntentMap();
	}
	public LinkedList<MetricValue> getEntityMetric(EMetricOperator eMetricIn) {
		LinkedList<MetricValue> metricRet;
		
		metricRet = null;
		
		if(dbCurrentBot != null)
			metricRet = dbCurrentBot.getEntityMetric(eMetricIn);
		else
			System.out.println("MetricDataBase - WARNING! The current bot is not assigned");
		
		return metricRet;
	}
	public LinkedList<MetricValue> getFlowMetric(EMetricOperator eMetricIn) {
		
		LinkedList<MetricValue> metricRet;
		
		metricRet = null;
		
		if(dbCurrentBot != null)
			metricRet = dbCurrentBot.getFlowMetric(eMetricIn);
		else
			System.out.println("MetricDataBase - WARNING! The current bot is not assigned");
		
		return metricRet;
		
	}
	public LinkedList<MetricValue> getIntentMetric(EMetricOperator eMetricIn) {
		LinkedList<MetricValue> metricRet;
		
		metricRet = null;
		
		if(dbCurrentBot != null)
			metricRet = dbCurrentBot.getIntentMetric(eMetricIn);
		else
			System.out.println("MetricDataBase - WARNING! The current bot is not assigned");
		
		return metricRet;
		
	}
	public MetricDbSingleEntry getNextEntry() {
		MetricDbSingleEntry ret;
		
		ret= null;
		if(it != null)
		{
			Map.Entry pair = (Map.Entry)it.next();
		    ret = (MetricDbSingleEntry) pair.getValue();
		}
		return ret;
	}
	public boolean hasNext() {
		return it == null ?  false :  it.hasNext();
	}
	public void resetIndex() {
		it = dbMap.entrySet().iterator();
	}
}
