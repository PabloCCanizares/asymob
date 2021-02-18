package transformation.dialogflow.agent.entities;

import java.util.List;

public class EntryLanguage {

	private String language;
	private List<Entry> entries;
	
	public String getLanguage() {
		if (language == null) {
			language = "";
		}
		return language;
	}
	public void setLanguage(String language) {
		language = language;
	}
	public List<Entry> getEntries() {
		return entries;
	}
	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	
}
