package metrics.operators;

public enum EMetricOperator {

	//Global
	eNumEntities, eNumPhrases, eAverageLength, eSentiment, eAvgParameters, 
	eNumLanguages, eNumIntents, eNumFlows, eNumPaths, eGlobalAveragePathFlow,
	eGlobalAvgEntityLiterals,  eGlobalAvgEntitySynonyms, eGlobalEntityWordLen,
	eGlobalFlowAvgActions, eGlobalAvgIntentPhrases, eGlobalAvgIntentWordPerPhrase,
	eGlobalAvgIntentParameters, eGlobalAvgIntentCharsPerPhrase, eGlobalAvgReadingTime, 
	eGlobalAvgReqIntentParameters, eBotSentiment, eBotConfusingPhrases, eGlobalAvgIntentVerbPerPhrase, 
	eGlobalAvgIntentCharsPerOutputPhrase, 
	
	//Entities
	eEntityAvgSynonyms, eEntityWordLen, eEntityNumLiterals,
	
	//Flows
	eFlowNumPaths, eFlowLength, eFlowActionsAverage,
	
	//Intents
	eIntentNumPhrases, eIntentAvgWordsPerPhrase, eIntentAvgCharsPerPhrase, 
	eIntentNumParameters, eIntentAvgNounsPerPhrase, eIntentAvgVerbsPerPhrase,
	eIntentSentiment,eIntentMaxWordLen, eReadabilityMetrics, eIntentAvgCosineSimilarity, 
	eIntentNumReqParameters, eIntentAvgCharsPerOutputPhrase,
	
	//Extended
	eExtended,              
}
