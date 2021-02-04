package aux;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.StringTokenizer;

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
			LinkedList<String> currentPhrase, String strSeparator) {
		String strPhrase, strPhrasePart;
		
		if(composedPhrasesList != null && currentPhrase != null)
		{
			
			strPhrase = "";
			for(int i=0;i<currentPhrase.size();i++)
			{
				strPhrasePart = currentPhrase.get(i);
				if(i+1 < currentPhrase.size())
					strPhrase += strPhrasePart+strSeparator;
				else
					strPhrase += strPhrasePart;
			}
			composedPhrasesList.add(strPhrase);
		}
	}
	
	public static URL openWordNet()
	{
		String wnhome;
		String strPath;
		URL url;
		
		url = null;		
		try{
			wnhome = System.getenv("WNHOME");
			
			if(wnhome == null)
				wnhome = "/usr/local/WordNet-3.0";
			strPath = wnhome + File.separator + "dict";
			url = new URL("file", null, strPath); 
		} 
		catch(MalformedURLException e)
		{
			e.printStackTrace(); 
		}
		
		return url;
	}
	
	public static LinkedList<String> SplitUsingTokenizer(String subject, String delimiters) {
		   StringTokenizer strTkn;
		   LinkedList<String> retList;
		   
		   strTkn = new StringTokenizer(subject, delimiters);		   
		   retList = new LinkedList<String>();

		   while(strTkn.hasMoreTokens())
			   retList.add(strTkn.nextToken());

		   return retList;
		}

	public static LinkedList<String> deleteRepeatedTerms(LinkedList<String> retList) {
		return retList = new LinkedList<>(new HashSet<>(retList));
	}
}
