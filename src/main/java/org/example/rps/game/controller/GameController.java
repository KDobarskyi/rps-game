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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameController {

    @Autowired
    GameService gameService;

    @Autowired
    BotService botService;

    public Message restart() {
        gameService.restart();
        return new Message("game has been restarted");
    }

    public Message score() {
        final Score score = gameService.totalScore();
        return new Message("Rounds played: {0}, your score: {1}, bot score: {2}", score.getRounds(),
                score.getPlayerScore(), score.getBotScore());
    }

    public Message newPlayerMove(final Move move) {
        final Move botMove = botService.makeMove(gameService.currentRound());
        final Round round = new Round(move, botMove);
        gameService.newRound(round);
        final Score score = gameService.totalScore();
        return roundToMessage(score, round);
    }

    public Message lastRound() {
        if (gameService.currentRound() != 0) {
            final Round round = gameService.lastRound();
            final Score score = gameService.totalScore();
            return roundToMessage(score, round);
        } else {
            return new Message("NONE");
        }
    }

    static Message roundToMessage(final Score score, final Round round) {
        return new Message(
                "Round {0}. Moves: you - {1}, bot - {2}. Total Score: you - {3}, bot - {4}",
                score.getRounds(), round.getPlayerMove(), round.getBotMove(),
                score.getPlayerScore(), score.getBotScore());
    }
}
