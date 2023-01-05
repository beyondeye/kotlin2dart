package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.iz
import com.pinterest.ktlint.core.ast.izNot
import com.pinterest.ktlint.core.ast.nextCodeSibling
import com.pinterest.ktlint.ruleset.k2dart.utils.addChildAfter
import com.pinterest.ktlint.ruleset.k2dart.utils.createEmptyClassBodyNode
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

// add an empty class body to a class, if it is missing
public class MissingClassBodyRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="missing-class-body"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if(node.isDartNode()) return
        if(node izNot ElementType.CLASS) return
        var classBodyNode=node.findChildByType(ElementType.CLASS_BODY)
        if(classBodyNode!=null) return //we already have one
        //generate an empty class body
        classBodyNode = createEmptyClassBodyNode()
        node.addChildAfter( classBodyNode,node.lastChildNode)
        val semicolonAfterClassDefNode=node.nextCodeSibling()
        if(semicolonAfterClassDefNode iz ElementType.SEMICOLON) //we had a semicolon immediately after a missing class body: remove it
        {
            semicolonAfterClassDefNode!!.treeParent.removeChild(semicolonAfterClassDefNode)
        }
    }
}
