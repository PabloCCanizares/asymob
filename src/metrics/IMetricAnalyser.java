package metrics;

import generator.Bot;

public interface IMetricAnalyser {

	boolean doAnalyse(Bot botIn, MetricOperatorsSet metricOps);
	
	void getAnalysisSummary();//Obtener un resumen: pensar que tipo de formato . MEtricas cumplidas, no cumplidas, warnings, etc.
	void getMetricsReport();//Obtener todas las metricas
}
