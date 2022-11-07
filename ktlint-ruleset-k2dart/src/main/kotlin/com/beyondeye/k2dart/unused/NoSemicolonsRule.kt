package com.beyondeye.k2dart.unused

import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.*
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.PsiComment
import org.jetbrains.kotlin.com.intellij.psi.PsiWhiteSpace
import org.jetbrains.kotlin.kdoc.psi.api.KDoc
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.getStrictParentOfType

//*DARIO* TODO the purpose of having this rule here is to adapt it to actually
//        require semicolumns at the end of statements!
public class NoSemicolonsRule : Rule("no-semi") {

    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if (node.elementType == ElementType.KDOC_TEXT) {
            return
        }
        if (node.elementType == ElementType.SEMICOLON &&
            !node.isPartOfString() &&
            !node.isPartOfEnumEntry()
        ) {
            val nextLeaf = node.nextLeaf()
            val prevCodeLeaf = node.prevCodeLeaf()
            if (doesNotRequirePreSemi(nextLeaf) && doesNotRequirePostSemi(prevCodeLeaf)) {
                emit(node.startOffset, "Unnecessary semicolon", true)
                if (autoCorrect) {
                    node.treeParent.removeChild(node)
                }
            } else if (nextLeaf !is PsiWhiteSpace) {
                val prevLeaf = node.prevLeaf()
                if (prevLeaf is PsiWhiteSpace && prevLeaf.textContains('\n')) { // \n;{
                    return
                }
                // todo: move to a separate rule
                emit(node.startOffset + 1, "Missing spacing after \";\"", true)
                if (autoCorrect) {
                    node.upsertWhitespaceAfterMe(" ")
                }
            }
        }
    }

    private fun doesNotRequirePreSemi(nextLeaf: ASTNode?): Boolean {
        if (nextLeaf is PsiWhiteSpace) {
            val nextNextLeaf = nextLeaf.nextLeaf {
                val psi = it.psi
                it !is PsiWhiteSpace && it !is PsiComment && psi.getStrictParentOfType<KDoc>() == null &&
                    psi.getStrictParentOfType<KtAnnotationEntry>() == null
            }
            return (
                nextNextLeaf == null || // \s+ and then eof
                    nextLeaf.textContains('\n') && nextNextLeaf.elementType != KtTokens.LBRACE
                )
        }
        return nextLeaf == null // eof
    }

    private fun doesNotRequirePostSemi(prevLeaf: ASTNode?): Boolean {
        if (prevLeaf?.elementType == ElementType.OBJECT_KEYWORD) {
            // https://github.com/pinterest/ktlint/issues/281
            return false
        }
        val parent = prevLeaf?.treeParent?.psi
        if (parent is KtLoopExpression && parent !is KtDoWhileExpression && parent.body == null) {
            // https://github.com/pinterest/ktlint/issues/955
            return false
        }
        if (parent is KtIfExpression && parent.then == null) {
            return false
        }
        return true
    }

    private fun ASTNode.isPartOfEnumEntry(): Boolean {
        if (isPartOf(KtEnumEntry::class)) return true
        val lBrace = prevLeaf { !it.isWhiteSpace() && !it.isPartOfComment() }
            ?.takeIf { it.elementType == KtTokens.LBRACE }
            ?: return false
        val classBody = lBrace.treeParent?.psi as? KtClassBody ?: return false
        if (classBody.children.isEmpty()) return false
        return (classBody.parent as? KtClass)?.isEnum() == true
    }
}
