package transformation.dialogflow.agent.intents;

public class Prompt{
	private String lang;
	private String value;
	public String getLang() {
		if (lang == null) {
			lang ="";
		}
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}