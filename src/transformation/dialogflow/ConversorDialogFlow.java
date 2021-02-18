package transformation.dialogflow;

import analyser.Conversor;
import generator.DefaultEntity;

public class ConversorDialogFlow implements Conversor {

	@Override
	public String convertLanguageToBot(String strLan) {
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
		String strRet;
		dataType = dataType.toUpperCase();
		if(dataType.equals(DefaultEntity.FLOAT.getName()))	
			strRet = "@sys.number";
		else if(dataType.equals(DefaultEntity.NUMBER.getName()))
			strRet ="@sys.number-integer";
		else if(dataType.equals(DefaultEntity.DATE.getName()))
			strRet ="@sys.date";
		else if(dataType.equals(DefaultEntity.TIME.getName()))
			strRet ="@sys.number-time";
		else
			strRet = "@sys.text";
		
		return strRet;
	}

}
