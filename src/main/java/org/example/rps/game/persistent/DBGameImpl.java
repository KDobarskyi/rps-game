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
import org.example.rps.game.model.Round;
import org.example.rps.game.persistent.dao.GameRoundRepository;
import org.example.rps.game.persistent.dao.GameScoreRepository;
import org.example.rps.game.model.Move;
import org.example.rps.game.model.Score;
import org.example.rps.game.persistent.entity.GameRound;
import org.example.rps.game.persistent.entity.GameScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;

@Component
public class DBGameImpl implements GameService {

    @Autowired
    GameRoundRepository roundRepository;

    @Autowired
    GameScoreRepository scoreRepository;

    @Autowired
    EntityManager entityManager;

    @Override
    @Transactional
    public void restart() {
        scoreRepository.deleteAll();
        roundRepository.deleteAll();
        entityManager.createNativeQuery("ALTER SEQUENCE HIBERNATE_SEQUENCE RESTART WITH 1")
                .executeUpdate();
    }

    @Override
    @Transactional
    public void newRound(final Move playerMove, final Move botMove) {
        GameRound gameRound = new GameRound(playerMove, botMove);
        roundRepository.save(gameRound);
        GameScore gameScore = new GameScore(gameRound);
        scoreRepository.save(gameScore);
    }

    @Override
    public Score totalScore() {
        return scoreRepository.totalScore();
    }

    @Override
    public Round lastRound() {
        return roundRepository.findTop1RoundByOrderByRoundDesc().asRound();
    }

    @Override
    public long currentRound() {
        final GameRound gameRound = roundRepository.findTop1RoundByOrderByRoundDesc();
        if (gameRound == null) {
            return 0;
        }
        return gameRound.getRound() + 1;
    }
}
