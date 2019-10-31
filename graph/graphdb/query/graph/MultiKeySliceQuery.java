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

package grakn.core.graph.graphdb.query.graph;

import com.google.common.base.Preconditions;
import org.janusgraph.diskstorage.BackendTransaction;
import org.janusgraph.diskstorage.EntryList;
import org.janusgraph.diskstorage.keycolumnvalue.KeySliceQuery;
import org.janusgraph.graphdb.query.BackendQuery;
import org.janusgraph.graphdb.query.BaseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MultiKeySliceQuery extends BaseQuery implements BackendQuery<org.janusgraph.graphdb.query.graph.MultiKeySliceQuery> {

    private final List<KeySliceQuery> queries;

    public MultiKeySliceQuery(List<KeySliceQuery> queries) {
        Preconditions.checkArgument(queries!=null && !queries.isEmpty());
        this.queries = queries;
    }

    @Override
    public org.janusgraph.graphdb.query.graph.MultiKeySliceQuery updateLimit(int newLimit) {
        org.janusgraph.graphdb.query.graph.MultiKeySliceQuery newQuery = new org.janusgraph.graphdb.query.graph.MultiKeySliceQuery(queries);
        newQuery.setLimit(newLimit);
        return newQuery;
    }

    public List<EntryList> execute(BackendTransaction tx) {
        int total = 0;
        final List<EntryList> result = new ArrayList<>(Math.min(getLimit(), queries.size()));
        for (KeySliceQuery ksq : queries) {
            EntryList next =tx.indexQuery(ksq.updateLimit(getLimit()-total));
            result.add(next);
            total+=next.size();
            if (total>=getLimit()) break;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(queries, getLimit());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        else if (other == null) return false;
        else if (!getClass().isInstance(other)) return false;
        org.janusgraph.graphdb.query.graph.MultiKeySliceQuery oth = (org.janusgraph.graphdb.query.graph.MultiKeySliceQuery) other;
        return getLimit()==oth.getLimit() && queries.equals(oth.queries);
    }

    @Override
    public String toString() {
        return "multiKSQ["+queries.size()+"]@"+getLimit();
    }

}