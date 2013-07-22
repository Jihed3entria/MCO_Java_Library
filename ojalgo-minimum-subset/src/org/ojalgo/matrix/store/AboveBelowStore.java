/*
 * Copyright 1997-2013 Optimatika (www.optimatika.se)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.ojalgo.matrix.store;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.ojalgo.ProgrammingError;
import org.ojalgo.matrix.MatrixUtils;
import org.ojalgo.scalar.Scalar;

/**
 * A merger of two {@linkplain MatrixStore} instances by placing one store below
 * the other. The two matrices must have the same number of columns. The
 * columns of the two matrices are logically merged to form new longer
 * columns.
 * 
 * @author apete
 */
public final class AboveBelowStore<N extends Number> extends DelegatingStore<N> {

    private final MatrixStore<N> myLowerStore;
    private final int myRowSplit;

    public AboveBelowStore(final MatrixStore<N> aBase, final MatrixStore<N> aLowerStore) {

        super(aBase.getRowDim() + aLowerStore.getRowDim(), aBase.getColDim(), aBase);

        myLowerStore = aLowerStore;
        myRowSplit = aBase.getRowDim();
    }

    @SuppressWarnings("unused")
    private AboveBelowStore(final MatrixStore<N> aBase) {

        this(aBase, null);

        ProgrammingError.throwForIllegalInvocation();
    }

    /**
     * @see org.ojalgo.matrix.store.MatrixStore#doubleValue(int, int)
     * @deprecated Use {@link #doubleValue(long,long)} instead
     */
    @Deprecated
    public double doubleValue(final int aRow, final int aCol) {
        return this.doubleValue(aRow, aCol);
    }

    /**
     * @see org.ojalgo.matrix.store.MatrixStore#doubleValue(long, long)
     */
    public double doubleValue(final long aRow, final long aCol) {
        return (aRow >= myRowSplit) ? myLowerStore.doubleValue(aRow - myRowSplit, aCol) : this.getBase().doubleValue(aRow, aCol);
    }

    public N get(final long aRow, final long aCol) {
        return (aRow >= myRowSplit) ? myLowerStore.get(aRow - myRowSplit, aCol) : this.getBase().get(aRow, aCol);
    }

    public boolean isLowerLeftShaded() {
        return BOOLEAN_FALSE;
    }

    public boolean isUpperRightShaded() {
        return this.getBase().isUpperRightShaded();
    }

    @Override
    public MatrixStore<N> multiplyLeft(final MatrixStore<N> aStore) {

        MatrixStore<N> tmpBase = null;
        final Future<MatrixStore<N>> tmpBaseFuture = this
                .executeMultiplyLeftOnBase(new ColumnsStore<N>(aStore, MatrixUtils.makeIncreasingRange(0, myRowSplit)));

        final MatrixStore<N> tmpDiff = myLowerStore.multiplyLeft(new ColumnsStore<N>(aStore, MatrixUtils.makeIncreasingRange(myRowSplit, aStore.getColDim()
                - myRowSplit)));

        try {
            tmpBase = tmpBaseFuture.get();
        } catch (final InterruptedException anException) {
            tmpBase = null;
        } catch (final ExecutionException anException) {
            tmpBase = null;
        }

        if (tmpBase instanceof ZeroStore<?>) {
            return tmpDiff;
        } else if (tmpDiff instanceof ZeroStore<?>) {
            return tmpBase;
        } else if (tmpBase instanceof PhysicalStore<?>) {
            ((PhysicalStore<N>) tmpBase).fillMatching(tmpBase, this.factory().getFunctionSet().add(), tmpDiff);
            return tmpBase;
        } else if (tmpDiff instanceof PhysicalStore<?>) {
            ((PhysicalStore<N>) tmpDiff).fillMatching(tmpBase, this.factory().getFunctionSet().add(), tmpDiff);
            return tmpDiff;
        } else {
            return new SuperimposedStore<N>(tmpBase, tmpDiff);
        }
    }

    @Override
    public MatrixStore<N> multiplyRight(final MatrixStore<N> aStore) {

        MatrixStore<N> tmpBase = null;
        final Future<MatrixStore<N>> tmpBaseFuture = this.executeMultiplyRightOnBase(aStore);

        final MatrixStore<N> tmpLower = myLowerStore.multiplyRight(aStore);

        try {
            tmpBase = tmpBaseFuture.get();
        } catch (final InterruptedException anException) {
            tmpBase = null;
        } catch (final ExecutionException anException) {
            tmpBase = null;
        }

        return new AboveBelowStore<N>(tmpBase, tmpLower);
    }

    public Scalar<N> toScalar(final int aRow, final int aCol) {
        return (aRow >= myRowSplit) ? myLowerStore.toScalar(aRow - myRowSplit, aCol) : this.getBase().toScalar(aRow, aCol);
    }

}
