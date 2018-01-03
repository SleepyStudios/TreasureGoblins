package net.sleepystudios.ld40.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * Created by tudor on 03/12/2017.
 */
public class Network {
    public static void register(EndPoint ep) {
        Kryo kryo = ep.getKryo();
        kryo.register(Packets.PlayerData.class);
        kryo.register(Packets.Move.class);
        kryo.register(Packets.Sleeping.class);
        kryo.register(Packets.NewBlock.class);
    }
}
