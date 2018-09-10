# RPS Game

Build: [![Build Status](https://travis-ci.com/KDobarskyi/rps-game.svg?branch=master)](https://travis-ci.com/KDobarskyi/rps-game)
Coverage: [![codecov](https://codecov.io/gh/KDobarskyi/rps-game/branch/master/graph/badge.svg)](https://codecov.io/gh/KDobarskyi/rps-game)
SonarCloud: [![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=KDobarskyi_rps-game&metric=alert_status)](https://sonarcloud.io/api/project_badges/measure?project=KDobarskyi_rps-game&metric=alert_status)

### Run game

```console
./gradlew assemble; java -jar build/libs/game-1.0.0-SNAPSHOT.jar 
```
Banner with help info is printed immediately.
Once game loading finishes it also will print own shell prompt:

`your turn>`

Once you see it, you can start playing by typing `r`, `p`, or `s`
Type `exit` to quit the game or just `restart` to reset statistics



### Commands and moves

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