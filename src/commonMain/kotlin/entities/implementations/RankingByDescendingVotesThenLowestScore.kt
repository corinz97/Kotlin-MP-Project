package entities.implementations

import entities.interfaces.Competitor
import entities.types.Comparator
import entities.types.ScoreMetric

/**
 * Class which orders intermediate ranking by descending number of votes, then by lowest score.
 */
class RankingByDescendingVotesThenLowestScore<S : ScoreMetric>(unorderedRanking: Map<Competitor<S>, Int>) :
    RankingByDescendingVotes<S>(unorderedRanking) {
    init {
        val competitors = super.ranking.flatMap { it.key }
        if (competitors.map { it.scores }.any { it.isEmpty() }) {
            error("Every competitor must have at least one score")
        }
    }

    override val ranking: Map<Set<Competitor<S>>, Int?>
        get() =
            super.ranking.computeWithLowestScoreRule()

    private fun Map<Set<Competitor<S>>, Int?>.computeWithLowestScoreRule(): Map<Set<Competitor<S>>, Int?> {
        val m = mutableMapOf<Set<Competitor<S>>, Int?>()
        for ((competitorsSetWithSameVotesNumber, votes) in this) {
            val competitorWithRelativeLowestScore =
                competitorsSetWithSameVotesNumber
                    .associateWith { competitor -> competitor.scores.minWith(Comparator.HighestScore()) }

            for ((competitor, lowestScore) in competitorWithRelativeLowestScore) {
                competitor.scores = competitor.scores.filter { it == lowestScore }.distinct()
            } // keep just highest score, list will be list of 1 element

            val a =
                competitorWithRelativeLowestScore.toList().groupBy { it.second.scoreValue }
                    .map { (k, v) -> k to v.map { p -> p.first }.toSet() }
                    .sortedBy { it.first } // useful for order of next cycle
                    .toMap()

            for ((_, competitors) in a) {
                m[competitors] = votes
            }
        }
        return m
    }
}