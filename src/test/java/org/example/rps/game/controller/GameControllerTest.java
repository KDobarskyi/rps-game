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
package org.example.rps.game.controller;

import org.example.rps.game.api.BotService;
import org.example.rps.game.api.GameService;
import org.example.rps.game.model.Message;
import org.example.rps.game.model.Move;
import org.example.rps.game.model.Round;
import org.example.rps.game.model.Score;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {

    @InjectMocks
    GameController gameController;

    @Mock
    GameService gameService;

    @Mock
    BotService botService;

    final static Round ROUND = new Round(Move.PAPER, Move.ROCK);
    final static Score SCORE = new Score(1L, 0L, 1L);
    final static String ROUND_TEXT =
            "Round 1. Moves: you - PAPER, bot - ROCK. Total Score: you - 1, bot - 0";
    final static String SCORE_TEXT = "Rounds played: 1, your score: 1, bot score: 0";

    @Test
    public void shouldRestartGame() {
        assertThat(String.valueOf(gameController.restart())).contains("restarted");
        verify(gameService).restart();
    }

    @Test
    public void shouldHandlePlayerMove() {
        when(botService.makeMove(anyLong())).thenReturn(ROUND.getBotMove());
        when(gameService.totalScore()).thenReturn(SCORE);

        assertThat(String.valueOf(gameController.newPlayerMove(ROUND.getPlayerMove())))
                .as("Should print correct round text ")
                .isEqualTo(ROUND_TEXT);

        verify(gameService).newRound(ROUND);
    }

    @Test
    public void shouldHandleLastRound() {
        when(gameService.currentRound()).thenReturn(0L);

        assertThat(gameController.lastRound())
                .as("Should handle no any rounds played")
                .isEqualTo(new Message("NONE"));

        when(gameService.currentRound()).thenReturn(1L);
        when(gameService.totalScore()).thenReturn(SCORE);
        when(gameService.lastRound()).thenReturn(ROUND);

        assertThat(String.valueOf(gameController.lastRound()))
                .as("Should handle one round played")
                .isEqualTo(ROUND_TEXT);

        verify(gameService).lastRound();
    }

    @Test
    public void shouldHandleScore() {
        when(gameService.totalScore()).thenReturn(SCORE);

        assertThat(String.valueOf(gameController.score()))
                .as("Should print correct score text")
                .isEqualTo(SCORE_TEXT);

        verify(gameService).totalScore();
    }
}
