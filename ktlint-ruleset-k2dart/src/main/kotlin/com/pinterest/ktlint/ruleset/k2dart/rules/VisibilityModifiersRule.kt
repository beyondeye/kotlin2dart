package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.iz
import com.pinterest.ktlint.core.ast.izNot
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiCommentImpl
import org.jetbrains.kotlin.com.intellij.psi.tree.TokenSet

// a rule that search for all visibility modifier and substitute them with a commented out version of them
public class VisibilityModifiersRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="visibility-modifiers"
    }
    private val modifiersTokenSet = TokenSet.create(
        ElementType.PUBLIC_KEYWORD,
        ElementType.PROTECTED_KEYWORD,
        ElementType.PRIVATE_KEYWORD,
        ElementType.INTERNAL_KEYWORD)
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if(node.isDartNode()) return
        if (node izNot ElementType.MODIFIER_LIST) return
        /*
           ~.psi.KtProperty (PROPERTY)
             ~.psi.KtDeclarationModifierList (MODIFIER_LIST)
               ~.c.i.p.impl.source.tree.LeafPsiElement (INTERNAL_KEYWORD) "internal"
         */
        //check if we have visibility modifiers in modifier list, if not return
        val modNode= node.findChildByType(modifiersTokenSet) ?: return

        val modText=modNode.text
        //remove the modifier (we are going to transform it into a block comment
        node.removeChild(modNode)
        //the object (property/class/method) to which the modifiers is applied to
        val modifiedNode=node.treeParent
        val whiteSpaceNode=modifiedNode.treePrev
        if(whiteSpaceNode iz ElementType.WHITE_SPACE) {
            val padding = whiteSpaceNode.text.dropWhile { it!=' ' && it!='\t' }
            val modCommentedNode= PsiCommentImpl(ElementType.BLOCK_COMMENT,"\n$padding /* $modText */")
            //add commented modifier node on previous line
            whiteSpaceNode.treeParent.addChild(modCommentedNode,whiteSpaceNode)
        } else { //add directly before modifier (with no newline)
            val modCommentedNode= PsiCommentImpl(ElementType.BLOCK_COMMENT,"/* $modText */")
            modifiedNode.treeParent.addChild(modCommentedNode,modifiedNode)
        }
    }
}
