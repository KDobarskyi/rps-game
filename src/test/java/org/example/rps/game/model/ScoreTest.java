/*
 * MIT License
 * 
 * Copyright (c) 2018 Kostiantyn Dobarskyi
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.example.rps.game.model;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ScoreTest {
    @Test
    public void shouldScoreMovesCorrectly() {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(new Score(Move.SCISSORS, Move.PAPER, 0))
                .extracting("playerScore", "botScore")
                .as("Scissors must win over Paper")
                .containsExactly(1L, 0L);
        softly.assertThat(new Score(Move.SCISSORS, Move.ROCK, 0))
                .extracting("playerScore", "botScore")
                .as("Scissors must lose to Rock")
                .containsExactly(0L, 1L);
        softly.assertThat(new Score(Move.SCISSORS, Move.SCISSORS, 0))
                .extracting("playerScore", "botScore")
                .as("Scissors must give no score over same move")
                .containsExactly(0L, 0L);
        softly.assertThat(new Score(Move.ROCK, Move.SCISSORS, 0))
                .extracting("playerScore", "botScore")
                .as("Rock must win over Scissors")
                .containsExactly(1L, 0L);
        softly.assertThat(new Score(Move.ROCK, Move.PAPER, 0))
                .extracting("playerScore", "botScore")
                .as("Rock must lose to Paper")
                .containsExactly(0L, 1L);
        softly.assertThat(new Score(Move.ROCK, Move.ROCK, 0))
                .extracting("playerScore", "botScore")
                .as("Rock must give no score over same move")
                .containsExactly(0L, 0L);
        softly.assertThat(new Score(Move.PAPER, Move.SCISSORS, 0))
                .extracting("playerScore", "botScore")
                .as("Paper must lose to Scissors")
                .containsExactly(0L, 1L);
        softly.assertThat(new Score(Move.PAPER, Move.ROCK, 0))
                .extracting("playerScore", "botScore")
                .as("Paper must win over Rock")
                .containsExactly(1L, 0L);
        softly.assertThat(new Score(Move.PAPER, Move.PAPER, 0))
                .extracting("playerScore", "botScore")
                .as("Paper must give no score over same move")
                .containsExactly(0L, 0L);
        softly.assertAll();
    }

    @Test
    public void shouldConstructValidScores() {
        assertThat(new Score(null, null, null))
                .as("Must equal to zero score when constructed for null args")
                .isEqualTo(new Score(0L, 0L, 0L));
    }

}
