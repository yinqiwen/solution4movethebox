/**
 * 
 */
package org.arch.movethebox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wqy
 *
 */
public class Main
{
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		Matrix matrix = new Matrix(9, 7);
		matrix.readInputFile(new File(args[0]));
		
		List<Operation> ops = new ArrayList<Operation>();
		matrix.move(Integer.parseInt(args[1]), ops);
		if(ops.isEmpty())
		{
			System.err.println("Failed to get the solution for moveing box.");
		}
		for(Operation opr:ops)
		{
			System.out.println(opr);
		}
	}
	
}
