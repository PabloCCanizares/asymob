package main;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import transformation.dialogflow.ReadAgent;
import transformation.dialogflow.agent.Agent;
import transformation.dialogflow.agent.intents.TrainingPhrase;

public class JSON_test {

	public static void main(String[] args) {
		
		loadBot();
		try {
		      TrainingPhrase std = new TrainingPhrase();
		     
		      std.setId("fa4031ed-528d-4bc3-8608-032bad02f98e");
		      //Creating the ObjectMapper object
		      ObjectMapper mapper = new ObjectMapper();
		      mapper.setSerializationInclusion(Include.NON_NULL);
		      
		      //Converting the Object to JSONString
		      String jsonString;
			
				jsonString = mapper.writeValueAsString(std);
				System.out.println(jsonString);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      
	}
	public static void loadBot()
	{
		String strPath;
		ReadAgent agentReader;
		Agent agent;

		//Initialise
		//strPath = "/localSpace/chatbots/Charm/chatbots/Jokes/dialogflow/Jokes.zip";
		strPath = "C:\\research\\asymob.git\\trunk\\chatbots\\dialogFlow\\malaynayak.zip";		
		agentReader = new ReadAgent();
		agent = null;
		
		try {
			agent = agentReader.getAgent(new File(strPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(agent != null)
		{
			System.out.println("Agent loaded OK");
		}
		else
			System.out.println("Problems loading agent");
		
	}

}
