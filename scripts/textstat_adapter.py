#!/usr/bin/python
import textstat
import sys

test_data = (
    "Playing games has always been thought to be important to "
    "the development of well-balanced and creative children; "
    "however, what part, if any, they should play in the lives "
    "of adults has never been researched that deeply. I believe "
    "that playing games is every bit as important for adults "
    "as for children. Not only is taking time out to play games "
    "with our children and other adults valuable to building "
    "interpersonal relationships but is also a wonderful way "
    "to release built up tension."
)

#mutop = str(sys.argv[1]) 
phrase = input('')

# Create row vector
vector = ["" for x in range(16)]  # N = size of list you want

vector[0] = textstat.flesch_reading_ease(phrase)
vector[1] =textstat.flesch_reading_ease(phrase)
vector[2] =textstat.smog_index(phrase)
vector[3] =textstat.flesch_kincaid_grade(phrase)
vector[4] = textstat.coleman_liau_index(phrase)
vector[5] = textstat.automated_readability_index(phrase)
vector[6] = textstat.dale_chall_readability_score(phrase)
vector[7] = textstat.difficult_words(phrase)
vector[8] = textstat.linsear_write_formula(phrase)
vector[9] = textstat.gunning_fog(phrase)
vector[10] = textstat.text_standard(phrase)
vector[11] = textstat.fernandez_huerta(phrase)
vector[12] = textstat.szigriszt_pazos(phrase)
vector[13] = textstat.gutierrez_polini(phrase)
vector[15] = textstat.crawford(phrase)

print(vector)

