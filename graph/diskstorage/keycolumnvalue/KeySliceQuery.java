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

package grakn.core.graph.diskstorage.keycolumnvalue;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.janusgraph.diskstorage.StaticBuffer;
import org.janusgraph.diskstorage.keycolumnvalue.SliceQuery;

/**
 * Extends {@link SliceQuery} by a key that identifies the location of the slice in the key-ring.
 *
 */

public class KeySliceQuery extends SliceQuery {

    private final StaticBuffer key;

    public KeySliceQuery(StaticBuffer key, StaticBuffer sliceStart, StaticBuffer sliceEnd) {
        super(sliceStart, sliceEnd);
        this.key = Preconditions.checkNotNull(key);
    }

    public KeySliceQuery(StaticBuffer key, SliceQuery query) {
        super(query);
        this.key = Preconditions.checkNotNull(key);
    }

    /**
     * @return the key of this query
     */
    public StaticBuffer getKey() {
        return key;
    }

    @Override
    public org.janusgraph.diskstorage.keycolumnvalue.KeySliceQuery setLimit(int limit) {
        super.setLimit(limit);
        return this;
    }

    @Override
    public org.janusgraph.diskstorage.keycolumnvalue.KeySliceQuery updateLimit(int newLimit) {
        return new org.janusgraph.diskstorage.keycolumnvalue.KeySliceQuery(key, this).setLimit(newLimit);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(key, super.hashCode());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        else if (other == null) return false;
        else if (!getClass().isInstance(other)) return false;
        org.janusgraph.diskstorage.keycolumnvalue.KeySliceQuery oth = (org.janusgraph.diskstorage.keycolumnvalue.KeySliceQuery) other;
        return key.equals(oth.key) && super.equals(oth);
    }

    public boolean subsumes(org.janusgraph.diskstorage.keycolumnvalue.KeySliceQuery oth) {
        return key.equals(oth.key) && super.subsumes(oth);
    }

    @Override
    public String toString() {
        return String.format("KeySliceQuery(key: %s, start: %s, end: %s, limit:%d)", key, getSliceStart(), getSliceEnd(), getLimit());
    }
}