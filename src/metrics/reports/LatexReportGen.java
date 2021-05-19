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
import metrics.db.MetricDbSingleEntry;

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
			System.out.println("[generateReport] - Exception catched: "+e.getMessage());
		}
		
		return bRet;
	}


	public String getStringReport()
	{
		StringBuffer buffOut;
		
		buffOut = new StringBuffer();
		
		//Append headers
		buffOut = buffOut.append(getLatexHeaders());
		
		//Append botMetric report
		buffOut = buffOut.append(botMetricsToString());
				
		//Append footers
		buffOut = buffOut.append(getLatexFooters());
		
		return buffOut.toString();
	}
	

	private String getLatexFooters() {
		return "";
	}

	private String getLatexHeaders() {
		StringBuffer buffOut;
		MetricDbSingleEntry chatbotEntry;
		LinkedList<MetricValue> botMetrics;
		
		buffOut = new StringBuffer();
		
		db.resetIndex();
		if(db.hasNext())
		{
			chatbotEntry = db.getNextEntry();
			botMetrics = chatbotEntry.getBotMetrics();
			
			for(MetricValue met: botMetrics)
			{
				//System.out.printf("%s = %s [%s]\n", met.getMetricApplied(), met.getValue(), met.getUnit());
				buffOut = buffOut.append(String.format("&%s", met.getMetricApplied()));
			}
			buffOut = buffOut.append("\n");
		}
		return buffOut.toString();
	}

	private String botMetricsToString() {
		
		StringBuffer buffOut;
		LinkedList<MetricValue> botMetrics;
		MetricDbSingleEntry chatbotEntry;
		
		buffOut = new StringBuffer();
		
		//Iterate all metricsEntry
		db.resetIndex();
		while(db.hasNext())
		{
			chatbotEntry = db.getNextEntry();
			botMetrics = chatbotEntry.getBotMetrics();
			if(botMetrics != null && botMetrics.size()>0)
			{
				
				buffOut.append(chatbotEntry.getBotName()+"&DialogFlow&G");
				//Titles
				/*for(MetricValue met: botMetrics)
				{
					//System.out.printf("%s = %s [%s]\n", met.getMetricApplied(), met.getValue(), met.getUnit());
					buffOut = buffOut.append(String.format("&%s ", met.getMetricApplied()));
				}*/
				//Values
				for(MetricValue met: botMetrics)
				{
					//System.out.printf("%s = %s [%s]\n", met.getMetricApplied(), met.getValue(), met.getUnit());
					buffOut = buffOut.append(String.format("&%s ", met.getValue()));
				}
				buffOut = buffOut.append("\\\\ \\hline\n");
			}
		}
		
		return buffOut.toString();
	}


	@Override
	public MetricReport getReport() {
		return repString;
	}
}
