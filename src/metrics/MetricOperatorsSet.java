package metrics;

import java.util.Iterator;
import java.util.LinkedList;

import metrics.base.Metric;

public class MetricOperatorsSet {

	//General list of metric operators
	LinkedList<Metric> operatorList;
	
	
	//Bot metrics
	LinkedList<Metric> botMetricsList;
	
	//Global FlowMetrics
	LinkedList<Metric> globalFlowMetricsList;
	
	//Analyse FlowMetrics
	LinkedList<Metric> flowMetricsList;
	
	//Entity metrics
	LinkedList<Metric> entityMetricsList;
	
	//Intent metrics
	LinkedList<Metric> intentMetricsList;
	
	//Iterator
	Iterator<Metric> itList, itBotMetric, itFlowMetric, itEntityMetric, itIntentMetric;
	public MetricOperatorsSet()
	{
		operatorList =  botMetricsList = globalFlowMetricsList = flowMetricsList = entityMetricsList = intentMetricsList = null;
		itIntentMetric = itList = itBotMetric = itFlowMetric = itEntityMetric = null;
	}
	
	public void insertMetric(Metric metricIn)
	{		
		try
		{
			switch(metricIn.getCategory())
			{
			case eBot:
				if(botMetricsList == null)
					botMetricsList = new LinkedList<Metric>();
				botMetricsList.add(metricIn);
				break;
			case eGlobalFlow:
				break;
			case eEntity:
				if(entityMetricsList == null)
					entityMetricsList = new LinkedList<Metric>();
				entityMetricsList.add(metricIn);					
				break;
			case eFlow:
				if(flowMetricsList == null)
					flowMetricsList = new LinkedList<Metric>();
				flowMetricsList.add(metricIn);				
				break;
			case eIntent:
				if(intentMetricsList == null)
					intentMetricsList = new LinkedList<Metric>();
				intentMetricsList.add(metricIn);				
				break;
				//TODO: Create rest of 
			default:
				break;
			}
			//Add to the general list
			if(operatorList == null)
				operatorList = new LinkedList<Metric>();
			operatorList.add(metricIn);
			
			resetIndex();
		}catch(Exception e)
		{
			System.out.println("[MetricOperator::insertMetric] - Exception while inserting a metric");
		}
	}
	public void resetIndex() {
		if(operatorList != null)
			itList = operatorList.iterator();
		
		if(botMetricsList != null)
			itBotMetric =botMetricsList.iterator(); 
		
		if(entityMetricsList != null)
			itEntityMetric = entityMetricsList.iterator();
		
		if(flowMetricsList != null)
			itFlowMetric = flowMetricsList.iterator();
		
		if(intentMetricsList != null)
			itIntentMetric = intentMetricsList.iterator();
	}
	public boolean hasNext() {
		return itList!= null ? itList.hasNext() : false;
	}
	public Metric getNext() {
		return itList!= null ? itList.next() : null;
	}
	public boolean hasNextBotMetric() {
		return itBotMetric!= null ? itBotMetric.hasNext() : false;
	}	
	public boolean hasNextFlowMetric() {
		return itFlowMetric!= null ? itFlowMetric.hasNext() : false;
	}		
	public boolean hasNextEntityMetric() {
		return itEntityMetric!= null ? itEntityMetric.hasNext() : false;
	}
	public boolean hasNextIntentMetric() {
		return itIntentMetric!= null ? itIntentMetric.hasNext() : false;
	}	
	public Metric getNextBotMetric() {
		return itBotMetric!= null ? itBotMetric.next() : null;
	}
	public Metric getNextIntentMetric()
	{
		return itIntentMetric!= null ? itIntentMetric.next() : null;
	}
	public Metric getNextFlowMetric() {
		return itFlowMetric!= null ? itFlowMetric.next() : null;
	}

	public Metric getNextEntityMetric() {
		return itEntityMetric!= null ? itEntityMetric.next() : null;
	}

}
