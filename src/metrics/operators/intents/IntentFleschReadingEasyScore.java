package metrics.operators.intents;

import java.util.LinkedList;

import analyser.IntentAnalyser;
import auxiliar.Common;
import auxiliar.JavaRunCommand;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;

/**
 * Measures the readability of a sentence, providing an score that represents:
 * 90-100	Very Easy
 * 80-89	Easy
 * 70-79	Fairly Easy
 * 60-69	Standard
 * 50-59	Fairly Difficult
 * 30-49	Difficult
 * 0-29		Very Confusing
 * @author Usuario
 *
 */
public class IntentFleschReadingEasyScore extends IntentMetricBase{
	private final String textStatPath= "/scripts/fleschReadability.py";
	private final String command= "/scripts/fleschReadability.py";
	public IntentFleschReadingEasyScore() {
		super(EMetricOperator.eFleschReadingEasyScore);
	}

	@Override
	public void calculateMetric() {

		IntentAnalyser inAnalyser;
		LinkedList<String> phrasesList, listRet = null;
		String strCompleteText, strMetrics;
		JavaRunCommand commandRunner;
		String strRes;
		float fValue, fAux, fMin, fMax;
		int nPhrases;
		
		//Initialise
		inAnalyser = new IntentAnalyser();
		commandRunner = new JavaRunCommand();
		strCompleteText="";
		//Extract the phrases
		
		phrasesList = inAnalyser.extractStringTrainingPhrasesFromIntent(this.intentIn);
		if(phrasesList != null)
		{
			nPhrases=0;
			fValue = 0;
			fMin = 100;
			fMax = -1;
			for(String strPhrase: phrasesList)
			{
				strCompleteText+=" "+strPhrase;
			
				//commandRunner.setProgram(strProgramId);
				if(!strCompleteText.isBlank())
				{
					commandRunner.setInputPhrase(strPhrase);
					commandRunner.setProgramPath(System.getProperty("user.dir")+textStatPath);
					commandRunner.resetLastResults();
					if (commandRunner.runCommand(""))
						listRet = commandRunner.getLastResults();			
				
					strMetrics = Common.listToString(listRet);
					
					try
					{
						strMetrics = strMetrics.replace("[", "");
						strMetrics = strMetrics.replace("]", "");
						//Convert to float
						fAux = Float.parseFloat(strMetrics);
						/*
						 *  * 90-100	Very Easy
							 * 80-89	Easy
							 * 70-79	Fairly Easy
							 * 60-69	Standard
							 * 50-59	Fairly Difficult
							 * 30-49	Difficult
							 * 0-29		Very Confusing
						 */
						
						if(fAux>100)
							fAux=100;
						if(fAux > fMax)
							fMax = fAux;
						
						if(fAux <fMin)
							fMin = fAux;
						
						fValue +=fAux;
						nPhrases++;	
					}catch(Exception e)
					{
						strMetrics = "";
					}						
				}
			}
			
			if(nPhrases>0)
				fValue = fValue /nPhrases;
			else
				fValue =0;
			
			strMetrics = String.valueOf(fValue);
			if(fValue >= 0 && fValue <=29)
				strMetrics = String.format("%.2f [%.2f | %.2f] (Very confusing)",fValue, fMin, fMax);		
			else if(fValue >= 30 && fValue <=49)
				strMetrics = String.format("%.2f [%.2f | %.2f] (Difficult)",fValue, fMin, fMax);
			else if(fValue >= 50 && fValue <=59)
				strMetrics = String.format("%.2f [%.2f | %.2f] (Fairly Difficult)",fValue, fMin, fMax);
			else if(fValue >= 60 && fValue <=69)
				strMetrics = String.format("%.2f [%.2f | %.2f] (Standard)",fValue, fMin, fMax);
			else if(fValue >= 70 && fValue <=79)
				strMetrics = String.format("%.2f [%.2f | %.2f] (Fairly Easy)",fValue, fMin, fMax);			
			else if(fValue >= 80 && fValue <=89)
				strMetrics = String.format("%.2f [%.2f | %.2f] (Easy)",fValue, fMin, fMax);
			else if(fValue >= 90 && fValue <=100)
				strMetrics = String.format("%.2f [%.2f | %.2f] (Very Easy)",fValue, fMin, fMax);	
				
			metricRet = new MetricValue(this);
			metricRet.setValue(strMetrics);
		}
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "IFRES";
		this.strMetricDescription = "Intent Flesch Reading Easy Score";
	}
}
