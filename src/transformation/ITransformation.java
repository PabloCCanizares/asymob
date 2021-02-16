package transformation;

import generator.Bot;

public interface ITransformation {

	boolean transform(String strPathIn, String strPathOut);
	boolean transform(Bot botIn, String strPathOut);
}
