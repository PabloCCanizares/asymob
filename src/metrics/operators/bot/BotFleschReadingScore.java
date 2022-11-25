package metrics.operators.bot;

import java.util.LinkedList;
import java.util.List;

import analyser.BotAnalyser;
import analyser.IntentAnalyser;
import auxiliar.Common;
import auxiliar.JavaRunCommand;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import generator.Bot;
import metrics.base.FloatMetricValue;
import metrics.base.IntegerListMetricValue;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;
import support.stanford.StandfordPipeline;

public class BotFleschReadingScore extends BotMetricBase{

	private final boolean PRINT = true; 
	private final String textStatPath= "/scripts/fleschReadability.py";
	private final String command= "/scripts/fleschReadability.py";
	public BotFleschReadingScore() {
		super(EMetricOperator.eGlobalFleschReadingEasyScore);
	}

	@Override
	public void calculateMetric() {
		LinkedList<Integer> intList;
		
		intList = getFlesch();
		metricRet = new IntegerListMetricValue(this, intList);
	}
	
	public LinkedList<Integer> getFlesch(){
		int nTotal, nAux, nPhrases;	
		float fValue, fAux, fMin, fMax;
		String strValue;
		Annotation annotation;
		BotAnalyser botAnalyser = null;
		LinkedList<String> phrasesList, listRet;
		LinkedList<Integer> intList;		
		JavaRunCommand commandRunner;
		String strMetrics;
		
		intList = null;
		botAnalyser = new BotAnalyser();
		listRet = null;
		
		//Extract phrases from intent
		phrasesList = botAnalyser.extractAllBotOutputPhrases(this.botIn);
		
		if(phrasesList != null  && phrasesList.size()>0)
		{
			
			nPhrases=0;
			fValue = 0;
			fMin = 120;
			fMax = -1;
			commandRunner = new JavaRunCommand();
			for(String strPhrase: phrasesList)
			{
				if(!strPhrase.isBlank())
				{
					int i=0;
					if(strPhrase.length()>500)
						i=1;
					
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
						
						if(fAux<0)
							fAux=0;
						else if (fAux >120)
							fAux =120;
						
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
				
			intList = new LinkedList<Integer>();
			
			intList.addLast((int)fMin);
			intList.addLast((int)fValue);
			intList.addLast((int)fMax);
			
			metricRet = new MetricValue(this);
			metricRet.setValue(strMetrics);
			
			System.out.println(strMetrics);
			
		}
		else
		{
			intList = Common.ConvertFloatToPercentages(0, 0, 0);
		}
		return intList;
	}
	
	

	@Override
	public void setMetadata() {
		this.strMetricName = "FRES";
		this.strMetricDescription = "Flesch Reading Easy Score";
	}
}
