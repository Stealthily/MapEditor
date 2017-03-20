package curves;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PolyLine extends Curve {

    private double length;
	private double area;
	public ArrayList<Point2D> finalArray = new ArrayList<Point2D>();
	public static String nameObstacle="";
	private boolean backgroundPolygon=false;
	private String backgroundCoords="";

	protected PolyLine(Point2D point, String name) {
        super(name);
        if(name.equalsIgnoreCase("sal"))
        {
           backgroundPolygon=true;
        }
        else
        {
            backgroundPolygon=false;
        }
        finalArray.add(point);
        super.points.add(point);

        recalcAaA();
    }

    @Override
    protected ArrayList<Point2D> getPlot(int interval) {
        return points;
    }
    
    @Override
    protected void setPoint(int index, double x, double y) {
		super.setPoint(index, x, y);
		recalcAaA();
	} 
    
    public void setClosed(boolean closed) {

	    if(backgroundPolygon)
        {
            for(Point2D n : finalArray)
            {
                backgroundCoords = backgroundCoords + n.getX() + " " + n.getY()+ " ";
            }
            String[] tmpbgCoordsArray = backgroundCoords.split(" ");
            int a = tmpbgCoordsArray.length/2;
           String[][] bgCoordsMatrix = new String[a][4];
           int ref =0;
           for(int i=0; i<bgCoordsMatrix.length; i++)
           {
               bgCoordsMatrix[i][0]=tmpbgCoordsArray[ref];
               bgCoordsMatrix[i][1]=tmpbgCoordsArray[ref+1];
               if(ref +3  > a)
               {
                   bgCoordsMatrix[i][2]=tmpbgCoordsArray[0];
                   bgCoordsMatrix[i][3]=tmpbgCoordsArray[1];
               }
               else
               {
                   bgCoordsMatrix[i][2]=tmpbgCoordsArray[ref+2];
                   bgCoordsMatrix[i][3]=tmpbgCoordsArray[ref+3];
               }

               ref=ref+2;
           }


           for(int i=0;i<bgCoordsMatrix.length;i++)
           {
               for(int j=0;j<4;j++)
               {
                   System.out.println(bgCoordsMatrix[i][j]);
               }
           }
        }

		for(Point2D n : finalArray)
		{
			nameObstacle = nameObstacle + n.getX() + " " + n.getY()+ " ";
		}

		nameObstacle = nameObstacle+"\n";


		if (closed == true && super.isClosed() == false){//close
			super.points.add(super.points.get(0));
			super.setClosed(closed);
		}
		else if (closed == false && super.isClosed() == true){//open
			super.points.remove(super.numberOfPoints()-1);
			super.setClosed(closed);
		}
		recalcAaA();

	}
    
    @Override
    protected int add(double x, double y) {
    	finalArray.add(new Point2D.Double(x,y));

		if (this.isClosed()){
    		this.setClosed(false);
    		this.points.add(new Point2D.Double(x, y));
    		this.setClosed(true);
    	}
    	else {
    		this.points.add(new Point2D.Double(x, y));
    	}
    	recalcAaA();
		return points.size() - 1;
	}
    
    private void recalcAaA() {
		this.area = NumericalApproximation.calcArea(this);
		this.length = NumericalApproximation.calcArcLength(this);
		if(backgroundPolygon)
        {
//            System.out.println(backgroundCoords);
        }

	}
    
    protected double area(int method) {
		if (method != areaAlgorithm){
			areaAlgorithm = method;
			recalcAaA();
		}
		return this.area;
	}
	
	@Override
	protected double length(int method) {
		if (method != arcLengthAlgorithm){
			arcLengthAlgorithm = method;
			recalcAaA();
		}
		return this.length;
	}

	@Override
	protected List<Point2D> getConversionPoints() {
		return (List<Point2D>)this.points;
	}
    
}
