package com.beyondeye.k2dart.rules

import com.beyondeye.k2dart.utils.asDartNode
import com.beyondeye.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafElement

public class NumericalLiteralsRule : Rule(ruleName) {
    public companion object {
        public const val ruleName:String="numerica-literals"

    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if(node.isDartNode()) return
        if (node.elementType == ElementType.FLOAT_LITERAL) {
            //*DARIO* important: need also to call emit, that log that we identified something to change in the code
            //        otherwise, the mutated flag will not be set, and the corrected ast will be ignored
            emit(node.startOffset, ruleName, true)
            val newNode=(node as LeafElement).replaceWithText("final")
            newNode.asDartNode()
        }
    }
}
