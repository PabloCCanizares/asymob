package analyser;

import com.fasterxml.jackson.databind.util.Converter;

import generator.Intent;
import generator.Literal;
import generator.Parameter;
import generator.ParameterReferenceToken;
import generator.ParameterToken;
import generator.Token;

public class TokenAnalyser {

	//Converter class, for conducting changes in 
	Conversor conversor;
	
	public TokenAnalyser()
	{
		conversor = null;
	}
	public TokenAnalyser(Conversor conversor)
	{
		this.conversor = conversor;
	}

	public String getTokenText(Token token, boolean bRefName) {
		String strText;
		Literal litIn;
		ParameterReferenceToken paramRefIn;
		ParameterToken paramToken;
		
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
				{
					if(bRefName==false)
						strText = paramRefIn.getTextReference();
					else
					{
						strText = paramRefIn.getParameter().getName();
						if(conversor!=null)
						{
							strText = conversor.convertReferenceToAgent(strText);
						}
					}
				}
			}
			else if(token instanceof ParameterToken)
			{
				paramToken = (ParameterToken) token;
				
				if(paramToken != null)
				{
					strText = paramToken.getParameter().getName();
					if(bRefName)						
					{
						if(conversor!=null)
						{
							strText = conversor.convertReferenceToAgent(strText);
						}
					}
				}
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

	public String getParameterToken(Token tokIn) {
		String strRet;
		ParameterToken paramToken;
		Parameter param;
		
		strRet = null;
		//If is instanceof Parameter token, it is necessary to extract the parameter to include it in the JSON
		if(tokIn instanceof ParameterToken)
		{
			paramToken = (ParameterToken)tokIn;
			if(paramToken != null)
			{
				param = paramToken.getParameter();
				if(param != null)
					strRet = param.getName();
			}
		}
		
		return strRet;
	}
}
