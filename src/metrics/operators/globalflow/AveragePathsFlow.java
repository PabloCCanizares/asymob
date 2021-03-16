package metrics.operators.globalflow;

import org.eclipse.emf.common.util.EList;

import analyser.BotAnalyser;
import generator.UserInteraction;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class AveragePathsFlow extends BotMetricBase{

	public AveragePathsFlow() {
		super(EMetricOperator.eAveragePathFlow);
	}

	@Override
	public void calculateMetric() {
		int nLength;
		float fValue;
		BotAnalyser botAnalyser;
		
		botAnalyser = new BotAnalyser();
		nLength = 0;
		fValue = (float) 0.0;
		
		if(botIn!= null)
		{
			EList<UserInteraction> userInList = botIn.getFlows();
			for(UserInteraction userIn: userInList)
			{
				nLength += botAnalyser.analyseNumPaths(userIn);

				//One per element
				nLength++;
			}
			fValue =  (float)((float)nLength / (float)userInList.size());
			
			metricRet = new FloatMetricValue(fValue);
			
			//Set applied metric
			metricRet.setMetricApplied(this);
		}
	}

}
