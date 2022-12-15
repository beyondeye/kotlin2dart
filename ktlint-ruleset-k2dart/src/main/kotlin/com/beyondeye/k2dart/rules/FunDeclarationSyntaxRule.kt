package com.beyondeye.k2dart.rules

import com.beyondeye.k2dart.k2dartRulesetId
import com.beyondeye.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.*
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.PsiTypeElementImpl
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl
import org.jetbrains.kotlin.psi.KtTypeReference


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
        if(funKeywordNode izNot ElementType.FUN) {
            funKeywordNode= funKeywordNode.nextSibling { it iz ElementType.FUN_KEYWORD } ?: return
        }

        val parameterListNode=funKeywordNode.nextSibling { it iz ElementType.VALUE_PARAMETER_LIST } ?:return
        //TODO instead of colonNode it is possible to have an = node that must be substituted with =>
        var isEqualSyntax=false
        var isUnitReturn=false
        val colonAfterParamsNode = parameterListNode.nextSibling { it iz ElementType.COLON }
        var returnTypeNode:ASTNode?=null
        if(colonAfterParamsNode==null) {
            val equalAfterParamsNode = parameterListNode.nextSibling { it iz ElementType.EQ }
            if(equalAfterParamsNode!=null) {
                isEqualSyntax=true
                equalAfterParamsNode  as LeafPsiElement?
                equalAfterParamsNode?.replaceWithText("=>")
            } else {
                isUnitReturn=true
                val parentNode=funKeywordNode.treeParent
//                parentNode.addChild("void",funKeywordNode ) //void type node
                parentNode.removeChild(funKeywordNode)
            }
        } else {
            returnTypeNode=colonAfterParamsNode.nextSibling { it iz ElementType.TYPE_REFERENCE }
            //parentNode.addChild(returnTypeNode,funKeywordNode ) //void type node
            //parentNode.removeChild(funKeywordNode)

        }

        var nextParam=parameterListNode.firstChildNode
        while(true) {
            nextParam=nextParam.nextSibling { it iz ElementType.VALUE_PARAMETER }
            if(nextParam==null) break
            val identifierNode=nextParam.findChildByType(ElementType.IDENTIFIER)
            if(identifierNode==null) {
                //todo this should not happen
                throw NotImplementedError()
            }
            val colonNode=identifierNode!!.nextCodeSibling()
            if(colonNode==null) {
                //todo this should not happen
                throw NotImplementedError()
            }
            val typeNode = colonNode!!.nextSibling { it iz ElementType.TYPE_REFERENCE }
            if(typeNode==null) {
                //TODO this should not happen
                throw NotImplementedError()
            }
            val parentNode=nextParam.treeParent //we actually already have this
            parentNode.removeChild(colonNode)
            parentNode.removeChild(typeNode)
            parentNode.addChild(typeNode,identifierNode) //add typeNode before identifierNode
            parentNode.addChild(PsiWhiteSpaceImpl(" "),identifierNode) //add space between typespec and identifier
        }

        /*
        var varvalKeywordNode=node.firstChildNode
        var isVar=true
        if(varvalKeywordNode.elementType==ElementType.VAL_KEYWORD) {
            isVar=false
            emit(varvalKeywordNode.startOffset, ruleName, true)
            varvalKeywordNode = (varvalKeywordNode as LeafPsiElement).replaceWithText("final") //replace "val" with "final"
            varvalKeywordNode.asDartNode() //set also the dart node flag so that we will avoid processing this node again

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
            if(typeSpecNode?.elementType==ElementType.TYPE_REFERENCE) {
                emit(node.startOffset, ruleName, true)
                val parentNode=typeSpecNode.treeParent //todo: we actually have this already as the "node
                parentNode.removeChild(typeSpecNode)
                parentNode.addChild(typeSpecNode,identifierNode) //add typespec before identifier
                parentNode.addChild(PsiWhiteSpaceImpl(" "),identifierNode) //add space between typespec and identifier
                if(isVar) //remove redundant "var" if this property is not finale
                {
                    val spaceAfterValVarKeyword=varvalKeywordNode.treeNext
                    parentNode.removeChild(varvalKeywordNode)
                    if(spaceAfterValVarKeyword.elementType==ElementType.WHITE_SPACE) //remove also original white space after var
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
    }
         */
    }
//    fun createTypeNode() :KtTypeReference {
//        PsiTypeElementImpl
//    }
}

