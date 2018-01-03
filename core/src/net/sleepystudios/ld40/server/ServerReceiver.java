package net.sleepystudios.ld40.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import net.sleepystudios.ld40.net.Packets;

/**
 * Created by tudor on 03/12/2017.
 */
public class ServerReceiver extends Listener {
    private Server server;

    public ServerReceiver(Server server) {
        this.server = server;
    }

    @Override
    public void connected(Connection c) {
        System.out.println("con");
    }

    @Override
    public void disconnected(Connection c) {
        Player p = LD40Server.getPlayer(c.getID());
        if(p!=null) {
            p.setSleeping();
        }
    }

    @Override
    public void received(Connection c, Object o) {
        if(o instanceof Packets.PlayerData) {
            // send everyone else
            for(int i=0; i<LD40Server.players.size(); i++) {
                Player p = LD40Server.players.get(i);

                Packets.PlayerData pd = new Packets.PlayerData();
                pd.name = p.name;
                server.sendToTCP(c.getID(), pd);

                Packets.Move m = new Packets.Move();
                m.name = p.name;
                m.x = p.x;
                m.y = p.y;
                server.sendToTCP(c.getID(), m);

                Packets.Sleeping s = new Packets.Sleeping();
                s.name = p.name;
                s.sleeping = p.sleeping;
                server.sendToTCP(c.getID(), s);
            }

            // send them blocks
            for(int i=0; i<LD40Server.blocks.size(); i++) {
                Block b = LD40Server.blocks.get(i);

                Packets.NewBlock nb = new Packets.NewBlock();
                nb.name = b.name;
                nb.x = b.x;
                nb.y = b.y;
                nb.angle = b.angle;
                server.sendToTCP(c.getID(), nb);
            }

            // send them to everyone else
            Player player = LD40Server.getPlayer(((Packets.PlayerData) o).name);
            if(player!=null) {
                if(player.id==-1) {
                    player.setAwake(c.getID());
                } else {
                    c.close();
                }
            } else {
                LD40Server.players.add(new Player(server, c.getID(), ((Packets.PlayerData) o).name));
            }
            server.sendToAllTCP(o);
        }

        if(o instanceof Packets.Move) {
            Player p = LD40Server.getPlayer(((Packets.Move) o).name);
            if(p!=null) {
                p.x = ((Packets.Move) o).x;
                p.y = ((Packets.Move) o).y;
                server.sendToAllExceptUDP(c.getID(), o);
            }
        }

        if(o instanceof Packets.NewBlock) {
            LD40Server.blocks.add(new Block(((Packets.NewBlock) o).name, ((Packets.NewBlock) o).x, ((Packets.NewBlock) o).y, ((Packets.NewBlock) o).angle));
            server.sendToAllTCP(o);
        }
    }
}
