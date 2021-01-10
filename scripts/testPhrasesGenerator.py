from uttInputReader import *
from random import randint, choice, choices, sample
from string import printable, whitespace
#from googletrans import Translator, LANGUAGES #pip install googletrans
#from translate import Translator  # PIP INSTALL TRANSLATE
import inflect
from word2number import w2n
from yandex.Translater import Translater

import stanfordnlp
#from pattern.en import *
#from PyDictionary import PyDictionary
import requests
from bs4 import BeautifulSoup
from operator import itemgetter
from os import remove
from maestral.cli import catch_maestral_errors

import nltk 
from nltk.corpus import wordnet 

nltk.download('wordnet', quiet=True)

keyboardQWERTYSpanish = [[['1','!'],['2','"'],['3','·'],['4','$'],['5','%'],['6','&'],['7','/'],['8','('],['9',')'],['0','='],['\'','?'],['¡','¿']],
                        [['q','Q'],['w', 'W'],['e','E'],['r','R'],['t','T'],['y','Y'],['u','U'],['i','I'],['o','O'],['p','P'],['`','^'],['+','*']],
                        [['a','A'],['s','S'],['d','D'],['f','F'],['g','G'],['h','H'],['j','J'],['k','K'],['l','L'],['ñ','Ñ'],['\'','̈́'],['ç','Ç']],
                        [['<', '>'],['z','Z'],['x','X'],['c','C'],['v','V'],['b','B'],['n','N'],['m','M'],[',',';'],['.',':'],['-','_']]]

"""
This function will return the keys that are surrounding the key passed.
For doing this we have to search the index of the key, and return all the keys that surround it in the matrix
"""
def getNearbyKeys(key, keyboardConfig):
    
    nbkeys = []

    def getKeyIndex(key, keyboardConfig):
        for i, row in enumerate(keyboardConfig):
            for j, keys in enumerate(row):
                if key in keys:
                    return i, j

    i, j = getKeyIndex(key, keyboardConfig)

    if i == 0: # linea 0
        if j==0: #izquierda linea 0
            nbkeys = [keyboardConfig[i][j+1], keyboardConfig[i+1][j+1], keyboardConfig[i+1][j]]
#            for elem in [keyboardConfig[i][j-1], keyboardConfig[i+1][j-1]]:
#                nbkeys.remove(elem)
        elif j==len(keyboardConfig[i])-1: # derecha linea 0
            nbkeys = [keyboardConfig[i+1][j], keyboardConfig[i+1][j-1], keyboardConfig[i][j-1]]
#            for elem in [keyboardConfig[i][j+1], keyboardConfig[i+1][j+1]]:
#                nbkeys.remove(elem)
        else:
            nbkeys = [keyboardConfig[i][j+1], keyboardConfig[i+1][j+1], keyboardConfig[i+1][j], keyboardConfig[i+1][j-1], keyboardConfig[i][j-1]]

    elif (i > 0 and i < 3): # lineas 1 o 2
        if j==0: #izquierda linea 1 o 2
            nbkeys = [keyboardConfig[i-1][j], keyboardConfig[i-1][j+1], keyboardConfig[i][j+1], 
                keyboardConfig[i+1][j+1], keyboardConfig[i+1][j]]
#            for elem in [keyboardConfig[i][j-1], keyboardConfig[i-1][j-1], keyboardConfig[i+1][j-1]]:
#                nbkeys.remove(elem)
        elif j==len(keyboardConfig[i])-1: # derecha linea 1 o 2 
            nbkeys = [keyboardConfig[i-1][j-1], keyboardConfig[i-1][j], keyboardConfig[i+1][j-1], keyboardConfig[i][j-1]]
#            for elem in [keyboardConfig[i][j+1], keyboardConfig[i-1][j+1], keyboardConfig[i+1][j+1]]:
#                nbkeys.remove(elem)
        else:
            nbkeys = [keyboardConfig[i-1][j-1], keyboardConfig[i-1][j], keyboardConfig[i-1][j+1], keyboardConfig[i][j+1], 
                        keyboardConfig[i+1][j+1], keyboardConfig[i+1][j], keyboardConfig[i+1][j-1], keyboardConfig[i][j-1]]
    else: # linea 3

        if j==0: #izquierda linea 3
            nbkeys = [keyboardConfig[i-1][j], keyboardConfig[i-1][j+1], keyboardConfig[i][j+1]]
