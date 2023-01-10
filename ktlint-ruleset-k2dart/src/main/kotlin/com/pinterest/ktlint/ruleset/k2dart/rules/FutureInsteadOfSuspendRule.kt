package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.asDartNode
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.iz
import com.pinterest.ktlint.core.ast.izNot
import com.pinterest.ktlint.core.ast.nextSibling
import com.pinterest.ktlint.ruleset.k2dart.utils.createSimpleTypeNode
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement

public class FutureInsteadOfSuspendRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="future-instead-of-suspend"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if(node.isDartNode()) return
        if (node izNot ElementType.MODIFIER_LIST) return
        /*
         ~.psi.KtNamedFunction (FUN)
           ~.psi.KtDeclarationModifierList (MODIFIER_LIST)
             ~.c.i.p.impl.source.tree.LeafPsiElement (SUSPEND_KEYWORD) "suspend"
           ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
           ~.c.i.p.impl.source.tree.LeafPsiElement (FUN_KEYWORD) "fun"
           ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
           ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "fun1"
           ~.psi.KtParameterList (VALUE_PARAMETER_LIST)
             ~.c.i.p.impl.source.tree.LeafPsiElement (LPAR) "("
             ~.c.i.p.impl.source.tree.LeafPsiElement (RPAR) ")"
           ~.c.i.p.impl.source.tree.LeafPsiElement (COLON) ":"
           ~.psi.KtTypeReference (TYPE_REFERENCE)
             ~.psi.KtUserType (USER_TYPE)
               ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
                 ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "A"
         */
        var suspendModNode=node.findChildByType(ElementType.SUSPEND_KEYWORD) ?:return

        //for a start replace the suspend modifier node with a commented out node (but keep the node and its type)
        suspendModNode as LeafPsiElement
        suspendModNode=suspendModNode.replaceWithText("/* suspend */")
        suspendModNode.asDartNode() //avoid processing it again

        // check if we have a return type
        val valueParamList= suspendModNode.treeParent.nextSibling { it iz ElementType.VALUE_PARAMETER_LIST }
        val colonNode=valueParamList?.nextSibling { it iz ElementType.COLON }
        var eqNode:ASTNode?=null
        //check if we have at least an implicit return type definition (like in "suspend fun fun2()=12")
        if(colonNode==null) {
            eqNode=valueParamList?.nextSibling { it iz ElementType.EQ }
        }
        if(colonNode!=null) {
            //replace the return value with the same type wrapped by Future<>
            colonNode.nextSibling { it iz ElementType.TYPE_REFERENCE }?.let { typeNode ->
                val newTypeStr= "Future<${typeNode.text}>"
                val newTypeNode=createSimpleTypeNode(newTypeStr)
                colonNode.treeParent.replaceChild(typeNode,newTypeNode)
            }
            } else if(eqNode!=null) { //implicit return type:  make it explicit
                val newColonNode= LeafPsiElement(ElementType.COLON,":")
                valueParamList!!.treeParent.addChild(newColonNode,eqNode)
                //note: the space at the end of the string in intentional, it is to avoid that last ">" and "=" to be joined
                val newTypeNode=createSimpleTypeNode("Future<Object> ")
                valueParamList.treeParent.addChild(newTypeNode,eqNode)
           }
    }
}
