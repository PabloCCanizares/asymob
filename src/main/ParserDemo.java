package main;
import java.util.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.trees.*;
//import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

class ParserDemo {
public static void main(String[] args) {
/*LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});
String[] sent = { "This", "is", "an", "easy", "sentence", "." };
List<CoreLabel> rawWords = Sentence.toCoreLabelList(sent);
Tree parse = lp.apply(rawWords);
parse.pennPrint();
System.out.println();
TreebankLanguagePack tlp = new PennTreebankLanguagePack();
GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
System.out.println(tdl);
System.out.println();
TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
tp.printTree(parse);*/
}}