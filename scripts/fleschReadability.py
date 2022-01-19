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
vector = ["" for x in range(1)]  # N = size of list you want

vector[0] = textstat.flesch_reading_ease(phrase)

print(vector)

