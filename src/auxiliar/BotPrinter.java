package auxiliar;

import java.util.List;

import org.eclipse.emf.common.util.EList;

import generator.Action;
import generator.Bot;
import generator.Entity;
import generator.Intent;
import generator.UserInteraction;
/**
 * 
 * @author Pablo C. Ca&ntildeizares
 *
 */
public class BotPrinter  {

	public static void printBot(Bot botIn)
	{
		String strBotName;
		
		strBotName = botIn.getName();
		
		System.out.println("[BotName] "+strBotName);
		printIntents (botIn.getIntents());
		printEntities(botIn.getEntities());
		printActions(botIn.getActions());
		printFlows(botIn.getFlows());
		
	}
	
	private static void printFlows(EList<UserInteraction> flowList) {
		int nIndex;		
		String strActions, strIntentName;
		Intent intentIn;
		
		nIndex = 0;
		System.out.println("<flows: "+flowList.size()+">");
		for (UserInteraction flow : flowList) {			
			
			intentIn = flow.getIntent();
			strActions = extractActions(flow);
			
			if(intentIn != null)
				strIntentName = intentIn.getName();
			else
				strIntentName = "null";
			
			System.out.printf("%d> %s -> <%s>\n", nIndex, strIntentName, strActions);
			nIndex++;
		}
		System.out.println("<actions/>");
	}

	private static String extractActions(UserInteraction flow) {
		String strActions;
		
		strActions = "";
		if(flow != null && flow.getTarget() != null)
		{
			EList<Action>  actionList = flow.getTarget().getActions(); 
			if(actionList != null)
			{
				strActions = String.format("(%d) ", actionList.size());
				for(Action a: actionList)
				{
					strActions += a.getName() + ", ";
				}				
			}
		}

		
		return strActions;
	}

	private static void printActions(EList<Action> actionList) {
		int nIndex;		
		String strIntentName;
		
		nIndex = 0;
		System.out.println("<actions: "+actionList.size()+">");
		for (Action intent : actionList) {
			strIntentName = intent.getName();	
			System.out.println(String.format(">%d ", nIndex)+strIntentName);
			nIndex++;
		}
		System.out.println("<actions/>");
	}

	public static void printIntents(List<Intent> intentList)
	{
		int nIndex;		
		String strIntentName;
		
		nIndex = 0;
		System.out.println("<intents: "+intentList.size()+">");
		for (Intent intent : intentList) {
			strIntentName = intent.getName();	
			System.out.println(String.format(">%d ", nIndex)+strIntentName);
			nIndex++;
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

	public static void printSummaryBot(Bot currentBot) {
		
		System.out.printf("[BotName: %s | Intents: %d | Entities: %d | Flows: %d | Actions: %d ]\n", currentBot.getName(), 
				currentBot.getIntents().size(), currentBot.getEntities().size(),  currentBot.getFlows().size(),  currentBot.getActions().size());

	}
}
