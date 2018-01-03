package net.sleepystudios.ld40.net;

/**
 * Created by tudor on 03/12/2017.
 */
public class Packets {
    public static class PlayerData {
        public String name;
    }

    public static class Move {
        public String name;
        public float x, y;
    }

    public static class Sleeping {
        public String name;
        public boolean sleeping;
    }

    public static class NewBlock {
        public String name;
        public float x, y, angle;
    }
}
