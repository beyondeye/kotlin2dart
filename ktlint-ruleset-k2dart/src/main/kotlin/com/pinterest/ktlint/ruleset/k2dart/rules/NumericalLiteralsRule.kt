package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.asDartNode
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.iz
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement

public class NumericalLiteralsRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="numerical-literals"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if(node.isDartNode()) return
        var newLiteral:String?=null
        if (node iz ElementType.FLOAT_LITERAL) {
            val literal=node.text
            if(literal.endsWith("f", ignoreCase = true)) { //TODO: this can be optimized
                //NOTE: DART accept definition of float variables also using an integer literals
                //      so we don't need to add here the suffix ".0" if it is missing
                newLiteral=literal.substring(0, literal.length - 1)
            }
        }
        if (node iz ElementType.INTEGER_LITERAL) {
            val literal=node.text
            if(literal.endsWith("L", ignoreCase = true)) { //TODO: this can be optimized
                newLiteral=literal.substring(0, literal.length - 1)
            }
        }
        if(newLiteral!=null) {
            //*DARIO* emit is useless in k2dart, unless we want to write some log of what is corrected (useful for debugging)
            // emit(node.startOffset, ruleName, true)
            val newNode=(node as LeafPsiElement).replaceWithText(newLiteral) //update the AST with the converted literal
            newNode.asDartNode() //set also the dart node flag so that we will avoid processing this node again
        }
    }
}
