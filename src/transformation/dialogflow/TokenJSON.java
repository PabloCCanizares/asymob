package transformation.dialogflow;

public class TokenJSON {

	String text;
	String meta;
	boolean userDefined;
	
	public TokenJSON()
	{
		text="";
		meta=null;
		userDefined=false;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}

	public boolean isUserDefined() {
		return userDefined;
	}

	public void setUserDefined(boolean userDefined) {
		this.userDefined = userDefined;
	}
}
