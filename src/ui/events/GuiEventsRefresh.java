package ui.events;

import curves.PolyLine;

import java.io.*;

/**
 * Event to handle the refreshing of Canvas
 *
 * @author Kareem Horstink
 */
public class GuiEventsRefresh extends GuiEvents {

    /**
     * Constructor
     *
     * @param source The source of the event
     */
    public GuiEventsRefresh(Object source) {
        super(source);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("obstacleList.txt"), "utf-8"))) {
            writer.write(PolyLine.nameObstacle);
        }
        catch (IOException e)
        {
            System.out.println(e);
        }


    }

}
