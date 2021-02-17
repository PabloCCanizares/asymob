package transformation.dialogflow;

import java.util.List;

public class TrainingPhraseJSON {

	String id;
	List<TokenJSON> data;
	boolean isTemplate;
	int count;
	String lang;
	int updated;
	
	public List<TokenJSON> getData() {
		return data;
	}

	public void setData(List<TokenJSON> data) {
		this.data = data;
	}

	public boolean isTemplate() {
		return isTemplate;
	}

	public void setTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}

	public String getId() {
		return id;
	}

	public TrainingPhraseJSON()
	{
		id="";
		data = null;
		isTemplate = false;
		count=0;
		lang="";
		updated=0;
	}

	public void setId(String id) {
		this.id = id;
		
	}
}
