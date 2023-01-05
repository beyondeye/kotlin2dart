package com.pinterest.ktlint.ruleset.k2dart.rules

import com.pinterest.ktlint.ruleset.k2dart.k2dartRulesetId
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import com.pinterest.ktlint.core.ast.iz
import com.pinterest.ktlint.core.ast.izNot
import com.pinterest.ktlint.core.ast.nextSibling
import com.pinterest.ktlint.ruleset.k2dart.utils.extractFieldNamesFromValueParameters
import com.pinterest.ktlint.ruleset.k2dart.utils.parseValueParameters
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiCommentImpl

// generate data class autogenerated method
public class DataClassesRule : Rule("$k2dartRulesetId:$ruleName") {
    public companion object {
        public const val ruleName:String="data-classes"
    }
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {
        // - create a list of all fields defined in primary constructor
        // generate equals/ hashcode/ copywith methods with dartcode elementype
        if(node izNot ElementType.DATA_KEYWORD) return
        val modlistNode=node.treeParent
        //remove the "data" modifier
        modlistNode.removeChild(node)
        val classNode = modlistNode.treeParent
        //add /* data */ before class modifiers
        classNode.addChild(PsiCommentImpl(ElementType.BLOCK_COMMENT,"/* data */"),modlistNode)

        //todo //if no primary constructor, currently I don't know how to handle it, so return
        val primaryConstrNode=classNode.findChildByType(ElementType.PRIMARY_CONSTRUCTOR) ?: return

        //now parse the list of fields of the data class
        val extractedParams= parseValueParameters(primaryConstrNode)

        //             ~.psi.KtParameter (VALUE_PARAMETER)
        //               ~.c.i.p.impl.source.tree.LeafPsiElement (VAR_KEYWORD) "var"
        //               ~.c.i.p.impl.source.tree.PsiWhiteSpaceImpl (WHITE_SPACE) " "
        //               ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "d"
        //               ~.c.i.p.impl.source.tree.LeafPsiElement (COLON) ":"
        //               ~.psi.KtTypeReference (TYPE_REFERENCE)
        //                 ~.psi.KtUserType (USER_TYPE)
        //                   ~.psi.KtNameReferenceExpression (REFERENCE_EXPRESSION)
        //                     ~.c.i.p.impl.source.tree.LeafPsiElement (IDENTIFIER) "Double"
        val fieldNames= extractFieldNamesFromValueParameters(extractedParams)
        //now check if we have a class body
        //we should always have one thanks to the MissingClassBodyRule
        val classBodyNode=primaryConstrNode.nextSibling{it iz ElementType.CLASS_BODY} ?:return

    }
}
