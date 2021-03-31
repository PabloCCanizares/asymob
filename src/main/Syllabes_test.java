package main;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.dkpro.core.readability.measure.WordSyllableCounter;
public class Syllabes_test {



	    public static void main(String[] args){
	        org.dkpro.core.readability.measure.WordSyllableCounter syllableCounter;        
	        syllableCounter = new WordSyllableCounter("en"); 
	        int nSyllabes = syllableCounter.countSyllables("here you are");
	        System.out.printf("Num syllabes %d\n", nSyllabes);
	    }
}
