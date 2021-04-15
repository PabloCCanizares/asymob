package metrics.operators.intents;

import org.eclipse.emf.common.util.EList;

import analyser.IntentAnalyser;
import generator.Parameter;
import metrics.base.IntegerMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;

public class IntentNumRequiredParameters extends IntentMetricBase{

	public IntentNumRequiredParameters() {
		super(EMetricOperator.eIntentNumReqParameters);
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
			{
				for(Parameter param: paramList)
				{
					if(param != null && param.isRequired())
						nParameters++;
				}
				
				nParameters = paramList.size();
			}
		}
		
		metricRet = new IntegerMetricValue(this, nParameters);
		metricRet.setMetricApplied(this);
	}

}
