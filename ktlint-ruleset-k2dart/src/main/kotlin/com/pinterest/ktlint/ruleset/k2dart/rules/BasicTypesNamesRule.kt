package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.asDartNode
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.izNot
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement

//
public class BasicTypesNamesRule : Rule("$k2dartRulesetId:$ruleName") {
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
        if (identifierNode izNot  ElementType.IDENTIFIER) return
        identifierNode as LeafPsiElement
        val identifier = identifierNode.text
        val newIdentifier: String? =
            when (identifier) {
                "Float" -> "double"
                "Double" -> "double"
                "Int" -> "int"
                "Long" -> "int"
                "Boolean" -> "bool"
                "Unit" -> "void"
                "Any" -> "Object"
                else -> null
            }
        newIdentifier?.let {
            //*DARIO* emit is useless in k2dart, unless we want to write some log of what is corrected (useful for debugging)
            //            emit(node.startOffset, ruleName, true) //
            val newIdentifierNode = identifierNode.replaceWithText(newIdentifier) //update the AST with the converted type identifier
            newIdentifierNode.asDartNode() //set also the dart node flag so that we will avoid processing this node again
        }
    }
}

