package curves;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.ArrayList;

/**
 * The controller which stores the curves and constructs the curves
 *
 * @author Kareem Horstink
 * @version 1.00
 */
public class Controller {

    /**
     * The variable to set the constructed line to be a polyline
     */
    public static final int POLYLINE = 11;

    /**
     * The variable to set the constructed line to be a natural cubic spline
     */
    public static final int CUBIC_N = 21;

    /**
     * The variable to set the constructed line to be a bezier curve
     */
    public static final int BEZIERCURVE = 31;

    /**
     * The variable to set the constructed line to be a bezier spline
     */
    public static final int BEZIERSPLINE = 32;

    /**
     * The variable to set the constructed line to be a colinear bezier spline
     */
    public static final int BEZIERSPLINECOLINEAR = 33;

    /**
     * The variable to set the constructed line to be a Bspline
     */
    public static final int BSPLINE = 34;

    /**
     * The variable to set the constructed line to be a bowl based on a GA
     */
    public static final int BOWLGA = 41;

    /**
     * The variable to set the constructed line to be a bowl based on a semi circle
     */
    public static final int BOWLSC = 42;

    /**
     * The variable to set the constructed line to be a ellipse
     */
    public static final int ELLIPSE = 51;

    /**
     * List of the all the curves
     */
    private final ArrayList<Curve> CURVES;

    private final boolean DEBUG = false;

    /**
     * Default constructor
     */
    public Controller() {
        CURVES = new ArrayList<>();
    }

    /**
     * Returns the amount of curves that is currently in the system
     *
     * @return Return the amount of curves in the system (integer)
     */
    public int amountOfCurves() {
        return CURVES.size();
    }

    /**
     * Gets the name of the current curve
     *
     * @param index The index of the line
     * @return The name of the curve
     */
    public String getCurveName(int index) {
        return CURVES.get(index).getName();
    }

    /**
     * Method to retrieve the plotting coordinates of each curve. NOTE THE DEFINITION OF SUB-POINTS!
     *
     * @param curveIndex index of which curve to retrieve
     * @param subPoints this number signifies the number of plotting points in-between each pair of
     * control-points. The larger the number, the more fine-grained the curve plot will be.
     * @return
     */
    public ArrayList<Point2D> getCurvePlot(int curveIndex, int subPoints) {
        return CURVES.get(curveIndex).getPlot(subPoints);
    }

    /**
     * Check if names are already listed
     *
     * @param name The name to be checked
     * @return The name return when its corrected
     */
    private String checkName(String name) {
        boolean passed = true;
        while (passed) {
            passed = false;
            if (CURVES.isEmpty()) {
                return name;
            } else {
                for (Curve CURVES1 : CURVES) {
                    if (CURVES1.getName().equals(name)) {
                        name = name + "i";
                        passed = true;
                    }
                }
            }
        }
        return name;
    }

    /**
     * Constructs the new spline/curve
     *
     * @param TYPE The type of curve/spline to be created
     * @param x The x location of the first point
     * @param y The y location of the first points
     * @param name The name of the curve/spline
     */
    public void createCurve(int TYPE, double x, double y, String name) {
        name = checkName(name);
        if (TYPE == POLYLINE) {
            CURVES.add(new PolyLine(new Point2D.Double(x, y), name));
            if (DEBUG) {
                System.out.println("Polyline Created");
            }
        }
    }

    /**
     * Converts one type of curve to another
     *
     * @param index The index of the current curve
     * @param TYPE The type of curve to transform it into
     */


    /**
     * Removes a control point
     *
     * @param CurveID The index of the curve
     * @param PointID The index of the point
     * @return Returns the deleted point
     */
    public Point2D removePoint(int CurveID, int PointID) {
        return CURVES.get(CurveID).removePoint(PointID);
    }

    /**
     * Closes the curve/spline
     *
     * @param curveIndex The index of the curve/spline
     * @return Returns if the curve/spline is closed or not
     */
    public boolean closeCurve(int curveIndex) {
        CURVES.get(curveIndex).setClosed(true);
        return CURVES.get(curveIndex).isClosed();
    }