#            for elem in [keyboardConfig[i][j-1], keyboardConfig[i-1][j-1]]:
#                nbkeys.remove(elem)
        elif j==len(keyboardConfig[i])-1: # derecha linea 3
            nbkeys = [keyboardConfig[i-1][j-1], keyboardConfig[i-1][j], keyboardConfig[i][j-1]]
#            for elem in [keyboardConfig[i][j+1], keyboardConfig[i-1][j+1]]:
#                nbkeys.remove(elem)
        else:
            
            nbkeys = [keyboardConfig[i-1][j-1], keyboardConfig[i-1][j], keyboardConfig[i-1][j+1], keyboardConfig[i][j+1], keyboardConfig[i][j-1]]
    

    return nbkeys

"""
     This function will mutate any of the letters of the utterance for any
     Other key which distance of the original letter is the correspondant to the variable 
     passed as an argument 

     To know the configuration of the keyboard, we will need the configuration of the keyboard aswell, 
     and a way to see the distances between keys (we could create a matrix)
"""

def mutateUtteranceWithDistances(utt, botDir, percentage=10, variability=0, keyboardConfig=keyboardQWERTYSpanish):
    counter = 0
    for pos, letter in enumerate(utt):
        if not(letter.isspace()):
            mutate = choices([True, False], [percentage/100, 1-percentage/100])[0]
            if mutate:
                uttAux = list(utt)
                i = randint(0, 1) # we choose between the shifted keyboard or normal one
                uttAux[pos] = choice(getNearbyKeys(letter, keyboardConfig))[i]
                utt = "".join(uttAux)
                if percentage+variability > 1:
                    percentage += variability
                counter += 1
    statsDir = "/localSpace/chatbots/Charm/proyecto/codigo/output/{}/".format(botDir)
    filename = "mutateUtteranceWithDistancesStats.txt"
    writeCounter(statsDir, filename, counter, len(utt))
    return utt

"""
    This function will mutate any of the letters of the utterance for any
    Other letter
    parameters: 
        utt: the utterance that is going to be muted
        percentage: the percentage of mutation
        variability: the value that the percentage will increase if there is a mutation
"""
def mutateUtterance(utt, botDir, percentage=10, variability=0):
    counter = 0
    test = printable
    keysAscii = list(test.strip(whitespace))
    for position, letter in enumerate(utt):
        if not(letter.isspace()):
            mutate = choices([True, False], [percentage/100, 1-percentage/100])[0]
            if mutate:
                uttAux = list(utt)
                uttAux[position] = choice(keysAscii)
                utt = "".join(uttAux)
                if percentage > 1:
                    percentage += variability
                counter += 1
    statsDir = "/localSpace/chatbots/Charm/proyecto/codigo/output/{}/".format(botDir)
    filename = "mutateUtteranceStats.txt"
    writeCounter(statsDir, filename, counter, len(utt))
    return utt

"""
    This function will delete any of the letters of the utterance if choiced randomly
    parameters: 
        utt: the utterance that is going to be muted
        percentage: the percentage of mutation
        variability: the value that the percentage will increase if there is a mutation
"""
def deleteChars(utt, botDir, percentage=10, variability=0, writeDisk=1):
    position = 0
    counter = 0
    lengthUtt = len(utt)
    for i in range(lengthUtt):
        mutate = choices([True, False], [percentage/100, 1-percentage/100])[0]
        if mutate:
            uttAux = list(utt)
            if position==0:
                uttAux = uttAux[position+1:] 
            elif position>=len(uttAux)-1:
                uttAux = uttAux[:position+1]
            else:
                uttAux = uttAux[:position]+uttAux[position+1:]  
            utt = "".join(uttAux)
            print(utt)		
            if percentage > 1:
                percentage += variability
            counter += 1
        else: 
            position += 1
    print("Eh q pasa")
    print(utt)	
    if(writeDisk>0):
        statsDir = "/localSpace/chatbots/Charm/proyecto/codigo/output/{}/".format(botDir)
        filename = "deleteCharsStats.txt"
        writeCounter(statsDir, filename, counter, lengthUtt)

    return utt

