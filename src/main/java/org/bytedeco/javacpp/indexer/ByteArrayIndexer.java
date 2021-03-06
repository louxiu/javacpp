/*
 * Copyright (C) 2014-2019 Samuel Audet
 *
 * Licensed either under the Apache License, Version 2.0, or (at your option)
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation (subject to the "Classpath" exception),
 * either version 2, or any later version (collectively, the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     http://www.gnu.org/licenses/
 *     http://www.gnu.org/software/classpath/license.html
 *
 * or as provided in the LICENSE.txt file that accompanied this code.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bytedeco.javacpp.indexer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * An indexer for a {@code byte[]} array.
 *
 * @author Samuel Audet
 */
public class ByteArrayIndexer extends ByteIndexer {
    /** The instance for the raw memory interface. */
    protected static final Raw RAW = Raw.getInstance();
    /** The wrapping buffer. */
    protected ByteBuffer buffer;
    /** The backing array. */
    protected byte[] array;

    /** Calls {@code ByteArrayIndexer(array, { array.length }, { 1 })}. */
    public ByteArrayIndexer(byte[] array) {
        this(array, new long[] { array.length }, ONE_STRIDE);
    }

    /** Calls {@code ByteArrayIndexer(array, sizes, strides(sizes))}. */
    public ByteArrayIndexer(byte[] array, long... sizes) {
        this(array, sizes, strides(sizes));
    }

    /** Constructor to set the {@link #array}, {@link #sizes} and {@link #strides}. */
    public ByteArrayIndexer(byte[] array, long[] sizes, long[] strides) {
        super(sizes, strides);
        this.array = array;
    }

    @Override public byte[] array() {
        return array;
    }

