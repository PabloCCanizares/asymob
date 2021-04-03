package metrics.reports;

public class StringReport extends MetricReport{

	String strReport;
	
	public void Initialise()
	{
		eReport = EMetricReports.eString;
	}
	public StringReport()
	{
		Initialise();
	}

	public StringReport(String strReport) {
		Initialise();
		this.strReport = strReport;
	}
	public String getReport() {
		return strReport;
	}
}
