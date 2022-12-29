package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.izNot
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement

// //todo the .inv() op is not yet translated
public class BitOpsRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="bits-ops"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if(node.isDartNode()) return
        if (node izNot ElementType.OPERATION_REFERENCE) return
        /*
            ~.psi.KtOperationReferenceExpression (OPERATION_REFERENCE)
              ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "xor"
        */
        val opIdentifierNode=node.firstChildNode ?: return
        if( opIdentifierNode !is LeafPsiElement) return
        val newOpName= when(opIdentifierNode.text) {
            "xor" -> "^"
            "and" -> "&"
            "or" -> "|"
            "shl" -> "<<"
            "shr" -> ">>"
            "ushr" -> ">>>"
            else ->null
        } ?: return

        opIdentifierNode.replaceWithText(newOpName)

        //todo the .inv() op is not yet translated
    }
}
