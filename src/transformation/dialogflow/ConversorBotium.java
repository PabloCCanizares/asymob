package transformation.dialogflow;

import analyser.Conversor;
import generator.DefaultEntity;
import generator.Language;

public class ConversorBotium implements Conversor{

	@Override
	public Language convertLanguageToBot(String strLan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String convertLanguageToAgent(String strLan) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String convertReferenceToBot(String strRef) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String convertReferenceToAgent(String dataType) {
		String strRet, strAux;
		strAux=dataType;
		dataType = dataType.toUpperCase();
		if(dataType.equals(DefaultEntity.FLOAT.getName()))	
			strRet = "@sys.number";
		else if(dataType.equals(DefaultEntity.NUMBER.getName()))
			strRet ="@sys.number-integer";
		else if(dataType.equals(DefaultEntity.DATE.getName()))
			strRet ="$date";
		else if(dataType.equals(DefaultEntity.TIME.getName()))
			strRet ="$time";
		else
			strRet = "$"+strAux;
		
		return strRet;
	}

}
