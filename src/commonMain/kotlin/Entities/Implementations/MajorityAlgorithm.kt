package Entities.Implementations

import Entities.Interfaces.*
import Entities.Types.ScoreMetrics

class MajorityAlgorithm<S : ScoreMetrics> : PollAlgorithm<S, SinglePreferenceVote<S>> {
    override val pollAlgorithmParameters = listOf<PollAlgorithmParameter>()
    override fun computeByAlgorithmRules(votes: List<SinglePreferenceVote<S>>): Ranking<S> {
        val votesCount = votes.groupingBy { it.votedCompetitor }.eachCount()

        val maxValue = votesCount.values.max()

        return RankingByDescendingVotes(votesCount.filterValues { it == maxValue })



    }
}