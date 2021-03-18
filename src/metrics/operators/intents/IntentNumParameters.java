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

		IntentAnalyser inAnalyser;
		EList<Parameter> paramList;
		int nParameters;
		
		inAnalyser = new IntentAnalyser();
		nParameters=0;
		
		if(intentIn != null)
		{
			paramList = this.intentIn.getParameters();
			if(paramList != null)
				nParameters = paramList.size();
		}
		
		metricRet = new IntegerMetricValue(nParameters);
		metricRet.setMetricApplied(this);
	}

}
