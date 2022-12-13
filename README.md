## Kotlin2Dart
This is a Kotlin to Dart transpiler, based  on the [ktlint engine](https://github.com/pinterest/ktlint).

It was designed not be a complete transpiler, but instead to facilitate manual porting of Kotlin code to Dart.

The output dart code is most cases will not be actual valid Dart code. The purpose is instead to make it easier and faster
for a programmer to complete the job of porting the code.
For a guide on how different language construct compares in the two languages, see [kotlin is dart](https://beyondeye.github.io/kotlin_is_dart/)

For porting code that relies on kotlin standard libraries and collections, we rely on
the [kt_dart package](https://pub.dev/packages/kt_dart), in other words, ``kt.dart`` will be a required dependency of
the produced code.
## Contributing
Writing additional rules to handle transpiling of code that is not currently supported are welcome.

Writing rules is not very difficult. Just take a look at the existing rules to have an idea of how 
it works, or open a issue with what you want to do, and we will try to help with adding the new rules

Thanks to the modular architecture of ktlint, it is also very easy to define different ruleset
to use in different scenarios.
