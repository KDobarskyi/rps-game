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

import lombok.*;

@Value
public class Score {

    long playerScore;

    long botScore;

    long rounds;

    public Score(final Move playerMove, final Move botMove, long rounds) {
        this.playerScore = playerMove.scoreAgainst(botMove);
        this.botScore = botMove.scoreAgainst(playerMove);
        this.rounds = rounds;
    }

    public Score(final Long playerScore, final Long botScore, final Long rounds) {
        this.playerScore = playerScore != null ? playerScore : 0;
        this.botScore = botScore != null ? botScore : 0;
        this.rounds = rounds != null ? rounds : 0;
    }
}
