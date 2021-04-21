package main;
import org.dkpro.core.readability.measure.WordSyllableCounter;
/**
 * Program test for counting the syllabes of a given phrase
 * @author Pablo C. Ca&ntildeizares
 *
 */
public class Syllabes_test {

	    public static void main(String[] args){
	        org.dkpro.core.readability.measure.WordSyllableCounter syllableCounter;        
	        syllableCounter = new WordSyllableCounter("en"); 
	        int nSyllabes = syllableCounter.countSyllables("here you are");
	        System.out.printf("Num syllabes %d\n", nSyllabes);
	    }
}
