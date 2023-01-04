In the ``code_to_translate`` directory, put the code that you want to translate from Kotlin to Dart.
This way you can translate the files by simply running the [k2dart.bat](../k2dart.bat) script that will automatically
scan for all kotlin files in all subdirectories. 
Note that you first need to build  ``k2dart`` by running the [rebuild_k2dart.bat](../../rebuild_k2dart.bat) script
in the root directory and then ``cd`` to the ``k2dart-executable`` directory and run the [fetch_k2dart_executable.bat](../fetch_k2dart_executable.bat) script. 
