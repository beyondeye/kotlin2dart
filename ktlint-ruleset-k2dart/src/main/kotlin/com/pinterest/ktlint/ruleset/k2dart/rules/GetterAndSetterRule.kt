package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.izNot
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

// FinalInsteadOfValRule this rule is obsolete, since it has been integrated in VariableTypeBeforeNameRule
// but we keep it here because we use it as template for generating new rules
public class GetterAndSetterRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="getter-and-setter"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if(node.isDartNode()) return
        if (node izNot  ElementType.PROPERTY_ACCESSOR) return
        val propAccessorNode=node
        val getKeywordNode=(propAccessorNode.findChildByType(ElementType.GET_KEYWORD))
        val setKeywordNode=(propAccessorNode.findChildByType(ElementType.SET_KEYWORD))
        getKeywordNode?.let { handleGetter(propAccessorNode,it) }
        setKeywordNode?.let { handleSetter(propAccessorNode,it) }
    }

   //    ~.psi.KtProperty (PROPERTY)
   //      ~.c.i.p.impl.source.tree.LeafPsiElement (VAL_KEYWORD) "val"
   //      ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
   //      ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "b"
   //      ~.c.i.p.impl.source.tree.LeafPsiElement (COLON) ":"
   //      ~.psi.KtTypeReference (TYPE_REFERENCE)
   //        ~.psi.KtUserType (USER_TYPE)
   //          ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
   //            ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "String"
   //      ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
   //      ~.psi.KtPropertyAccessor (PROPERTY_ACCESSOR)
   //        ~.c.i.p.impl.source.tree.LeafPsiElement (GET_KEYWORD) "get"
   //        ~.c.i.p.impl.source.tree.LeafPsiElement (LPAR) "("
   //        ~.c.i.p.impl.source.tree.LeafPsiElement (RPAR) ")"
   //        ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
   //        ~.c.i.p.impl.source.tree.LeafPsiElement (EQ) "="
   //        ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
   //        ~.psi.KtStringTemplateExpression (STRING_TEMPLATE)
   //          ~.c.i.p.impl.source.tree.LeafPsiElement (OPEN_QUOTE) """
   //          ~.psi.KtLiteralStringTemplateEntry (LITERAL_STRING_TEMPLATE_ENTRY)
   //            ~.c.i.p.impl.source.tree.LeafPsiElement (REGULAR_STRING_PART) "Hello"
   //          ~.c.i.p.impl.source.tree.LeafPsiElement (CLOSING_QUOTE) """
    private fun handleGetter(propAccessorNode: ASTNode, getKeywordNode: ASTNode) {
        val propertyNode=propAccessorNode.treeParent ?:return
    }

    private fun handleSetter(propAccessorNode: ASTNode, setKeywordNode: ASTNode) {
        val propertyNode=propAccessorNode.treeParent ?:return
    }
}
