/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gc_egames_549;

/**
 *
 * @author yolanda
 */
public class LeaderboardResult implements Comparable <LeaderboardResult>
{
    private String teamName;
    private int points;
    
    public LeaderboardResult (String teamName, int points)
    {
        this.teamName = teamName;
        this.points = points;
    }
    
    public String getTeamName()
    {
        return teamName;
    }
    
    public int getPoints()
    {
        return points;
    }
    
    public void setTeamName (String teamName)
    {
        this.teamName = teamName;
    }
    
    public void setPoints (int points)
    {
        this.points = points;
    }
    
@Override
public int compareTo (LeaderboardResult obj)
{
    if (this.points > obj.points)
    {
        return 1;
    }
    
    else if (this.points < obj.points)
    {
        return -1;
    }
    
    else
    {
        return 0;
    }
}

@Override
public String toString()
{
    return teamName + "," + points;
}

}
