package metrics.operators.bot.globalflow;

import org.eclipse.emf.common.util.EList;

import analyser.BotAnalyser;
import generator.UserInteraction;
import metrics.base.FloatMetricValue;
import metrics.base.IntegerMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class AvgFlowLen extends BotMetricBase{

	public AvgFlowLen() {
		super(EMetricOperator.eGlobalMaxFlowLength);
	}

	@Override
	public void calculateMetric() {
		int nMaxFlow, nAux;
		BotAnalyser botAnalyser;
		
		botAnalyser = new BotAnalyser();
		nMaxFlow = 0;
		
		if(botIn!= null)
		{
			EList<UserInteraction> userInList = botIn.getFlows();
			for(UserInteraction userIn: userInList)
			{
				nAux = botAnalyser.analyseMaxLenght(userIn);
				nMaxFlow = Math.max(nAux, nMaxFlow);
			}
			
			metricRet = new IntegerMetricValue(this, nMaxFlow);
			
			//Set applied metric
			metricRet.setMetricApplied(this);
		}
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "CL";
		this.strMetricDescription = "Conversation length";
	}
}
