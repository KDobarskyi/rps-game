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

import org.example.rps.game.api.GameService;
import org.example.rps.game.persistent.dao.GameRoundRepository;
import org.example.rps.game.persistent.dao.GameScoreRepository;
import org.example.rps.game.model.Move;
import org.example.rps.game.persistent.entity.GameRound;
import org.example.rps.game.persistent.entity.GameScore;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Basic tests to verify game service and repos
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringShellExcludedTestConfiguration.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersistentGameTests {

    @Autowired
    GameRoundRepository roundRepository;

    @Autowired
    GameScoreRepository scoreRepository;

    @Autowired
    GameService gameService;

    @Test
    public void shouldAddExactlyOneRoundWithCorrectScore() {
        gameService.restart();
        gameService.newRound(Move.ROCK, Move.PAPER);
        assertThat(roundRepository.count())
                .as("History contains only one Round")
                .isEqualTo(1);
        assertThat(roundRepository.findById(1L).get())
                .as("Moves should be tracked correctly")
                .isEqualTo(new GameRound(1L, Move.ROCK, Move.PAPER));
        assertThat(gameService.lastRound())
                .extracting("playerMove", "botMove")
                .as("Moves should be tracked correctly").containsExactly(Move.ROCK, Move.PAPER);
        assertThat(gameService.totalScore())
                .extracting("playerScore", "botScore")
                .as("Scores should be tracked correctly").containsExactly(0L, 1L);
    }

    @Test
    public void shouldScoreCorrectlyFewRound() {
        gameService.newRound(Move.ROCK, Move.PAPER);
        assertThat(gameService.totalScore())
                .extracting("playerScore", "botScore")
                .as("Scores for one Round should be tracked correctly").containsExactly(0L, 1L);
        gameService.newRound(Move.ROCK, Move.ROCK);
        assertThat(gameService.totalScore())
                .extracting("playerScore", "botScore")
                .as("Total Scores for second draw Round should be tracked correctly")
                .containsExactly(0L, 1L);
        gameService.newRound(Move.PAPER, Move.ROCK);
        assertThat(gameService.totalScore())
                .extracting("playerScore", "botScore")
                .as("Total Scores for third Round should be tracked correctly")
                .containsExactly(1L, 1L);
        gameService.newRound(Move.PAPER, Move.SCISSORS);
        assertThat(gameService.totalScore())
                .extracting("playerScore", "botScore")
                .as("Total Scores for forth Round should be tracked correctly")
                .containsExactly(1L, 2L);
        assertThat(scoreRepository.findById(4L).get())
                .as("Scores for forth Round should be consistent with Score Repo")
                .isEqualTo(new GameScore(4L, 0L, 1L));
        assertThat(roundRepository.findById(4L).get())
                .extracting("playerMove", "botMove")
                .as("Moves for forth Round should be consistent with Round Repo")
                .containsExactly(Move.PAPER, Move.SCISSORS);
    }

    @Test
    public void shouldRestartGameClearingHistory() {
        gameService.restart();
        assertThat(gameService.currentRound())
                .as("Should be 0 rounds after restart")
                .isEqualTo(0L);
        assertThat(gameService.totalScore())
                .extracting("playerScore", "botScore", "rounds")
                .as("Scores with for restarted game should be tracked correctly")
                .containsExactly(0L, 0L, 0L);
    }

}
