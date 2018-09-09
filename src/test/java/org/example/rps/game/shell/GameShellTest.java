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
package org.example.rps.game.shell;

import org.example.rps.game.Game;
import org.example.rps.game.controller.GameController;
import org.example.rps.game.model.Move;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)

@SpringBootTest(classes = {Game.class},
        properties = {InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
                "spring.main.banner-mode=off"})
@ActiveProfiles("shell-test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GameShellTest {

    @Autowired
    Shell shell;

    @SpyBean
    GameController gameController;

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Before
    public void reset() {
        shell.evaluate(() -> "restart");
        Mockito.reset(gameController);
    }

    @Test
    public void shouldHandleStats() {
        assertThat(String.valueOf(shell.evaluate(() -> "stats")))
                .as("'stats' should be mapped correctly")
                .contains("Rounds played");
        Mockito.verify(gameController).score();
    }

    @Test
    public void shouldHandleLast() {
        assertThat(String.valueOf(shell.evaluate(() -> "last")))
                .as("'last' should be mapped correctly")
                .contains("NONE");
        Mockito.verify(gameController).lastRound();
    }

    @Test
    public void shouldHandleRestart() {
        assertThat(String.valueOf(shell.evaluate(() -> "restart")))
                .as("'restart' should be mapped correctly")
                .contains("restarted");

        Mockito.verify(gameController).restart();
    }

    @Test
    public void shouldHandleRock() {
        final String expectedPart = "you - ROCK";
        assertThat(String.valueOf(shell.evaluate(() -> "r")))
                .as("'r' should be mapped correctly to Rock")
                .contains(expectedPart);

        assertThat(String.valueOf(shell.evaluate(() -> "rock")))
                .as("'rock' should be mapped correctly to Rock")
                .contains(expectedPart);

        Mockito.verify(gameController, times(2)).newPlayerMove(Move.ROCK);
    }

    @Test
    public void shouldHandlePaper() {
        final String expectedPart = "you - PAPER";
        assertThat(String.valueOf(shell.evaluate(() -> "p")))
                .as("'p' should be mapped correctly to Paper")
                .contains(expectedPart);

        assertThat(String.valueOf(shell.evaluate(() -> "paper")))
                .as("'paper' should be mapped correctly to Paper")
                .contains(expectedPart);

        Mockito.verify(gameController, times(2)).newPlayerMove(Move.PAPER);
    }

    @Test
    public void shouldHandleScissors() {
        final String expectedPart = "you - SCISSORS";
        assertThat(String.valueOf(shell.evaluate(() -> "s")))
                .as("'s' should be mapped correctly to Scissors")
                .contains(expectedPart);

        assertThat(String.valueOf(shell.evaluate(() -> "scissors")))
                .as("'scissors' should be mapped correctly to Scissors")
                .contains(expectedPart);

        Mockito.verify(gameController, times(2)).newPlayerMove(Move.SCISSORS);
    }

    @Test
    public void shouldHandleZeroCodedExit() {
        exit.expectSystemExitWithStatus(0);
        shell.evaluate(() -> "exit");
    }

}
