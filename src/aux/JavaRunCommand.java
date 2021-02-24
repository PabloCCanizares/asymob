package aux;
import java.io.*;
import java.util.LinkedList;

public class JavaRunCommand {

	private final boolean DEBUG_COMMAND = false;
	private String strInputPhrase;
	private String strProgramPath;
	private String strProgram;
	
	private LinkedList<String> outputList;
	
	public JavaRunCommand()
	{
		this.strInputPhrase = "";
		this.strProgram = "python3";
		this.outputList = new LinkedList<String>();
	}

	public void setInputPhrase(String strInputPhrase)
	{
		this.strInputPhrase = strInputPhrase;
	}

	public void setProgramPath(String strProgramPath)
	{
		this.strProgramPath = strProgramPath;
	}

	public void resetLastResults()
	{
		outputList.clear();
	}
	public LinkedList<String> getLastResults() {
		return outputList;
	}
	
	public void setProgram(String strProgramId)
	{
		strProgram = strProgramId;
	}
	public boolean runCommand(String strCommand)
	{
		boolean bRet = false;
		String s = null;
		
		if(!strCommand.isEmpty() && !strProgramPath.isEmpty())
		{	
			try {
				
				// run the Unix "ps -ef" command
				// using the Runtime exec method:
				//Process p = Runtime.getRuntime().exec("ps -ef");
				//if(strProgram.isEmpty())
				//	strProgram = "python3";
				if(DEBUG_COMMAND)
					System.out.println("Executing command:"+strProgram+" "+strProgramPath+" "+strCommand);
				
				Process p = Runtime.getRuntime().exec(strProgram+" "+strProgramPath+" "+strCommand);

				//We config
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(p.getOutputStream()));

				if(!strInputPhrase.isEmpty())
				{
					writer.write(strInputPhrase);
					writer.newLine();
					writer.close();
				}


				BufferedReader stdInput = new BufferedReader(new 
						InputStreamReader(p.getInputStream()));

				BufferedReader stdError = new BufferedReader(new 
						InputStreamReader(p.getErrorStream()));

				// read the output from the command
				if(DEBUG_COMMAND)
					System.out.println("Here is the standard output of the command:\n");
				while ((s = stdInput.readLine()) != null) {
					if(DEBUG_COMMAND)
						System.out.println(s);
					outputList.add(s);
				}

				// read any errors from the attempted command
				if(DEBUG_COMMAND)
				{
					System.out.println("Here is the standard error of the command (if any):\n");
					while ((s = stdError.readLine()) != null) {
						System.out.println(s);
					}
				}
	
				bRet = true; 
				
			}
			catch (IOException e) {
				System.out.println("exception happened - here's what I know: ");
				e.printStackTrace();
				System.exit(-1);
			}

		}
		
		return bRet;
	}
	public static void main(String args[]) {

		String s = null;

		try {

			// run the Unix "ps -ef" command
			// using the Runtime exec method:
			//Process p = Runtime.getRuntime().exec("ps -ef");
			Process p = Runtime.getRuntime().exec("python3 /localSpace/chatbots/python_tests/test.py");

			//We config
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(p.getOutputStream()));

			writer.write("antonio");
			writer.newLine();
			writer.close();

			BufferedReader stdInput = new BufferedReader(new 
					InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new 
					InputStreamReader(p.getErrorStream()));

			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			// read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}

			System.exit(0);
		}
		catch (IOException e) {
			System.out.println("exception happened - here's what I know: ");
			e.printStackTrace();
			System.exit(-1);
		}
	}


}