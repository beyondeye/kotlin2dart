package com.pinterest.ktlint.ruleset.k2dart.utils

import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.nextSibling
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.openapi.util.Key
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.CompositeElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl
import org.jetbrains.kotlin.com.intellij.psi.tree.IElementType

internal object K2Dart {
    val dartNodeKey= Key<Boolean>("*DRT*")
}

internal fun ASTNode.isDartNode() = getCopyableUserData(K2Dart.dartNodeKey)!=null
internal fun ASTNode.isNotDartNode() = getCopyableUserData(K2Dart.dartNodeKey)==null

internal fun ASTNode.asDartNode():ASTNode {
    this.putCopyableUserData(K2Dart.dartNodeKey,true)
    return this
}

/**
 * the standard [ASTNode.addChild] can be used to add a child node BEFORE another node
 * This method is for adding a child AFTER another node
 */
internal fun ASTNode.addChildAfter(nodeToAdd:ASTNode,addAfterNode:ASTNode?) {
    val nextNode =
        if (addAfterNode == null) { //add as first child node
            firstChildNode.treeNext
        } else
            addAfterNode.treeNext

    this.addChild(nodeToAdd,nextNode)
}


/**
 * check if this is of the specified [elementType], if this not the case
 * then search for the next sibling with the specified type
 */
internal fun ASTNode?.thisOrNextSiblingOfType(elementType: IElementType):ASTNode?
{
    if(this==null) return  null
    if(this.elementType==elementType) return this
    return nextSibling { it.elementType==elementType }
}

/**
 * create an ASTNode for specifing a simple type (like void), with the same structure as it is returned by the Kotlin compiler
 * Note tha I need to use rawAddChildren here because addChild throw exception when working on ASTNode not generated from a file by the compiler
 * Element(TYPE_REFERENCE)->Element(USER_TYPE)->Element(REFERENCE_EXPRESSION->PsiElement(IDENTIFIER)
 *  ~.psi.KtTypeReference (TYPE_REFERENCE)
 *    ~.psi.KtUserType (USER_TYPE)
 *      ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
 *         ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "AClass"
 */
internal fun createSimpleTypeNode(simpleTypeName:String) : ASTNode {
    val identifierNode= LeafPsiElement(ElementType.IDENTIFIER,simpleTypeName)
    val refExpressionNode = CompositeElement(ElementType.REFERENCE_EXPRESSION)
    refExpressionNode.rawAddChildren(identifierNode)
    val userTypeNode = CompositeElement(ElementType.USER_TYPE)
    userTypeNode.rawAddChildren(refExpressionNode)
    val typeReferenceNode = CompositeElement(ElementType.TYPE_REFERENCE)
    typeReferenceNode.rawAddChildren(userTypeNode)
    return  typeReferenceNode
}

/**
//         ~.psi.KtClassBody (CLASS_BODY)
//           ~.c.i.p.impl.source.tree.LeafPsiElement (LBRACE) "{"
//           ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) "\n\n"
//           ~.c.i.p.impl.source.tree.LeafPsiElement (RBRACE) "}"
 */
internal fun createEmptyClassBodyNode() : ASTNode {
    val classBodyNode = CompositeElement(ElementType.CLASS_BODY)
    classBodyNode.rawAddChildren(LeafPsiElement(ElementType.LBRACE,"{"))
    classBodyNode.rawAddChildren(PsiWhiteSpaceImpl("\n"))
    classBodyNode.rawAddChildren(LeafPsiElement(ElementType.RBRACE,"}"))
    return  classBodyNode
}
