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

import org.example.rps.game.controller.GameController;
import org.example.rps.game.model.Message;
import org.example.rps.game.model.Move;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent("GameShell")
public class GameShell {

    @Autowired
    GameController gameController;

    @Autowired
    Terminal terminal;

    @ShellMethod(value = "restart the game and reset statistics", key = {"restart"})
    public Message restart() {
        return gameController.restart();
    }

    @ShellMethod(value = "rock move", key = {"r", "rock"})
    public Message rock() {
        return handleMove(Move.ROCK);
    }

    @ShellMethod(value = "paper move", key = {"p", "paper"})
    public Message paper() {
        return handleMove(Move.PAPER);
    }

    @ShellMethod(value = "scissors move", key = {"s", "scissors"})
    public Message scissors() {
        return handleMove(Move.SCISSORS);
    }

    @ShellMethod(value = "total score")
    public Message stats() {
        return gameController.score();
    }

    @ShellMethod(value = "show last round results")
    public Message last() {
        return gameController.lastRound();
    }

    @ShellMethod(value = "exit", key = {"exit", "quit"})
    public void exit() {
        System.exit(0);
    }

    Message handleMove(final Move playerMove) {
        return gameController.newPlayerMove(playerMove);
    }

}
