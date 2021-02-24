package dict.disambiguate;

import edu.stanford.nlp.ling.TaggedWord;

/**
 * Dissambiguated tagged word
 * @author Pablo C. Cañizares
 *
 */
public class DisTaggedWord extends TaggedWord{

	private static final long serialVersionUID = -3503672497659989431L;
	private int nSynsetOrder;

	public DisTaggedWord()
	{
		super();
	}
	public int getSynsetOrder() {
		return nSynsetOrder;
	}

	public void setSynsetOrder(int nSynsetOrder) {
		this.nSynsetOrder = nSynsetOrder;
	}
	public void setTag(String strIn)
	{
		if(strIn.equals("n"))
			super.setTag("NN");
		else if(strIn.equals("v"))
			super.setTag("VB");
		else
			super.setTag(strIn);
		
		//TODO: Complete
	}
}
