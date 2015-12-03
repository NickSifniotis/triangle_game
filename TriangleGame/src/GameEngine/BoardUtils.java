package GameEngine;

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
//        for (int i = 0; i < 15; i ++)
//        {
//            System.out.print(Get(state, i) ? "1" : "0");
//            if (i % 5 == 4)
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
        int one_corner = (state & 0x002F) << 10;
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
     *
     * @param line The line (virtual ycoordinate) to get.
     * @param column The column (virtual xcoordinate) to get.
     * @return The bit number on the bitmap.
     */
    private static int CoordinateToBit(int line, int column)
    {
        int location = 0;

        if (line == 1)
            location = (column == 0) ? 1 : 3;
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
                    location = 11;
                    break;
            }

        return location;
    }


    /**
     * Returns the state of the given bit within the bitmap.
     *
     * @param location The bit to get the state of.
     * @return True if the bit is set, False if the bit is not set.
     */
    private static boolean Get(int state, int location)
    {
        return ((state & (int)Math.pow(2, location)) != 0);
    }


    /**
     * Sets the bit at the given location.
     *
     * @param location The bit within the bitmap to set.
     * @param value The value to set it to.
     */
    private static int Set(int state, int location, boolean value)
    {
        if (value)
            state = (state | (int)Math.pow(2, location));
        else
            state = (state & ~((int)Math.pow(2, location)));

        return state;
    }
}
