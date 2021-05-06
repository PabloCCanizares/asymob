package metrics.reports;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import generator.Entity;
import generator.Intent;
import generator.UserInteraction;
import metrics.base.MetricValue;
import metrics.db.MetricDataBase;

public class LatexReportGen extends MetricReportGenerator{

	String strReport;
	StringReport repString;
	
	public LatexReportGen() {
		super(null);
		
		repString = null;
		strReport = null;
	}
	
	public LatexReportGen(MetricDataBase dbIn) {
		super(dbIn);
		
		repString = null;
		strReport = null;
	}
	
	//TODO: Dividir el report en 3 partes, cabeceras, cuerpo y final.
	@Override
	public void configure() {
	}

	@Override
	public boolean generateReport() {
		boolean bRet;
		
		try {
			bRet = true;
			strReport = getStringReport();
			repString = new StringReport(strReport);
		}
		catch(Exception e)
		{
			bRet = false;
		}
		
		return bRet;
	}


	public String getStringReport()
	{
		StringBuffer buffOut;
		
		buffOut = new StringBuffer();
		
		//Append botMetric report
		buffOut = buffOut.append(botMetricsToString());
		
		/*//Append entityMetrics report
		buffOut = buffOut.append(entityMetricsToString());
		
		//Append flowMetrics report		
		buffOut = buffOut.append(flowMetricsToString());
		
		//Append flowMetrics report		
		buffOut = buffOut.append(intentMetricsToString());*/
				
				
		return buffOut.toString();
	}
	private Object intentMetricsToString() {
		StringBuffer buffOut;
		LinkedList<MetricValue> metricList;
		Intent intentIn;
		
		buffOut = new StringBuffer();
		intentMap = db.getIntentMap();
		if(intentMap != null && intentMap.size()>0)
		{
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
	
	HashMap<UserInteraction, List<MetricValue>> flowMap;
	HashMap<Intent, List<MetricValue>> intentMap;
	
	private String entityMetricsToString() {
		StringBuffer buffOut;
		LinkedList<MetricValue> metricList;
		Entity en;
		HashMap<Entity, List<MetricValue>> entityMap;
		
		entityMap = db.getEntityMap();
		
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
		LinkedList<MetricValue> botMetrics;
		
		buffOut = new StringBuffer();
		botMetrics = db.getBotMetrics();
		if(botMetrics != null && botMetrics.size()>0)
		{
			//System.out.println("============================");
			//System.out.println("BOT METRICS");
			buffOut = buffOut.append("============================\n");
			buffOut = buffOut.append("BOT METRICS: \n");
			
			//Titles
			for(MetricValue met: botMetrics)
			{
				//System.out.printf("%s = %s [%s]\n", met.getMetricApplied(), met.getValue(), met.getUnit());
				buffOut = buffOut.append(String.format("%s &", met.getMetricApplied()));
			}
			buffOut = buffOut.append("\n");
			//Values
			for(MetricValue met: botMetrics)
			{
				//System.out.printf("%s = %s [%s]\n", met.getMetricApplied(), met.getValue(), met.getUnit());
				buffOut = buffOut.append(String.format("%s &", met.getValue()));
			}
			
		}
		return buffOut.toString();
	}
	private String flowMetricsToString() {
		StringBuffer buffOut;
		LinkedList<MetricValue> metricList;
		UserInteraction userIn;
		
		buffOut = new StringBuffer();
		flowMap = db.getFlowMap();
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


	@Override
	public MetricReport getReport() {
		return repString;
	}
}
