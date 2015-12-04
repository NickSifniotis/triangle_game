package GameEngine;


import java.util.LinkedList;
import java.util.List;

/**
 * BoardUtils - This class contains a number of useful static methods for manipulating game board state
 * bitmaps within the TriangleGame application.
 *
 * @author Nick Sifniotis
 * @version 1.1.0
 * @since 03/12/2015
 */
public class BoardUtils
{
    public static void Display(int state)
    {
        for (int i = 0; i < 5; i ++)
        {
            for (int j = 0; j < (5 - i); j ++)
                System.out.print(" ");

            for (int j = 0; j < i + 1; j ++)
                System.out.print(Get(state, CoordinateToBit(i, j)) ? "* " : ". ");

            System.out.println();
        }

//        System.out.println();
//        for (int i = 14; i >= 0; i --)
//        {
//            System.out.print(Get(state, i) ? "1" : "0");
//            if (i % 5 == 0)
//                System.out.print(".");
//        }

        System.out.println("\n");
    }


    /**
     * Goes through the six transformations (2 reflections, 3 rotations) for the given game state
     * and returns the state's unique identifier (Which is defined to be the lowest value for state)
     *
     * Every state is uniquely identified by this lowest value.
     *
     * @param state The state to normalise.
     * @return The normalised (lowest) equivalent state for the one supplied.
     */
    public static int Normalise(int state)
    {
        int res = state;
        if (Reflect(state) < res)
            res = Reflect(state);

        for (int i = 0; i < 2; i ++)
        {
            state = Rotate(state);

            if (state < res)
                res = state;
            if (Reflect(state) < res)
                res = Reflect(state);
        }

        return res;
    }


    /**
     * Performs the bitwise rotation of the given state.
     *
     * @param state The state to transform.
     * @return The transformed state.
     */
    public static int Rotate(int state)
    {
        int one_corner = (state & 0x001F) << 10;
        int working_state = state >>> 5;

        return one_corner + working_state;
    }


    /**
     * Reflect the given board state.
     *
     * @param state The state to reflect.
     * @return The reflected state.
     */
    public static int Reflect(int state)
    {
        // pretty crappy way to do it eh
        state = swap_bits(state, 1, 3);
        state = swap_bits(state, 2, 12);
        state = swap_bits(state, 5, 10);
        state = swap_bits(state, 6, 13);
        state = swap_bits(state, 8, 11);
        state = swap_bits(state, 9, 14);

        return state;
    }


    private static int swap_bits(int state, int b1, int b2)
    {
        boolean v1 = Get(state, b1);
        boolean v2 = Get(state, b2);
        state = Set(state, b1, v2);
        state = Set(state, b2, v1);

        return state;
    }


    /**
     * Convert from (x, y) coordinates to positions on the bitmap.
     * Returns -1 if the supplied coordinates do not correspond to a legal location.
     *
     * @param line The line (virtual ycoordinate) to get.
     * @param column The column (virtual xcoordinate) to get.
     * @return The bit number on the bitmap.
     */
    private static int CoordinateToBit(int line, int column)
    {
        int location = -1;

        if (line == 0 && column == 0)
            location = 0;
        else if (line == 1)
        {
            if (column == 0)
                location = 1;
            else if (column == 1)
                location = 3;
        }
        else if (line == 2)
            switch (column)
            {
                case 0:
                    location = 2;
                    break;
                case 1:
                    location = 4;
                    break;
                case 2:
                    location = 12;
            }
        else if (line == 3)
            switch (column)
            {
                case 0:
                    location = 8;
                    break;
                case 1:
                    location = 9;
                    break;
                case 2:
                    location = 14;
                    break;
                case 3:
                    location = 11;
                    break;
            }
        else if (line == 4)
            switch (column)
            {
                case 0:
                    location = 5;
                    break;
                case 1:
                    location = 6;
                    break;
                case 2:
                    location = 7;
                    break;
                case 3:
                    location = 13;
                    break;
                case 4:
                    location = 10;
                    break;
            }

        return location;
    }


