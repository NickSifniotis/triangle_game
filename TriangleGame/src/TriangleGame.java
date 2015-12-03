import GameEngine.BoardState;

/**
 * Created by nsifniotis on 3/12/15.
 */
public class TriangleGame
{
    public static void main(String[] args)
    {
        BoardState bs = new BoardState();

        bs.state = bs.get_best_state((short)4235);
        bs.Display();
        bs.state = bs.get_best_state((short)14);
        bs.Display();
        bs.state = bs.get_best_state((short)537);
        bs.Display();
        bs.state = bs.get_best_state((short)16384);
        bs.Display();


    }
}
