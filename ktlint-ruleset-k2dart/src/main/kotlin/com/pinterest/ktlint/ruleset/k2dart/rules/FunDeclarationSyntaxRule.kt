package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.*
import com.pinterest.ktlint.ruleset.k2dart.utils.createSimpleTypeNode
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl

public class FunDeclarationSyntaxRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="fun-decl-syntax"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {

        //  ~.psi.KtNamedFunction (FUN)
        //    ~.c.i.p.impl.source.tree.LeafPsiElement (FUN_KEYWORD) "fun"
        //    ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
        //    ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "fn1"
        //    ~.psi.KtParameterList (VALUE_PARAMETER_LIST)
        //      ~.c.i.p.impl.source.tree.LeafPsiElement (LPAR) "("
        //      ~.psi.KtParameter (VALUE_PARAMETER)
        //        ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "arg1"
        //        ~.c.i.p.impl.source.tree.LeafPsiElement (COLON) ":"
        //        ~.psi.KtTypeReference (TYPE_REFERENCE)
        //          ~.psi.KtUserType (USER_TYPE)
        //            ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
        //              ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "Double"
        //      ~.c.i.p.impl.source.tree.LeafPsiElement (COMMA) ","
        //      ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
        //      ~.psi.KtParameter (VALUE_PARAMETER)
        //        ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "arg2"
        //        ~.c.i.p.impl.source.tree.LeafPsiElement (COLON) ":"
        //        ~.psi.KtTypeReference (TYPE_REFERENCE)
        //          ~.psi.KtUserType (USER_TYPE)
        //            ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
        //              ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "Float"
        //      ~.c.i.p.impl.source.tree.LeafPsiElement (COMMA) ","
        //      ~.psi.KtParameter (VALUE_PARAMETER)
        //        ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "arg3"
        //        ~.c.i.p.impl.source.tree.LeafPsiElement (COLON) ":"
        //        ~.psi.KtTypeReference (TYPE_REFERENCE)
        //          ~.psi.KtUserType (USER_TYPE)
        //            ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
        //              ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "AClass"
        //      ~.c.i.p.impl.source.tree.LeafPsiElement (RPAR) ")"
        //    ~.c.i.p.impl.source.tree.LeafPsiElement (COLON) ":"
        //    ~.psi.KtTypeReference (TYPE_REFERENCE)
        //      ~.psi.KtUserType (USER_TYPE)
        //        ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
        //          ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "Double"
        if (node izNot ElementType.FUN) return

        if (node.isDartNode()) return
        //find the "fun" keyword node, we will substitute it with return type specification
        var funKeywordNode =node.firstChildNode ?: return
        if(funKeywordNode izNot ElementType.FUN_KEYWORD) {
            funKeywordNode= funKeywordNode.nextSibling { it iz ElementType.FUN_KEYWORD } ?: return
        }

        val parameterListNode=funKeywordNode.nextSibling { it iz ElementType.VALUE_PARAMETER_LIST } ?:return
        //TODO instead of colonNode it is possible to have an = node that must be substituted with =>
        var isEqualSyntax=false
        var isUnitReturn=false
        val colonAfterParamsNode = parameterListNode.nextSibling { it iz ElementType.COLON }
        val returnTypeNode:ASTNode?
        if(colonAfterParamsNode==null) {
            val equalAfterParamsNode = parameterListNode.nextSibling { it iz ElementType.EQ }
            if(equalAfterParamsNode!=null) {
                isEqualSyntax=true
                equalAfterParamsNode  as LeafPsiElement
                equalAfterParamsNode.replaceWithText("=>")
            } else {
                isUnitReturn=true
                val parentNode=funKeywordNode.treeParent
                parentNode.addChild(createSimpleTypeNode("void"),funKeywordNode ) //void type node
                parentNode.removeChild(funKeywordNode)
            }
        } else { //we have a return type specification (colonAfterParamsNode!=null)
            returnTypeNode=colonAfterParamsNode.nextSibling { it iz ElementType.TYPE_REFERENCE }
            if(returnTypeNode!=null) {
                val parentNode=funKeywordNode.treeParent
                parentNode.addChild(returnTypeNode,funKeywordNode ) //void type node
                parentNode.removeChild(funKeywordNode)
                parentNode.removeChild(colonAfterParamsNode)
            }
        }

        var nextParam=parameterListNode.firstChildNode
        while(true) {
            nextParam=nextParam.nextSibling { it iz ElementType.VALUE_PARAMETER }
            if(nextParam==null) break
            val identifierNode=nextParam.findChildByType(ElementType.IDENTIFIER)
            if(identifierNode==null) {
                //todo this should not happen
                continue //skip to next //cannot handle this
            }
            val colonNode= identifierNode.nextCodeSibling()
                ?: //todo this should not happen
                continue //skip to next //cannot handle this
            val typeNode = colonNode.nextSibling { it iz ElementType.TYPE_REFERENCE }
                ?: //TODO this should not happen
                continue //skip to next //cannot handle this
            val parentNode=typeNode.treeParent //we actually already have this
            parentNode.removeChild(colonNode)
            parentNode.removeChild(typeNode)
            parentNode.addChild(typeNode,identifierNode) //add typeNode before identifierNode
            parentNode.addChild(PsiWhiteSpaceImpl(" "),identifierNode) //add space between typespec and identifier
        }
    }
}

