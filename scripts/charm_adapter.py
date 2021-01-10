#!/usr/bin/python

import sys
import os
from auxiliars import *
from mutation_operators import *
from testPhrasesGenerator import *
#frases gramaticalmente correctas: https://stackoverflow.com/questions/10252448/how-to-check-whether-a-sentence-is-correct-simple-grammar-check-in-python


#Checks if the provided mutation operator corresponds with one of the elements that compose an specific mutation operators set.
def isSynonymORAntonym(mutop):
    ret = False;
    if(mutop == MutationOperator.EMutateUtterance or mutop == MutationOperator.EMutAdjectivesToSynonyms or mutop == MutationOperator.EMutAdjectivesToAntonyms or mutop == MutationOperator.EMutAdverbsToSynonyms or mutop == MutationOperator.EMutAdverbsToAntonyms):
        ret = True;    
    return ret;
    	

def synonymsAndAntonyms(mutop, utt, percentage, entitiesIndex, nlp):
    sw = {
	MutationOperator.EMutAdjectivesToSynonyms: convertAdjectivesToSynonyms(utt, percentage, entitiesIndex, nlp),
	MutationOperator.EMutAdjectivesToAntonyms: convertAdjectivesToAntonyms(utt, percentage, entitiesIndex, nlp),
        MutationOperator.EMutObjectsToSynonyms: convertObjectsToSynonyms(utt, percentage, entitiesIndex, nlp),
	MutationOperator.EMutAdverbsToSynonyms: convertAdverbsToSynonyms(utt, percentage, entitiesIndex, nlp),
	MutationOperator.EMutAdverbsToAntonyms: convertAdverbsToAntonyms(utt, percentage, entitiesIndex, nlp),
    }
    return sw.get(mutop, '')	

#Main

eprint('Argument List:', str(sys.argv))
mutedphrase= ""

#read the mutation operator
mutop = str(sys.argv[1]) 

phrase = input('')

#EMutateUtterance: works!
#Adapted
if mutop == MutationOperator.EMutateUtterance and len(sys.argv) == 4:
	nMax = int(sys.argv[2])
	nVar = int(sys.argv[3])
	eprint('[EMutateUtterance] operator');
	eprint('Input phrase [', phrase, ']')
	mutedphrase = mutateUtterance(phrase,'', nMax, nVar)
	eprint('Muted phrase [', mutedphrase, ']')

#EMutateUtteranceDistances
#Adapted
elif mutop == MutationOperator.EMutateUtteranceDistances:
	nMax = int(sys.argv[2])
	nVar = int(sys.argv[3])
	eprint('[EMutateUtteranceDistances] operator');
	eprint('Input phrase [', phrase, ']')
	mutedphrase = mutateUtteranceWithDistances(phrase,'', nMax, nVar, keyboardQWERTYSpanish)
	eprint('Muted phrase [', mutedphrase, ']')
	
#EChangeWordToNumber: works
#Adapted
elif str(sys.argv[1]) == "EChangeWordToNumber":
	#we must redirect the output to not affect the returning results
	devnull = open(os.devnull, 'w')	
	with RedirectStdStreams(stdout=devnull, stderr=devnull):
		mutedphrase = changeWordToNumber(phrase)

	eprint('Muted phrase [', mutedphrase, ']')
	if(mutedphrase == phrase):
		eprint('Warning, the mutedphrase is equivalent to the original one!')

#ETraductionChained: not working -> Forbidden
#Not working yet
elif mutop == MutationOperator.ETraductionChained and len(sys.argv) >= 3:
	eprint('[ETraductionChained] operator');
	eprint('Input phrase [', phrase, ']')
	devnull = open(os.devnull, 'w')	
	with RedirectStdStreams(stdout=devnull, stderr=devnull):	
		mutedphrase = traductionChained(phrase,["de", "pl", "zh"])
	eprint('Muted phrase [', mutedphrase, ']')

#EMutObjectsToSynonyms: works
#Adapted
elif isSynonymORAntonym(mutop) and len(sys.argv) >= 3:
	eprint('Multicheck')
	eprint(mutop)
	nPercentage = int(sys.argv[2])	
	devnull = open(os.devnull, 'w')	

	#Open the nlp library
	with RedirectStdStreams(stdout=devnull, stderr=devnull):
		nlp = stanfordnlp.Pipeline()	

	#call to the specific library
	with RedirectStdStreams(stdout=devnull, stderr=devnull):		
		mutedphrase = synonymsAndAntonyms(mutop, phrase, nPercentage, [0], nlp)
	eprint('Muted phrase [', mutedphrase, ']')

#EMutDeleteChars: works
#Adapted (Be careful with the probability of deleting chars, or the final sentence will be empty)
elif mutop == MutationOperator.EMutDeleteChars and len(sys.argv) >= 4:
	nMax = int(sys.argv[2])
	nVar = int(sys.argv[3])
	eprint('[EDeleteChars] operator');
	eprint('Input phrase [', phrase, ']')
	mutedphrase = deleteChars(phrase,'', nMax, nVar, 0)
	eprint('Muted phrase [', mutedphrase, ']')

#ERamdomTraductionChained
#Not working: 
elif mutop == MutationOperator.ERandomTraductionChained and len(sys.argv) >= 2:
	eprint('[ERamdomTraductionChained] operator');
	nLang = int(sys.argv[2])
	mutedphrase = randomTraductionChained(phrase, nLang)

#EMutActiveToPassive
#It doesnt work very fine...
elif mutop == MutationOperator.EMutActiveToPassive:
	eprint('[EMutActiveToPassive] operator');

	#Open the nlp library
	devnull = open(os.devnull, 'w')
	with RedirectStdStreams(stdout=devnull, stderr=devnull):
		nlp = stanfordnlp.Pipeline()	

	with RedirectStdStreams(stdout=devnull, stderr=devnull):
		mutedphrase = activeToPassive(phrase, nlp)
	
else: 
	print("Warning! The selected operator is not currently supported: ", mutop);


eprint('End of story')
print(mutedphrase)
