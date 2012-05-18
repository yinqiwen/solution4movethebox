/**
 * 
 */
package org.arch.movethebox;

/**
 * @author wqy
 * 
 */
public class Operation
{
	public static enum Direction
	{
		LEFT, RIGHT, UP, DOWN
	}
	
	Direction	direction;
	int	      x;
	int	      y;
	
	public Operation(Direction direction, int x, int y)
	{
		this.direction = direction;
		this.x = x;
		this.y = y;
	}
	
	public String toString()
	{
		return "Move [" + x+"][" + y+"] to " + direction;
	}
}
