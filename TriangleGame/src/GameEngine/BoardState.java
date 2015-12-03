package GameEngine;

/**
 * Created by nsifniotis on 3/12/15.
 */
public class BoardState
{
    public short state;

    public void Display()
    {
        int position = 0;
        for (int i = 0; i < 5; i ++)
        {
            for (int j = 0; j < (5 - i); j ++)
                System.out.print(" ");

            for (int j = 0; j < i + 1; j ++)
            {
                System.out.print(get_value(i, j) ? "* " : ". ");
                position ++;
            }
            System.out.println();
        }

        System.out.println();
        for (int i = 0; i < 15; i ++)
        {
            System.out.print(get_value(this.state, i) ? "1" : "0");
            if (i % 5 == 4)
                System.out.print(".");
        }

        System.out.println("\n");
    }


    /**
     * Goes through the six transformations (2 reflections, 3 rotations) for the given game state
     * and returns the state's unique identifier (Which is defined to be the lowest value for state)
     *
     * Every state is uniquely identified by this lowest value.
     *
     * @param state
     * @return
     */
    public short get_best_state(short state)
    {
        short res = state;
        if (reflect_state(state) < res)
            res = reflect_state(state);

        for (int i = 0; i < 2; i ++)
        {
            state = rotate_state(state);
            if (state < res)
                res = state;
            if (reflect_state(state) < res)
                res = reflect_state(state);
        }

        return res;
    }


    /**
     * Returns the two rotations of this board; one clockwise, the other anticlockwise.
     *
     * @return
     */
    public BoardState[] Rotations()
    {
        BoardState[] res = new BoardState[2];
        res[0] = new BoardState();
        res[1] = new BoardState();

        res[0].state = rotate_state(this.state);
        res[1].state = rotate_state(res[0].state);

        return res;
    }


    public BoardState Reflection()
    {
        BoardState res = new BoardState();
        res.state = reflect_state(this.state);
        return res;
    }


    /**
     * Performs the bitwise rotation of the given state.
     *
     * @param state
     * @return
     */
    public short rotate_state(short state)
    {
        int one_corner = (state & 0x002F) << 10;
        int working_state = state >>> 5;

        return (short)(one_corner + working_state);
    }


    /**
     * Reflect the bitmap
     *
     * @param state
     * @return
     */
    public short reflect_state(short state)
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


    private short swap_bits(short state, int b1, int b2)
    {
        boolean v1 = get_value(state, b1);
        boolean v2 = get_value(state, b2);
        state = set_value(state, b1, v2);
        state = set_value(state, b2, v1);

        return state;
    }


    /**
     * Convert from (x, y) coordinates to positions on the bitmap.
     * Yes, it's a pretty silly way to do it.
     *
     * @param line
     * @param column
     * @return
     */
    private boolean get_value(int line, int column)
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

        return get_value(this.state, location);
    }


    /**
     * Returns the state of the given bit within the bitmap.
     * @param location
     * @return
     */
    private boolean get_value(short state, int location)
    {
        return ((state & (int)Math.pow(2, location)) != 0);
    }


    /**
     * Sets the bit at the given location.
     *
     * @param location
     * @param value
     */
    private short set_value(short state, int location, boolean value)
    {
        if (value)
            state = (short)(state | (short)Math.pow(2, location));
        else
            state = (short)(state & ~((short)Math.pow(2, location)));

        return state;
    }
}
