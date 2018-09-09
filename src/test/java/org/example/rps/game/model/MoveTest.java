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
import org.junit.runner.RunWith;

public class MoveTest {

    @Test
    public void shouldScoreMovesCorrectly() {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(Move.SCISSORS.scoreAgainst(Move.PAPER))
                .as("Scissors must win over Paper")
                .isEqualTo(1);
        softly.assertThat(Move.SCISSORS.scoreAgainst(Move.ROCK))
                .as("Scissors must lose to Rock")
                .isEqualTo(0);
        softly.assertThat(Move.SCISSORS.scoreAgainst(Move.SCISSORS))
                .as("Scissors must give no score over same move")
                .isEqualTo(0);
        softly.assertThat(Move.ROCK.scoreAgainst(Move.SCISSORS))
                .as("Rock must win over Scissors")
                .isEqualTo(1);
        softly.assertThat(Move.ROCK.scoreAgainst(Move.PAPER))
                .as("Rock must lose to Paper")
                .isEqualTo(0);
        softly.assertThat(Move.ROCK.scoreAgainst(Move.ROCK))
                .as("Rock must give no score over same move")
                .isEqualTo(0);
        softly.assertThat(Move.PAPER.scoreAgainst(Move.SCISSORS))
                .as("Paper must lose to Scissors")
                .isEqualTo(0);
        softly.assertThat(Move.PAPER.scoreAgainst(Move.ROCK))
                .as("Paper must win over Rock")
                .isEqualTo(1);
        softly.assertThat(Move.PAPER.scoreAgainst(Move.PAPER))
                .as("Paper must give no score over same move")
                .isEqualTo(0);
        softly.assertAll();
    }
}
