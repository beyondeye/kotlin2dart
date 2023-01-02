package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.ruleset.k2dart.utils.isDartNode
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.*
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.CompositeElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl

// FinalInsteadOfValRule this rule is obsolete, since it has been integrated in VariableTypeBeforeNameRule
// but we keep it here because we use it as template for generating new rules
public class CompanionObjectRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="companion-object"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        if(node.isDartNode()) return
        if (node izNot ElementType.OBJECT_DECLARATION) return
        val objectNode=node
        val spaceBeforeObjectNode=objectNode.prevSibling { it iz ElementType.WHITE_SPACE }
        var indentationStr=""
        if(spaceBeforeObjectNode!=null) {
            indentationStr=spaceBeforeObjectNode.text
            indentationStr=indentationStr.replace("\r","")
            indentationStr=indentationStr.replace("\n","")
        }
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
        //check if this is a companion object
        if(modListNode!=null) {
            val companionNode=modListNode.findChildByType(ElementType.COMPANION_KEYWORD)
            isCompanion = companionNode!=null
        }

        val classBodyNode = objectNode.findChildByType( ElementType.CLASS_BODY ) ?:return
        //now loop over properties and named functions that are children of class body and moved then before
        //companion object declaration and and to them a "static" modifier
        val listOfChildrenToMove = mutableListOf<ASTNode>()
        var nextChild=classBodyNode.firstChildNode
        while(nextChild!=null) {
            if(nextChild iz ElementType.PROPERTY) {
                createOrAddStaticKeywordToModifierList(nextChild)
                listOfChildrenToMove.add(nextChild)
            } else if(nextChild iz ElementType.FUN){
                createOrAddStaticKeywordToModifierList(nextChild)
                listOfChildrenToMove.add(nextChild)
            }
            nextChild=nextChild.nextCodeSibling()
        }
        //if this is not a companion object but instead a regular object keep the code where it is don't move it
        if(!isCompanion) return

        if(listOfChildrenToMove.isEmpty()) return
        //the node before which we will move the static definition
        val anchorNode= spaceBeforeObjectNode ?: objectNode
        val companionParentNode=objectNode.treeParent
        var isFirst=true
        for(child in listOfChildrenToMove) {
            child.treeParent.removeChild(child)
            val retNode=PsiWhiteSpaceImpl("\n")
            companionParentNode.addChild(retNode,anchorNode)
            companionParentNode.addChild(child,retNode)
            val curIndentNode=PsiWhiteSpaceImpl(indentationStr)
            companionParentNode.addChild(curIndentNode,child)
            if(isFirst) {
                //add also a new line before all added children
                companionParentNode.addChild(PsiWhiteSpaceImpl("\n"),curIndentNode)
            }
            isFirst=false
        }
    }

    private fun createOrAddStaticKeywordToModifierList(nextChild: ASTNode) {
        val modListNode = nextChild.findChildByType(ElementType.MODIFIER_LIST)
        if (modListNode != null) {
            modListNode.addChild(createStaticModifierKeywordNode(), modListNode.firstChildNode)
        } else {
            addStaticModifierToNode(nextChild)
        }
    }

    private fun createStaticModifierKeywordNode() =LeafPsiElement(ElementType.DART_CODE,"static ")

    private fun addStaticModifierToNode(node: ASTNode) {
        val staticModNode= createStaticModifierKeywordNode()
        val modListNode = CompositeElement(ElementType.MODIFIER_LIST)
        modListNode.rawAddChildren(staticModNode)
        node.addChild(modListNode,node.firstChildNode)
    }
}
