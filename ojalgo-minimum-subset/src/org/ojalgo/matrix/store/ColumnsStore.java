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

import org.ojalgo.ProgrammingError;
import org.ojalgo.scalar.Scalar;

/**
 * A selection (re-ordering) of columns.
 * 
 * @author apete
 */
public final class ColumnsStore<N extends Number> extends SelectingStore<N> {

    private final int[] myColumns;
    private final int myFirst;

    public ColumnsStore(final int aFirst, final int aLimit, final MatrixStore<N> aBase) {

        super(aBase.getRowDim(), aLimit - aFirst, aBase);

        myColumns = null;
        myFirst = aFirst;
    }

    public ColumnsStore(final MatrixStore<N> aBase, final int... someColumns) {

        super(aBase.getRowDim(), someColumns.length, aBase);

        myColumns = someColumns;
        myFirst = 0;
    }

    @SuppressWarnings("unused")
    private ColumnsStore(final MatrixStore<N> aBase) {

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
        if (myColumns != null) {
            return this.getBase().doubleValue(aRow, myColumns[(int) aCol]);
        } else {
            return this.getBase().doubleValue(aRow, myFirst + aCol);
        }
    }

    public N get(final long aRow, final long aCol) {
        if (myColumns != null) {
            return this.getBase().get(aRow, myColumns[(int) aCol]);
        } else {
            return this.getBase().get(aRow, myFirst + aCol);
        }
    }

    public boolean isLowerLeftShaded() {
        return BOOLEAN_FALSE;
    }

    public boolean isUpperRightShaded() {
        return BOOLEAN_FALSE;
    }

    public Scalar<N> toScalar(final int aRow, final int aCol) {
        if (myColumns != null) {
            return this.getBase().toScalar(aRow, myColumns[aCol]);
        } else {
            return this.getBase().toScalar(aRow, myFirst + aCol);
        }
    }

}
