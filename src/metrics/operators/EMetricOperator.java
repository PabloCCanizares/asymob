package metrics.operators;

public enum EMetricOperator {

	//Global
	eNumEntities, eNumPhrases, eAverageLength, eSentiment, eAvgParameters, 
	eNumLanguages, eNumIntents, eNumFlows, eNumPaths, eAveragePathFlow,
	eGlobalAvgEntityLiterals,  
	//Entities
	eEntityAvgSynonyms, eEntityWordLen, eEntityNumLiterals,
	//Flows
	eFlowNumPaths, eFlowLength, eFlowActionsAverage,
	//Intents
	eIntentNumPhrases, eIntentAvgWordsPerPhrase, eIntentAvgCharsPerPhrase, 
	eIntentNumParameters, eIntentAvgNounsPerPhrase, eIntentAvgVerbsPerPhrase,
	ePositiveSentiment,eIntentMaxWordLen, eReadabilityMetrics, 
	//Extended
	eExtended,  
	
	//TODO: Incluir el resto
}
