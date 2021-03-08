package variants;

import java.util.LinkedList;

public interface IVariantGenerator {

	LinkedList<String> doMutate(String strInputPhrase);
}
