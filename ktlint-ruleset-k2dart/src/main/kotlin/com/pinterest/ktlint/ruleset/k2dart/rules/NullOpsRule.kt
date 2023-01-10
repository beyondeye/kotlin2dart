package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.asDartNode
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.iz
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement

public class NullOpsRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="null-ops"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if(node.isDartNode()) return
        if(node iz ElementType.ELVIS) {
            node as LeafPsiElement
            node.replaceWithText("??") //the replacement of ?: in dart
            node.asDartNode()
        }
        if(node iz ElementType.EXCLEXCL) {
            node as LeafPsiElement
            node.replaceWithText("!") //in dart only ! is needed not !!
            node.asDartNode()
        }
    }
}
