package validation;

import java.util.LinkedList;

import generator.Bot;
import validation.info.BotIssue;

public interface IBotValidationMethod {

	LinkedList<BotIssue> getReport();

	boolean doValidate(Bot botIn);
}
