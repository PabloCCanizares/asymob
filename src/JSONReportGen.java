package metrics.reports;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

import generator.Entity;
import generator.Intent;
import generator.UserInteraction;
import metrics.base.MetricValue;
import metrics.db.MetricDataBase;
import metrics.db.MetricDbSingleEntry;

public class JSONReportGen extends MetricReportGenerator {

	private StringReport repString;
	private HashMap<Intent, List<MetricValue>> intentMap;
	private HashMap<Entity, List<MetricValue>> entityMap;
	private HashMap<UserInteraction, List<MetricValue>> flowMap;

	public JSONReportGen() {	
		super(null);
	}

	public JSONReportGen(MetricDataBase dbIn) {
		super(dbIn);
	}

	@Override
	public boolean generateReport() {
		boolean bRet = true;		
		try {			
			String strReport=this.getJSONReport();
			this.repString = new StringReport(strReport);
		}
		catch(Exception e)
		{
			bRet = false;
			System.out.println("[generateReport] - Exception catched: "+e.getMessage());
		}

		return bRet;
	}

	private String getJSONReport() {
		return this.botMetricsToString()+this.intentMetricsToString()+
			   this.entityMetricsToString()+this.flowMetricsToString()+"}\n";
	}

	private String botMetricsToString() {
		StringBuffer buffOut = new StringBuffer();

		//Iterate all metricsEntry
		db.resetIndex();
		while(db.hasNext())
		{
			MetricDbSingleEntry chatbotEntry = db.getNextEntry();
			List<MetricValue> botMetrics = chatbotEntry.getBotMetrics();
			buffOut.append("{\n\"BotName\": \""+chatbotEntry.getBotName()+"\"");
			buffOut.append(", \n\"Global Metrics\" : {");
			//Values
			this.writeMetricsList(buffOut, botMetrics);
			buffOut = buffOut.append("\n}");
		}

		return buffOut.toString();
	}

	private void writeMetricsList(StringBuffer buffOut, List<MetricValue> metrics) {
		if (metrics == null) return;

		boolean comma = false;
		for(MetricValue met: metrics)
		{
			this.writeMetric(buffOut, met, comma);
			comma = true;
		}
	}

	private void writeMetric(StringBuffer buffOut, MetricValue met, boolean writeComma) {
		if (met.getValue()!=null && !met.getMetricApplied().equals("")) {
			Double d = getDoubleValue(met);
			if (writeComma) buffOut = buffOut.append(",");
			buffOut = buffOut.append("\n   \""+met.getMetricApplied()+"\": "+
					String.format(Locale.ENGLISH, "%.2f", d));
		}		
	}

	private String intentMetricsToString() {			
		StringBuffer buffOut = new StringBuffer(",\n\"Intent Metrics\": [");
		intentMap = db.getIntentMap();
		if(intentMap != null && intentMap.size()>0)
		{			
			boolean comma = false;
			for (Intent intentIn: intentMap.keySet()) {
				if (comma) buffOut.append(",");
				comma = true;
				buffOut = buffOut.append("\n{ \"name\": \""+intentIn.getName()+"\",");
				List<MetricValue> metricList = intentMap.get(intentIn);				
				this.writeMetricsList(buffOut, metricList);
				buffOut = buffOut.append("\n}");
			}
		}
		buffOut = buffOut.append("]");
		return buffOut.toString();
	}
	
	private String entityMetricsToString() {			
		StringBuffer buffOut = new StringBuffer(",\n\"Entity Metrics\": [");
		entityMap = db.getEntityMap();
		if (entityMap != null && entityMap.size()>0)
		{			
			boolean comma = false;
			for (Entity entity: entityMap.keySet()) {
				if (comma) buffOut.append(",");
				comma = true;
				buffOut = buffOut.append("\n{ \"name\": \""+entity.getName()+"\",");
				List<MetricValue> metricList = entityMap.get(entity);				
				this.writeMetricsList(buffOut, metricList);
				buffOut = buffOut.append("\n}");
			}
		}
		buffOut = buffOut.append("]");
		return buffOut.toString();
	}
	
	private String flowMetricsToString() {			
		StringBuffer buffOut = new StringBuffer(",\n\"Flow Metrics\": [");
		flowMap = db.getFlowMap();
		if (flowMap != null && flowMap.size()>0)
		{			
			boolean comma = false;
			for (UserInteraction flow: flowMap.keySet()) {
				if (comma) buffOut.append(",");
				comma = true;
				buffOut = buffOut.append("\n{");
				List<MetricValue> metricList = flowMap.get(flow);				
				this.writeMetricsList(buffOut, metricList);
				buffOut = buffOut.append("\n}");
			}
		}
		buffOut = buffOut.append("]");
		return buffOut.toString();
	}

	private double getDoubleValue(MetricValue met) {
		NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
		Number number;
		try {
			number = format.parse(met.getValue());
			return number.doubleValue();
		} catch (ParseException e) {
			System.out.println("format: "+met.getValue());
		}		
		return 0;
	}

	@Override
	public MetricReport getReport() {
		return this.repString;
	}

	@Override
	public void configure() {
	}

}
