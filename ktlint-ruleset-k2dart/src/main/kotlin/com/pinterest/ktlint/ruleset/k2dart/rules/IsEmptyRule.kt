package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.asDartNode
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.izNot
import com.pinterest.ktlint.core.ast.nextCodeSibling
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

public class IsEmptyRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="is-empty"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if(node.isDartNode()) return
        if (node izNot ElementType.IDENTIFIER) return
        val identifierName=node.text
        if(identifierName != "isEmpty" && identifierName!="isNotEmpty") return
        val refExprNode = node.treeParent
        if(refExprNode izNot  ElementType.REFERENCE_EXPRESSION) return
        val argListNode=refExprNode.nextCodeSibling()
        if(argListNode izNot ElementType.VALUE_ARGUMENT_LIST) return
        if(argListNode!!.text!="()") return //not empty arg list
        //ok so we have identified a call to isEmpty/isNotEmpty: remove the arglist to transform it in a property
        argListNode.treeParent.removeChild(argListNode)
        node.asDartNode()
    }
}
