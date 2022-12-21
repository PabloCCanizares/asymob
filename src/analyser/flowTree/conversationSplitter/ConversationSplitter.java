package analyser.flowTree.conversationSplitter;

import java.util.LinkedList;

import analyser.Conversor;
import analyser.IntentAnalyser;
import analyser.flowTree.TreeInterAction;
import generator.Intent;
import generator.IntentInput;
import generator.Language;
import generator.Parameter;

public class ConversationSplitter {

	private IntentAnalyser intentAnalyser;
	private Conversor converter;
	
	public ConversationSplitter(Conversor converter) {
		this.converter = converter;
		intentAnalyser = new IntentAnalyser(converter);
	}

	public ConversationSplitter() {
		intentAnalyser = new IntentAnalyser();
	}

	public LinkedList<ConversationGroup> splitByParameterConvenion(TreeInterAction treeIntentAction)
	{
		LinkedList<ConversationGroup> conversationList;
		LinkedList<IntentConversationGroup> intentGroupList;
		ActionConversationGroup actionGroup;
		ConversationGroup conversationPair;
		
		//First, we analyse the intents
		intentGroupList = separateIntentByParamRequirements(treeIntentAction);

		conversationList = new LinkedList<ConversationGroup>();
		//Then, we process each intent and associate actions
		for(IntentConversationGroup intentGroup: intentGroupList)
		{
			//Given a group, obtain the output
			actionGroup = new ActionConversationGroup(this.converter);
			
			if(actionGroup.extractActions(intentGroup, treeIntentAction)) {
				conversationPair = new ConversationGroup(intentGroup, actionGroup);
				conversationList.add(conversationPair);
			}
		}
		
		//Finally, we return the list of pairs, group - actions (to be plained)

		return conversationList;
	}

	private LinkedList<IntentConversationGroup> separateIntentByParamRequirements(TreeInterAction treeIntentAction) {
		Intent intentIn;
		LinkedList<Parameter> paramList;
		LinkedList<IntentInput> intentInputList;
		LinkedList<IntentConversationGroup> intentGroupList;
		IntentSplitter splitter;
		int nParameters;

		intentGroupList = null;
		
		try
		{						
			//La idea es, de esta dupla intent, lista de acciones: generar una lista de <intent, acciones>
			//Hay que separarlos por frases de entrenamiento teniendo en cuenta:
			//*Los parametros requeridos del intent
			//*Un intent nuevo, por cada conjunto de frases que tenga el mismo tipo de parametros:
			//Ej: Un intent tiene 2 parametros requeridos. Si X frases de entrenamiento tienen el parametro p1, pero le falta el p2: formaran un grupo
			//Si Y frases tienen p1 y p2, formaran otro.
	
			//
			//Sacamos la lista de parametros requeridos del intent 
			intentIn = treeIntentAction.getIntent();
			paramList = this.intentAnalyser.getRequiredParameters(intentIn);
			splitter = new IntentSplitter(intentIn.getName(),paramList);
	
			if(paramList != null)
				nParameters = paramList.size();
			else
				nParameters = 0;
	
			if(nParameters>-1)
			{
				System.out.printf("splitByParameterConvenion - Number of required parameters detected: %d, max: %d\n", nParameters, 2^nParameters);
				//Con el número de parameters p, sabemos la cota superior de grupos que va a haber: 2^p
	
				//A partir de aqui, analizamos frase a frase, y nos devolverá una lista de booleanos
				intentInputList= this.intentAnalyser.extractAllInputs(intentIn, Language.ENGLISH);
	
				for(IntentInput intentInput: intentInputList)
				{
					//Analizamos en cada intent, que parametros ha encontrado, y con esto se generaun grupo
					splitter.matchParameters(intentInput);
				}
				intentGroupList = splitter.processGroups();
	
				//Print the groups to understand the 
				/*System.out.println("Total groups extracted: "+intentGroupList.size());
				for(IntentConversationGroup groupInfo: intentGroupList)
				{
					System.out.println(groupInfo.ToString());
				}*/
			}
			else
			{
				System.out.println("splitByParameterConvenion - No parameters found");
				//emptyGroup = new IntentConversationGroup(strIntentName, "", null, phraseList);
			}
		}
		catch(Exception e)
		{
			System.out.println("separateIntentByParamRequirements - Exception while separating the conversation");
		}
		
		return intentGroupList;
	}
}
