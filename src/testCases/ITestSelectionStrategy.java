package testCases;

import java.util.LinkedList;

import analyser.BotAnalyser;
import analyser.flowTree.TreeBranch;
import testCases.botium.testcase.BotiumTestCase;

public interface ITestSelectionStrategy {
    LinkedList<BotiumTestCase> createTestCasesFromBranches(TreeBranch treeBranch, BotAnalyser botAnalyser);
}

