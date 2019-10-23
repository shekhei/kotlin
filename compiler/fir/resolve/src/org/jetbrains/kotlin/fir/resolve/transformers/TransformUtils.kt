/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.resolve.transformers

import org.jetbrains.kotlin.fir.FirElement
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.fir.expressions.FirStatement
import org.jetbrains.kotlin.fir.expressions.FirWrappedArgumentExpression
import org.jetbrains.kotlin.fir.expressions.impl.FirNoReceiverExpression
import org.jetbrains.kotlin.fir.references.FirNamedReference
import org.jetbrains.kotlin.fir.references.FirResolvedCallableReference
import org.jetbrains.kotlin.fir.types.FirImplicitTypeRef
import org.jetbrains.kotlin.fir.types.FirTypeRef
import org.jetbrains.kotlin.fir.visitors.FirDefaultTransformer
import org.jetbrains.kotlin.fir.visitors.FirTransformer


internal object MapArguments : FirDefaultTransformer<Map<FirElement, FirElement>>() {
    override fun <E : FirElement> transformElement(element: E, data: Map<FirElement, FirElement>): E {
        return ((data[element] ?: element) as E)
    }

    override fun transformFunctionCall(
        functionCall: FirFunctionCall,
        data: Map<FirElement, FirElement>
    ): FirStatement {
        return (functionCall.transformArguments(this, data) as FirStatement)
    }

    override fun transformWrappedArgumentExpression(
        wrappedArgumentExpression: FirWrappedArgumentExpression,
        data: Map<FirElement, FirElement>
    ): FirStatement {
        return (wrappedArgumentExpression.transformChildren(this, data) as FirStatement)
    }
}

internal object StoreType : FirDefaultTransformer<FirTypeRef>() {
    override fun <E : FirElement> transformElement(element: E, data: FirTypeRef): E {
        return element
    }

    override fun transformTypeRef(typeRef: FirTypeRef, data: FirTypeRef): FirTypeRef {
        return data
    }
}

internal object TransformImplicitType : FirDefaultTransformer<FirTypeRef>() {
    override fun <E : FirElement> transformElement(element: E, data: FirTypeRef): E {
        return element
    }

    override fun transformImplicitTypeRef(
        implicitTypeRef: FirImplicitTypeRef,
        data: FirTypeRef
    ): FirTypeRef {
        return data
    }
}


internal object StoreNameReference : FirDefaultTransformer<FirNamedReference>() {
    override fun <E : FirElement> transformElement(element: E, data: FirNamedReference): E {
        return element
    }

    override fun transformNamedReference(
        namedReference: FirNamedReference,
        data: FirNamedReference
    ): FirNamedReference {
        return data
    }
}

internal object StoreCalleeReference : FirTransformer<FirResolvedCallableReference>() {
    override fun <E : FirElement> transformElement(element: E, data: FirResolvedCallableReference): E {
        return element
    }

    override fun transformNamedReference(
        namedReference: FirNamedReference,
        data: FirResolvedCallableReference
    ): FirNamedReference {
        return data
    }

    override fun transformResolvedCallableReference(
        resolvedCallableReference: FirResolvedCallableReference,
        data: FirResolvedCallableReference
    ): FirNamedReference {
        return data
    }
}

internal object StoreReceiver : FirTransformer<FirExpression>() {
    override fun <E : FirElement> transformElement(element: E, data: FirExpression): E {
        return element
    }

    override fun transformExpression(expression: FirExpression, data: FirExpression): FirStatement {
        if (expression !is FirNoReceiverExpression) return expression
        return data
    }
}