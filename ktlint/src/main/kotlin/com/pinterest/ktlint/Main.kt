@file:JvmName("Main")

package com.pinterest.ktlint

import com.pinterest.ktlint.internal.GenerateEditorConfigSubCommand
import com.pinterest.ktlint.internal.KtlintCommandLine
import com.pinterest.ktlint.internal.PrintASTSubCommand
import com.pinterest.ktlint.internal.printCommandLineHelpOrVersionUsage
import picocli.CommandLine

public fun main(args: Array<String>) {
    val ktlintCommand = KtlintCommandLine()
    val commandLine = CommandLine(ktlintCommand)
        .addSubcommand(PrintASTSubCommand.COMMAND_NAME, PrintASTSubCommand())
        .addSubcommand(GenerateEditorConfigSubCommand.COMMAND_NAME, GenerateEditorConfigSubCommand())
    val parseResult = commandLine.parseArgs(*args)

    commandLine.printCommandLineHelpOrVersionUsage()

    if (parseResult.hasSubcommand()) {
        handleSubCommand(commandLine, parseResult)
    } else {
        ktlintCommand.run()
    }
}

private fun handleSubCommand(
    commandLine: CommandLine,
    parseResult: CommandLine.ParseResult,
) {
    when (val subCommand = parseResult.subcommand().commandSpec().userObject()) {
        is PrintASTSubCommand -> subCommand.run()
        is GenerateEditorConfigSubCommand -> subCommand.run()
        else -> commandLine.usage(System.out, CommandLine.Help.Ansi.OFF)
    }
}
