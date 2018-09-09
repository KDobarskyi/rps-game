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
package org.example.rps.game.persistent.entity;

import lombok.*;
import org.example.rps.game.model.Move;
import org.example.rps.game.model.Round;
import javax.persistence.*;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class GameRound {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long round;

    @NonNull
    private Move playerMove;

    @NonNull
    private Move botMove;

    public Round asRound() {
        return new Round(playerMove, botMove);
    }
}