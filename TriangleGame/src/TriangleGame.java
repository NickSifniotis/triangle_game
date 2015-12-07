import GameEngine.BoardUtils;
import GameEngine.StateTuple;

import java.util.*;

/**
 * Created by nsifniotis on 3/12/15.
 */
public class TriangleGame
{
    public static StateTuple[] GetAllMoves(StateTuple[] states)
    {
        List<StateTuple> moves = new LinkedList<StateTuple>();
        for (StateTuple state: states)
        {
            int curr_state = state.CurrentState;
            for (int i = 0; i < 15; i++)
            {
                if (BoardUtils.Get(curr_state, i))
                {
                    int[] move_hold = BoardUtils.GetValidMoves(curr_state, i);
                    for (int direction : move_hold)
                        moves.add(new StateTuple(BoardUtils.MakeMove(curr_state, i, direction), curr_state));
                }
            }
        }

        StateTuple[] res = new StateTuple[moves.size()];
        return moves.toArray(res);
    }


    /**
     * Goes through the unordered list of moves, acquires the normal form of the move,
     * and adds to the results array only those moves whos normal forms have not been seen already.
     *
     * @param move_list The list of moves to reduce.
     * @return The reduced list.
     */
    public static int[] ReduceMoves(int[] move_list)
    {
        Map <Integer, Boolean> normalised_forms = new HashMap<Integer, Boolean>();
        List<Integer> reduced_list = new LinkedList<Integer>();

        for (int move: move_list)
        {
            int normal = BoardUtils.Normalise(move);
            if (!normalised_forms.containsKey(normal))
            {
                normalised_forms.put(normal, true);
                reduced_list.add(move);
            }
        }

        int[] res = new int[reduced_list.size()];
        for (int i = 0; i < res.length; i ++)
            res[i] = reduced_list.get(i);

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

        StateTuple[] states = new StateTuple[1];
        states[0] = new StateTuple(starting_state);

        for (int i = 0; i < 13; i ++)
        {
            // there can only be thirteen moves in a game.
            StateTuple[] moves = GetAllMoves(states);
            states = ReduceMoves(moves);

            System.out.println("Move " + (i+1) + ": " + states.length);
        }
    }
}
