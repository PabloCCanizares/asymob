package metrics.db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import generator.Entity;
import generator.Intent;
import generator.UserInteraction;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;

public class ReadOnlyMetricDB {

	MetricDataBase mdb;
	public ReadOnlyMetricDB(MetricDataBase mdb)
	{
		this.mdb = mdb;
	}
	public HashMap<Entity, List<MetricValue>> getEntityMap() {
		return this.mdb.getEntityMap();
	}

	public LinkedList<MetricValue> getBotMetrics() {
		return this.mdb.getBotMetrics();
	}
	public HashMap<UserInteraction, List<MetricValue>> getFlowMap() {
		// TODO Auto-generated method stub
		return this.mdb.getFlowMap();
	}
	public HashMap<Intent, List<MetricValue>> getIntentMap() {		
		return mdb == null ? null : mdb.getIntentMap();
	}
	public LinkedList<MetricValue> getEntityMetric(EMetricOperator eMetricIn) {
		return mdb == null ? null : mdb.getEntityMetric(eMetricIn);
	}
	public LinkedList<MetricValue> getFlowMetric(EMetricOperator eMetricIn) {
		return mdb == null ? null : mdb.getFlowMetric(eMetricIn);
	}
	public LinkedList<MetricValue> getIntentMetric(EMetricOperator eMetricIn) {
		return mdb == null ? null : mdb.getIntentMetric(eMetricIn);
	}
	
}