    /**
     * Returns the coordinates of the given location.
     *
     * @param location The location of the bitmap to convert.
     * @return An (int, int) pair representing the coordinates of the supplied location.
     */
    public static int[] BitToCoordinate(int location)
    {
        int[] res = new int[2];

        switch(location)
        {
            case 0:
                res[0] = 0; res[1] = 0;
                break;
            case 1:
                res[0] = 1; res[1] = 0;
                break;
            case 2:
                res[0] = 2; res[1] = 0;
                break;
            case 3:
                res[0] = 1; res[1] = 1;
                break;
            case 4:
                res[0] = 2; res[1] = 1;
                break;
            case 5:
                res[0] = 4; res[1] = 0;
                break;
            case 6:
                res[0] = 4; res[1] = 1;
                break;
            case 7:
                res[0] = 4; res[1] = 2;
                break;
            case 8:
                res[0] = 3; res[1] = 0;
                break;
            case 9:
                res[0] = 3; res[1] = 1;
                break;
            case 10:
                res[0] = 4; res[1] = 4;
                break;
            case 11:
                res[0] = 3; res[1] = 3;
                break;
            case 12:
                res[0] = 2; res[1] = 2;
                break;
            case 13:
                res[0] = 4; res[1] = 3;
                break;
            case 14:
                res[0] = 3; res[1] = 2;
                break;
        }

        return res;
    }


    /**
     * Returns the state of the given bit within the bitmap.
     *
     * @param location The bit to get the state of.
     * @return True if the bit is set, False if the bit is not set.
     */
    public static boolean Get(int state, int location)
    {
        return ((state & (int)Math.pow(2, location)) != 0);
    }


    /**
     * Sets the bit at the given location.
     *
     * @param location The bit within the bitmap to set.
     * @param value The value to set it to.
     */
    public static int Set(int state, int location, boolean value)
    {
        if (value)
            state = (state | (int)Math.pow(2, location));
        else
            state = (state & ~((int)Math.pow(2, location)));

        return state;
    }


    /**
     * Takes the state and applies a 'move' in the direction supplied, starting from the location.
     * Importantly, this method applies no checks - it assumes the location is valid, there are enough
     * steps (2) in the given direction to execute the move, and that the move itself is a valid move.
     * These all need to be verified by other code before this method is called.
     *
     * @param state The current game state.
     * @param location The 'starting position' for the move.
     * @param direction The direction in which to make the move.
     * @return The new game state.
     */
    public static int MakeMove(int state, int location, int direction)
    {
        for (int i = 0; i < 3; i ++)
        {
            state = Set(state, location, !Get(state, location));
            location = Step(location, direction);
        }
        return state;
    }


    /**
     * Returns a location on the bitmap one step in the direction supplied away from
     * the provided location. If the step will take the location out of bounds, return -1.
     *
     * @param location The location to take the step from.
     * @param direction The direction in which to move.
     * @return The new location.
     */
    public static int Step(int location, int direction)
    {
        int [] coords = BitToCoordinate(location);

        switch (direction)
        {
            case 1:
                coords[0] ++;
                break;
            case 2:
                coords[1] ++;
                break;
            case 3:
                coords[0] ++;
                coords[1] ++;
                break;
            case -1:
                coords[0] --;
                break;
            case -2:
                coords[1] --;
                break;
            case -3:
                coords[0] --;
                coords[1] --;
                break;
        }

        return CoordinateToBit(coords[0], coords[1]);
    }


    /**
     * Returns an array of valid moves from the given location on the given board state.
     * The returned values are the directions in which a valid move may be made.
     * The array could be of any length between zero and six inclusive.
     *
     * @param state The current game state.
     * @param location The location from which we are testing the validity of moves. It is a given that
     *                 the bit that location represents is True - you cannot play a move from an empty peg.
     * @return An array of directions that correspond to valid moves from this location.
     */
    public static int[] GetValidMoves(int state, int location)
    {
        List<Integer> res_list = new LinkedList<Integer>();
        for (int i = 1; i < 4; i ++)
        {
            if (IsValidMove(state, location, i))
                res_list.add(i);

            if (IsValidMove(state, location, -i))
                res_list.add(-i);
        }

        int[] res = new int[res_list.size()];
        for (int i = 0; i < res.length; i ++)
            res[i] = res_list.get(i);

        return res;
    }


    /**
     * Tests whether or not a move can be played in the direction given, from the location,
     * on the supplied game state.
     *
     * A valid move would be - peg on this location, peg on location + step, empty on location+step+step
     *
     * @param state The game state being tested.
     * @param location The location to play from
     * @param direction The direction to make the move in
     * @return True if a move can be played here, False otherwise.
     */
    public static boolean IsValidMove(int state, int location, int direction)
    {
        if (!Get(state, location))
            return false;

        if (Step(location, direction) == -1)
            return false;
        if (!Get(state, Step(location, direction)))
            return false;

        if (Step(Step(location, direction), direction) == -1)
            return false;

        return !Get(state, Step(Step(location, direction), direction));
    }
}
