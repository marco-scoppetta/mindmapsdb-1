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

package grakn.core.graph.core;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.janusgraph.core.JanusGraphEdge;
import org.janusgraph.core.JanusGraphVertex;
import org.janusgraph.core.JanusGraphVertexProperty;
import org.janusgraph.core.schema.Parameter;

import java.util.stream.Stream;

/**
 * A GraphQuery that queries for graph elements directly against a particular indexing backend and hence allows this
 * query mechanism to exploit the full range of features and functionality of the indexing backend.
 * However, the results returned by this query will not be adjusted to the modifications in a transaction. If there
 * are no changes in a transaction, this won't matter. If there are, the results of this query may not be consistent
 * with the transactional state.
 */
public interface JanusGraphIndexQuery {

    /**
     * Specifies the maximum number of elements to return
     */
    org.janusgraph.core.JanusGraphIndexQuery limit(int limit);

    /**
     * Specifies the offset of the query. Query results will be retrieved starting at the given offset.
     */
    org.janusgraph.core.JanusGraphIndexQuery offset(int offset);

    /**
     * Orders the element results of this query according
     * to their property for the given key in the given order (increasing/decreasing).
     */
    org.janusgraph.core.JanusGraphIndexQuery orderBy(String key, Order order);

    /**
     * Adds the given parameter to the list of parameters of this query.
     * Parameters are passed right through to the indexing backend to modify the query behavior.
     */
    org.janusgraph.core.JanusGraphIndexQuery addParameter(Parameter para);

    /**
     * Adds the given parameters to the list of parameters of this query.
     * Parameters are passed right through to the indexing backend to modify the query behavior.
     */
    org.janusgraph.core.JanusGraphIndexQuery addParameters(Iterable<Parameter> paras);

    /**
     * Adds the given parameters to the list of parameters of this query.
     * Parameters are passed right through to the indexing backend to modify the query behavior.
     */
    org.janusgraph.core.JanusGraphIndexQuery addParameters(Parameter... paras);

    /**
     * Sets the element identifier string that is used by this query builder as the token to identifier key references
     * in the query string.
     * <p>
     * For example, in the query 'v.name: Tom' the element identifier is 'v.'
     *
     * @param identifier The element identifier which must not be blank
     * @return This query builder
     */
    org.janusgraph.core.JanusGraphIndexQuery setElementIdentifier(String identifier);

    /**
     * Returns all vertices that match the query in the indexing backend.
     *
     * @deprecated use {@link #vertexStream()} instead.
     */
    @Deprecated
    Iterable<Result<JanusGraphVertex>> vertices();

    /**
     * Returns all vertices that match the query in the indexing backend.
     */
    Stream<Result<JanusGraphVertex>> vertexStream();

    /**
     * Returns all edges that match the query in the indexing backend.
     *
     * @deprecated use {@link #edgeStream()} instead.
     */
    @Deprecated
    Iterable<Result<JanusGraphEdge>> edges();

    /**
     * Returns all edges that match the query in the indexing backend.
     */
    Stream<Result<JanusGraphEdge>> edgeStream();

    /**
     * Returns all properties that match the query in the indexing backend.
     *
     * @deprecated use {@link #propertyStream()} instead.
     */
    @Deprecated
    Iterable<Result<JanusGraphVertexProperty>> properties();

    /**
     * Returns all properties that match the query in the indexing backend.
     */
    Stream<Result<JanusGraphVertexProperty>> propertyStream();

    /**
     * Returns total vertices that match the query in the indexing backend ignoring limit and offset.
     */
    Long vertexTotals();

    /**
     * Returns total edges that match the query in the indexing backend ignoring limit and offset.
     */
    Long edgeTotals();

    /**
     * Returns total properties that match the query in the indexing backend ignoring limit and offset.
     */
    Long propertyTotals();

    /**
     * Container of a query result with its score.
     *
     * @param <V>
     */
    interface Result<V extends Element> {

        /**
         * Returns the element that matches the query
         */
        V getElement();

        /**
         * Returns the score of the result with respect to the query (if available)
         */
        double getScore();

    }


}