package com.david.gameofshapes;

import java.util.ArrayList;

/**
 * Created by david on 16/06/2015.
 * A puzzle instance as represented in the database
 */
public class Puzzle {

    private int puzzle_id;
    private String difficulty;
    private int num_moves;
    private int size;
    private int solved;

    public Puzzle(int puzzle_id, String difficulty, int num_moves, int size, int solved){
        this.puzzle_id = puzzle_id;
        this.difficulty = difficulty;
        this.num_moves = num_moves;
        this.size = size;
        this.solved = solved;
    }

    public int getPuzzle_id(){
        return this.puzzle_id;
    }
    public String getDifficulty(){
        return this.difficulty;
    }
    public int getNum_moves(){
        return this.num_moves;
    }
    public int getSize(){
        return this.size;
    }
    public int getSolved(){
        return this.solved;
    }
}