"""
    This method will change each number of the utterance to a word 
"""
def changeNumberToWord(utt):
    words = []
    p = inflect.engine()
    nums = [int(word) for word in utt.split() if word.isdigit()]
    if nums:
        for num in nums:
            words.append(p.number_to_words(num))
        i=0
        for word in utt.split():
            if word.isdigit():
                utt = utt.replace(word, words[i], 1)
                i+=1
    return utt

"""
    This method will change each word that means a number to a number
"""
def changeWordToNumber(utt): # TO FIX: for large numbers, it recognizes one by one "two thousands twenty 1"

    def isNum(word):
        try:
            w2n.word_to_num(word)
        except ValueError:
            return False
        return True 

    uttAux = utt.split()
    numbersDict = {}
    numJoin = 0
    numbers=[]
    posDel=[]

    print("new iteration", utt)
    for i, word in enumerate(uttAux):
        if isNum(word):
            if not numJoin in numbersDict.keys():
                numbersDict[numJoin] = []
            numbersDict[numJoin].append((i, word))
            print("\nuttAux i: ", uttAux[i], " len: ", len(uttAux), ", i: ", i)

            if i+1 < len(uttAux): 
                if  uttAux[i+1] == "and":        
                    numbersDict[numJoin].append((i+1, uttAux[i+1]))
                if not isNum(uttAux[i+1]) and uttAux[i+1] != "and":
                    numJoin += 1
    for value in numbersDict.values():
        number = " ".join([elem[1] for elem in value])
        numbers.append((value[0][0], number))
        if len(value) > 1: 
            posDel = [elem[0] for elem in value[1:]]

    if numbers:
        utt = utt.split()
        for i, num in enumerate(numbers):
            utt[num[0]] = str(w2n.word_to_num(num[1])) 
    if posDel:
        utt = [word for i, word in enumerate(utt) if not i in posDel]
    
    if isinstance(utt, str):
        return utt
    else:
        return " ".join(utt) 

"""
     This function will submit the utterance to a traduction chain that will change the structure of the utterance 
    and will simplify it normally.
"""
# TIENEN UN MAXIMO DE TRADUCCIONES PERMITIDAS, VER SI HAY ALGUNO GRATUITO QUE PERMITA HACER MÁS TRADUCCIONES.

def traductionChained(utt, languages):
    
    tr = Translater()
    tr.set_key('trnsl.1.1.20200203T095917Z.30bf7af5cf091999.513bc5df39c8c45fd3cc2baa0ee4c8af7669202a')
    tr.set_text(utt)
    originLang = tr.detect_lang()
    tr.set_from_lang(originLang)
    tr.set_to_lang(languages[0])
    tr.set_text(utt)
    utt = tr.translate()

    for i, lan in enumerate(languages):    
        tr.set_from_lang(lan)
        if i < len(languages)-1:
            tr.set_to_lang(languages[i+1])
        else:
            tr.set_to_lang(originLang)

        utt = tr.translate()
        tr.set_text(utt)

    #print(utt)
    return utt
#    translator = Translator()
#    oriLang = translator.detect(utt)
#    return

#    lanAux = originLang
#
#    for language in languages:
#        translator= Translator(from_lang=lanAux, to_lang=language)
#        utt = translator.translate(utt)
#        lanAux = language
#
#    translator = Translator(from_lang=lanAux, to_lang=originLang) # We convert it to the original language    
#    utt = translator.translate(utt)
#
#    return utt.replace("&#39;", "'")

def randomTraductionChained(utt, numLanguages=2):
    languagesSupported = ["az","ml","sq","mt","am","mk","en","mi","ar","mr","hy","mhr","af","mn","eu","de","ba","ne","be","no","bn","pa","my","pap","bg","fa","bs","pl","cy","pt","hu","ro","vi","ru","ht","ceb","gl","sr","nl","si","mr","sk","el","sl","ka","sw","gu","su","da","tg","he","th","yi","tl","id","ta","ga","tt","it","te","is","tr","es","udm","kk","uz","kn","uk","ca","ur","ky","fi","zh","fr","ko","hi","xh","hr","km","cs","lo","sv","la","gd","lv","et","lt","eo","lb","jv","mg","ja","ms"]
    languages = sample(languagesSupported, numLanguages)
    return traductionChained(utt, languages)
