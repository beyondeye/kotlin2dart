package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.iz
import com.pinterest.ktlint.core.ast.izNot
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafElement

// FinalInsteadOfValRule this rule is obsolete, since it has been integrated in VariableTypeBeforeNameRule
// but we keep it here because we use it as template for generating new rules
public class CompanionObjectRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="companion_object"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if(node.isDartNode()) return
        if (node izNot ElementType.OBJECT_DECLARATION) return
        val objectNode=node
        /*
        ~.psi.KtObjectDeclaration (OBJECT_DECLARATION)
          ~.psi.KtDeclarationModifierList (MODIFIER_LIST)
            ~.c.i.p.impl.source.tree.LeafPsiElement (COMPANION_KEYWORD) "companion"
          ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
          ~.c.i.p.impl.source.tree.LeafPsiElement (OBJECT_KEYWORD) "object"
          ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
         */
        var isCompanion=false
        val modListNode=objectNode.findChildByType(ElementType.MODIFIER_LIST)
        if(modListNode!=null) {
            val companionNode=modListNode.findChildByType(ElementType.COMPANION_KEYWORD)
            isCompanion = companionNode!=null
        }
        //TODO implement handling of object declaration that is not a companion object
        if(!isCompanion) return

        //check if this is a companion object
    }
}
