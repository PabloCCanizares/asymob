package analyser;

public interface Conversor {

	String convertLanguageToBot(String strLan);
	String convertLanguageToAgent(String strLan);
	String convertReferenceToBot(String strRef);
	String convertReferenceToAgent(String strRef);
}
