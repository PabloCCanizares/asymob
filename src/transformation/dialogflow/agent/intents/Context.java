package transformation.dialogflow.agent.intents;

import java.util.List;

public class Context{
	private String name;
	private int lifespan;
	private List<Parameter> parameters;
	
	public String getName() {
		return name;
	}

	public int getLifespan() {
		return lifespan;
	}

	public void setLifespan(int lifespan) {
		this.lifespan = lifespan;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