#    languages = sample(LANGUAGES.keys(), numLanguages)
#    translator = Translator()
#    oriLang = translator.detect(utt).lang
#
#    languages.insert(0, oriLang)
#    languages.append(oriLang)
#
#    print(languages)
#    print(utt)
#
#    for ind, language in enumerate(languages):
#        if not(ind == len(languages)-1):
#            print(language, languages[ind+1])
#            utt = translator.translate(utt, src=language, dest=languages[ind+1]).text
#            print(utt)
#    print("\n______________________________________\n")
#
#    return
# Cuarentena SERIA---------------------------------------------------------------------------------------------------------------------------

def activeToPassive(utt, nlp):

    def findDependencyBtWords(index, indexWordsDict, doc, listAux):

        for key in indexWordsDict.keys():
            print("-------------------------------------")
            print(key == index, key, index)
            print("-------------------------------------")
            if str(key) == str(index):
                for value in indexWordsDict[key]:
                    listAux.append(doc.sentences[0].words[int(value)-1])
                    findDependencyBtWords(doc.sentences[0].words[int(value)-1].index, indexWordsDict, doc, listAux)
    #                print("-------------------------------------")
    #                print(key, index, doc.sentences[0].words[int(value)-1])
    #                print("-------------------------------------")
        return []

    def findDirectGovernors(index, indexWordsDict, doc):
        listAux = {}    
        for key in indexWordsDict.keys():
            if str(key) == str(index):
                for value in indexWordsDict[key]:
                    listAux.append(doc.sentences[0].words[int(value)-1])


    prueba = False
    indexObj = -1
    indexRoot = -1
    negative = False
    indexWordsDict = {}

    
    doc = nlp(utt)
    if prueba == True:
        return str(doc.sentences[0].words) # For examples

    # this par corresponds to the first 3 steps
        # get the index of the object
        # also de dependencies of each word
    for word in doc.sentences[0].words:
        print(word)
        if not word.governor in indexWordsDict.keys():
            indexWordsDict[word.governor] = []
        indexWordsDict[word.governor].append(word.index) # De esta manera nos quedamos con las palabras relacionadas con el objeto
        if word.dependency_relation == 'obj':
            indexObj = word.index
        elif word.dependency_relation == 'root':
            indexRoot = word.index
        elif word.lemma    == 'not':
            negative = True
    # Collect all the words that depends on the object and sort them
    if not (indexObj == -1 or indexRoot == -1):
        for key in indexWordsDict.keys():
            if str(key) == indexObj: 
                wordsRelatedWithObj = []
                for value in indexWordsDict[key]:
                    wordsRelatedWithObj.append(doc.sentences[0].words[int(value)-1])
                    findDependencyBtWords(doc.sentences[0].words[int(value)-1].index, indexWordsDict, doc, wordsRelatedWithObj)
                wordsRelatedWithObj.append(doc.sentences[0].words[int(indexObj)-1])    
                uttPassive = [word.text for word in sorted(wordsRelatedWithObj, key=lambda tup: tup.index)]
                print("uttPassive: "+ str(uttPassive))

        # This part correspond to the 4th step
        # get the verb in active voice and the auxiliars of the verb that may have information about the tense of it
        

        negative = False
        thereIsAux = False 
        wordsRelatedWithVerb = []
        isThereTense = doc.sentences[0].words[int(indexRoot)-1].feats.find('Tense')
        findDependencyBtWords(indexRoot, indexWordsDict, doc, wordsRelatedWithVerb)
        if isThereTense != -1:
            feats = doc.sentences[0].words[int(indexRoot)-1].feats
            if [word.feats for word in wordsRelatedWithVerb if (word.lemma).find('not')]:
                negative = True 
        else:
            feats = "".join([word.feats for word in wordsRelatedWithVerb if (word.feats).find('Tense')])
            thereIsAux = True
        print(feats)
        tenseVerb = "".join([feats[6:] for feats in feats.split('|') if feats.find('Tense')!=-1])
        print("\n__________________________________________\n", "tense: ", tenseVerb, "words related with the verb", str([word.lemma for word in wordsRelatedWithVerb]), "\n__________________________________________\n ")


        
        """
        Conjugations of the verb 'to be'

        Tense: Indicative
        Mood: Present, Preterite, Present Continuous, Present Perfect, Future, 
        Future Perfect, Past Continuous, Past Perfect, Future Continuous, Present 
        perfect continuous, Past perfect continuous, Future perfect continuous
        
        Tense: Imperative
        Tense: Participle Mood: Present, Past
        Tense: Infinitive
        Tense: Perfect Participle

        """
        if thereIsAux: # se pone todos los verbos auxiliares y después verbo to be en el tense que sea, si es infinitivo se añade 'to'
            return utt
        else: # si es negativo sería añadir detrás de la conjugación del verbo be añadir not 
            if negative:
                return utt
            return utt

        """print("\n*****************\n", conjugate(doc.sentences[0].words[int(indexRoot)-1], tense=PAST+PARTICIPLE, parse=True),"\n*****************\n ")"""
         
    

    # In this case we cannot convert the phrase into passive
    return utt
    #return str([str(doc.sentences[0].tokens), "\n", str(doc.sentences[0])])
    

