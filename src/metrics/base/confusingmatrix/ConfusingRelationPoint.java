package metrics.base.confusingmatrix;

public class ConfusingRelationPoint {

	String strSrcPhrase;
	String strDstPhrase;
	public String getStrSrcPhrase() {
		return strSrcPhrase;
	}

	public void setSrcPhrase(String strSrcPhrase) {
		this.strSrcPhrase = strSrcPhrase;
	}

	public String getDstPhrase() {
		return strDstPhrase;
	}

	public void setStrDstPhrase(String strDstPhrase) {
		this.strDstPhrase = strDstPhrase;
	}

	public double getCnfElement() {
		return cnfElement;
	}

	public void setCnfElement(double cnfElement) {
		this.cnfElement = cnfElement;
	}

	double cnfElement;
	
	public ConfusingRelationPoint(String strSrcPhrase, String strDstPhrase, double cnfElement)
	{
		this.strDstPhrase = strDstPhrase;
		this.strSrcPhrase = strSrcPhrase;
		this.cnfElement = cnfElement;
	}
}
