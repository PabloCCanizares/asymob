package metrics.reports;

import metrics.db.MetricDataBase;
import metrics.db.ReadOnlyMetricDB;

public abstract class MetricReportGenerator {

	ReadOnlyMetricDB db;
	
	public MetricReportGenerator(MetricDataBase dbIn)
	{
		db = new ReadOnlyMetricDB(dbIn);
	}
	
	public void setDB(MetricDataBase metricDB) {
		db = new ReadOnlyMetricDB(metricDB);
	}
	
	public abstract void configure();
	public abstract boolean generateReport();
	public abstract MetricReport getReport();
}
