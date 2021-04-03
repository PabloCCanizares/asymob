package metrics.db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import generator.Entity;
import generator.Intent;
import generator.UserInteraction;
import metrics.base.MetricValue;

public class MetricDataBase {

	private static MetricDataBase instanceDB;

	LinkedList<MetricValue> botMetrics;
	HashMap<Entity, List<MetricValue>> entityMap;
	HashMap<UserInteraction, List<MetricValue>> flowMap;
	HashMap<Intent, List<MetricValue>> intentMap;
	
	/*public static MetricDataBase getInstance() {

		if (instanceDB==null) {

			instanceDB=new MetricDataBase();
		}
		return instanceDB;
	}*/
	
	public MetricDataBase()
	{
		botMetrics = new LinkedList<MetricValue>();
		entityMap = new HashMap<Entity, List<MetricValue>>();
		flowMap = new HashMap<UserInteraction, List<MetricValue>>();
		intentMap = new HashMap<Intent, List<MetricValue>>();
	}
	public void addBotMetric(MetricValue metricRes) {

		botMetrics.add(metricRes);
	}
	
	public void addEntityMetric(Entity enIn, MetricValue metricRes) {
		List<MetricValue> metricList;
		
		if(!entityMap.containsKey(enIn))
			entityMap.put(enIn, new LinkedList<MetricValue>());
		
		metricList = entityMap.get(enIn);
		if(metricList != null)
			metricList.add(metricRes);
	}
	public void addFlowMetric(UserInteraction flowIn, MetricValue metricRes) {
		List<MetricValue> metricList;
		
		if(!flowMap.containsKey(flowIn))
			flowMap.put(flowIn, new LinkedList<MetricValue>());
		
		metricList = flowMap.get(flowIn);
		if(metricList != null)
			metricList.add(metricRes);
		
	}
	public void addIntentMetric(Intent intentIn, MetricValue metricRes) {
		List<MetricValue> metricList;
		
		if(!intentMap.containsKey(intentIn))
			intentMap.put(intentIn, new LinkedList<MetricValue>());
		
		metricList = intentMap.get(intentIn);
		if(metricList != null)
			metricList.add(metricRes);
	}

	public HashMap<Entity, List<MetricValue>> getEntityMap() {
		return entityMap;
	}

	public LinkedList<MetricValue> getBotMetrics() {
		return botMetrics;
	}
	public HashMap<UserInteraction, List<MetricValue>> getFlowMap() {
		return flowMap;
	}
	public HashMap<Intent, List<MetricValue>> getIntentMap() {
		return intentMap;
	}
}
