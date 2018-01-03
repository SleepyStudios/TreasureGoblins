package net.sleepystudios.ld40.server;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import net.sleepystudios.ld40.net.Network;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by tudor on 03/12/2017.
 */
public class LD40Server {
    public Server server;
    static ArrayList<Player> players = new ArrayList<Player>();
    static ArrayList<Block> blocks = new ArrayList<Block>();

    public LD40Server() {
        server = new Server();
        try {
            server.bind(5000, 5001);
        } catch (IOException e) {
            System.out.println("Coudln't bind, exiting");
            return;
        }

        server.addListener(new Listener.ThreadedListener(new ServerReceiver(server)));
        server.start();
        Network.register(server);
        System.out.println("LD40 server running!");
    }

    public static Player getPlayer(String name) {
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).name.equals(name)) return players.get(i);
        }
        return null;
    }

    public static Player getPlayer(int id) {
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).id==id) return players.get(i);
        }
        return null;
    }

    public static void main(String[] args) {
        new LD40Server();
    }
}