def passiveToActive(utt):
    return

# FIN DE CUARENTENA

"""
This function will change all the adjectives of a utt to synonims.
First we have to get all the adjectives that the oration has.
Then we will find a synonim.
We can create as many orations as synonyms are for every adjectives and we can combine them.

"""

def synonyms(term):
#    Deprecated: it doesnt work anymore
#    response = requests.get('http://www.thesaurus.com/browse/{}'.format(term))
#    soup = BeautifulSoup(response.text, 'html')
#    section = soup.findAll('ul')[5] ## I dont think this is such a great idea, i have to see this better
#    return [span.text for span in section.findAll('span')]
    synonyms = [] 
 	
    for syn in wordnet.synsets(term): 
        for l in syn.lemmas(): 
            synonyms.append(l.name()) 

    synonymRet = list(dict.fromkeys(synonyms))
    print(synonymRet)
    return synonymRet

def antonyms(term):
 #   response = requests.get('http://www.thesaurus.com/browse/{}'.format(term))
 #   soup = BeautifulSoup(response.text, 'html')
 #   section = soup.findAll('ul')[6]
 #   return [span.text for span in section.findAll('span')]
    synonyms = [] 
    antonyms = [] 
      
    for syn in wordnet.synsets(term): 
        for l in syn.lemmas(): 
            synonyms.append(l.name()) 
            if l.antonyms(): 
                antonyms.append(l.antonyms()[0].name()) 
    
    antonymRet = list(dict.fromkeys(antonyms))
    return antonymRet

def convertAdjectivesToSynonyms(utt, percentage, entitiesIndex, nlp): 

    def isAmod(word):

        try:
            doc = nlp(str(word))
            
            for wordAux in doc.sentences[0].words:
#                print(wordAux.upos)
                if wordAux.upos == 'ADJ':
                    return True
            return False
        except:
            return False

    synonymsList = []
    adjIndex = []
    #discountIndex = 1
    utt = utt.split(' ')

    for i, untknWord in enumerate(utt):
#        print(untknWord)
        if isAmod(untknWord) and not i in entitiesIndex:
            synonymsList.append(synonyms(untknWord))
            adjIndex.append(i)

#    print("synonymsList: ", synonymsList)

    if synonymsList:
        if synonymsList[0]:
#            print("adjIndex: ", adjIndex)
            for count, i in enumerate(adjIndex):
                if synonymsList[count]:
                    randomN = randint(0, len(synonymsList[count])-1)
                    changeSin = choices([True, False], [percentage/100, 1-percentage/100])[0]
#                    print("randomN: ", randomN, "count: ", count)
#                    print("utt: ", utt)
                    if changeSin:
                        utt[i] = synonymsList[count][randomN]

    return " ".join(utt)

