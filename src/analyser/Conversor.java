package analyser;

import generator.Language;

public interface Conversor {

	Language convertLanguageToBot(String strLan);
	String convertLanguageToAgent(String strLan);
	String convertReferenceToBot(String strRef);
	String convertReferenceToAgent(String strRef);
}
