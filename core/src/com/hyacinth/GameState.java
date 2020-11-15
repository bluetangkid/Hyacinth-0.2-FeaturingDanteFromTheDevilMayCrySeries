package com.hyacinth;

public enum GameState {
    TITLE(0), GAME(1), LEVEL_SELECT(2), DEATH(3), COMPLETE(4), CONTROLS(5), DEFAULT(-1);
    int id;
    GameState(int id){
        this.id = id;
    }
    public int getId() {
        return id;
    }
}

/*

Credits:

Warren Funk and Tyler Hempe - Programming
Sam Taylor - Art
Taro Sharkey - Music and SFX

idk some shoutout to hackumbc here probably

 */