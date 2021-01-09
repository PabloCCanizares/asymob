package utteranceVariantCore;

import java.util.LinkedList;

import operators.base.MutationOperator;

public interface IVariantGenerator {

	LinkedList<String> doMutate(String strInputPhrase);
}
