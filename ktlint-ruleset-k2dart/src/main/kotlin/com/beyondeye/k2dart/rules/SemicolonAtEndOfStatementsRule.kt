package com.beyondeye.k2dart.rules

import com.beyondeye.k2dart.utils.asDartNode
import com.beyondeye.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement

//
public class SemicolonAtEndOfStatementsRule : Rule(ruleName) {
    public companion object {
        public const val ruleName:String="basic-types-names"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if (node.isDartNode()) return
        //~.psi.KtTypeReference (TYPE_REFERENCE)
        //1:               ~.psi.KtUserType (USER_TYPE)
        //1:                 ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
        //1:                   ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "Float"
        if (node.elementType != ElementType.TYPE_REFERENCE) return
        val identifierNode: ASTNode? = node.firstChildNode.firstChildNode.firstChildNode
        if (identifierNode?.isDartNode() != false) return
        if (identifierNode.elementType != ElementType.IDENTIFIER) return
        identifierNode as LeafPsiElement
        val identifier = identifierNode.text
        val newIdentifier: String? =
            when (identifier) {
                "Float" -> "double"
                "Double" -> "double"
                "Int" -> "int"
                "Long" -> "int"
                "Boolean" -> "bool"
                else -> null
            }
        newIdentifier?.let {
            //*DARIO* important: need also to call emit, that log that we identified something to change in the code
            //        otherwise, the mutated flag will not be set, and the corrected ast will be ignored
            emit(node.startOffset, ruleName, true)
            val newIdentifierNode = identifierNode.replaceWithText(newIdentifier)
            newIdentifierNode.asDartNode()
        }
    }
}

