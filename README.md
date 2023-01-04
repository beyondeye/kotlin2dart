# Kotlin2Dart
This is a Kotlin to Dart transpiler, based  on the [ktlint engine](https://github.com/pinterest/ktlint).

It was not designed to be a complete transpiler, but instead to facilitate manual porting of Kotlin code to Dart.

The output Dart code is most cases will not be actual valid Dart code. The purpose is instead to make it easier and faster
for a programmer to complete the job of porting the code.

For a guide on how different language construct compares in the two languages, see [kotlin is dart](https://beyondeye.github.io/kotlin_is_dart/).

For porting code that relies on kotlin standard libraries and collections, we rely on
the [kt_dart package](https://pub.dev/packages/kt_dart), in other words, ``kt.dart`` will be a required dependency of
the translated code.

## Example of transpiled code


## Building the k2dart executable jar file.
First clone this repository.
Then, in order to build the ``ktlint.jar`` file, you need to run the ``shadowJarExecutable`` gradle task:
See [rebuild_k2dart.bat](./rebuild_k2dart.bat) in the root project directory.

Then ``cd`` to the [k2dart-executables](./k2dart-executables) directory
and run the [fetch_k2dart_executable.bat](./k2dart-executables/fetch_k2dart_executable.bat) script that
will copy the jar file to this directory and rename it ``k2dart.jar``

## Project Directory Structure
All the modules that come from the original ``ktlint`` project (directories whose names starts with 
``ktlint-``) are not probably going to be of interest, except for [ktlint-ruleset-k2dart](./ktlint-ruleset-k2dart) 
 that is actually a new module, not present in ``ktlint``, that contains the rules that are used to transform Kotlin code into Dart.

A very important directory is [k2dart-executables](./k2dart-executables). When translating Kotlin code to Dart you
will probably work in this directory. The suggested workflow, is to copy the kotlin code to convert in the
[code_to_translate](./k2dart-executables/code_to_translate) directory and then run the [k2dart.bat](./k2dart-executables/k2dart.bat) script.


## List of existing rules for translating Kotlin code to Dart
See [here](./ktlint-ruleset-k2dart/src/main/kotlin/com/pinterest/ktlint/ruleset/k2dart/rules) for the list of current rules.

See [here](./ktlint-ruleset-k2dart/src/test/kotlin/com/beyondeye/k2dart) for the corresponding tests.


## Kotlin code to be careful with, when using  ``k2dart``.
- Code containing low level async code (low level coroutine builders like ``suspendCoroutine`` ) should be
definitely being translated manually. Also, extra care should be put in methods transformed
from ``suspend`` methods to ``async`` functions, that all calls to ``suspend`` functions 
must be prefixed with ``await``. Currently, this is not done automatically.
- Class constructors syntax is very different in Dart, and only translation of basic kotlin primary
  constructor is supported. Most constructor declaration code would be probably need manual translation.



## How to write a new rule to convert some not yet supported Kotlin syntax
- First make sure you have built k2dart as described [here](#building-the-k2dart-executable-jar-file)
- Then you should create a new kotlin code file that contains the syntax that you would like to add support for in the transpiler.
  You should put it in the [k2dart-executables/test_code](./k2dart-executables/test_code) directory. See the other files in this directory
  for examples.
- Next run [``k2dart_printast.bat``](./k2dart-executables/k2dart_printast.bat) script to just output the Abstract Syntax Tree (AST) for the code you have written.
 
  ```
  k2dart_printast.bat  .\test_code\<your_code_filename>.kts
  ```
- Once you have the AST of the code with syntax you want to support, you can analyze it to understand how
 to write a new k2dart rule to support it.
- There is a kotlin script that automates generating the boilerplate code for a new rule, a test class for it and linking it in the
  [``K2DartRuleSetProvider.kt``](./ktlint-ruleset-k2dart/src/main/kotlin/com/pinterest/ktlint/ruleset/k2dart/K2DartRuleSetProvider.kt). 
  It assumes that you already have a test kotlin code file, somewhere in the [``test_code``](./k2dart-executables/test_code) directory or subdirectory.
  To run it you need to have the kotlin CLI compiler installed in the [kotlinc](./kotlinc) directory. See [here](./kotlinc/installing_kotlinc.md) on
      instructions on how to install it. Once you have ``kotlinc`` installed ``cd`` to the
      [k2dart-executables](./k2dart-executables) directory. and run
```
.\generate_new_rule_boilerplate.bat .\test_code\<test_code_filename>.kts
```
The new generated rule name will be equal to ``<test_code_filename>``

## Writing a new rule without using the ``generate_new_rule_boilerplate`` script.
For writing a new k2dart rule, it is best to start by copying an existing rule from the [rules directory](./ktlint-ruleset-k2dart/src/main/kotlin/com/beyondeye/k2dart/rules)
- in the [ktlint-ruleset-k2dart](./ktlint-ruleset-k2dart) module, and add it in the same directory. You also have to add the new rule
to the ``k2dart`` [ruleset file](./ktlint-ruleset-k2dart/src/main/kotlin/com/beyondeye/k2dart/CustomRuleSetProvider.kt) in ``getRuleProviders()`` method. Note that unlike in the original ``ktlint`` each rule
is associated with a priority that determine the execution order of the rule (higher priority: rule executes earlier). Default priority, if not specified, is zero. An alternative way
to estabilish dependency in execution order between rules, that is also used in the original ktlint, is to define
the ``visitorModifiers`` argument in the constructor of ``Rule`` with the list of dependency, for example
```kotlin
class MyKotlinToDartRule :
    Rule(
        id = "my-kotlin-to-dart-rule",
        visitorModifiers = setOf(
            VisitorModifier.RunAfterRule(
                ruleId = "k2dart:some-other-k2dart-rule",
                loadOnlyWhenOtherRuleIsLoaded = true, //or false
                runOnlyWhenOtherRuleIsEnabled = true, //or false
            ),
            VisitorModifier.RunAfterRule(
              ruleId = "k2dart:another-k2dart-rule",
              loadOnlyWhenOtherRuleIsLoaded = true, //or false
              runOnlyWhenOtherRuleIsEnabled = true, //or false
            ),
          // VisitorModifier.RunAsLateAsPossible, //optionally add this
        ),
    )
    {
        // ... rule class definition here
    }
```
**Important** make sure that you define a unique name for your rule by overriding the ``ruleName`` property, or in 
general passing a unique ``id`` parameter to the parent ``Rule`` class constructor.
Note that ordered defined by priority will override order of rules defined by ``RunAfterRule``
- For each new rule you define, define also a test class in [test](./ktlint-ruleset-k2dart/src/test/kotlin/com/beyondeye/k2dart)
directory see for example [this test class](ktlint-ruleset-k2dart/src/test/kotlin/com/beyondeye/k2dart/BasicTypeNamesRuleTest.kt).


# Contributing
Writing additional rules to handle transpiling of code that is not currently supported are welcome.

Writing rules is not very difficult. Just take a look at the existing rules to have an idea of how 
it works, or open a issue with what you want to do, and we will try to help with adding the new rules

Thanks to the modular architecture of ktlint, it is also very easy to define different ruleset
to use in different scenarios.

# License
Copyright 2023 by Dario Elyasy.

See details [here](./LICENSE).

