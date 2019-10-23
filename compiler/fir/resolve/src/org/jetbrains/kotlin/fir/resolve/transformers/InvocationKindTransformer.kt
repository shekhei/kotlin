/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.resolve.transformers

import org.jetbrains.kotlin.contracts.description.InvocationKind
import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.declarations.FirAnonymousFunction
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.expressions.FirLambdaArgumentExpression
import org.jetbrains.kotlin.fir.expressions.FirNamedArgumentExpression
import org.jetbrains.kotlin.fir.expressions.FirStatement
import org.jetbrains.kotlin.fir.visitors.FirTransformer


object InvocationKindTransformer : FirTransformer<InvocationKind?>() {
    override fun <E : FirElement> transformElement(element: E, data: InvocationKind?): E {
        return element
    }

    override fun transformAnonymousFunction(
        anonymousFunction: FirAnonymousFunction,
        data: InvocationKind?
    ): FirStatement {
        if (data != null) {
            anonymousFunction.replaceInvocationKind(data)
        }
        return anonymousFunction
    }

    override fun transformLambdaArgumentExpression(
        lambdaArgumentExpression: FirLambdaArgumentExpression,
        data: InvocationKind?
    ): FirStatement {
        lambdaArgumentExpression.transformChildren(this, data)
        return lambdaArgumentExpression
    }

    override fun transformNamedArgumentExpression(
        namedArgumentExpression: FirNamedArgumentExpression,
        data: InvocationKind?
    ): FirStatement {
        namedArgumentExpression.transformChildren(this, data)
        return namedArgumentExpression
    }

    override fun transformFunctionCall(functionCall: FirFunctionCall, data: InvocationKind?): FirStatement {
        // TODO: add contracts handling and inline handling
        return (functionCall.transformChildren(this, InvocationKind.EXACTLY_ONCE) as FirFunctionCall)
    }
}