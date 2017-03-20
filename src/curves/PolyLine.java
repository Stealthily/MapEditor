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
            final Double alpha = 0.01;

            for(Point2D n : finalArray)
            {
                backgroundCoords = backgroundCoords + n.getX() + " " + n.getY()+ " ";
            }
            String[] tmpbgCoordsAr = backgroundCoords.split(" ");
            Double[] tmpbgCoordsArray = new Double[tmpbgCoordsAr.length];

            for(int i=0; i<tmpbgCoordsAr.length;i++)
            {
                tmpbgCoordsArray[i]=Double.parseDouble(tmpbgCoordsAr[i]);
            }
            int a = tmpbgCoordsArray.length/2;
           Double[][] bgCoordsMatrix = new Double[a][4];
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


//           for(int i=0;i<bgCoordsMatrix.length;i++)
//           {
//               for(int j=0;j<4;j++)
//               {
//                   System.out.println(bgCoordsMatrix[i][j]);
//               }
//           }

            Double[] slopes = new Double[a];

           for(int i=0; i<a;i++)
           {
               slopes[i]=(bgCoordsMatrix[i][3]-bgCoordsMatrix[i][1])/(bgCoordsMatrix[i][2]-bgCoordsMatrix[i][0]);
//               System.out.println(slopes[i]);
           }

           Double[] shifts = new Double[a];
           for(int i=0; i<a;i++)
           {
               shifts[i]=bgCoordsMatrix[i][1]-slopes[i]*bgCoordsMatrix[i][0];
//               System.out.println(shifts[i]);
           }
           Double[][] modifiedShifts = new Double[a][2];
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
                Double x1 =Math.round(modifiedShifts[k][0]-shifts[k]-alpha)/(slopes[k]-(1/slopes[k]));
                Double x2 =(modifiedShifts[k][0]-shifts[k]+alpha)/(slopes[k]-(1/slopes[k]));
                Double x3 =(modifiedShifts[k][1]-shifts[k]-alpha)/(slopes[k]-(1/slopes[k]));
                Double x4 =(modifiedShifts[k][1]-shifts[k]+alpha)/(slopes[k]-(1/slopes[k]));
                Double y1=slopes[k]*x1+shifts[k];
                Double y2=slopes[k]*x2+shifts[k];
                Double y3=slopes[k]*x3+shifts[k];
                Double y4=slopes[k]*x4+shifts[k];
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
