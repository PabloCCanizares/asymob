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
	
	String strReportId;
	String strBotPlatform;
	String strBotSource;
	
	public LatexReportGen() {
		super(null);
		
		repString = null;
		strReport = null;
		
		strReportId="report_id";
		strBotPlatform="DF";
		strBotSource="P";
	}
	
	public LatexReportGen(MetricDataBase dbIn) {
		super(dbIn);
		
		repString = null;
		strReport = null;
		
		strReportId="report_id";
		strBotPlatform="DF";
		strBotSource="P";
	}
	
	//TODO: Permitir editar identificador de tabla, nombre de la tabla, y los tamanyos de los grupos (entities) y el numero de columnas, calculado automaticamente
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
		StringBuffer buffOut;
		
		buffOut = new StringBuffer();
		
		buffOut = buffOut.append("		\\end{tabular}}\n");
		buffOut = buffOut.append("	\\end{center}\n");
		buffOut = buffOut.append("\\end{table*}\n");
		
		return buffOut.toString();
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
			
			buffOut = buffOut.append("\\begin{table*}\n");
			buffOut = buffOut.append("\\begin{center}\n");
			buffOut = buffOut.append("	  \\caption{Summary of the evaluation. Columns use abbreviations for \\underline{D}ialog\\underline{f}low (DF), \\underline{R}a\\underline{s}a (RS), \\underline{G}ithub (G) and \\underline{P}redefined (P).}\n");    
			buffOut = buffOut.append("	      \\label{tab:metrics}\n");
			buffOut = buffOut.append("     \\resizebox{\\linewidth}{!}{\n");
			buffOut = buffOut.append("     \\begin{tabular}{|l|c|c|r|r|r|r|r|r|r|r|r|r|r|r|r|r|r|r|r|r|r|r|r|}\n");
			buffOut = buffOut.append("     		\\hline\n");
			buffOut = buffOut.append("		\\multicolumn{3}{|c|}{{\\bf Chatbot}}& \\multicolumn{7}{|c|}{{\\bf Global metrics}}&\\multicolumn{8}{|c|}{{\\bf Intent metrics}}&\\multicolumn{3}{|c|}{{\\bf Entity metrics}}&\\multicolumn{3}{|c|}{{\\bf Flow metrics}} \\\\ \\hline\n");
					
			buffOut = buffOut.append(String.format("{\\bf Name} &{\\bf Plat.} &{\\bf Src}"));
			for(MetricValue met: botMetrics)
			{
				//System.out.printf("%s = %s [%s]\n", met.getMetricApplied(), met.getValue(), met.getUnit());
				buffOut = buffOut.append(String.format("&{ \\bf %s}", met.getMetricApplied()));
			}
			buffOut = buffOut.append("\\\\ \\hline \n");
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
				
				buffOut.append(chatbotEntry.getBotName()+"&"+strBotPlatform+"&"+strBotSource);

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
