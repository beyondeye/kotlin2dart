package com.beyondeye.k2dart.rules

import com.beyondeye.k2dart.utils.asDartNode
import com.beyondeye.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.nextCodeSibling
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl


public class FunDeclarationSyntaxRule : Rule(ruleName) {
    public companion object {
        public const val ruleName:String="basic-types-names"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if (node.isDartNode()) return
        //  ~.psi.KtProperty (PROPERTY)
        //    ~.c.i.p.impl.source.tree.LeafPsiElement (VAR_KEYWORD) "var"
        //    ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
        //    ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "d"
        //    ~.c.i.p.impl.source.tree.LeafPsiElement (COLON) ":"
        //    ~.psi.KtTypeReference (TYPE_REFERENCE)
        //      ~.psi.KtUserType (USER_TYPE)
        //        ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
        //          ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "Double"
        if(node.elementType != ElementType.PROPERTY) return
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
}

