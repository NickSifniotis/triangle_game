import GameEngine.BoardUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nsifniotis on 3/12/15.
 */
public class TriangleGame
{
    public static int[] GetAllMoves(int[] states)
    {
        List<Integer> moves = new LinkedList<Integer>();
        for (int state: states)
        {
            for (int i = 0; i < 15; i++)
            {
                if (BoardUtils.Get(state, i))
                {
                    int[] move_hold = BoardUtils.GetValidMoves(state, i);
                    for (int direction : move_hold)
                        moves.add(BoardUtils.MakeMove(state, i, direction));
                }
            }
        }

        int[] res = new int[moves.size()];
        for (int i = 0; i < res.length; i ++)
            if (moves.get(i) != 0)
                res[i] = moves.get(i);

        return res;
    }


    /**
     * Goes through the unordered list of moves, normalises each of them,
     * sorts the list, removes duplicates, and returns a new list.
     *
     * @param move_list The list to apply this operation to.
     * @return The reduced form list.
     */
    public static int[] NormaliseMoves(int[] move_list)
    {
        for (int i = 0; i < move_list.length; i ++)
            move_list[i] = BoardUtils.Normalise(move_list[i]);

        Arrays.sort(move_list);

        List<Integer> uniques = new ArrayList<Integer>();
        int first = 0;
        for (int i: move_list)
        {
            if (i != first)
                uniques.add(i);

            first = i;
        }

        int[] res = new int[uniques.size()];
        for (int i = 0; i < res.length; i ++)
            res[i] = uniques.get(i);

        return res;
    }


    public static void main(String[] args)
    {
        int starting_state = 0x7FFF;
        starting_state = BoardUtils.Set(starting_state, 0, false);

        int[] states = new int[1];
        states[0] = starting_state;

        for (int i = 0; i < 13; i ++)
        {
            // there can only be thirteen moves in a game.
            System.out.println("Move: " + (i+1));
            int[] moves = GetAllMoves(states);
            states = NormaliseMoves(moves);

            for (int move : states)
            {
                System.out.println(Integer.toBinaryString(move));
                BoardUtils.Display(move);
            }
        }
    }
}
