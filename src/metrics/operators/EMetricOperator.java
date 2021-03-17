package metrics.operators;

public enum EMetricOperator {

	eNumEntities, eNumPhrases, eAverageLength, eSentiment, eAvgParameters, eExtended,
	eNumLanguages, eNumIntents, eNumFlows, eNumPaths, eAveragePathFlow,
	//Entities
	eAverageSynonyms, eEntityWordLen, eNumLiterals,
	//Flows
	eFlowNumPaths, eFlowLength, eFlowActionsAverage,
	//Intents
	eIntentNumPhrases
	
	//TODO: Incluir el resto
}
