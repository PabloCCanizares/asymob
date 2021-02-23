package transformation.dialogflow;

import analyser.Conversor;
import generator.DefaultEntity;
import generator.Language;

public class ConversorDialogFlow implements Conversor {

	@Override
	public Language convertLanguageToBot(String strLan) {
			switch (strLan) {
			case "en":
				return Language.ENGLISH;
			case "es":
				return Language.SPANISH;
			case "da":
				return Language.DANISH;
			case "de":
				return Language.GERMAN;
			case "fr":
				return Language.FRENCH;
			case "hi":
				return Language.HINDI;
			case "id":
				return Language.INDONESIAN;
			case "it":
				return Language.ITALIAN;
			case "ja":
				return Language.JAPANESE;
			case "ko":
				return Language.KOREAN;
			case "nl":
				return Language.DUTCH;
			case "no":
				return Language.NORWEGIAN;
			case "pl":
				return Language.POLISH;
			case "pt":
				return Language.PORTUGUESE;
			case "ru":
				return Language.RUSIAN;
			case "sv":
				return Language.SWEDISH;
			case "th":
				return Language.THAI;
			case "tr":
				return Language.TURKISH;
			case "uk":
				return Language.UKRANIAN;
			case "zh":
				return Language.CHINESE;
			default:
				return Language.ENGLISH;
			}
	}

	@Override
	public String convertLanguageToAgent(String strLan) {
		String strRet;
		
		if(strLan != null)
		{
			switch (strLan) {
			case "ENGLISH":
				strRet ="en"; 
				break;
			case "SPANISH":
				strRet ="es";
				break;
			case "DANISH":
				strRet ="da";
				break;
			case "GERMAN":
				strRet ="de";
				break;
			case "FRENCH":
				strRet ="fr";
				break;
			case "HINDI":
				strRet ="hi";
				break;
			case "INDONESIAN":
				strRet ="id";
				break;			
			case "ITALIAN":
				strRet ="it";
				break;			
			case "JAPANESE":
				strRet ="ja";
				break;			
			case "KOREAN":
				strRet ="ko";
				break;			
			case "DUTCH":
				strRet ="nl";
				break;			
			case "NORWEGIAN":
				strRet ="no";
				break;			
			case "POLISH":
				strRet ="pl";
				break;			
			case "PORTUGUESE":
				strRet ="pt";
				break;			
			case "RUSIAN":
				strRet ="ru";
				break;			
			case "SWEDISH":
				strRet ="sv";
				break;			
			case "THAI":
				strRet ="th";
				break;			
			case "TURKISH":
				strRet ="tr";
				break;			
			case "UKRANIAN":
				strRet ="uk";
				break;			
			case "CHINESE":
				strRet ="zh";
				break;			
			default:
				strRet ="en";
				break;			
			}
		}
		else
			strRet = null;
		
		return strRet;
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
