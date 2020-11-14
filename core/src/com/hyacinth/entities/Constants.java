package com.hyacinth.entities;

import com.badlogic.gdx.Gdx;

public class Constants {
    public static int   MAGIC_NUMBER = 7; // @taro

    public static final float GUN_FORCE_STATIC_MULT = 5000f;
    public static final float PLAYER_DENSITY = 1.6f;
    public static final float PLAYER_RADIUS = 4.5f;
    public static final float PLAYER_FRICTION = 1.8f;
    public static final float PLAYER_RESTITUTION = 0.05f;
    public static final float PLAYER_JUMP_FORCE = 1.2f;
    public static final short PLAYER_JUMP_TIMER = 30;
    public static final float PLAYER_IMPULSE_MUL = 5000f;
    public static final float PLAYER_MAX_SPEED = 15f;
    public static final float PLAYER_FASTFALL_SPEED = 0.05f;

    public static final float GRAVITY = 10f;

    public static final float BULLET_DENSITY = 0.5f;
    public static final float BULLET_RADIUS = 0.5f;
    public static final float BULLET_FRICTION = 0.3f;
    public static final float BULLET_RESTITUTION = 0.9f;
    public static final int   BULLET_MAX_TIME = 240;
    public static final float BULLET_SPEED_SCALE = 100f;
}
