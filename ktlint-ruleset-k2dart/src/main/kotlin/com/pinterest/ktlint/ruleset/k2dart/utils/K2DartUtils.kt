package com.pinterest.ktlint.ruleset.k2dart.utils

import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.openapi.util.Key

internal object K2Dart {
    val dartNodeKey= Key<Boolean>("*DRT*")
}

internal fun ASTNode.isDartNode() = getCopyableUserData(K2Dart.dartNodeKey)!=null
internal fun ASTNode.isNotDartNode() = getCopyableUserData(K2Dart.dartNodeKey)==null

internal fun ASTNode.asDartNode():ASTNode {
    this.putCopyableUserData(K2Dart.dartNodeKey,true)
    return this
}
