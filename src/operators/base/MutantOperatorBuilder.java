package operators.base;

import utteranceVariantCore.VariantGenByCommand;

public class MutantOperatorBuilder {

	public MutationOperator buildMutationOperator(MutationOperator mutateOpIn,
			VariantGenByCommand uttVariantGeneratorCommand) {
		
		if(mutateOpIn != null)
		{
			uttVariantGeneratorCommand.setCommandIn(mutateOpIn.ToString());
			mutateOpIn.setVariantGen(uttVariantGeneratorCommand);
		}
		
		return mutateOpIn;
		
	}

	public MutationOperator buildMutationOperator(MutationOperator mutateOpIn) {
		
		if(mutateOpIn != null)
		{
			//Check if all the elements are correct and valids
			
		}
		
		return mutateOpIn;
		
	}
	
}
