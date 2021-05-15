package metrics.operators;

public enum EMetricOperator {

	//Global
	eNumEntities, eNumPhrases, eAverageLength, eSentiment, eAvgParameters, 
	eNumLanguages, eNumIntents, eNumFlows, eNumPaths, eGlobalAveragePathFlow,
	eGlobalAvgEntityLiterals,  eGlobalAvgEntitySynonyms, eGlobalEntityWordLen,
	eGlobalFlowAvgActions, eGlobalAvgIntentPhrases, eGlobalAvgIntentWordPerPhrase, eGlobalMaxFlowLength,
	eGlobalAvgIntentParameters, eGlobalAvgIntentCharsPerPhrase, eGlobalAvgReadingTime, 
	eGlobalAvgReqIntentParameters, eBotTrainingSentiment, eBotOutputSentiment, eBotConfusingPhrases, eGlobalAvgIntentVerbPerPhrase, 
	eGlobalAvgIntentCharsPerOutputPhrase, 
	
	//Entities
	eEntityAvgSynonyms, eEntityWordLen, eEntityNumLiterals,
	
	//Flows
	eFlowNumPaths, eFlowLength, eFlowActionsAverage,
	
	//Intents
	eIntentNumPhrases, eIntentAvgWordsPerPhrase, eIntentAvgCharsPerPhrase, 
	eIntentNumParameters, eIntentAvgNounsPerPhrase, eIntentAvgVerbsPerPhrase,
	eIntentMaxWordLen, eReadabilityMetrics, eIntentAvgCosineSimilarity, eIntentOutputSentiment, 
	eIntentNumReqParameters, eIntentAvgCharsPerOutputPhrase, eIntentTrainingSentiment,
	
	//Extended
	eExtended,                
}