def convertAdjectivesToAntonyms(utt, percentage, entitiesIndex, nlp): 

    def isAmod(word):

        try:    
            doc = nlp(str(word))
        except:
            return False
        for wordAux in doc.sentences[0].words:
    #        print(wordAux.upos)
            if wordAux.upos == 'ADJ':
                return True
        return False

    antonymsList = []
    adjIndex = []
    #discountIndex = 1
    utt = utt.split(' ')

    for i, untknWord in enumerate(utt):
#        print(untknWord)
        if isAmod(untknWord) and not i in entitiesIndex:
            antonymsList.append(antonyms(untknWord))
            adjIndex.append(i)

#    print("antonymsList: ", antonymsList)

    if antonymsList:
        if antonymsList[0]:
#            print("adjIndex: ", adjIndex)
            for count, i in enumerate(adjIndex):
                if antonymsList[count]:
                    randomN = randint(0, len(antonymsList[count])-1)
                    changeSin = choices([True, False], [percentage/100, 1-percentage/100])[0]
    #                print("randomN: ", randomN, "count: ", count)
    #                print("utt: ", utt)
                    if changeSin:    
                        utt[i] = antonymsList[count][randomN] #HAY UN POSIBLE ERROR AQUÍ

    return " ".join(utt)

def convertObjectsToSynonyms(utt, percentage, entitiesIndex, nlp): 

    def isAmod(word):

        try:            
            doc = nlp(str(word))
            for wordAux in doc.sentences[0].words:
#                print(wordAux.upos)
                if wordAux.upos == 'NOUN':
                    return True
            return False
        except:
            return False

    synonymsList = []
    objIndex = []
    #discountIndex = 1
    utt = utt.split(' ')

    for i, untknWord in enumerate(utt):
        print(untknWord)
        if isAmod(untknWord) and not i in entitiesIndex:
            synonymsList.append(synonyms(untknWord))
            objIndex.append(i)

    print("synonymsList: ", synonymsList)

    if synonymsList:
        if synonymsList[0]:
#            print("objIndex: ", objIndex)
            for count, i in enumerate(objIndex):
                if synonymsList[count]:
                    randomN = randint(0, len(synonymsList[count])-1)
                    changeSin = choices([True, False], [percentage/100, 1-percentage/100])[0]
    #                print("randomN: ", randomN, "count: ", count)
    #                print("utt: ", utt)
                    if changeSin:    
                        utt[i] = synonymsList[count][randomN] #HAY UN POSIBLE ERROR AQUÍ

    return " ".join(utt)

#def convertAdjectivesToSynonyms(utt, entitiesIndex, nlp): #(falla con la frase: Can you pretty please offer me that incredible job, i want to buy a big red car")
#
#    print (dictionary.meaning("indentation"))
#    synonymsList = [] 
#    adjIndex = []
#    discountIndex = 1
#    doc = nlp(utt)
#    utt = utt.split(' ')
#    
#    
#        
#    
#
#    for word in doc.sentences[0].words:
#        print("word: ", word)
#        if '\'' in word.text: #caso de contracciones
#            discountIndex += 1
#
#        if word.dependency_relation == 'amod' and not (int(word.index) in entitiesIndex):
#            inWord = int(word.index) - discountIndex
#            synonymsList.append(synonyms(word.lemma))
#            adjIndex.append(inWord)
#
#    print("synonymsList: ", synonymsList)
#
#    if synonymsList:
#        if synonymsList[0]:
#            print("adjIndex: ", adjIndex)
#            for count, i in enumerate(adjIndex):
#                randomN = randint(0, len(synonymsList[count])-1)
#                print("randomN: ", randomN, "count: ", count)
#                print("utt: ", utt)
#                utt[i] = synonymsList[count][randomN] #HAY UN POSIBLE ERROR AQUÍ
#
#    return " ".join(utt)
#
#
#def convertAdjectivesToAntonyms(utt, entitiesIndex, nlp):
#    antonymsList = [] 
#    adjIndex = []
#    discountIndex = 1
#    doc = nlp(utt)
#    utt = utt.split(' ')
#    for word in doc.sentences[0].words:
#        if '\'' in word.text: #caso de contracciones
#            discountIndex += 1
#        if word.dependency_relation == 'amod' and not (int(word.index) in entitiesIndex):
#            inWord = int(word.index) - discountIndex
#            antonymsList.append(antonyms(word.lemma))
#            adjIndex.append(inWord)
#
#    if antonymsList:
#        if antonymsList[0]:
#            for count, i in enumerate(adjIndex):
#                randomN = randint(0, len(antonymsList[count])-1)
#                utt[i] = antonymsList[count][randomN]
#
#    return " ".join(utt)
#
##### VER AHORA
#def convertObjectsToSynonyms(utt, entitiesIndex, nlp):
#    synonymsList = [] 
#    objIndex = []
#    discountIndex = 1
#    doc = nlp(utt)
#    utt = utt.split(' ')
#    for word in doc.sentences[0].words:
#        if '\'' in word.text: #caso de contracciones
#            discountIndex += 1
#
#        if word.dependency_relation == 'obj' and not (int(word.index) in entitiesIndex):
#            inWord = int(word.index) - discountIndex
#            synonymsList.append(synonyms(word.lemma))
#            objIndex.append(inWord)
#
#    if synonymsList:
#        if synonymsList[0]:
#            for count, i in enumerate(objIndex):
#                randomN = randint(0, len(synonymsList[count])-1)
#                utt[i] = synonymsList[count][randomN]
#
#    return " ".join(utt)



