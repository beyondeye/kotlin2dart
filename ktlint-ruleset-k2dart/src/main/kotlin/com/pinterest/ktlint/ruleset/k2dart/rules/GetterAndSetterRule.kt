package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.iz
import com.pinterest.ktlint.core.ast.izNot
import com.pinterest.ktlint.core.ast.nextCodeSibling
import com.pinterest.ktlint.ruleset.k2dart.utils.asDartNode
import com.pinterest.ktlint.ruleset.k2dart.utils.valvarTokenSet
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl

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
        val propertyNode=propAccessorNode.treeParent ?:return
        if(propertyNode izNot  ElementType.PROPERTY) return

        //  ~.psi.KtProperty (PROPERTY)
        //    ~.c.i.p.impl.source.tree.LeafPsiElement (VAR_KEYWORD) "var"
        //    ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
        //    ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "d"
        //    ~.c.i.p.impl.source.tree.LeafPsiElement (COLON) ":"
        //    ~.psi.KtTypeReference (TYPE_REFERENCE)
        //      ~.psi.KtUserType (USER_TYPE)
        //        ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
        //          ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "Double"


        //NOTE the code here is a copy of the code in VariableTypeBeforeNameRule:
        //when we have getter or setter method we process it together with handling of the processing
        // needed for VariableTypeBeforeNameRule
        var varvalKeywordNode=propertyNode.findChildByType(valvarTokenSet) ?: return
        var isVar=true
        if(varvalKeywordNode iz ElementType.VAL_KEYWORD) {
            isVar=false
            varvalKeywordNode = (varvalKeywordNode as LeafPsiElement).replaceWithText("final") //replace "val" with "final"
        }
        val identifierNode =varvalKeywordNode.nextCodeSibling()
        if(identifierNode?.elementType!=ElementType.IDENTIFIER) {
            //TODO this should not happen
            return
        }
        val potentiallyColonNode=identifierNode.nextCodeSibling()
        if(potentiallyColonNode?.elementType!=ElementType.COLON) { //we don't have a type specification for the property

        } else { //we have a type specification
            val typeSpecNode=potentiallyColonNode.nextCodeSibling()
            if(typeSpecNode?.elementType==ElementType.TYPE_REFERENCE) { //TODO mark as dart code to avoid reprocessing?
                val parentNode=typeSpecNode.treeParent //todo: we actually have this already as the "propertyNode
                parentNode.removeChild(typeSpecNode)
                parentNode.addChild(typeSpecNode,identifierNode) //add typespec before identifier
                parentNode.addChild(PsiWhiteSpaceImpl(" "),identifierNode) //add space between typespec and identifier
                if(isVar) //remove redundant "var" if this property is not finale
                {
                    val spaceAfterValVarKeyword=varvalKeywordNode.treeNext
                    parentNode.removeChild(varvalKeywordNode)
                    if(spaceAfterValVarKeyword iz ElementType.WHITE_SPACE) //remove also original white space after var
                    {
                        parentNode.removeChild(spaceAfterValVarKeyword)
                    }
                }
                parentNode.removeChild(potentiallyColonNode)
            } else { //unhandled type definition
                return
            }
            //TODO this should not happen
            return
        }


        val getKeywordNode=(propAccessorNode.findChildByType(ElementType.GET_KEYWORD))
        val setKeywordNode=(propAccessorNode.findChildByType(ElementType.SET_KEYWORD))
        getKeywordNode?.let {
            //handle getter
        }
        setKeywordNode?.let {
            //handle setter
        }
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
    }

    private fun handleSetter(propAccessorNode: ASTNode, setKeywordNode: ASTNode) {
        val propertyNode=propAccessorNode.treeParent ?:return
    }
}
