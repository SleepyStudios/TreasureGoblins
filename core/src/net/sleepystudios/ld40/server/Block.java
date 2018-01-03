package net.sleepystudios.ld40.server;

/**
 * Created by tudor on 03/12/2017.
 */
public class Block {
    float x, y, angle, health = 100;
    String name;

    public Block(String name, float x, float y, float angle) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }
}
