package transformation.dialogflow.agent.entities;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import generator.EntityInput;
import generator.GeneratorFactory;
import generator.LanguageInput;
import generator.SimpleInput;
import transformation.dialogflow.agent.Agent;

@JsonIgnoreProperties({"entriesLanguage", "botEntity"})
public class Entity {

	private String id;
	private String name;
	private boolean isOverridable;
	private boolean isEnum;
	private boolean isRegexp;
	private boolean automatedExpansion;
	private boolean allowFuzzyExtraction;
	private List<EntryLanguage> entriesLanguage;
	

	@JsonProperty("isRegexp")
	public boolean isRegexp() {
		return isRegexp;
	}

	public void setRegexp(boolean isRegexp) {
		this.isRegexp = isRegexp;
	}

	public boolean isAutomatedExpansion() {
		return automatedExpansion;
	}

	public void setAutomatedExpansion(boolean automatedExpansion) {
		this.automatedExpansion = automatedExpansion;
	}


	public boolean isAllowFuzzyExtraction() {
		return allowFuzzyExtraction;
	}

	public void setAllowFuzzyExtraction(boolean allowFuzzyExtraction) {
		this.allowFuzzyExtraction = allowFuzzyExtraction;
	}	

	public String getId() {
		return id;
	}
	@JsonProperty("isOverridable")
	public boolean isOverridable() {
		return isOverridable;
	}

	public void setOverridable(boolean isOverridable) {
		this.isOverridable = isOverridable;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("isEnum")
	public boolean isEnum() {
		return isEnum;
	}

	public void setEnum(boolean isEnum) {
		this.isEnum = isEnum;
	}

	public List<EntryLanguage> getEntriesLanguage() {
		if (entriesLanguage == null) {
			entriesLanguage = new ArrayList<EntryLanguage>();
		}
		return entriesLanguage;
	}

	public void setEntriesLanguage(List<EntryLanguage> entriesLanguage) {
		this.entriesLanguage = entriesLanguage;
	}

	public void addEntry(EntryLanguage entry) {
		if (entriesLanguage == null) {
			entriesLanguage = new ArrayList<EntryLanguage>();
		}
		entriesLanguage.add(entry);
	}
	
	public generator.Entity getBotEntity() {
		if (this.isEnum() == false) {
			generator.Entity ret = GeneratorFactory.eINSTANCE.createEntity();
			ret.setName(this.getName());
			for (EntryLanguage entryLan : this.getEntriesLanguage()) {
				LanguageInput input = GeneratorFactory.eINSTANCE.createLanguageInput();
				input.setLanguage(Agent.castLanguage(entryLan.getLanguage()));
				for (Entry entry : entryLan.getEntries()) {
					EntityInput aux;
					if ((aux = input.getInput(entry.getValue())) != null) {
						if (aux instanceof SimpleInput) {
							for (String syn : entry.getSynonyms()) {
								if (!((SimpleInput) aux).getValues().contains(syn)) {
									((SimpleInput) aux).getValues().add(syn);
								}
							}
						}
					} else {
						SimpleInput simpleInput = GeneratorFactory.eINSTANCE.createSimpleInput();
						simpleInput.setName(entry.getValue());
						simpleInput.getValues().addAll(entry.getSynonyms());
						input.getInputs().add(simpleInput);
					}
				}
				ret.getInputs().add(input);
			}
			return ret;
		} else {
			// TODO
			return null;
		}
	}
}
