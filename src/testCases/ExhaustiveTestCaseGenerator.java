package testCases;

import java.util.LinkedList;

import analyser.BotAnalyser;
import analyser.flowTree.TreeBranch;
import analyser.flowTree.TreeInterAction;
import analyser.flowTree.conversationSplitter.ConversationGroup;
import auxiliar.Common;
import generator.Parameter;
import testCases.botium.testcase.BotiumAction;
import testCases.botium.testcase.BotiumIntent;
import testCases.botium.testcase.BotiumTestCase;
import testCases.botium.testcase.BotiumTestCaseFragment;

public class ExhaustiveTestCaseGenerator implements ITestSelectionStrategy {
	
	final boolean DEBUG = false;

    @Override
    public LinkedList<BotiumTestCase> createTestCasesFromBranches(TreeBranch treeBranch, BotAnalyser botAnalyser) {
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
					tcFragmentList = complementConversation(conversation, botAnalyser);

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
    
    private LinkedList<BotiumTestCaseFragment> complementConversation(ConversationGroup conversation, BotAnalyser botAnalyser) {
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
    
}



