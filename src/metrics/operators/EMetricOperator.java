package metrics.operators;

public enum EMetricOperator {

	//Global
	eNumEntities, eNumPhrases, eAverageLength, eSentiment, eAvgParameters, 
	eNumLanguages, eNumIntents, eNumFlows, eNumPaths, eGlobalAveragePathFlow,
	eGlobalAvgEntityLiterals,  eGlobalAvgEntitySynonyms, eGlobalEntityWordLen,
	eGlobalFlowAvgActions, eGlobalAvgIntentPhrases, eGlobalAvgIntentWordPerPhrase, eGlobalMaxFlowLength,
	eGlobalAvgIntentParameters, eGlobalAvgIntentCharsPerPhrase, eGlobalAvgReadingTime, 
	eGlobalAvgReqIntentParameters, eBotTrainingSentiment, eBotOutputSentiment, eBotConfusingPhrases, eGlobalAvgIntentVerbPerPhrase, 
	eGlobalAvgIntentCharsPerOutputPhrase, eGlobalAvgIntentVerbsPerOutputPhrase, eGlobalAvgRegularExpressions, eGlobalFleschReadingEasyScore,
	
	//Entities
	eEntityAvgSynonyms, eEntityWordLen, eEntityNumLiterals,
	
	//Flows
	eFlowNumPaths, eFlowLength, eFlowActionsAverage,
	
	//Intents
	eIntentNumPhrases, eIntentAvgWordsPerPhrase, eIntentAvgCharsPerPhrase, 
	eIntentNumParameters, eIntentAvgNounsPerPhrase, eIntentAvgVerbsPerTPhrase,
	eIntentMaxWordLen, eReadabilityMetrics, eIntentAvgCosineSimilarity, eIntentOutputSentiment, 
	eIntentNumReqParameters, eIntentAvgCharsPerOutputPhrase, eIntentTrainingSentiment, eIntentAvgVerbsPerOutputPhrase,
	eIntentNumRegularExpressions, eFleschReadingEasyScore,
	
	//Extended
	eExtended,                    
}
