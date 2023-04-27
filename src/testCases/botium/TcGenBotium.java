package testCases.botium;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.emf.common.util.EList;
import analyser.BotAnalyser;
import analyser.flowTree.TreeBranch;
import analyser.flowTree.TreeInterAction;
import analyser.flowTree.conversationSplitter.ConversationGroup;
import auxiliar.Common;
import generator.Bot;
import generator.Entity;
import generator.Parameter;
import generator.UserInteraction;
import testCases.ITestCaseGenerator;
import testCases.botium.testcase.BotiumAction;
import testCases.botium.testcase.BotiumIntent;
import testCases.botium.testcase.BotiumTestCase;
import testCases.botium.testcase.BotiumTestCaseFragment;
import transformation.dialogflow.ConversorBotium;


public class TcGenBotium implements ITestCaseGenerator {

	BotAnalyser botAnalyser;
	TcExportBotium botExport;
	final boolean DEBUG = false;

	public boolean generateTestCases(Bot botIn, String strPath) {

		boolean bRet;

		//Initialise
		bRet = true;

		try
		{
			botExport = new TcExportBotium(strPath);
			botAnalyser = new BotAnalyser();

			//Check if the path exists, if not -> create it
			if(botExport.checkDirectory())
			{
				//Export the flows in depth
				generateTestCaseFromBot(botIn);
			}
			else
			{
				bRet = false;
				System.out.println("[generateTestCases] - A problem with the directory has ocurred: "+strPath);
			}
		}
		catch(Exception e)
		{
			System.out.println("[generateTestCases] - Exception while generating TCs: "+e.getMessage());
		}

		return bRet;
	}
	/**
	 * Exports the test case by analysing and extracting all the information required from the input bot
	 * @param botIn: Bot provided to be exported
	 * @return True if the export is performed successfully
	 */
	private boolean generateTestCaseFromBot(Bot botIn) {
		boolean bRet;

		//Initialise
		bRet = false;
		
		if(botIn != null)
		{
			//Instance of a bot analyser
			botAnalyser = new BotAnalyser(new ConversorBotium());
			
			//Extract the parameter type, not the text referece, for extracting entities tokens
			botAnalyser.setCompactRefPhrasesMode(true); 

			//Export flows
			exportFlows(botIn.getFlows());
			
			//Export entities
			exportEntities(botIn, botIn.getEntities());

		}
		return bRet;
	}

	private void exportEntities(Bot botIn, EList<Entity> entities) {
		String strEntityName;
		Map<String, LinkedList<String>> entityMap;
		
		//Extract the entities and save them to disk
		for(Entity ent: entities)
		{
			strEntityName = ent.getName();
			entityMap = botAnalyser.getEntityMap(ent);

			System.out.println("Creating scriptfile for entity: "+ strEntityName);
			createEntityTestCase(strEntityName, entityMap);
		}

		//In addition, search an specific parameter, to create a new scripting file
		entityMap = extractStandardEntity(botIn, "date", "dateIn");
		createEntityTestCase("date", entityMap);

		entityMap = extractStandardEntity(botIn, "time", "timeIn");
		createEntityTestCase("time", entityMap);
		
	}
	/**
	 * Export the flows of a chatbot in shape of test case
	 * @param flows
	 */
	private void exportFlows(EList<UserInteraction> flows) {
		TreeBranch treeBranch;
		LinkedList<BotiumTestCase> tcList;
		
		//The flows are iterated, and converted into a list of pairs <UserInteraction, List<Actions>>
		for(UserInteraction flow: flows)
		{
			//Explore the flow, and plain the flow tree into several branches: 
			// List of TreeInterAction: UserInteraction - List of actions
			//Create the tree branch, a simplified representation of a tree and save into list			 
			treeBranch = botAnalyser.plainActionTreeInBranches(flow);

			//Create the test case and save to disk: each Tc is a convo in botium
			tcList = createTestCasesFromBranches(treeBranch);
			
			//Export the Tc list
			botExport.exportTestCases(tcList);
		}
	}
	/**
	 * Creates an script memory file, which contains all the entities of the chatbot
	 * @param strEntityName
	 * @param entityMap
	 */
	private void createEntityTestCase(String strEntityName, Map<String, LinkedList<String>> entityMap) {
		String literalName, strConvoBuffer;
		LinkedList<String> synList;
		int nElement;
		literalName = strConvoBuffer = "";
		nElement =0;
		try
		{
			strConvoBuffer = strConvoBuffer.concat("     |$"+strEntityName+"\n");
			//Loop the entityMap updating the buffer
			for (Entry<String, LinkedList<String>> entry : entityMap.entrySet()) {
				literalName = entry.getKey();
				synList = entry.getValue();
				System.out.println(literalName + "=" + synList.toString());

				for(String syn: synList)
				{
					strConvoBuffer =  strConvoBuffer.concat(String.format("case%d|%s\n", nElement, syn));
					nElement++;
				}
			}
			if(nElement>0)
			{
				//Create the file and save to disk
				botExport.createScriptingMemoryFile(strEntityName, strConvoBuffer);
			}

		}
		catch(Exception e)
		{
			System.out.println("[TcGenBotium::createEntityTestCase] Exception while creating a entity script memory");
		}
	}

