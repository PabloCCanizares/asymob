package analyser;

import generator.Intent;
import generator.Literal;
import generator.Parameter;
import generator.ParameterReferenceToken;
import generator.ParameterToken;
import generator.Token;

public class TokenAnalyser {

	public String getTokenText(Token token) {
		String strText;
		Literal litIn;
		ParameterReferenceToken paramRefIn;
		
		//Initially, the returning string is null
		strText = null;
		
		if(token != null)
		{
			if (token instanceof Literal) 
			{
				//process as literal
				litIn = (Literal) token;
				
				if(litIn != null)
					strText = litIn.getText();
			}
			else if(token instanceof ParameterReferenceToken)
			{
				paramRefIn = (ParameterReferenceToken) token;
				
				if(paramRefIn != null)
					strText = 	paramRefIn.getTextReference();
			}
		}

		return strText;
	}

	public Intent getIntentContainer(ParameterToken paramToken) {
		Parameter param;
		Intent intentContainer;
		
		intentContainer = null;
		
		if(paramToken != null)
		{
			param = paramToken.getParameter();
			
			if(param != null)
			{
				if(param.eContainer() instanceof Intent)
					intentContainer = (Intent) param.eContainer();
			}
		}
		
		return intentContainer;
	}
}
