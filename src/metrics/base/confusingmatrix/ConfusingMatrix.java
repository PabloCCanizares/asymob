package metrics.base.confusingmatrix;

public class ConfusingMatrix {

	ConfusingRelationPoint[][] confusingMatrix;
	int nDimPhrases;
	public ConfusingMatrix(int nPhrases)
	{
		nDimPhrases = nPhrases;
		confusingMatrix = new ConfusingRelationPoint[nPhrases][nPhrases];
	}
	public void addConfusingPoint(int nX, int nY, ConfusingRelationPoint confusingPointIn)
	{
		if(nX < this.nDimPhrases && nY < this.nDimPhrases)
		{
			confusingMatrix[nX][nY] = confusingPointIn;
		}
	}
	
	public ConfusingRelationPoint getInfo(int nX, int nY)
	{
		ConfusingRelationPoint confusingPoint;
		
		confusingPoint = null;
		if(nX < this.nDimPhrases && nY < this.nDimPhrases)
		{
			confusingPoint = confusingMatrix[nX][nY];
		}
		
		return confusingPoint;
	}
	
	public void print()
	{
		ConfusingRelationPoint cfPoint;
		for(int i=0;i<nDimPhrases;i++)
		{
			for(int j=0;j<nDimPhrases;j++)
			{
				cfPoint = confusingMatrix[i][j];
				System.out.printf("%f ", cfPoint.getCnfElement());
			}
			System.out.printf("\n");
		}
	}
	public String ToString() {
		String strRet;
		ConfusingRelationPoint cfPoint;
		
		strRet = "";
		for(int i=0;i<nDimPhrases;i++)
		{
			for(int j=0;j<nDimPhrases;j++)
			{
				cfPoint = confusingMatrix[i][j];
				strRet.concat(String.format("%f ", cfPoint.getCnfElement()));
			}
			strRet.concat("\n");			
		}
		
		return strRet;
	}
}
