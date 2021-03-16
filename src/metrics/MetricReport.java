package metrics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import generator.Entity;
import metrics.base.MetricValue;

public class MetricReport {

	LinkedList<MetricValue> botMetrics;
	HashMap<Entity, List<MetricValue>> entityMap;
	HashMap<Entity, List<MetricValue>> intentMap;
	
	public MetricReport()
	{
		botMetrics = new LinkedList<MetricValue>();
	}
	public void addBotMetric(MetricValue metricRes) {

		botMetrics.add(metricRes);
	}
	
	public String getStringReport()
	{
		StringBuffer buffOut;
		
		buffOut = new StringBuffer();
		if(botMetrics != null)
		{
			//System.out.println("============================");
			//System.out.println("BOT METRICS");
			buffOut = buffOut.append("============================\n");
			buffOut = buffOut.append("BOT METRICS\n");
			
			for(MetricValue met: botMetrics)
			{
				//System.out.printf("%s = %s [%s]\n", met.getMetricApplied(), met.getValue(), met.getUnit());
				buffOut = buffOut.append(String.format("%s = %s [%s]\n", met.getMetricApplied(), met.getValue(), met.getUnit()));
			}
			
		}
		return buffOut.toString();
	}
}
