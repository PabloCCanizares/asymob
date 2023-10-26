package coverage;

import generator.Bot;

public interface ICoverageMeter {

	float measureCoverage(Bot botIn, CoverageMethod method);
}
