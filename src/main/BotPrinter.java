package main;

import java.util.List;

import generator.Bot;
import generator.Entity;
import generator.Intent;

public class BotPrinter {

	public static void printBot(Bot botIn)
	{
		String strBotName;
		
		strBotName = botIn.getName();
		
		System.out.println("[BotName] "+strBotName);
		printIntents (botIn.getIntents());
		printEntities(botIn.getEntities());
		
	}
	
	public static void printIntents(List<Intent> intentList)
	{
		String strIntentName;
		System.out.println("<intents: "+intentList.size()+">");
		for (Intent intent : intentList) {
			strIntentName = intent.getName();	
			System.out.println("*"+strIntentName);
		}
		System.out.println("<intents/>");
	}
	
	public static void printEntities(List<Entity> entityList)
	{
		String strEntityName;
		System.out.println("<entities: "+entityList.size()+">");
		for (Entity entity : entityList) {
			strEntityName = entity.getName();	
			System.out.println("*"+strEntityName);
		}
		System.out.println("<entities/>");
	}
}
