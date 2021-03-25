package auxiliar;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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
	public static LinkedList<String> SplitUsingTokenizer(String subject) {
		   StringTokenizer strTkn;
		   LinkedList<String> retList;
		   
		   strTkn = new StringTokenizer(subject);		   
		   retList = new LinkedList<String>();

		   while(strTkn.hasMoreTokens())
			   retList.add(strTkn.nextToken());

		   return retList;
		}
	public static LinkedList<String> deleteRepeatedTerms(LinkedList<String> retList) {
		return retList = new LinkedList<>(new HashSet<>(retList));
	}
	
	public static LinkedList<String> splitPhrase(String strPhrase)
	{
		LinkedList<String> retList;
		String[] inputs;
		
		retList = null;
		inputs = strPhrase.split("(?!^)\\b");
		
		if(inputs != null)
		{
			retList = new LinkedList<String>(Arrays.asList(inputs));
		}
	
		return retList;
	}
	private static long get64LeastSignificantBitsForVersion1() {
		Random random;
		long random63BitLong, variant3BitFlag;
		
	    random = new Random();
	    random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
	    variant3BitFlag = 0x8000000000000000L;
	    
	    return random63BitLong + variant3BitFlag;
	}
	private static long get64MostSignificantBitsForVersion1() {
		long seconds, nanos, timeForUuidIn100Nanos, least12SignificatBitOfTime, version;
		LocalDateTime start;
		Duration duration;
		
	    start = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
	    duration = Duration.between(start, LocalDateTime.now());
	    
	    seconds = duration.getSeconds();
	    nanos = duration.getNano();
	    timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100;
	    least12SignificatBitOfTime = (timeForUuidIn100Nanos & 0x000000000000FFFFL) >> 4;
	    version = 1 << 12;
	    
	    return 
	      (timeForUuidIn100Nanos & 0xFFFFFFFFFFFF0000L) + version + least12SignificatBitOfTime;
	}
	public static UUID generateType1UUID() {

	    long most64SigBits = get64MostSignificantBitsForVersion1();
	    long least64SigBits = get64LeastSignificantBitsForVersion1();

	    return new UUID(most64SigBits, least64SigBits);
	}
	public static String generateType1UUID_String()
	{
		String strUuid;
		UUID intentId;
		
		strUuid = null;
		
		intentId = Common.generateType1UUID();
		
		if(intentId != null)
			strUuid = intentId.toString();
		
		return strUuid;
	}

	public static boolean checkStringContainedIn(String tag, String tag2) {
		boolean bRet;
		
		bRet  = false;
		if(tag != null &tag2!=null)
		{
			bRet = tag.contains(tag2) || tag2.contains(tag);
		}
		return bRet;
	}
	
	public static String progressBar(int currentValue, int maxValue) {
	    int progressBarLength = 33; //
	    if (progressBarLength < 9 || progressBarLength % 2 == 0) {
	        throw new ArithmeticException("formattedPercent.length() = 9! + even number of chars (one for each side)");
	    }
	    int currentProgressBarIndex = (int) Math.ceil(((double) progressBarLength / maxValue) * currentValue);
	    String formattedPercent = String.format(" %5.1f %% ", (100 * currentProgressBarIndex) / (double) progressBarLength);
	    int percentStartIndex = ((progressBarLength - formattedPercent.length()) / 2);

	    StringBuilder sb = new StringBuilder();
	    sb.append("[");
	    for (int progressBarIndex = 0; progressBarIndex < progressBarLength; progressBarIndex++) {
	        if (progressBarIndex <= percentStartIndex - 1
	        ||  progressBarIndex >= percentStartIndex + formattedPercent.length()) {
	            sb.append(currentProgressBarIndex <= progressBarIndex ? " " : "=");
	        } else if (progressBarIndex == percentStartIndex) {
	            sb.append(formattedPercent);
	        }
	    }
	    sb.append("]");
	    return sb.toString();
	}

	public static int generateRandom(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public static String listToString(LinkedList<String> listRet) {
		// TODO Auto-generated method stub
		return null;
	}
}