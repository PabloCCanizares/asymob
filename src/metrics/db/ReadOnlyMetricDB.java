package metrics.db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import generator.Entity;
import generator.Intent;
import generator.UserInteraction;
import metrics.base.MetricValue;

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
		return this.mdb.getIntentMap();
	}
}
