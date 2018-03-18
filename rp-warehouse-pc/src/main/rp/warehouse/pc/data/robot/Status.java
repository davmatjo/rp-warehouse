package rp.warehouse.pc.data.robot;

/**
 * Signifies the status of robot
 * 
 * @author roman
 */
public class Status {
    public static final int TRAVELING = 0;
    public static final int PICKING_UP = 1;
    public static final int DROPPING_OFF = 2;
    public static final int WAITING = 3;
    public static final int PLANNING = 4;
    public static final int LOCALISING = 5;
    public static final int WAITING_FOR_PICKUP = 6;
    public static final int WAITING_FOR_DROPOFF = 7;
    public static final int NOTHING = 8;

    public static String getWord(int status) {
        String returnWord = "";
        switch (status) {
        case TRAVELING:
            returnWord = "Traveling";
            break;
        case PICKING_UP:
            returnWord = "Picking Up";
            break;
        case DROPPING_OFF:
            returnWord = "Dropping Off";
            break;
        case WAITING:
            returnWord = "Waiting";
            break;
        case LOCALISING:
            returnWord = "Localising";
            break;
        case WAITING_FOR_PICKUP:
            returnWord = "Waiting for Pick Up";
            break;
        case WAITING_FOR_DROPOFF:
            returnWord = "Waiting For Drop Off";
            break;
        case NOTHING:
            returnWord = "Nothing";

            break;

        default:
            break;
        }

        return returnWord;

    }

}
