from os import listdir, makedirs
from os.path import isfile, join, exists, isdir
import json


def getInputFiles(dirPath):
	fList = []
	dirs = [ join(dirPath, dirName) for dirName in listdir(dirPath) if isdir(join(dirPath, dirName)) ]
	if dirs:
		for directory in dirs:
			fList.append([join(directory, f) for f in listdir(directory) if isfile(join(directory, f)) and f[-20:-15] == "input"])
	else:
		fList.append([join(dirPath, f) for f in listdir(dirPath) if isfile(join(dirPath, f)) and f[-20:-15] == "input"])
	return [item for sublist in fList for item in sublist]

def read(filename):
	f =  open(filename, "r")
	if f.mode == 'r':
		return f.readlines()
		
	else:
		print("The file did not open")
	
def getAllUtterancesFromInput(dir):
	uttInput = []
	uttInputs = []
	
	inputFiles = getInputFiles("../convosGen/{}".format(dir))

	for ifl in inputFiles:
		trainingUtt =read(ifl)

		for trU in trainingUtt:
			uttInput.append(trU.strip())
		uttInputs.append(uttInput) 
		uttInput = []
	return uttInputs

def getEntityDict(entitiesFile):

	with open(entitiesFile) as f:
	  entityDict = json.load(f)
	return entityDict


def writeGeneratedUttFile(filename, dir, function, firstLine, utts):
	
	dirAux = "../mutatedConvos/{}/".format(dir)
	if not exists(dirAux):
		makedirs(dirAux)
	dirAux = join(dirAux, function)
	if not exists(dirAux):
		makedirs(dirAux)

	filenameDirAux = join(dirAux, filename)
	f=open(filenameDirAux, "w+")
	f.write(firstLine)
	f.write("\n")
	for utt in utts:
		f.write(utt)
		f.write("\n")

def writeCounter(statsDir, filename, counter, lenght):

	if not exists(statsDir):
		makedirs(statsDir)
	fileDir = join(statsDir, filename)
	f=open(fileDir, "a")
	f.write(str(counter)+"\t")
	f.write(str(lenght)+"\t")
	f.write(str((counter/lenght)*100)+"\n")
