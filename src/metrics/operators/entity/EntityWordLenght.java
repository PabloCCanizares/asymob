package metrics.operators.entity;

import java.util.LinkedList;
import java.util.List;

import analyser.EntityAnalyser;
import generator.EntityInput;
import generator.LanguageInput;
import generator.SimpleInput;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.EntityMetricBase;

public class EntityWordLenght extends EntityMetricBase{

	public EntityWordLenght() {
		super(EMetricOperator.eEntityWordLen);
	}

	public void calculateMetric() {
		
		int nWordLen, nEntities;
		float fAverage;
		EntityAnalyser enAnalyser;
		SimpleInput simpleInput;
		List<String> valuesList;
		
		//Initialise
		nWordLen = nEntities = 0;
		enAnalyser = new EntityAnalyser();
		
		//Calculate
		try
		{
			for(LanguageInput lan: entityIn.getInputs())
			{
				for(EntityInput enIn: lan.getInputs())
				{
					if(enIn instanceof SimpleInput)
					{
						simpleInput = (SimpleInput)enIn;
						
						valuesList = (List<String>) simpleInput.getValues();
						
						for(String strValue: valuesList)
						{
							nWordLen+= strValue.length();
							nEntities++;
						}
					}
				}
			}
			fAverage = (float) nWordLen/ (float)nEntities;
			metricRet = new FloatMetricValue(fAverage);
			metricRet.setMetricApplied(this);
		}catch(Exception e)
		{
			metricRet = null;
			System.out.println("[EntityWordLen::calculateMetric] Exception while applying the metric");
		}
	}

}
