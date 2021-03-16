package metrics.operators.base;

import generator.Entity;
import generator.Intent;
import metrics.base.EMetricCategory;
import metrics.base.Metric;
import metrics.operators.EMetricOperator;

public abstract class EntityMetricBase extends Metric{

	protected Entity entityIn;
	public EntityMetricBase(EMetricOperator metric) {
		super(metric, EMetricCategory.eEntity);
	}

	public void configure(Entity entityIn)
	{
		this.entityIn = entityIn;
	}

}