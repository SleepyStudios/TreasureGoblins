package net.sleepystudios.ld40.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import net.sleepystudios.ld40.net.Network;
import net.sleepystudios.ld40.net.Packets;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class LD40 extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Client client;
	ShapeRenderer sr;

	static OrthographicCamera cam;
	static String myName;
	static int WORLD_SIZE = 4000;

	static ArrayList<Player> players = new ArrayList<Player>();
	static ArrayList<Block> blocks = new ArrayList<Block>();
	static TempBlock tb;

	@Override
	public void create() {
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		cam = new OrthographicCamera(Gdx.graphics.getWidth() , Gdx.graphics.getHeight());
		tb = new TempBlock();
		Gdx.input.setInputProcessor(this);

		myName = JOptionPane.showInputDialog("What's your name?");
		startClient();
	}

	private void startClient() {
		client = new Client();
		client.addListener(new Listener.QueuedListener(new ClientReceiver(client)) {
			protected void queue (Runnable runnable) {
				Gdx.app.postRunnable(runnable);
			}
		});
		client.start();
		Network.register(client);

		try {
			client.connect(1000, "", 5000, 5001);
			System.out.println("Connected");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Couldn't connect");
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();

		sr.setProjectionMatrix(cam.combined);
		sr.begin(ShapeRenderer.ShapeType.Line);
		sr.setColor(Color.WHITE);
		for(int i=0; i<blocks.size(); i++) sr.polygon(blocks.get(i).poly.getTransformedVertices());
		sr.end();

		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		for(int i=0; i<players.size(); i++) players.get(i).render(batch);
		for(int i=0; i<blocks.size(); i++) blocks.get(i).render(batch);

		if(tb.tname!=null) tb.render(batch);

		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}

	public static int rand(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}
	public static float rand(float min, float max) {
		return min + new Random().nextFloat() * (max - min);
	}

	public static float randNoZero(float min, float max) {
		float r = rand(min, max);
		return r != 0 ? r : randNoZero(min, max);
	}

	public static Player getPlayer(String name) {
		for(int i=0; i<players.size(); i++) {
			if(players.get(i).name.equals(name)) return players.get(i);
		}
		return null;
	}

	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.R && !client.isConnected()) startClient();
		return false;
	}

	public boolean keyUp(int keycode) {
		return false;
	}

	public boolean keyTyped(char character) {
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Input.Buttons.LEFT && tb.canPlace) {
			Packets.NewBlock nb = new Packets.NewBlock();
			nb.name = tb.tname;
			nb.x = tb.block.getX();
			nb.y = tb.block.getY();
			nb.angle = tb.block.getRotation();
			client.sendTCP(nb);
		}
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	public boolean scrolled(int amount) {
		return false;
	}
}
