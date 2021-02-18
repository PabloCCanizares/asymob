package transformation.dialogflow.agent.entities;

import java.util.ArrayList;
import java.util.List;

import generator.EntityInput;
import generator.GeneratorFactory;
import generator.LanguageInput;
import generator.SimpleInput;
import transformation.dialogflow.agent.Agent;

public class Entity {

	private String name;
	private boolean isEnum;
	private List<EntryLanguage> entriesLanguage;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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
		if (isEnum() == false) {
			generator.Entity ret = GeneratorFactory.eINSTANCE.createEntity();
			ret.setName(getName());
			for (EntryLanguage entryLan : getEntriesLanguage()) {
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
