package com.pinterest.ktlint.ruleset.k2dart.utils

import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.iz
import com.pinterest.ktlint.core.ast.nextSibling
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.openapi.util.Key
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.CompositeElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiCommentImpl
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl
import org.jetbrains.kotlin.com.intellij.psi.tree.IElementType
import org.jetbrains.kotlin.com.intellij.psi.tree.TokenSet

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
 * return the added node
 */
internal fun ASTNode.addChildAfter(nodeToAdd: ASTNode, addAfterNode: ASTNode?): ASTNode {
    val nextNode =
        if (addAfterNode == null) { //add as first child node
            firstChildNode.treeNext
        } else
            addAfterNode.treeNext

    this.addChild(nodeToAdd,nextNode)
    return nodeToAdd
}


/**
 * same as [addChildAfter] for adding a newLine node
 */
internal fun ASTNode.addChildNewlineAfter(addAfterNode:ASTNode?):ASTNode=
    addChildAfter(PsiWhiteSpaceImpl("\n"), addAfterNode)

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
 * find the first parent of this node that is a [ElementType.CLASS] node
 */
internal fun ASTNode?.getEnclosingClassNode():ASTNode? {
    if(this==null) return null
    var parentNode=this
    while(parentNode!=null) {
        parentNode=parentNode.treeParent
        if(parentNode iz ElementType.CLASS) return  parentNode
    }
    return null
}

/**
 * find the enclosing class name
 */
internal fun ASTNode?.getEnclosingClassName():String? {
    val classNode=getEnclosingClassNode() ?:return null
    val identifierNode = classNode.findChildByType(ElementType.IDENTIFIER) ?:return null
    return identifierNode.text
}


/**
 * substitute an [ASTNode] with its block-commented equivalent
 */
internal fun ASTNode?.commentOutNode() {
    if(this==null) return
    val parent=treeParent ?: return
    val kotlinCodeConvertedToComment= PsiCommentImpl(ElementType.BLOCK_COMMENT,"/* $text */")
    parent.addChild(kotlinCodeConvertedToComment,this)
    parent.removeChild(this)

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

internal  fun createDartCodeNode(dartCode:String) :ASTNode {
    return LeafPsiElement(ElementType.DART_CODE,dartCode)
}


/**
//         ~.psi.KtClassBody (CLASS_BODY)
//           ~.c.i.p.impl.source.tree.LeafPsiElement (LBRACE) "{"
//           ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) "\n\n"
//           ~.c.i.p.impl.source.tree.LeafPsiElement (RBRACE) "}"
 */
internal fun createEmptyClassBodyNode() : ASTNode {
    val classBodyNode = CompositeElement(ElementType.CLASS_BODY)
    with(classBodyNode) {
        rawAddChildren(PsiWhiteSpaceImpl("\n"))
        rawAddChildren(LeafPsiElement(ElementType.LBRACE,"{"))
        rawAddChildren(PsiWhiteSpaceImpl("\n"))
        rawAddChildren(LeafPsiElement(ElementType.RBRACE,"}"))
        rawAddChildren(PsiWhiteSpaceImpl("\n"))
    }
    return  classBodyNode
}

/**
 * return list of of nodes of type [ElementType.VALUE_PARAMETER]
 */
internal fun parseValueParameters(node:ASTNode): MutableList<ASTNode>? {
    val valueParamListNode =node.findChildByType(ElementType.VALUE_PARAMETER_LIST) ?: return null
    //note: that nextPar is not assigned an actual parameter, only after call to nextSibling it will contain it
    var nextPar=valueParamListNode.firstChildNode
    val extractedParams= mutableListOf<ASTNode>()
    while(nextPar!=null)  {
        nextPar=nextPar.nextSibling { it iz ElementType.VALUE_PARAMETER }
        if(nextPar!=null) {
            extractedParams.add(nextPar)
        }
    }
    return extractedParams
}


//             ~.psi.KtParameter (VALUE_PARAMETER)
//               ~.c.i.p.impl.source.tree.LeafPsiElement (VAR_KEYWORD) "var"
//               ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
//               ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "d"
//               ~.c.i.p.impl.source.tree.LeafPsiElement (COLON) ":"
//               ~.psi.KtTypeReference (TYPE_REFERENCE)
//                 ~.psi.KtUserType (USER_TYPE)
//                   ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
//                     ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "Double"
/**
 * input is a list of [ASTNode] object as obtained with method [parseValueParameters]
 * output is the list of field names (arguments that are marked as "var" or "val"
 */
internal fun extractFieldNamesFromValueParameters(valueParameters:List<ASTNode>?):List<String>? {
    val fieldNames=valueParameters?.mapNotNull {
        if(it.findChildByType(valvarTokenSet)!=null) it.findChildByType(ElementType.IDENTIFIER)?.text else null
    }
    return fieldNames
}

internal fun findFirstChildInsideClassBody(classBodyNode:ASTNode): ASTNode? {
    var classBodyNodeFirstChildInside=classBodyNode.findChildByType(ElementType.LBRACE)?.treeNext //skip lbrace
    if(classBodyNodeFirstChildInside iz ElementType.RBRACE) { //empty body: add a new line
        val newLineNode=PsiWhiteSpaceImpl("\n")
        classBodyNodeFirstChildInside?.treeParent?.addChild(newLineNode,classBodyNodeFirstChildInside)
        classBodyNodeFirstChildInside=newLineNode
    }
    return classBodyNodeFirstChildInside
}

internal val valvarTokenSet = TokenSet.create(
    ElementType.VAL_KEYWORD,
    ElementType.VAR_KEYWORD)
