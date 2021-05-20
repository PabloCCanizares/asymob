package metrics.operators.intents;

import org.eclipse.emf.common.util.EList;

import analyser.IntentAnalyser;
import generator.Parameter;
import metrics.base.IntegerMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;

public class IntentNumParameters extends IntentMetricBase{

	public IntentNumParameters() {
		super(EMetricOperator.eIntentNumParameters);
	}

	@Override
	public void calculateMetric() {

		EList<Parameter> paramList;
		int nParameters;
		
		nParameters=0;
		
		if(intentIn != null)
		{
			paramList = this.intentIn.getParameters();
			if(paramList != null)
				nParameters = paramList.size();
		}
		
		metricRet = new IntegerMetricValue(this, nParameters);
		metricRet.setMetricApplied(this);
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "INP";
		this.strMetricDescription = "Intent number of parameters";
	}
}
