# RPS Game

Build: [![Build Status](https://travis-ci.com/KDobarskyi/rps-game.svg?branch=master)](https://travis-ci.com/KDobarskyi/rps-game)
Coverage: [![codecov](https://codecov.io/gh/KDobarskyi/rps-game/branch/master/graph/badge.svg)](https://codecov.io/gh/KDobarskyi/rps-game)
SonarCloud: [![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=KDobarskyi_rps-game&metric=alert_status)](https://sonarcloud.io/api/project_badges/measure?project=KDobarskyi_rps-game&metric=alert_status)

### About

This is Rock–Paper–Scissors game played in console against bot. Idea was to develop game in less than 4 hours, code took actually near a hour and a half.

It is written in `Java` using `Spring Boot`, `Spring Shell`, and `Spring Data JPA`

#### Bot
Current version uses history-based heuristic to forecast player moves. During the first 3 rounds, it produces uniformly-distributed random moves. Starting from 4th round, bot is able to guess more precisely. 

For illustrational purposes, every game round is stored in a database. Bot also shares same in-memory instance of `HSQLDB` and uses it as source for prediction. It employs variation of [Pushdown Automaton](https://en.wikipedia.org/wiki/Pushdown_automaton) also known as ["Shannon's Fortune Teller"](https://translate.google.com/translate?hl=en&sl=auto&tl=en&u=https%3A%2F%2Fsites.google.com%2Fsite%2Fltwood%2Fprojects%2Fheshby%2Fshannon) 

### Download

Latest release is available at <https://github.com/KDobarskyi/rps-game/releases/latest>

#### v1.0.0
Direct link to executable jar: <https://github.com/KDobarskyi/rps-game/releases/download/v1.0.0/game-1.0.0-SNAPSHOT.jar>
Sources: <https://github.com/KDobarskyi/rps-game/archive/v1.0.0.zip>

### Run Game

#### Gradle
```console
./gradlew assemble; java -jar build/libs/game-1.0.0-SNAPSHOT.jar 
```

#### Jar
```console
java -jar game-1.0.0-SNAPSHOT.jar
```

Banner with help info is printed immediately.
Once game loading finishes it also will print own shell prompt:

`your turn>`

Once you see it, you can start playing by typing `r`, `p`, or `s`
Type `exit` to quit the game or just `restart` to reset statistics



### Commands and Moves

    Basic commands
        help            - prints help
        exit, quit      - quits the game
        last            - prints last moves)
        restart         - restarts the game setting score to 0
        stats           - prints current game statistics

    Moves:  
        r, rock         - rock beats scissors, but is beaten by paper
        p, paper        - paper beats rock, but is beaten by scissors
        s, scissors     - scissors beats paper, but is beaten by rock

    hitting tab may autocomplete if supported by your local shell