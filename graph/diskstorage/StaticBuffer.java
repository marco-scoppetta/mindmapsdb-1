// Copyright 2017 JanusGraph Authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package grakn.core.graph.diskstorage;

import org.janusgraph.diskstorage.ReadBuffer;
import org.janusgraph.diskstorage.util.StaticArrayBuffer;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * A Buffer that only allows static access. This Buffer is immutable if
 * any returned byte array or ByteBuffer is not mutated.
 *
 */
public interface StaticBuffer extends Comparable<org.janusgraph.diskstorage.StaticBuffer> {

    int length();

    byte getByte(int position);

    boolean getBoolean(int position);

    short getShort(int position);

    int getInt(int position);

    long getLong(int position);

    char getChar(int position);

    float getFloat(int position);

    double getDouble(int position);

    byte[] getBytes(int position, int length);

    short[] getShorts(int position, int length);

    int[] getInts(int position, int length);

    long[] getLongs(int position, int length);

    char[] getChars(int position, int length);

    float[] getFloats(int position, int length);

    double[] getDoubles(int position, int length);

    org.janusgraph.diskstorage.StaticBuffer subrange(int position, int length);

    org.janusgraph.diskstorage.StaticBuffer subrange(int position, int length, boolean invert);

    ReadBuffer asReadBuffer();

    <T> T as(Factory<T> factory);

    //Convenience method
    ByteBuffer asByteBuffer();

    interface Factory<T> {

        T get(byte[] array, int offset, int limit);

    }

    Factory<byte[]> ARRAY_FACTORY = (array, offset, limit) -> {
        if (offset==0 && limit==array.length) return array;
        else return Arrays.copyOfRange(array,offset,limit);
    };

    Factory<ByteBuffer> BB_FACTORY = (array, offset, limit) -> ByteBuffer.wrap(array, offset, limit - offset);

    Factory<org.janusgraph.diskstorage.StaticBuffer> STATIC_FACTORY = StaticArrayBuffer::new;

}