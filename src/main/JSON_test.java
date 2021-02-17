package main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import transformation.dialogflow.TrainingPhraseJSON;

public class JSON_test {

	public static void main(String[] args) {
		
		try {
		      TrainingPhraseJSON std = new TrainingPhraseJSON();
		     
		      std.setId("fa4031ed-528d-4bc3-8608-032bad02f98e");
		      //Creating the ObjectMapper object
		      ObjectMapper mapper = new ObjectMapper();
		      //Converting the Object to JSONString
		      String jsonString;
			
				jsonString = mapper.writeValueAsString(std);
				System.out.println(jsonString);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      
	}

}
