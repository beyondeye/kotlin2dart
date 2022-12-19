package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.*
import com.pinterest.ktlint.ruleset.k2dart.utils.*
import com.pinterest.ktlint.ruleset.k2dart.utils.addChildAfter
import com.pinterest.ktlint.ruleset.k2dart.utils.createEmptyClassBodyNode
import com.pinterest.ktlint.ruleset.k2dart.utils.getEnclosingClassName
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.CompositeElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.TreeElement
import org.jetbrains.kotlin.psi.psiUtil.children

//
public class ClassPrimaryConstructorRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="class-primary-constr"
        //             ~.psi.KtParameter (VALUE_PARAMETER)
        //               ~.c.i.p.impl.source.tree.LeafPsiElement (VAR_KEYWORD) "var"
        //               ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
        //               ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "d"
        //               ~.c.i.p.impl.source.tree.LeafPsiElement (COLON) ":"
        //               ~.psi.KtTypeReference (TYPE_REFERENCE)
        //                 ~.psi.KtUserType (USER_TYPE)
        //                   ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
        //                     ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "Double"
        private fun fieldDeclFromConstructorParameter(p: ASTNode): ASTNode? {
            val valOrVarNode= p.firstChildNode ?: return  null
            valOrVarNode as LeafPsiElement
            var isVar=false
            var isVal=false
            var isParam=false
            if(valOrVarNode iz ElementType.VAL_KEYWORD) {
                isVal=true
            } else if(valOrVarNode iz ElementType.VAR_KEYWORD) {
                isVar=true
            } else {
                isParam=true
            }
            if(isParam) return null //not a field
//            val identifierNode=valOrVarNode.nextSibling {it iz ElementType.IDENTIFIER} ?:return null
//            val typeRefNode= identifierNode.nextSibling { it iz ElementType.TYPE_REFERENCE } ?:return null

            val newProperty=CompositeElement(ElementType.PROPERTY)
            for(c in p.children())
            {
                newProperty.rawAddChildren(c.clone() as TreeElement)
            }
            newProperty.rawAddChildren(PsiWhiteSpaceImpl("\n"))
            return newProperty
        }
    }


    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        //       ~.psi.KtClass (CLASS)
        //         ~.c.i.p.impl.source.tree.LeafPsiElement (CLASS_KEYWORD) "class"
        //         ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
        //         ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "B"
        //         ~.psi.KtPrimaryConstructor (PRIMARY_CONSTRUCTOR)
        //           ~.psi.KtParameterList (VALUE_PARAMETER_LIST)
        //             ~.c.i.p.impl.source.tree.LeafPsiElement (LPAR) "("
        //             ~.psi.KtParameter (VALUE_PARAMETER)
        //               ~.c.i.p.impl.source.tree.LeafPsiElement (VAL_KEYWORD) "val"
        //               ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
        //               ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "c"
        //               ~.c.i.p.impl.source.tree.LeafPsiElement (COLON) ":"
        //               ~.psi.KtTypeReference (TYPE_REFERENCE)
        //                 ~.psi.KtUserType (USER_TYPE)
        //                   ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
        //                     ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "String"
        //             ~.c.i.p.impl.source.tree.LeafPsiElement (COMMA) ","
        //             ~.psi.KtParameter (VALUE_PARAMETER)
        //               ~.c.i.p.impl.source.tree.LeafPsiElement (VAR_KEYWORD) "var"
        //               ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
        //               ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "d"
        //               ~.c.i.p.impl.source.tree.LeafPsiElement (COLON) ":"
        //               ~.psi.KtTypeReference (TYPE_REFERENCE)
        //                 ~.psi.KtUserType (USER_TYPE)
        //                   ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
        //                     ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "Double"
        //             ~.c.i.p.impl.source.tree.LeafPsiElement (RPAR) ")"
        //         ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) "\n"
        //         ~.psi.KtClassBody (CLASS_BODY)
        //           ~.c.i.p.impl.source.tree.LeafPsiElement (LBRACE) "{"
        //           ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) "\n\n"
        //           ~.c.i.p.impl.source.tree.LeafPsiElement (RBRACE) "}"
        //       ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) "\n\n"
        if (node izNot ElementType.PRIMARY_CONSTRUCTOR) return

        if (node.isDartNode()) return
        //we are going to process the valueParamListNode and then remove it
        val valueParamListNode =node.firstChildNode ?: return
        if(valueParamListNode izNot ElementType.VALUE_PARAMETER_LIST)  return
        //note: that nextPar is not assigned an actual parameter, only after call to nextSibling it will contain it
        var nextPar=valueParamListNode.firstChildNode
        val extractedParams= mutableListOf<ASTNode>()
        while(nextPar!=null)  {
            nextPar=nextPar.nextSibling { it iz ElementType.VALUE_PARAMETER }
            if(nextPar!=null) {
                extractedParams.add(nextPar)
            }
        }
        //now check if we have a class body, and if not create it
        var classBodyNode=node.nextSibling{it iz ElementType.CLASS_BODY}
        if(classBodyNode==null)
        {
            classBodyNode=createEmptyClassBodyNode()
            val crAfterPrimaryConstructorNode =node.treeParent.addNewlineAfter(node)
            node.treeParent.addChildAfter(crAfterPrimaryConstructorNode, classBodyNode)
        }
        val callBodyNodeFirstChildInside=classBodyNode.firstChildNode.treeNext //skip lbrace

        //now move parameter declarations inside class body
        var prev=callBodyNodeFirstChildInside
        for(p in extractedParams)
        {
            val fieldDeclarationNode=fieldDeclFromConstructorParameter(p) ?:continue
            prev=classBodyNode.addChildAfter(prev, fieldDeclarationNode)
        }
        val className=node.getEnclosingClassName()
        //now write hire the constructor with this.a parameters
        var dartConstructorStr="$className("
        for(p in extractedParams)
        {
            val paramNameNode= p.findChildByType(ElementType.IDENTIFIER) ?: continue
            dartConstructorStr+= "this.${paramNameNode.text},"
        }
        dartConstructorStr +=");"
        val darConstructorNode=LeafPsiElement(ElementType.DART_CODE,dartConstructorStr)
        prev= classBodyNode.addNewlineAfter(prev)
        prev=classBodyNode.addChildAfter(prev, darConstructorNode)
        prev= classBodyNode.addNewlineAfter(prev)

        //finally convert the primary constructor code to a comment in case that we did not transpile it
        //correctly
        node.commentOutNode()
    }
}

