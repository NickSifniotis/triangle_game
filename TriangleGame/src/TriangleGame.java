import GameEngine.BoardUtils;

/**
 * Created by nsifniotis on 3/12/15.
 */
public class TriangleGame
{
    public static void main(String[] args)
    {
        BoardUtils.Display(4235);
        BoardUtils.Display(BoardUtils.MakeMove(4235, 3, 3));
    }
}
