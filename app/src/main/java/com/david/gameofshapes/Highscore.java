package com.david.gameofshapes;

/**
 * Created by david on 11/07/2015.
 */
public class Highscore implements Comparable<Highscore>{
    private String name;
    private int score;

    public Highscore(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(Highscore another) {
        if(this.getScore() < another.getScore()){
            return -1;
        }else if(this.getScore() > another.getScore()){
            return 1;
        }else{
            return 0;
        }
    }
}
