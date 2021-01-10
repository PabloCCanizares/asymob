#!/usr/bin/python
#otros traductores: https://huggingface.co/transformers/model_doc/marian.html

import sys
import os
import apertium;
from mutation_operators import *
from auxiliars import *
from contextlib import redirect_stdout
from enum import Enum

def filterWord(utt):
	utt = utt.replace("*", '')
	utt = utt.replace("@", '')
	utt = utt.replace("#", '')
	utt = utt.replace("  ", '')
	return utt

def translateUtt(utt, languages):
	#The main problem here, is related with the set of dictionaries, which are limited. 
	spanish = apertium.translate('en', 'spa', utt)
	eprint(spanish)
	french = apertium.translate('spa', 'fr', filterWord(spanish))
	eprint(french)	
	spanish = apertium.translate('fr', 'spa', filterWord(french))
	eprint(spanish)	
	eng = apertium.translate('spa', 'en', filterWord(spanish))


	return filterWord(eng);



eprint('Argument List:', str(sys.argv))

mutedphrase= ""

#read the mutation operator
mutop = str(sys.argv[1]) 
phrase = input('')

#ETraductionChained: not working -> Forbidden
if mutop == MutationOperator.ETraductionChained and len(sys.argv) >= 3:
	eprint('[apertium_core::ETraductionChained] operator')
	eprint('Input phrase [', phrase, ']')
	devnull = open(os.devnull, 'w')
	#with RedirectStdStreams(stdout=devnull, stderr=devnull):	
	mutedphrase = translateUtt(phrase, ['spa', 'en'])
	eprint('Muted phrase [', mutedphrase, ']')


eprint('Enf of story')

print(mutedphrase)
