package analyser;

import java.util.List;

import generator.Bot;
import generator.Intent;
import generator.IntentLanguageInputs;

public class BotAnalyser {

	private void extractAllIntentInputs(Bot botIn)
	{
		List<Intent> listIntent;
		List<IntentLanguageInputs> listLanguages;
		if(botIn != null)
		{
			//
			listIntent =  botIn.getIntents();
			
			for (Intent intent : listIntent) {
				listLanguages = intent.getInputs();
			}
		}
	}
	
}
