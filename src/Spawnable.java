import bagel.util.*;

public interface Spawnable {
    // Shift objects to the left
    void leftShift();
    // Check if object has passed the bird
    boolean checkBirdPass(Bird bird);
    // Check if object is out of window
    boolean checkWindowBounds();
    // Return Rectangle of object for comparisons
    Rectangle getBox();
}