package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.*
import com.pinterest.ktlint.ruleset.k2dart.utils.asDartNode
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.CompositeElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.TreeElement


/**
 * substitute method calls abs(..), round(..), ceil(..), floor(..), to (..).abs(), (..).round(), (..).ceil(), (..).floor()
 */
public class MathOpsRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="math-ops"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        //TODO: handle also the roundToInt() postfix function call: it should be translated to .round() postfix function call
        val fn_names= setOf("round","ceil","floor","abs") //"roundToInt"
        if(node.isDartNode()) return
        if (node izNot ElementType.IDENTIFIER) return
        val identifierName=node.text
        if(identifierName !in fn_names) return

        //     ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
        //       ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "Math"
        //     ~.c.i.p.impl.source.tree.LeafPsiElement (DOT) "."
        //     ~.psi.KtCallExpression (CALL_EXPRESSION)
        //       ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
        //         ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "round"
        //       ~.psi.KtValueArgumentList (VALUE_ARGUMENT_LIST)
        //         ~.c.i.p.impl.source.tree.LeafPsiElement (LPAR) "("
        //         ~.psi.KtValueArgument (VALUE_ARGUMENT)
        //           ~.psi.KtConstantExpression (FLOAT_CONSTANT)
        //             ~.c.i.p.impl.source.tree.LeafPsiElement (FLOAT_LITERAL) "1.2"
        //         ~.c.i.p.impl.source.tree.LeafPsiElement (RPAR) ")"
        val refExpNode=node.treeParent
        val callExprNode=refExpNode?.treeParent
        if(callExprNode izNot  ElementType.CALL_EXPRESSION) return //we found one of the identifier but this is not a method call
        //do we have the "Math." prefix for the function call?
        var mathNode = callExprNode?.prevCodeSibling()?.prevCodeSibling()
        if(mathNode?.text!="Math")  mathNode=null
        val dotAfterMathNode= if(mathNode!=null) mathNode.nextCodeSibling() else null
        val argListNode=node.treeParent.nextCodeSibling()
        if(argListNode izNot ElementType.VALUE_ARGUMENT_LIST) return

//  ~.psi.KtDotQualifiedExpression (DOT_QUALIFIED_EXPRESSION)
//    ~.psi.KtParenthesizedExpression (PARENTHESIZED)
//      ~.c.i.p.impl.source.tree.LeafPsiElement (LPAR) "("
//      ~.psi.KtBinaryExpression (BINARY_EXPRESSION)
//        ~.psi.KtConstantExpression (FLOAT_CONSTANT)
//          ~.c.i.p.impl.source.tree.LeafPsiElement (FLOAT_LITERAL) "1.2"
//        ~.psi.KtOperationReferenceExpression (OPERATION_REFERENCE)
//          ~.c.i.p.impl.source.tree.LeafPsiElement (PLUS) "+"
//        ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
//          ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "c2"
//      ~.c.i.p.impl.source.tree.LeafPsiElement (RPAR) ")"
//    ~.c.i.p.impl.source.tree.LeafPsiElement (DOT) "."
//    ~.psi.KtCallExpression (CALL_EXPRESSION)
//      ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
//        ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "roundToInt"
//      ~.psi.KtValueArgumentList (VALUE_ARGUMENT_LIST)
//        ~.c.i.p.impl.source.tree.LeafPsiElement (LPAR) "("
//        ~.c.i.p.impl.source.tree.LeafPsiElement (RPAR) ")"

        //transform the argument of the round/ceil/abs call to a KtParenthesizedExpression
        val arglistParentNode=argListNode?.treeParent ?:return //this should not be null!!
        val parenExprNode = CompositeElement(ElementType.PARENTHESIZED)
        //TODO instead of adding a clone of ArgListNode, I should build  the actual AST node structure of the
        // parenthesized expression: arg list contains VALUE_ARGUMENT nodes that are specific to arguments of function calls
        // but it seems to work also this way
        parenExprNode.rawAddChildren(argListNode.clone() as TreeElement)
        val newDotExprNode=CompositeElement(ElementType.DOT_QUALIFIED_EXPRESSION)
        newDotExprNode.rawAddChildren(parenExprNode)
        newDotExprNode.rawAddChildren(LeafPsiElement(ElementType.DOT,"."))
        newDotExprNode.rawAddChildren(createParameterlessMathOpNode(identifierName) as TreeElement)

        mathNode?.treeParent?.removeChild(mathNode)
        dotAfterMathNode?.treeParent?.removeChild(dotAfterMathNode)
        //add the new syntax
        arglistParentNode.addChild(newDotExprNode,argListNode)
        //finally remove the old syntax arg list
        callExprNode!!.removeChild(argListNode)
        //remove also the function method before the arg list
        callExprNode.removeChild(refExpNode)

    }
}

//   ~.psi.KtCallExpression (CALL_EXPRESSION)
//     ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
//       ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "roundToInt"
//     ~.psi.KtValueArgumentList (VALUE_ARGUMENT_LIST)
//       ~.c.i.p.impl.source.tree.LeafPsiElement (LPAR) "("
//       ~.c.i.p.impl.source.tree.LeafPsiElement (RPAR) ")"
private fun createParameterlessMathOpNode(opname:String): ASTNode {
    val callExprNode= CompositeElement(ElementType.CALL_EXPRESSION)
    val nameRefExprNode=CompositeElement(ElementType.REFERENCE_EXPRESSION)
    val identifierNode=LeafPsiElement(ElementType.IDENTIFIER,opname)
    identifierNode.asDartNode() //mark it as a dart node to avoid multiple processing
    nameRefExprNode.rawAddChildren(identifierNode)
    callExprNode.rawAddChildren(nameRefExprNode)
    val arglistNode=CompositeElement(ElementType.VALUE_ARGUMENT_LIST)
    arglistNode.rawAddChildren(LeafPsiElement(ElementType.LPAR,"("))
    arglistNode.rawAddChildren(LeafPsiElement(ElementType.RPAR,")"))
    callExprNode.rawAddChildren(arglistNode)
    return callExprNode
}