    /**
     * Opens the curve/spline
     *
     * @param curveIndex The index of the curve/index
     * @return Returns if the curve/spline is closed or not
     */
    public boolean openCurve(int curveIndex) {
        CURVES.get(curveIndex).setClosed(false);
        return CURVES.get(curveIndex).isClosed();
    }

    /**
     * Gets the index of the curve in the controller
     *
     * @param curve The controller to be searched
     * @return The index of the selected curve
     */
    public int getCurveID(Curve curve) {
        return CURVES.indexOf(curve);
    }

    /**
     * Add a point to last position of the curve/spline
     *
     * @param x The x location of the point
     * @param y The y location of the point
     * @param curveIndex The index of the curve/spline
     */
    public void addLastPoint(double x, double y, int curveIndex) {
        CURVES.get(curveIndex).add(x, y);
    }

    /**
     * Adds a point to desired location of the curve/spline
     *
     * @param x The x location of the point
     * @param y The y location of the point
     * @param curveIndex The index of the of the curve/spline
     * @param index The desired location to add the point
     */
    public void addPoint(double x, double y, int curveIndex, int index) {
        //TO DO
    }

    /**
     * Updates the location of a particular point
     *
     * @param x The x location of the point
     * @param y The y location of the point
     * @param curveIndex The index of the of the curve/spline
     * @param pointIndex The index of the point to be updated
     */
    public void setPointLocation(double x, double y, int curveIndex, int pointIndex) {
        CURVES.get(curveIndex).setPoint(pointIndex, x, y);
    }

    /**
     * Returns the list of the control points
     *
     * @param curveIndex The index of the curve/spline
     * @return Returns a list of Point2Dd
     */
    public List<Point2D> getControlsPoints(int curveIndex) {
        return CURVES.get(curveIndex).getControlPoints();
    }

    /**
     * Returns the area of a curve/spline
     *
     * @param curveID The index of the curve/spline
     * @return Returns the area of the curve/spline based on the selected method
     */
    public double curveArea(int curveID) {
        return CURVES.get(curveID).area(area);
    }

    /**
     * Returns the length of a curve/spline
     *
     * @param curveID The index of the curve/spline
     * @return Returns the length of the curve/spline based on the selected method
     */
    public double curveLength(int curveID) {
        return CURVES.get(curveID).length(length);
    }

    /**
     * Which Algorithm to use for find the area
     */
    private int area = 1;

    /**
     * Set the value of area
     *
     * @param area new value of area
     */
    public void setArea(int area) {
        if (DEBUG) {
            System.out.println("Setting area to: " + area);
        }
        this.area = area;
    }

    /**
     * Which Algorithm to use for find the length
     */
    private int length = 3;

    /**
     * Set the value of length Algorithm
     *
     * @param length new value of length
     */
    public void setLength(int length) {
        if (DEBUG) {
            System.out.println("Setting length to: " + length);
        }
        this.length = length;
    }

    /**
     * Gets the algorithm allowed for the getting the area of the curve
     *
     * @param index The index of the curve
     * @return An arraylist of the index of allowed algorithm
     */
    public ArrayList getAllowedAlgorithmsArea(int index) {
        return CURVES.get(index).areaAlgorithms;
    }

    /**
     * Gets the algorithm allowed for the getting the length of the curve
     *
     * @param index The index of the curve
     * @return An arraylist of the index of allowed algorithm
     */
    public ArrayList getAllowedAlgorithmsLength(int index) {
        return CURVES.get(index).arcLengthAlgorithms;
    }

    /**
     * Sets the current algorithms to the default one based on the line
     *
     * @param index The index of the line wanted
     */
    public void setDefault(int index) {
        if (DEBUG) {
            System.out.println("Set algorthms to default value");
        }
        if (CURVES.get(index).areaAlgorithms.size() > 0) {
            setArea(CURVES.get(index).areaAlgorithms.get(0));
        }

        if (CURVES.get(index).arcLengthAlgorithms.size() > 0) {
            setLength(CURVES.get(index).arcLengthAlgorithms.get(0));
        }
    }

}
