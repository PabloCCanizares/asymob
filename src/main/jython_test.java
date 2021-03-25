package main;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.dkpro.core.readability.measure.WordSyllableCounter;
public class jython_test {



	    public static void main(String[] args){
	        //ScriptEngine engine = new ScriptEngineManager().getEngineByName("python");
				/*engine.eval("import sys");

	        engine.eval("print sys");
	        engine.put("a", 42);
	        engine.eval("print a");
	        engine.eval("x = 2 + 2");
	        Object x = engine.get("x");*/
	        
	        org.dkpro.core.readability.measure.WordSyllableCounter syllableCounter;
	        
	        syllableCounter = new WordSyllableCounter("en"); 
	        
	        int nSyllabes = syllableCounter.countSyllables("here you are");
	        
	        
	        /*engine.eval("import textstat");
	        engine.eval("y = textstat.flesch_reading_ease(\"hi there!\")");
	        Object y = engine.get("y");
	        System.out.println("y: " + y);*/

	    }
}
