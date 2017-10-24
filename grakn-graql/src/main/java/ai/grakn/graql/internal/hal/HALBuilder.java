/*
 * Grakn - A Distributed Semantic Database
 * Copyright (C) 2016  Grakn Labs Limited
 *
 * Grakn is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Grakn is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Grakn. If not, see <http://www.gnu.org/licenses/gpl.txt>.
 */

package ai.grakn.graql.internal.hal;

import ai.grakn.Keyspace;
import ai.grakn.concept.Concept;
import ai.grakn.graql.Match;
import ai.grakn.graql.Printer;
import ai.grakn.graql.admin.Answer;
import ai.grakn.graql.admin.AnswerExplanation;
import ai.grakn.graql.internal.query.QueryAnswer;
import ai.grakn.graql.internal.reasoner.UnifierType;
import ai.grakn.graql.internal.reasoner.atom.Atom;
import ai.grakn.graql.internal.reasoner.atom.AtomicFactory;
import ai.grakn.graql.internal.reasoner.explanation.RuleExplanation;
import ai.grakn.graql.internal.reasoner.query.ReasonerAtomicQuery;
import ai.grakn.graql.internal.reasoner.query.ReasonerQueries;
import ai.grakn.graql.internal.reasoner.query.ReasonerQueryImpl;
import mjson.Json;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

/**
 * Class for building HAL representations of a {@link Concept} or a {@link Match}.
 *
 * @author Marco Scoppetta
 */
public class HALBuilder {

    public static String renderHALConceptData(Concept concept, boolean inferred, int separationDegree, Keyspace keyspace, int offset, int limit) {
        return new HALConceptData(concept, inferred, separationDegree, false, new HashSet<>(), keyspace, offset, limit).render();
    }

    @Nullable
    public static String HALExploreConcept(Concept concept, Keyspace keyspace, int offset, int limit) {
        String renderedHAL = null;

        if (concept.isThing()) {
            renderedHAL = new HALExploreInstance(concept, keyspace, offset, limit).render();
        }
        if (concept.isSchemaConcept()) {
            renderedHAL = new HALExploreSchemaConcept(concept, keyspace, offset, limit).render();
        }

        return renderedHAL;
    }

    public static Json explanationAnswersToHAL(Stream<Answer> answerStream, Printer halPrinter) {
        final Json conceptsArray = Json.array();
        answerStream.forEach(answer -> {
            AnswerExplanation expl = answer.getExplanation();
            if (expl.isLookupExplanation()) {
                conceptsArray.add(halPrinter.graqlString(false, answer));
            } else if (expl.isRuleExplanation()) {
                Atom atom = ((ReasonerAtomicQuery) expl.getQuery()).getAtom();
                Atom headAtom = ((RuleExplanation) expl).getRule().getHead().getAtom();

                List<Answer> list = ReasonerQueries.atomic(atom.rewriteWithRelationVariableMock()).getQuery().execute();
                Answer fake;
                if(list.isEmpty())
                    fake = new ReasonerAtomicQuery(headAtom.rewriteWithRelationVariableMock())
                            .materialise(answer.unify(headAtom.getMultiUnifier(atom, UnifierType.RULE).getUnifier().inverse()))
                            .iterator().next();
                else
                    fake = list.get(0);

                //TODO: handle case innerAtom isa resource
                if (atom.isRelation()) {
                    conceptsArray.add(halPrinter.graqlString(false, fake));
                }
                explanationAnswersToHAL(expl.getAnswers().stream(), halPrinter).asJsonList().forEach(conceptsArray::add);
            }
        });
        return conceptsArray;
    }

}