	/**
	 * Creates test cases from branches. From each conversation path List<interaction, list of actions> 
	 * generates a test case
	 * @param treeBranch: Conversation path
	 * @return
	 */
	private LinkedList<BotiumTestCase> createTestCasesFromBranches(TreeBranch treeBranch) {

		TreeInterAction treeIntentAction;
		BotiumTestCaseFragment botTestCaseFragment;
		LinkedList<BotiumTestCaseFragment> tcFragmentList;
		LinkedList<ConversationGroup> conversationGroupList;
		LinkedList<BotiumTestCase> testcaseList, auxTcList;
		BotiumTestCase auxTc;
		try
		{
			
			//Initialise
			testcaseList = null;
			testcaseList = new LinkedList<BotiumTestCase>();
			conversationGroupList = new LinkedList<ConversationGroup>();
			treeBranch.resetIndex();
			
			//We iterate the all the tree branches (conversation paths)
			while(treeBranch.hasNext())
			{
				treeIntentAction = treeBranch.getNext();

				//Split the training phrases considering the parameters of each phrase, and categorise them in groups
				//called conversations groups.
				//For example, if the whole set of training phrases have 3 required parameters: 
				//param1, param2 and param3
				// * If a phrase only uses the param3, the group of this phase will be "001"
				// * If a phrase has all the params, the group of this phase will be "111"
				conversationGroupList = botAnalyser.splitConversationByParam(treeIntentAction);

				//Estrategias aqui.
				//TODO: Aqui deberiamos poder trazar estrategias. Esta divide los grupos en archivos.
				//Otra podría ser incluirlo todo en un archivo como estaba antes
				//Acto seguido, se recorren todos los grupos y
				auxTcList = new LinkedList<BotiumTestCase>();
				for(ConversationGroup conversation: conversationGroupList)
				{
					//Check wheter the conversation group is complete
					//TODO: Explicar que es un TcFragment y por que se usa.
					botTestCaseFragment = convertConvGroupToTcFragment(conversation);

					//It may have a single or multiple fragments per group: If the phrase is not complete
					//lacks some of the parameters, we need to complement it.					
					tcFragmentList = complementConversation(conversation);

					if(tcFragmentList == null)
						tcFragmentList = new LinkedList<BotiumTestCaseFragment>();

					tcFragmentList.addFirst(botTestCaseFragment);

					//Iterate the testcase list, and add this fragment to all of them
					if(!testcaseList.isEmpty())
					{
						//Para cada uno de los test cases que hay, hay que añadirles los fragmentos nuevos
						for(BotiumTestCase botTcIn: testcaseList)
						{
							//Para cada TC que tenga la lista original, 
							auxTc = botTcIn.dup();

							//Add all the existing fragments
							for(BotiumTestCaseFragment tcFragment: tcFragmentList)
							{
								auxTc.addFragment(tcFragment);
							}

							auxTcList.add(auxTc);
						}
					}
					else
					{
						//Create a new test case
						auxTc = new BotiumTestCase();

						//Add all the existing fragments
						for(BotiumTestCaseFragment tcFragment: tcFragmentList)
						{
							auxTc.addFragment(tcFragment);
						}
						auxTcList.add(auxTc);
					}
				}
				testcaseList = auxTcList;
			}

		}
		catch(Exception e)
		{
			testcaseList = null;
			System.out.println("[TcGenBotium::createFlowTestCase] Exception while creating a flow");
		}

		return testcaseList;
	}