#### futura implementación
#### futura implementación
def convertAdverbsToSynonyms(utt, percentage, entitiesIndex, nlp): 

    def isAmod(word):

        try:    
            doc = nlp(str(word))
        except:
            return False
        
        for wordAux in doc.sentences[0].words:
#            print(wordAux.upos)
            if wordAux.upos == 'ADV':
                return True
        return False

    synonymsList = []
    adjIndex = []
    #discountIndex = 1
    utt = utt.split(' ')

    for i, untknWord in enumerate(utt):
#        print(untknWord)
        if isAmod(untknWord) and not i in entitiesIndex:
            synonymsList.append(synonyms(untknWord))
            adjIndex.append(i)

#    print("synonymsList: ", synonymsList)

    if synonymsList:
        if synonymsList[0]:
#            print("adjIndex: ", adjIndex)
            for count, i in enumerate(adjIndex):
                if synonymsList[count]:
                    randomN = randint(0, len(synonymsList[count])-1)
                    changeSin = choices([True, False], [percentage/100, 1-percentage/100])[0]
#                    print("randomN: ", randomN, "count: ", count)
#                    print("utt: ", utt)
                    if changeSin:    
                        utt[i] = synonymsList[count][randomN] #HAY UN POSIBLE ERROR AQUÍ

    return " ".join(utt)

def convertAdverbsToAntonyms(utt, percentage, entitiesIndex, nlp): 

    def isAmod(word):

        try:
            doc = nlp(str(word))        
            for wordAux in doc.sentences[0].words:
    #            print(wordAux.upos)
                if wordAux.upos == 'ADV':
                    return True
            return False
        except:
            return False

    antonymsList = []
    adjIndex = []
    #discountIndex = 1
    utt = utt.split(' ')

    for i, untknWord in enumerate(utt):
#        print(untknWord)
        if isAmod(untknWord) and not i in entitiesIndex:
            antonymsList.append(antonyms(untknWord))
            adjIndex.append(i)

#    print("antonymsList: ", antonymsList)

    if antonymsList:
        if antonymsList[0]:
#            print("adjIndex: ", adjIndex)
            for count, i in enumerate(adjIndex):
                if antonymsList[count]:
                    randomN = randint(0, len(antonymsList[count])-1)
                    changeSin = choices([True, False], [percentage/100, 1-percentage/100])[0]
    #                print("randomN: ", randomN, "count: ", count)
    #                print("utt: ", utt)
                    if changeSin:    
                        utt[i] = antonymsList[count][randomN] #HAY UN POSIBLE ERROR AQUÍ

    return " ".join(utt)




