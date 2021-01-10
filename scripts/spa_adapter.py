#!/usr/bin/python
import sys
import spacy
from pass2act.pass2act import pass2act
import pattern.en
from mutation_operators import *
from auxiliars import *
from contextlib import redirect_stdout
from enum import Enum

eprint('Argument List:', str(sys.argv))

mutedphrase= ""

#read the mutation operator
mutop = str(sys.argv[1]) 
phrase = input('')

#ETraductionChained: not working -> Forbidden
if mutop == MutationOperator.EMutPassiveToActive:
	eprint('[spacy_core::EMutPassiveToActive] operator')
	eprint('Input phrase [', phrase, ']')
	devnull = open(os.devnull, 'w')
#	with RedirectStdStreams(stdout=devnull, stderr=devnull):
	nlp = spacy.load('en')
	prev = ''
	acts = ''	
	prev = phrase
	mutedphrase = pass2act(phrase)	

	eprint('Muted phrase [', mutedphrase, ']')

eprint('Enf of story')

print(mutedphrase)
