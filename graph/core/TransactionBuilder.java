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

import org.janusgraph.graphdb.configuration.GraphDatabaseConfiguration;
import org.janusgraph.graphdb.transaction.StandardJanusGraphTx;

import java.time.Instant;

/**
 * Constructor returned by {@link org.janusgraph.core.JanusGraph#buildTransaction()} to build a new transaction.
 * The TransactionBuilder allows certain aspects of the resulting transaction to be configured up-front.
 *
 */
public interface TransactionBuilder {

    /**
     * Makes the transaction read only. Any writes will cause an exception.
     * Read-only transactions do not have to maintain certain data structures and can hence be more efficient.
     *
     * @return Object containing read-only properties set to true
     */
    org.janusgraph.core.TransactionBuilder readOnly();

    /**
     * Enabling batch loading disables a number of consistency checks inside JanusGraph to speed up the ingestion of
     * data under the assumptions that inconsistencies are resolved prior to loading.
     *
     * @return Object containting properties that will enable batch loading
     */
    org.janusgraph.core.TransactionBuilder enableBatchLoading();

    /**
     * Disables batch loading by ensuring that consistency checks are applied in this transaction. This allows
     * an individual transaction to use consistency checks when the graph as a whole is configured to not use them,
     * which is useful when defining schema elements in a graph with batch-loading enabled.
     *
     * @return Object containting properties that will disable batch loading
     */
    org.janusgraph.core.TransactionBuilder disableBatchLoading();

    /**
     * Configures the size of the internal caches used in the transaction.
     *
     * @param size The size of the initial cache for the transaction
     * @return Object containing the internal cache properties
     */
    org.janusgraph.core.TransactionBuilder vertexCacheSize(int size);

    /**
     * Configures the initial size of the map of modified vertices held by this
     * transaction. This is a performance hint, not a hard upper bound. The map
     * will grow if the transaction ends up modifying more vertices than
     * expected.
     *
     * @param size The initial size of the transaction's dirty vertex collection
     * @return Object containing properties that configure inital map size of modified vertices
     */
    org.janusgraph.core.TransactionBuilder dirtyVertexSize(int size);

    /**
     * Enables/disables checks that verify that each vertex actually exists in the underlying data store when it is retrieved.
     * This might be useful to address common data degradation issues but has adverse impacts on performance due to
     * repeated existence checks.
     * <p>
     * Note, that these checks apply to vertex retrievals inside the query execution engine and not to vertex ids provided
     * by the user.
     *
     * @param enabled Enable or disable the internal vertex existence checks
     * @return Object with the internal vertex existence check properties
     */
    org.janusgraph.core.TransactionBuilder checkInternalVertexExistence(boolean enabled);

    /**
     * Enables/disables checking whether the vertex with a user provided id indeed exists. If the user is absolutely sure
     * that the vertices for the ids provided in this transaction exist in the underlying data store, then disabling the
     * vertex existence check will improve performance because it eliminates a database call.
     * However, if a provided vertex id does not exist in the database and checking is disabled, JanusGraph will assume it
     * exists which can lead to data and query inconsistencies.
     *
     * @param enabled Enable or disable the external vertex existence checks
     * @return Object with the external vertex existence check properties
     */
    org.janusgraph.core.TransactionBuilder checkExternalVertexExistence(boolean enabled);

    /**
     * Sets the timestamp for this transaction. The transaction will be recorded
     * with this timestamp in those storage backends where the timestamp is
     * recorded.
     *
     * @param instant The instant at which the commit took place
     * @return Object with the commit time property
     */
    org.janusgraph.core.TransactionBuilder commitTime(Instant instant);


    /**
     * Sets the group name for this transaction which provides a way for gathering
     * reporting on multiple transactions into one group.
     *
     * By setting a group one enables Metrics for this transaction, and defines what string
     * should start the transaction's metric names.
     * <p>
     * If null, Metrics collection is totally disabled for this transaction.
     * <p>
     * If empty, Metrics collection is enabled, but there will be no prefix.
     * Where the default setting would generate metrics names in the form
     * "prefix.x.y.z", this transaction will instead use metric names in the
     * form "x.y.z".
     * <p>
     * If nonempty, Metrics collection is enabled and the prefix will be used
     * for all of this transaction's measurements.
     * <p>
     * Note: setting this to a non-null value only partially overrides
     * {@link GraphDatabaseConfiguration#BASIC_METRICS} = false in the graph
     * database configuration. When Metrics are disabled at the graph level and
     * enabled at the transaction level, storage backend timings and counters
     * will remain disabled.
     * <p>
     * The default value is
     * {@link GraphDatabaseConfiguration#METRICS_PREFIX_DEFAULT}.
     *
     * Sets the name prefix used for Metrics recorded by this transaction. If
     * metrics is enabled via {@link GraphDatabaseConfiguration#BASIC_METRICS},
     * this string will be prepended to all JanusGraph metric names.
     *
     * @param name Metric name prefix for this transaction
     * @return Object containing transaction prefix name property
     */
    org.janusgraph.core.TransactionBuilder groupName(String name);

    /**
     * Name of the LOG to be used for logging the mutations in this transaction. If no LOG identifier is set,
     * then this transaction will not be logged.
     *
     * @param logName name of transaction LOG
     * @return Object containing LOG identifier property
     */
    org.janusgraph.core.TransactionBuilder logIdentifier(String logName);

    /**
     * Configures this transaction such that queries against partitioned vertices are
     * restricted to the given partitions.
     *
     * @param partitions Array of the int identifier of the partitions to be queried
     * @return Object with restricted partitions
     */
    org.janusgraph.core.TransactionBuilder restrictedPartitions(int[] partitions);

    /**
     * Configures a custom option on this transaction which will be passed through to the storage and indexing backends.
     * @param k Name of the configuration element.
     * @param v Object containing the custom options to be applied.
     * @return Object containing the properties in param v
     */
    org.janusgraph.core.TransactionBuilder customOption(String k, Object v);

    /**
     * Starts and returns the transaction build by this builder
     *
     * @return A new transaction configured according to this builder
     */
    StandardJanusGraphTx start();

}