    @Override public byte get(long i) {
        return array[(int)i];
    }
    @Override public ByteIndexer get(long i, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = array[(int)i * (int)strides[0] + n];
        }
        return this;
    }
    @Override public byte get(long i, long j) {
        return array[(int)i * (int)strides[0] + (int)j];
    }
    @Override public ByteIndexer get(long i, long j, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = array[(int)i * (int)strides[0] + (int)j * (int)strides[1] + n];
        }
        return this;
    }
    @Override public byte get(long i, long j, long k) {
        return array[(int)i * (int)strides[0] + (int)j * (int)strides[1] + (int)k];
    }
    @Override public byte get(long... indices) {
        return array[(int)index(indices)];
    }
    @Override public ByteIndexer get(long[] indices, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            b[offset + n] = array[(int)index(indices) + n];
        }
        return this;
    }

    @Override public ByteIndexer put(long i, byte b) {
        array[(int)i] = b;
        return this;
    }
    @Override public ByteIndexer put(long i, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            array[(int)i * (int)strides[0] + n] = b[offset + n];
        }
        return this;
    }
    @Override public ByteIndexer put(long i, long j, byte b) {
        array[(int)i * (int)strides[0] + (int)j] = b;
        return this;
    }
    @Override public ByteIndexer put(long i, long j, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            array[(int)i * (int)strides[0] + (int)j * (int)strides[1] + n] = b[offset + n];
        }
        return this;
    }
    @Override public ByteIndexer put(long i, long j, long k, byte b) {
        array[(int)i * (int)strides[0] + (int)j * (int)strides[1] + (int)k] = b;
        return this;
    }
    @Override public ByteIndexer put(long[] indices, byte b) {
        array[(int)index(indices)] = b;
        return this;
    }
    @Override public ByteIndexer put(long[] indices, byte[] b, int offset, int length) {
        for (int n = 0; n < length; n++) {
            array[(int)index(indices) + n] = b[offset + n];
        }
        return this;
    }

    ByteBuffer getBuffer() {
        if (buffer == null) {
            buffer = ByteBuffer.wrap(array).order(ByteOrder.nativeOrder());
        }
        return buffer;
    }

    @Override public short getShort(long i) {
        if (RAW != null) {
            return RAW.getShort(array, checkIndex(i, array.length - 1));
        } else {
            return getBuffer().getShort((int)i);
        }
    }
    @Override public ByteIndexer putShort(long i, short s) {
        if (RAW != null) {
            RAW.putShort(array, checkIndex(i, array.length - 1), s);
        } else {
            getBuffer().putShort((int)i, s);
        }
        return this;
    }

    @Override public int getInt(long i) {
        if (RAW != null) {
            return RAW.getInt(array, checkIndex(i, array.length - 3));
        } else {
            return getBuffer().getInt((int)i);
        }
    }
    @Override public ByteIndexer putInt(long i, int j) {
        if (RAW != null) {
            RAW.putInt(array, checkIndex(i, array.length - 3), j);
        } else {
            getBuffer().putInt((int)i, j);
        }
        return this;
    }

    @Override public long getLong(long i) {
        if (RAW != null) {
            return RAW.getLong(array, checkIndex(i, array.length - 7));
        } else {
            return getBuffer().getLong((int)i);
        }
    }
    @Override public ByteIndexer putLong(long i, long j) {
        if (RAW != null) {
            RAW.putLong(array, checkIndex(i, array.length - 7), j);
        } else {
            getBuffer().putLong((int)i, j);
        }
        return this;
    }

    @Override public float getFloat(long i) {
        if (RAW != null) {
            return RAW.getFloat(array, checkIndex(i, array.length - 3));
        } else {
            return getBuffer().getFloat((int)i);
        }
    }
    @Override public ByteIndexer putFloat(long i, float f) {
        if (RAW != null) {
            RAW.putFloat(array, checkIndex(i, array.length - 3), f);
        } else {
            getBuffer().putFloat((int)i, f);
        }
        return this;
    }

    @Override public double getDouble(long i) {
        if (RAW != null) {
            return RAW.getDouble(array, checkIndex(i, array.length - 7));
        } else {
            return getBuffer().getDouble((int)i);
        }
    }
    @Override public ByteIndexer putDouble(long i, double d) {
        if (RAW != null) {
            RAW.putDouble(array, checkIndex(i, array.length - 7), d);
        } else {
            getBuffer().putDouble((int)i, d);
        }
        return this;
    }

    @Override public char getChar(long i) {
        if (RAW != null) {
            return RAW.getChar(array, checkIndex(i, array.length - 1));
        } else {
            return getBuffer().getChar((int)i);
        }
    }
    @Override public ByteIndexer putChar(long i, char c) {
        if (RAW != null) {
            RAW.putChar(array, checkIndex(i, array.length - 1), c);
        } else {
            getBuffer().putChar((int)i, c);
        }
        return this;
    }

    @Override public int getUByte(long i) {
        if (RAW != null) {
            return RAW.getByte(array, checkIndex(i, array.length - 1)) & 0xFF;
        } else {
            return getBuffer().get((int)i) & 0xFF;
        }
    }
    @Override public ByteIndexer putUByte(long i, int b) {
        if (RAW != null) {
            RAW.putByte(array, checkIndex(i, array.length - 1), (byte)b);
        } else {
            getBuffer().put((int)i, (byte)b);
        }
        return this;
    }

    @Override public int getUShort(long i) {
        if (RAW != null) {
            return RAW.getShort(array, checkIndex(i, array.length - 1)) & 0xFFFF;
        } else {
            return getBuffer().getShort((int)i) & 0xFFFF;
        }
    }
    @Override public ByteIndexer putUShort(long i, int s) {
        if (RAW != null) {
            RAW.putShort(array, checkIndex(i, array.length - 1), (short)s);
        } else {
            getBuffer().putShort((int)i, (short)s);
        }
        return this;
    }

    @Override public float getHalf(long i) {
        if (RAW != null) {
            return HalfIndexer.toFloat(RAW.getShort(array, checkIndex(i, array.length - 1)));
        } else {
            return HalfIndexer.toFloat(getBuffer().getShort((int)i));
        }
    }
    @Override public ByteIndexer putHalf(long i, float h) {
        if (RAW != null) {
            RAW.putShort(array, checkIndex(i, array.length - 1), (short)HalfIndexer.fromFloat(h));
        } else {
            getBuffer().putShort((int)i, (short)HalfIndexer.fromFloat(h));
        }
        return this;
    }

    @Override public float getBfloat16(long i) {
        if (RAW != null) {
            return Bfloat16Indexer.toFloat(RAW.getShort(array, checkIndex(i, array.length - 1)));
        } else {
            return Bfloat16Indexer.toFloat(getBuffer().getShort((int)i));
        }
    }
    @Override public ByteIndexer putBfloat16(long i, float h) {
        if (RAW != null) {
            RAW.putShort(array, checkIndex(i, array.length - 1), (short)Bfloat16Indexer.fromFloat(h));
        } else {
            getBuffer().putShort((int)i, (short)Bfloat16Indexer.fromFloat(h));
        }
        return this;
    }

    @Override public boolean getBoolean(long i) {
        if (RAW != null) {
            return RAW.getByte(array, checkIndex(i, array.length - 1)) != 0;
        } else {
            return getBuffer().get((int)i) != 0;
        }
    }
    @Override public ByteIndexer putBoolean(long i, boolean b) {
        if (RAW != null) {
            RAW.putByte(array, checkIndex(i, array.length - 1), b ? (byte)1 : (byte)0);
        } else {
            getBuffer().put((int)i, b ? (byte)1 : (byte)0);
        }
        return this;
    }

    @Override public void release() { array = null; }
}
