package net.sleepystudios.ld40.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Client;
import net.sleepystudios.ld40.net.Packets;

import static net.sleepystudios.ld40.client.LD40.WORLD_SIZE;

/**
 * Created by tudor on 03/12/2017.
 */
public class Player {
    String name;
    float x, y, shownCamX, shownCamY, camX, camY;
    boolean firstUpdate, shakeScreen, sleeping;
    Sprite sprite;
    Client client;

    public Player(Client client, String name) {
        this.client = client;
        this.name = name;
        sprite = new Sprite(new Texture("goblin.png"));

        updateCam();
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);

        update();
    }

    public void update() {
        if(name.equals(LD40.myName)) {
            // camera smoothing
            float panSpeed = 0.08f;
            LD40.cam.position.set(shownCamX+=(camX-shownCamX)*panSpeed, shownCamY+=(camY-shownCamY)*panSpeed, 0);

            // moving
            float speed = (float) 200 * Gdx.graphics.getDeltaTime();

            if(Gdx.input.isKeyPressed(Input.Keys.W)) {
                move(x, y+speed);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.A)) {
                move(x-speed, y);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.S)) {
                move(x, y-speed);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.D)) {
                move(x+speed, y);
            }
        }
    }

    public void move(float x, float y) {
        if(isBlocked(x, y)) return;

        this.x = x;
        this.y = y;
        sprite.setPosition(x, y);

        if(name.equals(LD40.myName)) {
            updateCam();
            Packets.Move m = new Packets.Move();
            m.name = name;
            m.x = x;
            m.y = y;
            client.sendUDP(m);
        }
    }

    private boolean isBlocked(float x, float y) {
        return x<0 || x+ sprite.getWidth()>WORLD_SIZE || y<0 || y+ sprite.getHeight()>WORLD_SIZE;
    }

    public void updateCam() {
        int w = WORLD_SIZE;
        int h = WORLD_SIZE;

        float minX = LD40.cam.zoom * (LD40.cam.viewportWidth / 2);
        float maxX = (w) - minX;
        float minY = LD40.cam.zoom * (LD40.cam.viewportHeight / 2);
        float maxY = (h) - minY;

        camX = Math.min(maxX, Math.max(x, minX));
        camY = Math.min(maxY, Math.max(y, minY));

        if(!firstUpdate) {
            shownCamX = camX;
            shownCamY = camY;
            LD40.cam.position.set(shownCamX, shownCamY, 0);
            firstUpdate = true;
        }

        float ox, oy;
        if(shakeScreen) {
            ox = LD40.rand(2, 6);
            oy = LD40.rand(2, 6);
            LD40.cam.position.set(shownCamX+ox, shownCamY+oy, 0);
        }
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
        this.sprite.setAlpha(sleeping ? 0.5f : 1f);
    }
}
