package operators.base;

import utteranceVariantCore.VariantGenByCommand;

public class MutantOperatorBuilder {

	public MutationOperator buildMutationOperator(MutationOperator mutateOpIn,
			VariantGenByCommand uttVariantGeneratorCommand) {
		
		//TODO: Analyse if the variant command exists, if not -> exception and return null element
		
		try
		{
			if(checkCommandProgram(uttVariantGeneratorCommand.getProgramPath()))
				throw new Exception("The mutation generator associated to the mutation operator not exists");
			
			if(mutateOpIn == null)
				throw new Exception("The mutation operator provided is null");
			
			uttVariantGeneratorCommand.setCommandIn(mutateOpIn.ToString());
			mutateOpIn.setVariantGen(uttVariantGeneratorCommand);
		}
		catch(Exception e)
		{
			System.out.println("[buildMutationOperator] - Exception while creating a mutation operator: "+e.getMessage());
			mutateOpIn = null;
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

	private boolean checkCommandProgram(String programPath) {

		return false;
	}
}
