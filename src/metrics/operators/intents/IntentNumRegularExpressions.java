package metrics.operators.intents;

import org.eclipse.emf.common.util.EList;

import analyser.IntentAnalyser;
import generator.Parameter;
import metrics.base.IntegerMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;

public class IntentNumRegularExpressions extends IntentMetricBase{

	public IntentNumRegularExpressions() {
		super(EMetricOperator.eIntentNumRegularExpressions);
	}

	@Override
	public void calculateMetric() {

		IntentAnalyser inAnalyser;
		EList<Parameter> paramList;
		int nParameters;
		
		inAnalyser = new IntentAnalyser();
		nParameters=0;
		
		if(intentIn != null)
		{
			nParameters = inAnalyser.getTotalRegularExp(intentIn);
		}
		
		metricRet = new IntegerMetricValue(this, nParameters);
		metricRet.setMetricApplied(this);
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "INRP";
		this.strMetricDescription = "Number of required parameters";
	}
}
