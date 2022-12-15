package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.*
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

public class SemicolonAtEndOfStatementsRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="semicolon-at-end"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if (node.isDartNode()) return
        var needToAddSemicolon=false
        if(node.elementType==ElementType.PROPERTY) { //KtProperty
            //do we already have a semicolon?
            if(node.nextCodeSibling()?.elementType==ElementType.SEMICOLON) return
            needToAddSemicolon=true

        }else if(node.elementType==ElementType.RETURN) { //KtReturnExpression
            //do we already have a semicolon?
            if(node.nextCodeSibling()?.elementType==ElementType.SEMICOLON) return
            needToAddSemicolon=true
        } else if(node.elementType==ElementType.CALL_EXPRESSION) { //KtCallExpression
            //do we already have a semicolon?
            if(node.nextCodeSibling()?.elementType==ElementType.SEMICOLON) return
            //is this a nested call?
            if(node.treeParent.elementType!=ElementType.BLOCK) return
            needToAddSemicolon=true
        }
        if(needToAddSemicolon) {
            //*DARIO* important: need also to call emit, that log that we identified something to change in the code
            //        otherwise, the mutated flag will not be set, and the corrected ast will be ignored
            emit(node.startOffset, NumericalLiteralsRule.ruleName, true)
            node.upsertSemicolonAfterMe()
        }
    }
}

