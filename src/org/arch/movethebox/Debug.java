/**
 * 
 */
package org.arch.movethebox;

import java.io.File;
import java.io.IOException;

import org.arch.movethebox.Operation.Direction;

/**
 * @author wqy
 * 
 */
public class Debug
{
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		Matrix matrix = new Matrix(10, 10);
		matrix.readInputFile(new File(args[0]));
		matrix.move(new Operation(Direction.UP, 1, 2));
		matrix.move(new Operation(Direction.UP, 5, 2));
		matrix.move(new Operation(Direction.UP, 3, 2));
		for (int a = 0; a < matrix.maxRow; a++)
		{
			for (int k = 0; k < matrix.maxColumn; k++)
			{
				int box = matrix.matrix[a][k];
				if (0 != box)
				{
					System.out.println("###[" + a + "," + k + "]=" + box);
				}
			}
		}
	}
	
}
