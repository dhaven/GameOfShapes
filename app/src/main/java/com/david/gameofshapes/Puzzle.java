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
    private String config;
    private int size;
    private int solved;

    public Puzzle(int puzzle_id, String difficulty, int num_moves, String config, int size, int solved){
        this.puzzle_id = puzzle_id;
        this.difficulty = difficulty;
        this.num_moves = num_moves;
        this.config = config;
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
    public int getSolved(){return this.solved;}
    public String getConfig(){ return this.config; }

    public void setPuzzle_id(int puzzle_id){ this.puzzle_id = puzzle_id;
    }
    public void setDifficulty(String difficulty){
        this.difficulty = difficulty;
    }
    public void setNum_moves(int num_moves){
        this.num_moves = num_moves;
    }
    public void setSize(int size){this.size = size;}
    public void setSolved(int solved){this.solved = solved;}
    public void setConfig(String config){ this.config = config; }
}
