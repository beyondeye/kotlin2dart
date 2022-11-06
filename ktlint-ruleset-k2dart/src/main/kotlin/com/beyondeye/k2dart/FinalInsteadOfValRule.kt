package com.beyondeye.k2dart

import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType.VAR_KEYWORD
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafElement

public class FinalInsteadOfValRule : Rule("no-val") {

    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if (node.elementType == VAR_KEYWORD) {
            //*DARIO* important: need also to call emit, that log that we identified something to change in the code
            //        otherwise, the mutated flag will not be set, and the corrected ast will be ignored
            emit(node.startOffset, "final_no_val", true)
//            (node as LeafPsiElement).rawReplaceWithText("final")
            (node as LeafElement).replaceWithText("final")
        }
    }
}
