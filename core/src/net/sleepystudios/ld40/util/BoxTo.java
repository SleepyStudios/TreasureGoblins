package net.sleepystudios.ld40.util;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by tudor on 04/12/2017.
 */
public class BoxTo {
    public static Polygon poly(Rectangle box, int FW, int FH, int OX, int OY, float angle) {
        Polygon poly = new Polygon(new float[] {
                box.getX(), box.getY(),
                box.getX(), box.getY()+box.getHeight(),
                box.getX()+box.getWidth(), box.getY()+box.getHeight(),
                box.getX()+box.getWidth(), box.getY()});
        poly.setOrigin(box.getX()+FW/2-OX, box.getY()+FH/2-OY);
        poly.rotate(angle);

        return poly;
    }
}
