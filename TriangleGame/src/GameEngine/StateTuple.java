package GameEngine;

/**
 * Created by nsifniotis on 7/12/15.
 */
public class StateTuple
{
    public int CurrentState;
    public int ParentState;

    public StateTuple(int curr)
    {
        CurrentState = curr;
        ParentState = -1;
    }

    public StateTuple(int curr, int parent)
    {
        CurrentState = curr;
        ParentState = parent;
    }
}
