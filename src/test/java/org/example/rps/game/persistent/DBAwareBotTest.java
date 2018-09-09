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
package org.example.rps.game.persistent;

import org.example.rps.game.model.Move;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringShellExcludedTestConfiguration.class)
public class DBAwareBotTest {

    @Autowired
    DBGameImpl dbGame;

    @Autowired
    DBAwareBot dbAwareBot;


    @Before
    public void restartGame() {
        dbGame.restart();
    }

    /**
     * When rounds history is empty, Bot expected to make random moves That is we can expect same
     * ~33% distribution among Rock-Paper-Scissor choices With deviation of ~6.66%
     */
    @Test
    public void shouldReturnUniformlyDistributedRandomDuringFirstTwoMoves() {
        final int counts[] = new int[Move.values().length];
        final int tries = 333;
        IntStream.range(0, tries).forEach(i -> {
            dbGame.restart();
            ++counts[dbAwareBot.makeMove(1).ordinal()];
            ++counts[dbAwareBot.makeMove(2).ordinal()];
        });

        double avg = Arrays.stream(counts).mapToDouble(i -> i / 6.66).max().getAsDouble();
        assertThat(avg).as("Most frequent move should be within ~33% + 6.66% distribution")
                .isBetween(33.33, 40.0);
    }

    /**
     * Bot should be able to guess something even if no simple pattern found in a couple of moves
     */
    @Test
    public void shouldGuessSomethingAfter3DifferentRounds() {
        dbGame.newRound(Move.ROCK, Move.SCISSORS);
        dbGame.newRound(Move.SCISSORS, Move.ROCK);
        dbGame.newRound(Move.PAPER, Move.PAPER);
        assertThat(dbAwareBot.makeMove(dbGame.currentRound()))
                .as("Should guess something after 3 different rounds").isNotNull();
    }

    /**
     * Bot should detect simple repetitive patterns in player moves
     */
    @Test
    public void shouldAdoptGuessCorrectlyBasedOnFewSameRounds() {
        dbGame.newRound(Move.ROCK, Move.SCISSORS);
        dbGame.newRound(Move.ROCK, Move.SCISSORS);
        dbGame.newRound(Move.ROCK, Move.SCISSORS);
        dbGame.newRound(Move.ROCK, Move.SCISSORS);
        assertThat(dbAwareBot.makeMove(dbGame.currentRound()))
                .as("Recent Rock few times in a row should convince bot to make Paper move")
                .isEqualTo(Move.PAPER);

        dbGame.newRound(Move.SCISSORS, Move.PAPER);
        dbGame.newRound(Move.SCISSORS, Move.PAPER);
        dbGame.newRound(Move.SCISSORS, Move.ROCK);
        assertThat(dbAwareBot.makeMove(dbGame.currentRound()))
                .as("Recent Scissors few times in a row should convince bot to make Rock move")
                .isEqualTo(Move.ROCK);

        dbGame.newRound(Move.PAPER, Move.ROCK);
        dbGame.newRound(Move.PAPER, Move.SCISSORS);
        dbGame.newRound(Move.PAPER, Move.SCISSORS);
        assertThat(dbAwareBot.makeMove(dbGame.currentRound()))
                .as("Recent Paper few times in a row should convince bot to make Rock move")
                .isEqualTo(Move.SCISSORS);
    }

    /**
     * Bot should detect right pattern in player moves taking into account which own moves led to
     * what player move
     */
    @Test
    public void shouldGuessTakingInAccountOwnMoves() {
        dbGame.newRound(Move.PAPER, Move.SCISSORS);
        dbGame.newRound(Move.PAPER, Move.PAPER); // <- this pattern should not match
        dbGame.newRound(Move.SCISSORS, Move.ROCK); // <- and we don't expect scissors from Bot
        dbGame.newRound(Move.PAPER, Move.SCISSORS);
        dbGame.newRound(Move.PAPER, Move.SCISSORS); // <- this pattern should match
        dbGame.newRound(Move.ROCK, Move.PAPER); // <- and we expect Paper move to be made by Bot
        dbGame.newRound(Move.SCISSORS, Move.ROCK);
        dbGame.newRound(Move.PAPER, Move.SCISSORS);
        dbGame.newRound(Move.PAPER, Move.PAPER); // <- this pattern again should not match
        dbGame.newRound(Move.PAPER, Move.SCISSORS); // <- and we don't expect scissors from Bot
        dbGame.newRound(Move.PAPER, Move.SCISSORS);
        assertThat(dbAwareBot.makeMove(dbGame.currentRound()))
                .as("'Paper-Scissors, Paper-Scissors, Rock' pattern should be matched")
                .isEqualTo(Move.PAPER);
    }
}
