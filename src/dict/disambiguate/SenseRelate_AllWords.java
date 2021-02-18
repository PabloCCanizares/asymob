package dict.disambiguate;

import java.util.LinkedList;

import aux.JavaRunCommand;

public class SenseRelate_AllWords implements IWSD_Disambiguator {

	@Override
	public boolean disambiguatePhrase(String strPhrase) {
		boolean bRet;
		JavaRunCommand command;
		LinkedList<String> resList;
		
		bRet = false;
		command = new JavaRunCommand();
		
		command.setProgram("perl");
		command.setInputPhrase(strPhrase);
		command.setProgramPath(System.getProperty("user.dir")+"/scripts/wsd_asymob.pl");
		if(command.runCommand("--trace 0 --context \"\" --format raw\n"))
		{
			resList = command.getLastResults();
			
			//Here we parse: split in lines ' ' + each component split in lines with #
			//if()
			
		}
		
		//Now, we must parse and generate a LinkedList<WordTagged>
		return bRet;
	}

}
