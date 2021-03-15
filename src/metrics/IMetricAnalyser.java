package metrics;

import generator.Bot;

public interface IMetricAnalyser {

	boolean doAnalyse(String strPathIn, MetricOperatorsSet metricOps);
	boolean doAnalyse(Bot botIn, MetricOperatorsSet metricOps);
	
	void getAnalysisSummary();//Obtener un resumen: pensar que tipo de formato . MEtricas cumplidas, no cumplidas, warnings, etc.
	String getMetricsReport();//Obtener todas las metricas
}
