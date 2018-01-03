package net.sleepystudios.ld40.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.sleepystudios.ld40.net.Packets;

/**
 * Created by tudor on 03/12/2017.
 */
public class ClientReceiver extends Listener {
    private Client client;

    public ClientReceiver(Client client) {
        this.client = client;
    }

    @Override
    public void connected(Connection c) {
        Packets.PlayerData p = new Packets.PlayerData();
        p.name = LD40.myName;
        client.sendTCP(p);
    }

    @Override
    public void disconnected(Connection c) {
        System.exit(0);
    }

    @Override
    public void received(Connection c, Object o) {
        if(o instanceof Packets.PlayerData) {
            Player p = LD40.getPlayer(((Packets.PlayerData) o).name);
            if(p==null) {
                LD40.players.add(new Player(client, ((Packets.PlayerData) o).name));
                if(((Packets.PlayerData) o).name.equals(LD40.myName)) {
                    LD40.tb.setSprite("wood");
                }
            }
        }

        if(o instanceof Packets.Move) {
            Player p = LD40.getPlayer(((Packets.Move) o).name);
            if(p!=null) {
                p.move(((Packets.Move) o).x, ((Packets.Move) o).y);
            }
        }

        if(o instanceof Packets.Sleeping) {
            Player p = LD40.getPlayer(((Packets.Sleeping) o).name);
            if(p!=null) {
                p.setSleeping(((Packets.Sleeping) o).sleeping);
            }
        }

        if(o instanceof Packets.NewBlock) {
            LD40.blocks.add(new Block(((Packets.NewBlock) o).name, ((Packets.NewBlock) o).x, ((Packets.NewBlock) o).y, ((Packets.NewBlock) o).angle));
        }
    }
}
