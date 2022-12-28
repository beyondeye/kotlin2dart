package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.iz
import com.pinterest.ktlint.ruleset.k2dart.utils.createDartCodeNode
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiCommentImpl

// FinalInsteadOfValRule this rule is obsolete, since it has been integrated in VariableTypeBeforeNameRule
// but we keep it here because we use it as template for generating new rules
public class OverrideModifierRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="override-modifier"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if(node iz  ElementType.OVERRIDE_KEYWORD) handleOverrideModifier(node)
        else if (node iz ElementType.ANNOTATION_ENTRY) handleOverrideAnnotationEntry(node)
    }

    /*
     ~.psi.KtAnnotationEntry (ANNOTATION_ENTRY)
       ~.c.i.p.impl.source.tree.LeafPsiElement (AT) "@"
       ~.psi.KtConstructorCalleeExpression (CONSTRUCTOR_CALLEE)
         ~.psi.KtTypeReference (TYPE_REFERENCE)
           ~.psi.KtUserType (USER_TYPE)
             ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
               ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "Override"
     */
    private fun handleOverrideAnnotationEntry(node: ASTNode) {
        val constuctorCalleNode=node.findChildByType(ElementType.CONSTRUCTOR_CALLEE) ?:return
        val identifierNode=constuctorCalleNode.firstChildNode?.firstChildNode?.firstChildNode?.firstChildNode ?:return
        if(identifierNode.text=="Override") {
            identifierNode as LeafPsiElement
            identifierNode.replaceWithText("override") // in dart override annotation is not capitalized.
        }
    }

    private fun handleOverrideModifier(node: ASTNode) {
        val parentNode = node.treeParent

        //remove the override modifier (we are going to transform it into @override annotation
        parentNode.removeChild(node)
        //the object (property/class/method) to which the modifiers is applied to
        val modifiedNode = parentNode.treeParent
        val whiteSpaceNode = modifiedNode.treePrev
        if (whiteSpaceNode iz ElementType.WHITE_SPACE) {
            val padding = whiteSpaceNode.text.dropWhile { it != ' ' && it != '\t' }
            val modCommentedNode = createDartCodeNode("\n${padding} @override")
            //add commented modifier node on previous line
            whiteSpaceNode.treeParent.addChild(modCommentedNode, whiteSpaceNode)
        } else { //add directly before modifier (with no newline)
            val modCommentedNode = PsiCommentImpl(ElementType.BLOCK_COMMENT, "@override")
            modifiedNode.treeParent.addChild(modCommentedNode, modifiedNode)
        }
    }
}
