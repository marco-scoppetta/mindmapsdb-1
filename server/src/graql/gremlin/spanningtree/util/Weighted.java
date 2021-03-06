/*
 * GRAKN.AI - THE KNOWLEDGE GRAPH
 * Copyright (C) 2018 Grakn Labs Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package grakn.core.graql.gremlin.spanningtree.util;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Add a weight to anything!
 *
 * @param <DirectedEdge> the type of object
 */
public class Weighted<DirectedEdge> implements Comparable<Weighted<DirectedEdge>> {
    public final DirectedEdge val;
    public final double weight;

    public Weighted(DirectedEdge val, double weight) {
        checkNotNull(val);
        this.val = val;
        this.weight = weight;
    }

    /**
     * Convenience static constructor
     */
    public static <T> Weighted<T> weighted(T value, double weight) {
        return new Weighted<>(value, weight);
    }

    /**
     * High weights first, use val.hashCode to break ties
     */
    public int compareTo(Weighted<DirectedEdge> other) {
        return ComparisonChain.start()
                .compare(other.weight, weight)
                .compare(Objects.hashCode(other.val), Objects.hashCode(val))
                .result();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Weighted)) return false;
        final Weighted wOther = (Weighted) other;
        return Objects.equal(val, wOther.val) && Objects.equal(weight, wOther.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(val, weight);
    }

    @Override
    public String toString() {
        return "Weighted{" +
                "val=" + val +
                ", weight=" + weight +
                '}';
    }
}
