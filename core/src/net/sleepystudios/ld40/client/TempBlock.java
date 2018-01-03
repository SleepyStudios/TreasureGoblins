package net.sleepystudios.ld40.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import net.sleepystudios.ld40.util.BoxTo;


/**
 * Created by tudor on 03/12/2017.
 */
public class TempBlock {
    public Sprite block;
    float maxDist = 160f, angle;
    public String tname;

    public TempBlock() {}

    public void setSprite(String tname) {
        this.tname = tname;
        initGraphics();
    }

    public void initGraphics() {
        block = new Sprite(new Texture( tname  + ".png"));
        block.setOrigin(block.getWidth()/2, block.getHeight()/2);
    }

    public boolean canPlace;
    public void render(SpriteBatch batch) {
        Player p = LD40.getPlayer(LD40.myName);
        if(p==null) return;

        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        LD40.cam.unproject(mouse);
        getAngle(mouse);

        Vector3 player = new Vector3(p.x+p.sprite.getWidth()/2, p.y+p.sprite.getHeight()/2, 0);
        float dist = player.dst(mouse);

        canPlace = true;
        checkCollisions();
        castRay();
        block.setColor(new Color(0.4f,0.4f,0.7f,0.6f));
        if(dist>maxDist || !canPlace) {
            block.setColor(new Color(0.7f,0.2f,0.2f,0.6f));
            canPlace = false;
        }

        // angle
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || tname.equals("door")) {
            block.setRotation(90*(Math.round(angle/90)));
            if(tname.equals("door")) block.setRotation(block.getRotation()+90);

            block.setPosition((float) (block.getWidth()*(Math.floor(mouse.x/block.getWidth()))), (float) (block.getHeight()*(Math.floor(mouse.y/block.getHeight()))));
        } else {
            block.setRotation(Math.round(angle));
            block.setPosition(mouse.x-block.getOriginX(), mouse.y-block.getOriginY());
        }

        block.draw(batch);
    }

    private void getAngle(Vector3 mouse) {
        Player p = LD40.getPlayer(LD40.myName);
        if(p==null) return;

        float yDist = p.y - mouse.y + p.sprite.getHeight()/2;
        float xDist = p.x - mouse.x + p.sprite.getWidth()/2;

        angle = (float) (Math.toDegrees(Math.atan2(yDist, xDist)) - 180f);
        if(angle < 0) angle += 360;
    }

    private Polygon makePoly() {
        Rectangle r = new Rectangle(block.getX()+4, block.getY()+2, block.getWidth()-8, block.getHeight()-4);
        return BoxTo.poly(r, (int) block.getWidth(), (int) block.getHeight(), 0, 0, block.getRotation());
    }

    private void checkCollisions() {
        // make a polygon for this block
        Polygon me = makePoly();

        for(int i=0; i<LD40.blocks.size(); i++) {
            Block b = LD40.blocks.get(i);

            if(b.poly!=null) {
                // check placing on top of another block
                if(Intersector.overlapConvexPolygons(me, b.poly)) {
                    canPlace = false;
                }
            }
        }
    }

    private void castRay() {
        Player p = LD40.getPlayer(LD40.myName);
        if(p==null) return;

        // make a polygon for this block
        Polygon me = makePoly();
        Vector2 player = new Vector2(p.x+p.sprite.getWidth()/2, p.y+p.sprite.getHeight()/2);
        Vector2 block = new Vector2(me.getOriginX(), me.getOriginY());

        for(int i=0; i<LD40.blocks.size(); i++) {
            Block b = LD40.blocks.get(i);
            if(b!=null) {
                // check for collision
                if(b.poly!=null && Intersector.intersectSegmentPolygon(player, block, b.poly)) {
                    canPlace = false;
                    return;
                }
            }
        }
    }
}