package com.pinterest.ktlint.ruleset.k2dart.utils

import com.pinterest.ktlint.core.ast.ElementType
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.openapi.util.Key
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.CompositeElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement

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
 * create an ASTNode for specifing a simple type (like void), with the same structure as it is returned by the Kotlin compiler
 * Element(TYPE_REFERENCE)->Element(USER_TYPE)->Element(REFERENCE_EXPRESSION->PsiElement(IDENTIFIER)
 *  ~.psi.KtTypeReference (TYPE_REFERENCE)
 *    ~.psi.KtUserType (USER_TYPE)
 *      ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
 *         ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "AClass"
 */
internal fun createSimpleTypeNode(simpleTypeName:String) : ASTNode {
    val identifierNode= LeafPsiElement(ElementType.IDENTIFIER,simpleTypeName)
    val refExpressionNode = CompositeElement(ElementType.REFERENCE_EXPRESSION)
    refExpressionNode.addChild(identifierNode)
    val userTypeNode = CompositeElement(ElementType.USER_TYPE)
    userTypeNode.addChild(refExpressionNode)
    val typeReferenceNode = CompositeElement(ElementType.TYPE_REFERENCE)
    typeReferenceNode.addChild(userTypeNode)
    return  typeReferenceNode
}
