import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

// for APIs used in this script see
// https://docs.oracle.com/javase/tutorial/essential/io/pathOps.html
// https://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.html
// https://www.techiedelight.com/write-to-file-kotlin/#:~:text=Write%20to%20a%20file%20in%20Kotlin%201%201.,File.bufferedWriter%20%28%29%20function%20...%207%207.%20Using%20PrintStream
// https://www.techiedelight.com/read-a-text-file-into-string-in-kotlin/#:~:text=Read%20a%20text%20file%20into%20a%20string%20in,character%20array.%20...%204%204.%20Using%20Input%20Stream
// how this script works:
// it takes as input the filename of a kotlin file in /test_code directoyr

fun readFileToString(path: Path, encoding: Charset=Charsets.UTF_8): String {
    val lines = Files.readAllLines(path, encoding)
    return lines.joinToString(lineSep)
}

val snippetDelim="\"\"\""
val lineSep="\r\n"
fun replaceCodeSnippet(source:String,prefix:String,newSnippet:String):String {
    val prefixPos=source.indexOf(prefix)
    val snippetStart=source.indexOf(snippetDelim,prefixPos);
    val snippetEnd=source.indexOf(snippetDelim,snippetStart+3)
    val trimmed_start=source.substring(0,snippetStart+3)
    val trimmed_end=source.substring(snippetEnd)
    return "$trimmed_start$lineSep$newSnippet$lineSep$trimmed_end"
}

fun appendNewRuleInRuleSetProvider(ruleSetProviderTxt: String, newRuleName: String): String {
    val ruleDeclPrefix="RuleProvider { "
    val ruleDeclSuffix="() },"
    val ruleLine="$ruleDeclPrefix$newRuleName$ruleDeclSuffix"
    // rule already present in list: nothing to do
    if(ruleSetProviderTxt.indexOf(ruleLine)>=0) return  ruleSetProviderTxt
    //scan all rules in list
    var afterRulePos=0;
    var newAfterRulePos=0
    do {
        newAfterRulePos= ruleSetProviderTxt.indexOf(ruleDeclPrefix,afterRulePos)
        if(newAfterRulePos>=0) {
            newAfterRulePos = ruleSetProviderTxt.indexOf(ruleDeclSuffix,newAfterRulePos+1)
        }
        if(newAfterRulePos>=0) {
            afterRulePos=newAfterRulePos+ruleDeclSuffix.length
        }
    } while(newAfterRulePos>=0)

    val before_str=ruleSetProviderTxt.substring(0,afterRulePos)
    val after_str=ruleSetProviderTxt.substring(afterRulePos)
    return  "$before_str$lineSep$ruleLine$lineSep$after_str"
}


val isDebugMode=true

// Get the passed in path to kotlin file, i.e. "-d some/path"
val input_test_code_file_path = if (args.contains("-d")) args[1 + args.indexOf("-d")]  else ""
if(input_test_code_file_path.isEmpty()) throw Exception("Missing  argument with path to file in test_code to base the rule on: specify it with -d /some/path ")
val base_path= Paths.get("C:\\startup\\WORK\\sel_app\\TOOLS\\K2DART\\kotlin2dart")
val executables_path=base_path.resolve("k2dart-executables")
val test_code_path=executables_path.resolve("test_code")
val ruleset_module_path=base_path.resolve("ktlint-ruleset-k2dart")
val src_path=ruleset_module_path.resolve("src")
val src_path_k2dart=src_path.resolve("main\\kotlin\\com\\pinterest\\ktlint\\ruleset\\k2dart\\")
val test_path_k2dart=src_path.resolve("test\\kotlin\\com\\beyondeye\\k2dart")
val src_path_k2dart_rules=src_path_k2dart.resolve("rules")
val ruleset_provider_file_path=src_path_k2dart.resolve("K2DartRuleSetProvider.kt")
val template_rule_file_path=src_path_k2dart_rules.resolve("FinalInsteadOfValRule.kt")
val template_test_file_path=test_path_k2dart.resolve("FinalInsteadOfValRuleTest.kt")

//print(ruleset_file_path.toFile().exists())
//print (template_rule_file_path.toFile().exists())
//print (template_test_file_path.toFile().exists())


//print(src_path_k2dart_rules)
//print(ruleset_file_path)
//print(template_rule_file_path)
//print(test_path_k2dart)

//    if (isDebugMode) "comments.kts" else if (args.contains("-f")) args[1 + args.indexOf("-d")] else ""
val code_path = executables_path.resolve(input_test_code_file_path).normalize()
val code_file=code_path.toFile()

val code_fname = code_path.fileName.toString()

if(code_fname.isEmpty()) {
    val errmsg="""
        error: please specify the name of kotlin code file in test_code directory
         with the syntax -f <filename>.kts
        """
    throw Exception(errmsg)
}

if(!code_file.exists()) {
    throw Exception("file $code_path with test code for new rule not found")
}
if(!code_fname.endsWith(".kts")&& !code_fname.endsWith(".kt")) {
    throw Exception("file $code_fname is not a Kotlin file!")
}

val new_name=code_fname.removeSuffix(".kts").removeSuffix(".kt")
val new_rule_name=new_name+"Rule"
val new_rule_test_name=new_name+"RuleTest"

println("adding template code for new rule: $new_rule_name")

val newrule_file_path=src_path_k2dart_rules.resolve(new_rule_name+".kt")
println("new rule code path: $newrule_file_path")
val newRuleExists=newrule_file_path.toFile().exists()
if(newRuleExists) {
    println("warning: rule with specified name already exists:overwriting code")
}
val newrule_test_file_path=test_path_k2dart.resolve(new_rule_name+"Test.kt");
println("new rule test path: $newrule_test_file_path")

val template_test_file=template_test_file_path.toFile()
val template_rule_file=template_rule_file_path.toFile()

// no need to copy this file we write after adapting them
//template_test_file.copyTo(newrule_test_file_path.toFile(),overwrite = true)
//template_rule_file.copyTo(newrule_file_path.toFile(),overwrite = true)

var ruleTxt=readFileToString(template_rule_file_path)
ruleTxt=ruleTxt.replace("public class FinalInsteadOfValRule","public class $new_rule_name")
ruleTxt=ruleTxt.replace("ruleName:String=\"final-instead-of-val\"","ruleName:String=\"$new_name\"")
newrule_file_path.toFile().writeText(ruleTxt, Charsets.UTF_8)

// read code snippet for test_code for this rule
val snippet = readFileToString(code_path)

var testTxt=readFileToString(template_test_file_path)
testTxt=testTxt.replace("FinalInsteadOfValRuleTest",new_rule_test_name)
testTxt=replaceCodeSnippet(testTxt,"val code =",snippet)
testTxt=replaceCodeSnippet(testTxt,"val formattedCode =",snippet)

newrule_test_file_path.toFile().writeText(testTxt, Charsets.UTF_8)

//now finally add a line with the new rule in K2DartRuleSetProvider
var ruleSetProviderTxt= readFileToString(ruleset_provider_file_path)
ruleSetProviderTxt = appendNewRuleInRuleSetProvider(ruleSetProviderTxt,new_rule_name)
ruleset_provider_file_path.toFile().writeText(ruleSetProviderTxt, Charsets.UTF_8)

//val folders = File(executables_path).listFiles { file -> file.isDirectory() }
//folders?.forEach { folder -> println(folder) }

//File(path+"ast.txt").copyTo(File(path+"ast_copy.txt"))

