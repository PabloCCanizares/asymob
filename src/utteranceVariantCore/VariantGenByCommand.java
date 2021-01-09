package utteranceVariantCore;

import java.util.LinkedList;

import aux.JavaRunCommand;
import operators.base.MutationOperator;

public class VariantGenByCommand implements IVariantGenerator {

	JavaRunCommand commandRunner;
	String strCommandIn;
	String strProgramPath;
	
	public VariantGenByCommand(String strProgramPath)
	{
		this.configure(strProgramPath);		
	}
	public void setCommandIn(String strCommandIn)
	{
		this.strCommandIn = strCommandIn;
	}
	public void configure(String strProgramPath) {
		commandRunner = new JavaRunCommand();
		commandRunner.setProgramPath(strProgramPath);
	}

	@Override
	public LinkedList<String> doMutate(String strInputPhrase) {
		LinkedList<String> listRet;

		listRet = null;
		commandRunner.setInputPhrase(strInputPhrase);
		
		commandRunner.resetLastResults();
		if (commandRunner.runCommand(strCommandIn))
			listRet = commandRunner.getLastResults();

		return listRet;
	}

	public String getProgramPath() {
		return strProgramPath;
	}
	public void setProgramPath(String strProgramPath) {
		this.strProgramPath = strProgramPath;
	}
}
