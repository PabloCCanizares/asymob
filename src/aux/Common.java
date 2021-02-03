package aux;

import java.io.File;
import java.util.LinkedList;

public class Common {

	/** Creates parent directories if necessary. Then returns file */
	public static File fileWithDirectoryAssurance(String directory, String filename) {
	    File dir = new File(directory);
	    if (!dir.exists()) dir.mkdirs();
	    return new File(directory + "/" + filename);
	}
	
	public static boolean checkDirectory(String directory) {
		boolean bRet;
	    File dir;
	    
	    try
	    {
	    	bRet = true;
	    	dir = new File(directory);
	    	if (!dir.exists()) dir.mkdirs();
	    }
	    catch(Exception e)
	    {
	    	bRet = false;
	    }
	    	
	    return bRet;
	}

	public static void addOrReplaceToken(LinkedList<String> currentPhrase, int nIndex, String strText) {
		
		//Protegemos
		if(currentPhrase != null && nIndex >=0 && strText != null)
		{
			if(nIndex<currentPhrase.size())
			{
				//Replace
				currentPhrase.remove(nIndex);
				currentPhrase.add(nIndex, strText);
			}
			else
			{
				currentPhrase.add(nIndex, strText);
			}
		}
		
	}


	public static void addCopyToRetList(LinkedList<String> composedPhrasesList,
			LinkedList<String> currentPhrase) {
		String strPhrase;
		
		if(composedPhrasesList != null && currentPhrase != null)
		{
			strPhrase = "";
			for(String strPhrasePart: currentPhrase)
			{
				strPhrase += strPhrasePart;
			}
			composedPhrasesList.add(strPhrase);
		}
		
	}
}
