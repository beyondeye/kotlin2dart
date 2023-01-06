package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.*
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement

// convert Kotlin for loop in the equivalent Dart constructs
public class ForLoopIndicesRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="for-loop-indices"
        private const val dotdotOp=".."
        private const val downToOp="downTo"
        private const val untilOp="until"
        private const val stepOp="step"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if(node.isDartNode()) return
        if(node.izNot( ElementType.FOR)) return
        val forNode=node
        //      ~.psi.KtForExpression (FOR)
        //        ~.c.i.p.impl.source.tree.LeafPsiElement (FOR_KEYWORD) "for"
        //        ~.c.i.p.impl.source.tree.LeafPsiElement (LPAR) "("
        //        ~.psi.KtParameter (VALUE_PARAMETER)
        //          ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "i"
        //        ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
        //        ~.c.i.p.impl.source.tree.LeafPsiElement (IN_KEYWORD) "in"
        //        ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "

        //    ~.psi.KtForExpression (FOR)
        //      ~.c.i.p.impl.source.tree.LeafPsiElement (FOR_KEYWORD) "for"
        //      ~.c.i.p.impl.source.tree.LeafPsiElement (LPAR) "("
        //      ~.psi.KtParameter (VALUE_PARAMETER)
        //        ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "i"
        //        ~.c.i.p.impl.source.tree.LeafPsiElement (COLON) ":"
        //        ~.psi.KtTypeReference (TYPE_REFERENCE)
        //          ~.psi.KtUserType (USER_TYPE)
        //            ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
        //              ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "Int"
        val valueParamNode=forNode.findChildByType(ElementType.VALUE_PARAMETER) ?: return
        val identifierNode=valueParamNode.findChildByType(ElementType.IDENTIFIER) ?: return
        //this can be null
        val loopIndexType=identifierNode.nextSibling { it iz ElementType.TYPE_REFERENCE }

        val loopRangeNode = valueParamNode.nextSibling { it iz ElementType.LOOP_RANGE } ?: return

        //     ~.psi.KtContainerNode (LOOP_RANGE)
        //       ~.psi.KtBinaryExpression (BINARY_EXPRESSION)
        //         ~.psi.KtConstantExpression (INTEGER_CONSTANT)
        //           ~.c.i.p.impl.source.tree.LeafPsiElement (INTEGER_LITERAL) "0"
        //         ~.psi.KtOperationReferenceExpression (OPERATION_REFERENCE)
        //           ~.c.i.p.impl.source.tree.LeafPsiElement (RANGE) ".."
        //         ~.psi.KtConstantExpression (INTEGER_CONSTANT)
        //           ~.c.i.p.impl.source.tree.LeafPsiElement (INTEGER_LITERAL) "3"
        var loopRangeBinarExprNode=loopRangeNode.firstChildNode
        if(loopRangeBinarExprNode izNot ElementType.BINARY_EXPRESSION) return

        //       ~.psi.KtBinaryExpression (BINARY_EXPRESSION)
        //         ~.psi.KtBinaryExpression (BINARY_EXPRESSION)
        //           ~.psi.KtConstantExpression (INTEGER_CONSTANT)
        //             ~.c.i.p.impl.source.tree.LeafPsiElement (INTEGER_LITERAL) "6"
        //           ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
        //           ~.psi.KtOperationReferenceExpression (OPERATION_REFERENCE)
        //             ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "downTo"
        //           ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
        //           ~.psi.KtConstantExpression (INTEGER_CONSTANT)
        //             ~.c.i.p.impl.source.tree.LeafPsiElement (INTEGER_LITERAL) "1"
        //         ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
        //         ~.psi.KtOperationReferenceExpression (OPERATION_REFERENCE)
        //           ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "step"
        //         ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
        //         ~.psi.KtConstantExpression (INTEGER_CONSTANT)
        //           ~.c.i.p.impl.source.tree.LeafPsiElement (INTEGER_LITERAL) "2"
        var opReferenceNode= loopRangeBinarExprNode.findChildByType(ElementType.OPERATION_REFERENCE) ?: return
        val stepOpReferenceNode:ASTNode?
        var stepBinaryExprNode:ASTNode? = null
        var stepValueNode:ASTNode? = null
        if(opReferenceNode.text== stepOp) { //we have a step, the LOOP_RANGE AST structure has an additional level (a binary expr in a binary expr)
            stepBinaryExprNode=loopRangeBinarExprNode
            stepOpReferenceNode=opReferenceNode
            stepValueNode = stepOpReferenceNode.nextCodeSibling() ?:return
            loopRangeBinarExprNode = loopRangeBinarExprNode.firstChildNode
            opReferenceNode = loopRangeBinarExprNode.findChildByType(ElementType.OPERATION_REFERENCE) ?: return
        }
        val opTxt=opReferenceNode.text
        if(opTxt!= dotdotOp && opTxt!= downToOp && opTxt!= untilOp) return //unsupported type of loop
        val loopStartNode=opReferenceNode.prevCodeSibling() ?:return
        val loopEndNode=opReferenceNode.nextCodeSibling() ?: return
        val isDecreasingIndexLoop=opTxt== downToOp
        val idTxt=identifierNode.text
        val stepNodeTxt=stepValueNode?.text
        val dartStepTxt:String=
            if (stepNodeTxt == null) {
                if (!isDecreasingIndexLoop) "$idTxt++" else "$idTxt--"
            } else { //stepNodeTxt!=null
                if (!isDecreasingIndexLoop) "$idTxt+=$stepNodeTxt" else "$idTxt-=$stepNodeTxt"
            }
        val dartLoopInitTxt="var $idTxt=${loopStartNode.text}"

        val loopEndTxt = loopEndNode.text
        val dartLimitTxt: String = if (isDecreasingIndexLoop) {
            "$idTxt>=$loopEndTxt"
        } else { //not decreasing
            if (opTxt == dotdotOp) {
                "$idTxt<=$loopEndTxt"
            } else "$idTxt<$loopEndTxt" //until
        }

        val dartLoopFullText="for ( $dartLoopInitTxt; $dartLimitTxt; $dartStepTxt ) "
        val bodyNode=forNode.findChildByType(ElementType.BODY) ?:return
        forNode.removeRange(forNode.firstChildNode,bodyNode) //bodyNode is not included in range
        forNode.addChild(LeafPsiElement(ElementType.DART_CODE,dartLoopFullText),bodyNode)
    }
}
