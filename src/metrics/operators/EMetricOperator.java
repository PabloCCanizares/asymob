package metrics.operators;

public enum EMetricOperator {

	//Global
	eNumEntities, eNumPhrases, eAverageLength, eSentiment, eAvgParameters, 
	eNumLanguages, eNumIntents, eNumFlows, eNumPaths, eAveragePathFlow,
	//Entities
	eAverageSynonyms, eEntityWordLen, eNumLiterals,
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
