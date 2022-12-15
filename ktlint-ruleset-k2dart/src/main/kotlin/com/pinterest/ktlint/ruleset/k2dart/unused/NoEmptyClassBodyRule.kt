package com.pinterest.ktlint.ruleset.k2dart.unused

import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.children
import com.pinterest.ktlint.core.ast.isPartOf
import com.pinterest.ktlint.core.ast.nextLeaf
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtObjectLiteralExpression

public class NoEmptyClassBodyRule : Rule("no-empty-class-body") {

    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if (node.elementType == ElementType.CLASS_BODY &&
            node.firstChildNode?.let { n ->
                n.elementType == ElementType.LBRACE &&
                    n.nextLeaf { it.elementType != ElementType.WHITE_SPACE }?.elementType == ElementType.RBRACE
            } == true &&
            !node.isPartOf(KtObjectLiteralExpression::class) &&
            node.treeParent.firstChildNode.children().none { it.text == "companion" }
        ) {
            emit(node.startOffset, "Unnecessary block (\"{}\")", true)
            if (autoCorrect) {
                val prevNode = node.treePrev
                if (prevNode.elementType == ElementType.WHITE_SPACE) {
                    // remove space between declaration and block
                    prevNode.treeParent.removeChild(prevNode)
                }
                // remove block
                node.treeParent.removeChild(node)
            }
        }
    }
}
