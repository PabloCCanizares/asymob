package metrics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import generator.Entity;
import metrics.base.MetricValue;

public class MetricReport {

	LinkedList<MetricValue> botMetrics;
	HashMap<Entity, List<MetricValue>> entityMap;
	HashMap<Entity, List<MetricValue>> intentMap;
	
	public MetricReport()
	{
		botMetrics = new LinkedList<MetricValue>();
		entityMap = new HashMap<Entity, List<MetricValue>>();
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
		
		buffOut = buffOut.append(entityMetricsToString());
		
		return buffOut.toString();
	}
	private String entityMetricsToString() {
		StringBuffer buffOut;
		LinkedList<MetricValue> metricList;
		Entity en;
		
		buffOut = new StringBuffer();
		if(entityMap != null)
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
		if(botMetrics != null)
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
	public void addEntityMetric(Entity enIn, MetricValue metricRes) {
		List<MetricValue> metricList;
		
		if(!entityMap.containsKey(enIn))
			entityMap.put(enIn, new LinkedList<MetricValue>());
		
		metricList = entityMap.get(enIn);
		if(metricList != null)
			metricList.add(metricRes);
	}
}
