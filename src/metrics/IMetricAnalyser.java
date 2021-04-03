package metrics;

import generator.Bot;
import metrics.reports.MetricReport;
import metrics.reports.MetricReportGenerator;

public interface IMetricAnalyser {

	boolean doAnalyse(String strPathIn, MetricOperatorsSet metricOps);
	boolean doAnalyse(Bot botIn, MetricOperatorsSet metricOps);
	
	void configureReport(MetricReportGenerator metricGen);
	void getAnalysisSummary();
	MetricReport getMetricsReport(MetricReportGenerator metricReport);
}
