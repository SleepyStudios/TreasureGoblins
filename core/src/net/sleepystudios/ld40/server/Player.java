package net.sleepystudios.ld40.server;

import com.esotericsoftware.kryonet.Server;
import net.sleepystudios.ld40.net.Packets;

/**
 * Created by tudor on 03/12/2017.
 */
public class Player {
    String name;
    float x, y;
    int id = -1;
    boolean sleeping;
    Server server;

    public Player(Server server, int id, String name) {
        this.server = server;
        this.id = id;
        this.name = name;
    }

    public void setID(int id) { this.id = id; }

    public void setSleeping() {
        setID(-1);
        sleeping = true;
        sendSleeping();
    }

    public void setAwake(int id) {
        setID(id);
        sleeping = false;
        sendSleeping();
    }

    private void sendSleeping() {
        Packets.Sleeping s = new Packets.Sleeping();
        s.name = name;
        s.sleeping = sleeping;
        server.sendToAllTCP(s);
    }
}
