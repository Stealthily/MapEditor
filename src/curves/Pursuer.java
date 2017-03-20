package curves;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 * Created by alexisguillot on 19/03/2017.
 */
public class Pursuer extends Curve {


    public ArrayList<Point2D> finalArray = new ArrayList<Point2D>();
    public static String listCoord="";

    protected Pursuer(Point2D point, String name) {
        super(name);
        finalArray.add(point);
        super.points.add(point);
    }

    @Override
    protected ArrayList<Point2D> getPlot(int interval) {
        return points;
    }

//    @Override
//    protected void setPoint(int index, double x, double y) {
//        super.setPoint(index, x, y);
//
//    }

    public void setClosed(boolean closed) {

        String obstacleCoordinates;

        for(Point2D n : finalArray)
        {
            listCoord = listCoord + n.getX() + " " + n.getY()+ " ";
        }

        listCoord = listCoord+"\n";


        if (closed == true && super.isClosed() == false){//close
            super.points.add(super.points.get(0));
            super.setClosed(closed);
        }
        else if (closed == false && super.isClosed() == true){//open
            super.points.remove(super.numberOfPoints()-1);
            super.setClosed(closed);
        }
    }


    @Override
    protected List<Point2D> getConversionPoints() {
        return (List<Point2D>)this.points;
    }

}
