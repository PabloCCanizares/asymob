package metrics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import generator.Entity;
import generator.Intent;
import generator.UserInteraction;
import metrics.base.MetricValue;

public class MetricReport {

	LinkedList<MetricValue> botMetrics;
	HashMap<Entity, List<MetricValue>> entityMap;
	HashMap<UserInteraction, List<MetricValue>> flowMap;
	HashMap<Intent, List<MetricValue>> intentMap;
	
	public MetricReport()
	{
		botMetrics = new LinkedList<MetricValue>();
		entityMap = new HashMap<Entity, List<MetricValue>>();
		flowMap = new HashMap<UserInteraction, List<MetricValue>>();
		intentMap = new HashMap<Intent, List<MetricValue>>();
	}
	public void addBotMetric(MetricValue metricRes) {

		botMetrics.add(metricRes);
	}
	
	public String getStringReport()
	{
		StringBuffer buffOut;
		
		buffOut = new StringBuffer();
		
		//Append botMetric report
		buffOut = buffOut.append(botMetricsToString());
		
		//Append entityMetrics report
		buffOut = buffOut.append(entityMetricsToString());
		
		//Append flowMetrics report		
		buffOut = buffOut.append(flowMetricsToString());
		
		//Append flowMetrics report		
		buffOut = buffOut.append(intentMetricsToString());
				
				
		return buffOut.toString();
	}
	private Object intentMetricsToString() {
		StringBuffer buffOut;
		LinkedList<MetricValue> metricList;
		Intent intentIn;
		
		buffOut = new StringBuffer();
		if(flowMap != null && flowMap.size()>0)
		{
			buffOut = buffOut.append("============================\n");
			buffOut = buffOut.append("INTENT METRICS: \n");
			
			
			for (Map.Entry me : intentMap.entrySet()) {
				
				intentIn = (Intent) me.getKey();
				metricList = (LinkedList<MetricValue>) me.getValue();
				buffOut = buffOut.append(" * ").append(intentIn.getName()).append(" [");
				
				for(MetricValue met: metricList)
				{
					try
					{
						buffOut.append(met.toString()).append(" | ");
					}
					catch(Exception e)
					{
						System.err.println("[intentMetricsToString] Exception catched while creating string report");
					}
				}
				buffOut.append("]\n");
				
	        }
		}
		return buffOut.toString();
	}
	private String entityMetricsToString() {
		StringBuffer buffOut;
		LinkedList<MetricValue> metricList;
		Entity en;
		
		buffOut = new StringBuffer();
		if(entityMap != null && entityMap.size()>0)
		{
			buffOut = buffOut.append("============================\n");
			buffOut = buffOut.append("ENTITY METRICS: \n");
			
			
			for (Map.Entry me : entityMap.entrySet()) {
				
				en = (Entity) me.getKey();
				metricList = (LinkedList<MetricValue>) me.getValue();
				buffOut = buffOut.append(" * ").append(en.getName()).append(" [");
				
				for(MetricValue met: metricList)
				{
					buffOut.append(met.toString()).append(" | ");
				}
				buffOut.append("]\n");
				
	        }
		}
		return buffOut.toString();
	}
	private String botMetricsToString() {
		
		StringBuffer buffOut;
		buffOut = new StringBuffer();
		if(botMetrics != null && botMetrics.size()>0)
		{
			//System.out.println("============================");
			//System.out.println("BOT METRICS");
			buffOut = buffOut.append("============================\n");
			buffOut = buffOut.append("BOT METRICS: \n");
			
			for(MetricValue met: botMetrics)
			{
				//System.out.printf("%s = %s [%s]\n", met.getMetricApplied(), met.getValue(), met.getUnit());
				buffOut = buffOut.append(" * ").append(String.format("%s = %s [%s]\n", met.getMetricApplied(), met.getValue(), met.getUnit()));
			}
			
		}
		return buffOut.toString();
	}
	private String flowMetricsToString() {
		StringBuffer buffOut;
		LinkedList<MetricValue> metricList;
		UserInteraction userIn;
		
		buffOut = new StringBuffer();
		if(flowMap != null && flowMap.size()>0)
		{
			buffOut = buffOut.append("============================\n");
			buffOut = buffOut.append("FLOW METRICS: \n");
			
			
			for (Map.Entry me : flowMap.entrySet()) {
				
				userIn = (UserInteraction) me.getKey();
				metricList = (LinkedList<MetricValue>) me.getValue();
				buffOut = buffOut.append(" * ").append(userIn.getIntent().getName()).append(" [");
				
				for(MetricValue met: metricList)
				{
					buffOut.append(met.toString()).append(" | ");
				}
				buffOut.append("]\n");
				
	        }
		}
		return buffOut.toString();
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
}
