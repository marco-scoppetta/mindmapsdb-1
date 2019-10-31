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

package grakn.core.graph.graphdb.query.profile;

import org.janusgraph.graphdb.query.Query;
import org.janusgraph.graphdb.query.graph.JointIndexQuery.Subquery;

import java.util.Collection;
import java.util.function.Function;


public interface QueryProfiler {

    String CONDITION_ANNOTATION = "condition";
    String ORDERS_ANNOTATION = "orders";
    String LIMIT_ANNOTATION = "limit";

    String MULTIQUERY_ANNOTATION = "multi";
    String MULTIPREFETCH_ANNOTATION = "multiPreFetch";
    String NUMVERTICES_ANNOTATION = "vertices";
    String PARTITIONED_VERTEX_ANNOTATION = "partitioned";

    String FITTED_ANNOTATION = "isFitted";
    String ORDERED_ANNOTATION = "isOrdered";
    String QUERY_ANNOTATION = "query";
    String FULLSCAN_ANNOTATION = "fullscan";
    String INDEX_ANNOTATION = "index";

    String OR_QUERY = "OR-query";
    String AND_QUERY = "AND-query";
    String OPTIMIZATION = "optimization";

    org.janusgraph.graphdb.query.profile.QueryProfiler NO_OP = new org.janusgraph.graphdb.query.profile.QueryProfiler() {
        @Override
        public org.janusgraph.graphdb.query.profile.QueryProfiler addNested(String groupName) {
            return this;
        }

        @Override
        public org.janusgraph.graphdb.query.profile.QueryProfiler setAnnotation(String key, Object value) {
            return this;
        }

        @Override
        public void startTimer() {
        }

        @Override
        public void stopTimer() {
        }

        @Override
        public void setResultSize(long size) {
        }
    };


    org.janusgraph.graphdb.query.profile.QueryProfiler addNested(String groupName);

    org.janusgraph.graphdb.query.profile.QueryProfiler setAnnotation(String key, Object value);

    void startTimer();

    void stopTimer();

    void setResultSize(long size);

    static<Q extends Query,R extends Collection> R profile(org.janusgraph.graphdb.query.profile.QueryProfiler profiler, Q query, Function<Q, R> queryExecutor) {
        return profile(profiler,query,false,queryExecutor);
    }

    static<Q extends Query,R extends Collection> R profile(String groupName, org.janusgraph.graphdb.query.profile.QueryProfiler profiler, Q query, Function<Q, R> queryExecutor) {
        return profile(groupName,profiler,query,false,queryExecutor);
    }

    static<Q extends Query,R extends Collection> R profile(org.janusgraph.graphdb.query.profile.QueryProfiler profiler, Q query, boolean multiQuery, Function<Q, R> queryExecutor) {
        return profile("backend-query",profiler,query,multiQuery,queryExecutor);
    }

    static<Q extends Query,R extends Collection> R profile(String groupName, org.janusgraph.graphdb.query.profile.QueryProfiler profiler, Q query, boolean multiQuery, Function<Q, R> queryExecutor) {
        final org.janusgraph.graphdb.query.profile.QueryProfiler sub = profiler.addNested(groupName);
        sub.setAnnotation(QUERY_ANNOTATION, query);
        if (query.hasLimit()) sub.setAnnotation(LIMIT_ANNOTATION,query.getLimit());
        sub.startTimer();
        final R result = queryExecutor.apply(query);
        sub.stopTimer();
        long resultSize = 0;
        if (multiQuery && profiler!= org.janusgraph.graphdb.query.profile.QueryProfiler.NO_OP) {
            //The result set is a collection of collections, but don't do this computation if profiling is disabled
            for (Object r : result) {
                if (r instanceof Collection) resultSize+=((Collection)r).size();
                else resultSize++;
            }
        } else {
            resultSize = result.size();
        }
        sub.setResultSize(resultSize);
        return result;
    }

    static org.janusgraph.graphdb.query.profile.QueryProfiler startProfile(org.janusgraph.graphdb.query.profile.QueryProfiler profiler, Subquery query) {
        final org.janusgraph.graphdb.query.profile.QueryProfiler sub = profiler.addNested("backend-query");
        sub.setAnnotation(QUERY_ANNOTATION, query);
        if (query.hasLimit()) sub.setAnnotation(LIMIT_ANNOTATION,query.getLimit());
        sub.startTimer();
        return sub;
    }
}