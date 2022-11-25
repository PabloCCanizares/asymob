package metrics.reports;

import java.util.LinkedList;

import metrics.base.MetricValue;
import metrics.db.MetricDataBase;
import metrics.db.MetricDbSingleEntry;

public class CsvReportGen extends MetricReportGenerator {

	String strReport;
	StringReport repString;
	
	String strReportId;
	String strBotPlatform;
	String strBotSource;
	
	public CsvReportGen() {
		super(null);
	}
	
	public CsvReportGen(MetricDataBase dbIn) {
		super(dbIn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void configure() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean generateReport() {
		boolean bRet;
		String strReport;
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

	private String getStringReport() {
		StringBuffer buffOut;
		
		buffOut = new StringBuffer();
		
		//Append headers
		buffOut = buffOut.append(getCsvHeaders());
		
		//Append botMetric report
		buffOut = buffOut.append(botMetricsToString());
				
		return buffOut.toString();
	}

	private String getCsvHeaders() {
		StringBuffer buffOut;
		MetricDbSingleEntry chatbotEntry;
		LinkedList<MetricValue> botMetrics;
		
		buffOut = new StringBuffer();
		
		db.resetIndex();
		if(db.hasNext())
		{
			chatbotEntry = db.getNextEntry();
			botMetrics = chatbotEntry.getBotMetrics();

			buffOut = buffOut.append(String.format("Name"));
			for(MetricValue met: botMetrics)
			{
				//System.out.printf("%s = %s [%s]\n", met.getMetricApplied(), met.getValue(), met.getUnit());
				buffOut = buffOut.append(String.format(",%s", met.getMetricApplied()));
			}
			buffOut = buffOut.append("\n");
		}
		return buffOut.toString();
	}

	@Override
	public MetricReport getReport() {
		return repString;
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
				
				//buffOut.append(chatbotEntry.getBotName()+","+strBotPlatform+","+strBotSource);
				buffOut.append(chatbotEntry.getBotName());
				
				//Values
				for(MetricValue met: botMetrics)
				{
					//System.out.printf("%s = %s [%s]\n", met.getMetricApplied(), met.getValue(), met.getUnit());
					buffOut = buffOut.append(String.format(",%s", met.getValue()));
				}
				buffOut = buffOut.append("\n");
			}
		}
		
		return buffOut.toString();
	}
}
