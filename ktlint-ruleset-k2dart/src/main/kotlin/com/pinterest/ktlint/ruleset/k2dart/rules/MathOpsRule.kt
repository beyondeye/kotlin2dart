package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.asDartNode
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.iz
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafElement

// FinalInsteadOfValRule this rule is obsolete, since it has been integrated in VariableTypeBeforeNameRule
// but we keep it here because we use it as template for generating new rules
public class MathOpsRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="math_ops"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if(node.isDartNode()) return
        if (node iz ElementType.VAL_KEYWORD) {
            //*DARIO* emit is useless in k2dart, unless we want to write some log of what is corrected (useful for debugging)
            // emit(node.startOffset, ruleName, true)
            val newNode=(node as LeafElement).replaceWithText("final")  //update the AST with the converted keyword
            newNode.asDartNode() //set also the dart node flag so that we will avoid processing this node again
        }
    }
}
