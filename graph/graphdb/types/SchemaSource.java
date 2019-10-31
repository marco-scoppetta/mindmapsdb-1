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

package grakn.core.graph.graphdb.types;

import com.google.common.base.Preconditions;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.janusgraph.core.schema.SchemaStatus;
import org.janusgraph.graphdb.types.IndexType;
import org.janusgraph.graphdb.types.TypeDefinitionCategory;
import org.janusgraph.graphdb.types.TypeDefinitionMap;


public interface SchemaSource {

    long longId();

    String name();

    SchemaStatus getStatus();

    TypeDefinitionMap getDefinition();

    Iterable<Entry> getRelated(TypeDefinitionCategory def, Direction dir);

    /**
     * Resets the internal caches used to speed up lookups on this schema source.
     * This is needed when the source gets modified.
     */
    void resetCache();

    IndexType asIndexType();

    class Entry {

        private final org.janusgraph.graphdb.types.SchemaSource schemaType;
        private final Object modifier;

        public Entry(org.janusgraph.graphdb.types.SchemaSource schemaType, Object modifier) {
            this.schemaType = Preconditions.checkNotNull(schemaType);
            this.modifier = modifier;
        }

        public org.janusgraph.graphdb.types.SchemaSource getSchemaType() {
            return schemaType;
        }

        public Object getModifier() {
            return modifier;
        }
    }

}