package auxiliar;

import java.io.*;
import java.util.Properties;

public class AsymobProperties extends Properties {
	private static String PYTHON = "python3";
	private static AsymobProperties prop = new AsymobProperties();
	
	private AsymobProperties() {
		FileInputStream is;
		try {
			is = new FileInputStream("asymob.properties");
			this.load(is);
		} catch (FileNotFoundException e) {			
			generateFile();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	private void generateFile() {
		this.setProperty("python", PYTHON);
		try {
			this.store(new PrintWriter("asymob.properties"), "default file generated");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static AsymobProperties getInstance() {		
		return prop;
	}	
}
