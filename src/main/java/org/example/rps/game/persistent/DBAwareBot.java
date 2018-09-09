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

import org.example.rps.game.api.BotService;
import org.example.rps.game.model.Move;
import org.example.rps.game.persistent.dao.GameRoundRepository;
import org.example.rps.game.persistent.entity.GameRound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class DBAwareBot implements BotService {

    @Autowired
    GameRoundRepository roundRepository;

    @Autowired
    EntityManager entityManager;

    static final Random RAND = new Random();

    static Move counterMoveTo(final Move expectedPlayerMove) {

        switch (expectedPlayerMove) {
            case PAPER:
                return Move.SCISSORS;
            case ROCK:
                return Move.PAPER;
        }

        return Move.ROCK;
    }

    static Move randoMove() {
        return Move.values()[RAND.nextInt(Move.values().length)];
    }

    @Override
    public Move makeMove(final long currentRound) {

        if (currentRound > 3) {
            GameRound previousRound = roundRepository.findById(currentRound - 1).get();
            GameRound roundBeforePrevious = roundRepository.findById(currentRound - 2).get();

            Move complex = guessPlayerMoveByPlayerAndBotMoves(previousRound, roundBeforePrevious);
            if (complex != null) {
                return counterMoveTo(complex);
            }

            Move simple = guessPlayerMoveByPlayerOnlyMoves(previousRound, roundBeforePrevious);
            if (simple != null) {
                return counterMoveTo(simple);
            }
        }

        return randoMove();
    }

    Move guessPlayerMoveByPlayerOnlyMoves(final GameRound previousRound,
            final GameRound roundBeforePrevious) {
        Query query = entityManager.createNativeQuery("select player_Move from "
                + "(SELECT o.player_Move, count(o.player_Move) as c "
                + "from GAME_ROUND o join GAME_ROUND t on o.round=t.round+1 join GAME_ROUND s on o.round=s.round+2 "
                + "where t.player_Move=? and s.player_Move=? group by o.player_Move order by c desc"
                + ") limit 1");

        return executeQueryWithParams(query, previousRound.getPlayerMove().ordinal(),
                roundBeforePrevious.getPlayerMove().ordinal());
    }

    Move guessPlayerMoveByPlayerAndBotMoves(final GameRound previousRound,
            final GameRound roundBeforePrevious) {
        Query query = entityManager.createNativeQuery("select player_Move from "
                + "(SELECT o.player_Move, count(o.player_Move) as c "
                + "from GAME_ROUND o join GAME_ROUND t on o.round=t.round+1 join GAME_ROUND s on o.round=s.round+2 "
                + "where t.player_Move=? and s.player_Move=? "
                + "and t.bot_Move=? and s.bot_Move=? group by o.player_Move order by c desc"
                + ") limit 1");

        return executeQueryWithParams(query, previousRound.getPlayerMove().ordinal(),
                roundBeforePrevious.getPlayerMove().ordinal(), previousRound.getBotMove().ordinal(),
                roundBeforePrevious.getBotMove().ordinal());
    }

    Move executeQueryWithParams(final Query query, final Object... paramValues) {
        IntStream.range(0, paramValues.length)
                .forEach(i -> query.setParameter(i + 1, paramValues[i]));

        List<Object> result = query.getResultList();
        if (result.size() == 0) {
            return null;
        }

        return Move.values()[(int) result.get(0)];
    }

}
