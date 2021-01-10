#!/usr/bin/python
from enum import Enum
#Enum class for the mutation operators
class MutationOperator(str, Enum):
    EMutateUtterance = "EMutateUtterance"
    EChangeWordToNumber = "EChangeWordToNumber"	
    EMutAdjectivesToSynonyms = "EMutAdjectivesToSynonyms"
    EMutAdjectivesToAntonyms = "EMutAdjectivesToAntonyms"
    EMutObjectsToSynonyms = "EMutObjectsToSynonyms"
    EMutAdverbsToSynonyms = "EMutAdverbsToSynonyms"
    EMutAdverbsToAntonyms = "EMutAdverbsToAntonyms"
    EMutDeleteChars = "EMutDeleteChars"
    EMutateUtteranceDistances = "EMutateUtteranceDistances"	
    ETraductionChained = "ETraductionChained"
    ERandomTraductionChained = "ERandomTraductionChained"
    EMutActiveToPassive = "EMutActiveToPassive"
    EMutPassiveToActive = "EMutPassiveToActive"