	/**
	 * Add new fragments to the conversation in order to cover all the parameters that are not present in the training phrases
	 * The main idea is to totally cover all the parameters defined as required in the Bot
	 * @param conversation
	 * @return
	 */
	private LinkedList<BotiumTestCaseFragment> complementConversation(ConversationGroup conversation) {
		LinkedList<Parameter> paramList;
		LinkedList<BotiumTestCaseFragment> tcFragmentList;
		BotiumTestCaseFragment newFragment;
		LinkedList<String> defaultResp, defaultPrompts, finishingResponse;

		tcFragmentList = null;
		//If it is not present. Obtain all the parameters 
		if(!conversation.allParametersPresent())
		{
			//disable the action: not include it the convos
			conversation.disableActions();

			tcFragmentList = new LinkedList<BotiumTestCaseFragment>();
			
			//Si no estan presentes, hay que complementar el fragmento
			paramList = conversation.getNotPresentParams();

			//Iterate the non present parameters and create fragments
			for(Parameter param: paramList)
			{
				defaultPrompts = botAnalyser.extractParameterPrompt(param);

				defaultResp = conversation.getDefaultResponsesForParam(param);

				System.out.println("Param: "+param.getName()+" | bot: "+defaultPrompts.getFirst()+" | user: "+defaultResp.getFirst());

				//Create the fragment, and select the reverse mode
				//TODO: Actually, this is done by inline actions. It can be done by using these lists (defaultResp and defaultPrompts)
				newFragment = new BotiumTestCaseFragment(defaultResp.getFirst(), defaultPrompts.getFirst(), true);
				//newFragment = new BotiumTestCaseFragment(conversation.getIntentGroupName()+"_"+param.getName(), conversation.getActionGroupName()+"_"+param.getName(), defaultResp, defaultPrompts, true);
				//Now, we have the pair prompt/response
				tcFragmentList.add(newFragment);
			}

			//Finally, insert the complete response
			finishingResponse = conversation.getFinishingResponse();
			newFragment = new BotiumTestCaseFragment(null, finishingResponse.getFirst(), true);
			tcFragmentList.add(newFragment);
		}

		return tcFragmentList;
	}

	private BotiumTestCaseFragment convertConvGroupToTcFragment(ConversationGroup conversation) {
		String strActionGroupName;
		BotiumIntent botIntent;
		BotiumAction botAction;
		BotiumTestCaseFragment botTestCase;
		String strGroupName;
		LinkedList<String> trainingPhrases;
		LinkedList<String> responses;

		botAction = null;
		botIntent = null;

		if(!conversation.allParametersPresent())
			conversation.disableActions();

		//Intent
		if(!conversation.isDisabledIntent())
		{
			strGroupName = conversation.getIntentGroupName();
			if(DEBUG) System.out.println("IntentGroup: "+strGroupName);
			trainingPhrases = conversation.getPlainTrainingPhases();
			if(DEBUG) System.out.println("Training phrases:\n"+Common.listToStringWithBreak(trainingPhrases));
			botIntent = new BotiumIntent(strGroupName, trainingPhrases);
		}

		//Action
		if(!conversation.isDisabledAction())
		{
			strActionGroupName = conversation.getActionGroupName();
			responses = conversation.getActionResponses();
			if(DEBUG) System.out.println("Responses:\n"+Common.listToStringWithBreak(responses));
			botAction = new BotiumAction(strActionGroupName, responses);
		}


		botTestCase = new BotiumTestCaseFragment(botIntent, botAction, false);
		return botTestCase;
	}

	private Map<String, LinkedList<String>> extractStandardEntity(Bot botIn, String strParamName, String newName) {

		Map<String, LinkedList<String>> mapRet;
		LinkedList<String> paramValues;
		
		mapRet = new HashMap<String, LinkedList<String>>();
		//Extract all the sentences with a
		paramValues = botAnalyser.extractAllBotParameterValues(botIn, strParamName);
		
		mapRet.put(newName, paramValues);
		
		return mapRet;
	}
	
}