"""
     We can convert the utterances in many different ways:
        1. Use all the generative functions over the utterance
        2. Use randomly one function over the utterance
        3. We could also give some priority to some phrases
        4. Set a parameter with the functions to use, and another for the probability of using one or another
"""
"""
    changeNumberToWord, changeWordToNumber: We could use both methods in every utt with a probability associated 
    We can upgrade mutateUtterance so it gets the distances of the nearby keys in the keyboard depending on the 
    configuration of the keyboard.

    An interesting thing to do would be to check the effectivity of each method and balance the parameters
WE CAN ACTUALLY CHECK IF THE UTTERANCE CAN BE SYNTACTICALLY MODIFIED:
    IF IT HAS 2 SENTENCES IN THE SAME UTT...

"""

def generateUtterances(functions, chatbot, dirFunction, distribution, parameters=[keyboardQWERTYSpanish, 3, 0, ["de", "pl", "zh"], 2, [0], 50], extension="utterance.txt"):


    def useFunction(utt, function, botDir, parameters, nlp):

        if function == "mutateUtterance":
            return mutateUtterance(utt, botDir, parameters[1], parameters[2])
        elif function == "mutateUtteranceWithDistances":
            return mutateUtteranceWithDistances(utt, botDir, parameters[1], parameters[2], parameters[0])
        elif function == "deleteChars":
            return deleteChars(utt, botDir, parameters[1], parameters[2])
        elif function == "traductionChained":
            return traductionChained(utt, parameters[3])
        elif function == "randomTraductionChained":
            return randomTraductionChained(utt, parameters[4])
        elif function == "changeNumberToWord":
            return changeNumberToWord(utt)
        elif function == "changeWordToNumber":
            return changeWordToNumber(utt)
        elif function == "activeToPassive":
            return activeToPassive(utt, nlp)
        elif function == "convertAdjectivesToSynonyms":
            return convertAdjectivesToSynonyms(utt, parameters[6], parameters[5], nlp)
        elif function == "convertAdjectivesToAntonyms":
            return convertAdjectivesToAntonyms(utt, parameters[6], parameters[5], nlp)
        elif function == "convertObjectsToSynonyms":
            return convertObjectsToSynonyms(utt, parameters[6], parameters[5], nlp)
        elif function == "convertAdverbsToSynonyms":
            return convertAdverbsToSynonyms(utt, parameters[6], parameters[5], nlp)
        elif function == "convertAdverbsToAntonyms":
            return convertAdverbsToAntonyms(utt, parameters[6], parameters[5], nlp)
        else:
            return utt

    botDir = chatbot
    #print("chatbot: ", chatbot)

    inputsAux = getAllUtterancesFromInput(botDir)
    #entityDict = getEntityDict("/home/sergio/Desktop/proyecto/codigo/convosGen/{}/entities/entities.txt".format(chatbot))
    
    #print("inputsAux: ", inputsAux)
    #stanfordnlp.download('en') ejecutar en la consola para instalar el idioma inglés
    nlp = stanfordnlp.Pipeline()
    
    for inAux in inputsAux:
        generatedUtterances = []
        if inAux:
            print("inAux:", inAux[0], "utts: ", inAux[1:])
            inputFilename = inAux[0] + extension
            firstLine = inAux[0]
            inputUtts = inAux[1:]
            for utt in inputUtts:
                function = choices(functions, distribution)[0]                
                generatedUtterances.append(useFunction(utt, function, botDir, parameters, nlp))
                
            #print(generatedUtterances)
            writeGeneratedUttFile(inputFilename, botDir, dirFunction, firstLine, generatedUtterances)




#    for utt in inputUtts:
#        print(utt)
#        print(mutateUtterance(utt, 2, 10), "\n______________________________________\n")

if __name__ == "__main__":

    generateUtterances(["mutateUtterance", "mutateUtteranceWithDistances", "deleteChars", "traductionChained", "randomTraductionChained", "changeNumberToWord", "changeWordToNumber", 
                        "activeToPassive", "convertAdjectivesToSynonyms", "convertAdjectivesToAntonyms", "convertObjectsToSynonyms", "noMutation"], [0, 0, 0, 0, 0, 0, 0.2, 0.2, 0.2, 0.2, 0.2, 0])



    
