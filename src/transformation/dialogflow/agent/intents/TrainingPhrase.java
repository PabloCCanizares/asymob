package transformation.dialogflow.agent.intents;

import java.util.List;

import generator.GeneratorFactory;
import generator.Literal;
import generator.ParameterReferenceToken;

public class TrainingPhrase {
	
	private String id;
	private List<Data> data;
	private boolean isTemplate;
	private int count;
	private String lang;
	private int updated;	
	
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public boolean isTemplate() {
		return isTemplate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public void setTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}
	
	public generator.TrainingPhrase getBotTrainingPhrase(generator.Intent intent) {
		generator.TrainingPhrase training = GeneratorFactory.eINSTANCE.createTrainingPhrase();
		String text = "";
		for (Data data : getData()) {
			if (!data.getAlias().isEmpty()) {
				if (!text.isEmpty()) {
					Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
					literal.setText(text);
					training.getTokens().add(literal);
					text = "";
				}
				generator.Parameter param = intent.getParameter(data.getAlias());
				ParameterReferenceToken reference = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
				reference.setTextReference(data.getText());
				reference.setParameter(param);
				training.getTokens().add(reference);
			} else {
				text+=data.getText();
			}
		}
		if (!text.isEmpty()) {
			Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
			literal.setText(text);
			training.getTokens().add(literal);
			text = "";
		}
		return training;
	}
	
}
