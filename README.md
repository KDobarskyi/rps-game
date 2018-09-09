# RPS Game

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