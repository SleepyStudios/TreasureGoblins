package net.sleepystudios.ld40.client;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import net.sleepystudios.ld40.util.BoxTo;

/**
 * Created by tudor on 03/12/2017.
 */
public class Block {
    String name;
    float x, y, angle;
    Polygon poly;
    Sprite sprite;

    public Block(String name, float x, float y, float angle) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.angle = angle;

        sprite = new Sprite(new Texture(name + ".png"));
        sprite.setPosition(x, y);
        sprite.setRotation(angle);

        Rectangle r = new Rectangle(x+4, y+2, sprite.getWidth()-8, sprite.getHeight()-4);
        poly = BoxTo.poly(r, (int) r.getWidth(), (int) r.getHeight(), 0, 0, angle);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
