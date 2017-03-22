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
        if(name.equalsIgnoreCase("gen"))
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
	    //Here we check whether this is the outlying polygon or not
        {
            final Double alpha = 0.8;
            //This is the parameter responsible for the width of the rectangle

            for(Point2D n : finalArray)
            {
                backgroundCoords = backgroundCoords + n.getX() + " " + n.getY()+ " ";
            }

            //Here we collect the coordinates of the original outlying polygon into a new array that we split later on and convert to Double string.
            String[] tmpbgCoordsAr = backgroundCoords.split(" ");
            Double[] tmpbgCoordsArray = new Double[tmpbgCoordsAr.length];

            for(int i=0; i<tmpbgCoordsAr.length;i++)
            {
                tmpbgCoordsArray[i]=Double.parseDouble(tmpbgCoordsAr[i]);
            }


            int a = tmpbgCoordsArray.length/2;

            //Here a corresponds to the number of vectors we have in our figure

           Double[][] bgCoordsMatrix = new Double[a][4];

           //This is an important matrix that has all pairs of points that are linked by a line (vectors in some sense) that we have to use later on to extract the linear function out of it
           int ref =0;
           for(int i=0; i<bgCoordsMatrix.length; i++)
           {
               bgCoordsMatrix[i][0]=tmpbgCoordsArray[ref];
               bgCoordsMatrix[i][1]=tmpbgCoordsArray[ref+1];
               if(ref > a)
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
           //Here we basically fill the array so that each first point is the ending point of the previous line in the matrix, except for the case at the end, where the ending point is just the beginning of the first line since they are linked (closed shape)

            Double[] slopes = new Double[a];

           //This containes each slope of each function in the polygon

           for(int i=0; i<a;i++)
           {
               slopes[i]=(bgCoordsMatrix[i][3]-bgCoordsMatrix[i][1])/(bgCoordsMatrix[i][2]-bgCoordsMatrix[i][0]);
//               System.out.println(slopes[i]);
           }

           Double[] shifts = new Double[a];

           //This contains all the "b"s of the linear functions
           for(int i=0; i<a;i++)
           {
               shifts[i]=bgCoordsMatrix[i][1]-slopes[i]*bgCoordsMatrix[i][0];
//               System.out.println(shifts[i]);
           }
           Double[][] modifiedShifts = new Double[a][2];

           //This contains all the b[1] and b[2] of the perpendicular functions (given by a^=1x+b[1] or [2]

            for(int i=0; i<a;i++)
            {
                modifiedShifts[i][0]=(1/(slopes[i]))*(-bgCoordsMatrix[i][0])+bgCoordsMatrix[i][1];
                modifiedShifts[i][1]=(1/(slopes[i]))*(-bgCoordsMatrix[i][2])+bgCoordsMatrix[i][3];
//                System.out.println(modifiedShifts[i][0]);
//                System.out.println(modifiedShifts[i][1]);
            }

            Double[][] finished = new Double[a][8];

            for(int k=0; k<a;k++)
            {
//                Double x1 =Math.round(((modifiedShifts[k][0]-shifts[k]-alpha)/(slopes[k]-(1/slopes[k])))*100d)/100d;
                Double x1 =(modifiedShifts[k][0]-shifts[k]-alpha)/(slopes[k]-(1/slopes[k]));
                Double x2 =(modifiedShifts[k][0]-shifts[k]+alpha)/(slopes[k]-(1/slopes[k]));
//                Double x2 =Math.round(((modifiedShifts[k][0]-shifts[k]+alpha)/(slopes[k]-(1/slopes[k])))*100d)/100d;
                Double x3 =(modifiedShifts[k][1]-shifts[k]-alpha)/(slopes[k]-(1/slopes[k]));
//                Double x3 =Math.round(((modifiedShifts[k][1]-shifts[k]-alpha)/(slopes[k]-(1/slopes[k])))*100d)/100d;
                Double x4 =(modifiedShifts[k][1]-shifts[k]+alpha)/(slopes[k]-(1/slopes[k]));
//                Double x4 =Math.round(((modifiedShifts[k][1]-shifts[k]+alpha)/(slopes[k]-(1/slopes[k])))*100d)/100d;
//                Double y1=Math.round((slopes[k]*x1+shifts[k])*100d)/100d;
//                Double y2=Math.round((slopes[k]*x2+shifts[k])*100d)/100d;
//                Double y3=Math.round((slopes[k]*x3+shifts[k])*100d)/100d;
//                Double y4=Math.round((slopes[k]*x4+shifts[k])*100d)/100d;
                Double y1=((slopes[k]*x1+shifts[k]));
                Double y2=((slopes[k]*x2+shifts[k]));
                Double y3=((slopes[k]*x3+shifts[k]));
                Double y4=((slopes[k]*x4+shifts[k]));
                //(double)Math.round(value * 100000d) / 100000d


                finished[k][0]=x1;
                finished[k][1]=y1;
                finished[k][2]=x2;
                finished[k][3]=y2;
                finished[k][4]=x3;
                finished[k][5]=y3;
                finished[k][6]=x4;
                finished[k][7]=y4;
            }

            for (int i=0; i<a;i++)
            {
                for (int j=0; j<8;j++)
                {
                    nameObstacle = nameObstacle + finished[i][j] + " ";
                }
                nameObstacle = nameObstacle + "\n";
            }

        }
        else
        {
            for(Point2D n : finalArray)
            {
                nameObstacle = nameObstacle + n.getX() + " " + n.getY()+ " ";
            }

            nameObstacle = nameObstacle+"\n";
        }




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
