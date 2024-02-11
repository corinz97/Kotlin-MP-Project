package entities.interfaces.dsls

import entities.interfaces.Score
import entities.interfaces.ScoreMetric

/**
 * DSL for competitors.
 */
interface CompetitorDSL<S : ScoreMetric> {
    /**
     * Shortcut useful to add an element in [scores].
     */
    operator fun Score<S>.unaryPlus()
}