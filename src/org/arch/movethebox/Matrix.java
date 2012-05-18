/**
 * 
 */
package org.arch.movethebox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

import org.arch.movethebox.Operation.Direction;

/**
 * @author wqy
 * 
 */
public class Matrix
{
	private int[][]	matrix;
	private int maxRow;
	private int maxColumn;
	
	public Matrix(int maxRow, int maxColumn)
	{
		matrix = new int[maxRow][maxColumn];
		this.maxRow= maxRow;
		this.maxColumn = maxColumn;
	}
	
	public void readInputFile(File file) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		int j = 0;
		while((line = reader.readLine()) != null)
		{
			String[] ss = line.split(",");
			for(int i = 0; i<ss.length;i++)
			{
				matrix[i][j] = Integer.parseInt(ss[i].trim());
				if(matrix[i][j] > 0)
				{
					//System.out.println("@@@[" + i + "," + j+ "]");
				}
				
			}
			j++;
		}

//		for (int a = 0; a < maxRow; a++)
//		{
//			for (int k = 0; k < maxColumn; k++)
//			{
//				int box = matrix[a][k];
//				if(0 != box)
//				{
//					System.out.println("###[" + a + "," + k+ "]");
//				}
//			}
//		}
	}
	
	private int tidyUp(int[][] boxes)
	{
		int rest = 0;
		for (int i = 0; i < maxRow; i++)
		{
			for (int j = 0; j < maxColumn; j++)
			{
				int box = boxes[i][j];
				if(0 != box)
				{
					if(j >= 1 && boxes[i][j-1] == 0)
					{
						//drop box if below is null
						boxes[i][j-1] = box;
						boxes[i][j] = 0;
						return tidyUp(boxes);
					}
				}
			}
		}
		

		//clear matched column
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = 0; i < maxRow; i++)
		{
			stack.clear();
			for (int j = 0; j < maxColumn; j++)
            {
				if(stack.empty() && boxes[i][j] != 0)
				{
					stack.push(boxes[i][j]);
				}
				else
				{
					if(!stack.empty())
					{
						int box = stack.peek();
						if(box==(boxes[i][j]))
						{
							stack.push(boxes[i][j]);
						}
						else
						{
							if(stack.size() >= 3)
							{
								for(int k = 0; k < stack.size(); k++)
								{
									boxes[i][j-k-1] = 0;
								}
								stack.clear();
								stack = null;
								return tidyUp(boxes);
							}
							else
							{
								stack.clear();
								if(boxes[i][j] != 0)
								{
									stack.push(boxes[i][j]);
								}
							}
						}
					}
				}
            }
		}
		
		//clear matched row

		stack = new Stack<Integer>();
		for (int i = 0; i < maxColumn; i++)
		{
			stack.clear();
			for (int j = 0; j < maxRow; j++)
            {
				if(stack.empty() && boxes[j][i] != 0)
				{
					stack.push(boxes[j][i]);
				}
				else
				{
					if(!stack.empty())
					{
						int box = stack.peek();
						if(box==(boxes[j][i]))
						{
							stack.push(boxes[j][i]);
						}
						else
						{
							if(stack.size() >= 3)
							{
								for(int k = 0; k < stack.size(); k++)
								{
									boxes[j-k-1][i] = 0;
									
								}
								stack.clear();
								stack = null;
								return tidyUp(boxes);
							}
							else
							{
								stack.clear();
								if(boxes[j][i] != 0)
								{
									stack.push(boxes[j][i]);
								}
							}
						}
					}
				}
            }
		}
		
		for (int i = 0; i < maxRow; i++)
		{
			for (int j = 0; j < maxColumn; j++)
			{
				int box = boxes[i][j];
				if(0 != box)
				{
					rest++;
				}
			}
		}
		return rest;
	}
	
	private int[][] cloneMatrix(int[][] boxes)
	{
		int[][] clone = new int[boxes.length][];
		for(int i = 0; i < boxes.length; i++)
		{
			clone[i] = new int[boxes[i].length];
			for(int j = 0; j < boxes[i].length; j++)
			{
				clone[i][j] = boxes[i][j];
			}
		}
		
		return clone;
	}
	
	private boolean moveUp(int[][] boxes, int x, int y)
	{
		return moveDown(boxes, x, y + 1);
	}
	
	private boolean moveDown(int[][] boxes, int x, int y)
	{
		if (y == 0)
		{
			return false;
		}
		int box = boxes[x][y];
		if (0 == box)
		{
			return false;
		}
		int next = boxes[x][y - 1];
		if (0 == next)
		{
			return false;
		}
		boxes[x][y] = next;
		boxes[x][y - 1] = box;
		return true;
	}
	
	private boolean moveLeft(int[][] boxes, int x, int y)
	{
		return moveRight(boxes, x - 1, y);
	}
	
	private boolean moveRight(int[][] boxes, int x, int y)
	{
		if (x < 0)
		{
			return false;
		}
		int box = boxes[x][y];
		int next = boxes[x + 1][y];
		if (0 == box && 0 == next)
		{
			return false;
		}
		boxes[x][y] = next;
		boxes[x + 1][y] = box;
		return true;
	}
	
	public boolean moveBox(int[][] boxes, int step_num, List<Operation> ops)
	{
		if (step_num == 1)
		{
			Operation opr = moveBox1(boxes);
			if (null == opr)
			{
				return false;
			}
			
			ops.add(opr);
			return true;
		}
		tidyUp(boxes);

		for (int i = 0; i < boxes.length; i++)
		{
			for (int j = 0; j < boxes[i].length; j++)
			{
				int box = boxes[i][j];
				if (0 == box)
				{
					continue;
				}
				int[][] clone = cloneMatrix(boxes);
				if (moveLeft(clone, i, j))
				{
					tidyUp(clone);
//					if(i == 3 && j == 1)
//					{
//						System.out.println("%#%#$%");
//						for (int a = 0; a < maxRow; a++)
//						{
//							for (int k = 0; k < maxColumn; k++)
//							{
//								int tmp = clone[a][k];
//								if(0 != tmp)
//								{
//									System.out.println("###[" + a + "," + k+ "] = " + tmp);
//								}
//							}
//						}
//					}
					ops.add(new Operation(Direction.LEFT, i, j));
					if (moveBox(clone, step_num - 1, ops))
					{
						return true;
					}
					else
					{
						ops.remove(ops.size() - 1);
					}
				}

				clone = cloneMatrix(boxes);
				if (moveRight(clone, i, j))
				{
					tidyUp(clone);
					ops.add(new Operation(Direction.RIGHT, i, j));
					if (moveBox(clone, step_num - 1, ops))
					{
						return true;
					}
					else
					{
						ops.remove(ops.size() - 1);
					}
				}
				clone = cloneMatrix(boxes);
				if (moveUp(clone, i, j))
				{
					tidyUp(clone);
					ops.add(new Operation(Direction.UP, i, j));
					if (moveBox(clone, step_num - 1, ops))
					{
						return true;
					}
					else
					{
						ops.remove(ops.size() - 1);
					}
				}
				clone = cloneMatrix(boxes);
				if (moveDown(clone, i, j))
				{
					tidyUp(clone);
					ops.add(new Operation(Direction.DOWN, i, j));
					if (moveBox(clone, step_num - 1, ops))
					{
						return true;
					}
					else
					{
						ops.remove(ops.size() - 1);
					}
				}
			}
		}
		return false;
	}
	
	private Operation moveBox1(int[][] boxes)
	{
		for (int i = 0; i < boxes.length; i++)
		{
			for (int j = 0; j < boxes[i].length; j++)
			{
				int box = boxes[i][j];
				if (0 == box)
				{
					continue;
					
				}
				int[][] clone = cloneMatrix(boxes);
				boolean b = clone[i][j] == 3 && clone[4][1] ==1;


				if (moveLeft(clone, i, j) && tidyUp(clone) == 0)
				{
					return new Operation(Direction.LEFT, i, j);
				}

				clone = cloneMatrix(boxes);

				if (moveRight(clone, i, j) )
				{
					if(tidyUp(clone) == 0)
					{
						return new Operation(Direction.RIGHT, i, j);
					}
//					if(i == 3 && j == 1 && b)
//					{
//						for (int a = 0; a < maxRow; a++)
//						{
//							for (int k = 0; k < maxColumn; k++)
//							{
//								int tmp = clone[a][k];
//								if(0 != tmp)
//								{
//									System.out.println("###[" + a + "," + k+ "] = " + tmp);
//								}
//							}
//						}
//						System.out.println("=============");
//					}
				}
				
//				for (int a = 0; a < maxRow; a++)
//				{
//					for (int k = 0; k < maxColumn; k++)
//					{
//						int tt = clone[a][k];
//						if(0 != tt)
//						{
//							System.out.println("######" + a + ", " + k);
//						}
//					}
//				}
				clone = cloneMatrix(boxes);
				if (moveUp(clone, i, j) && tidyUp(clone) == 0)
				{
					return new Operation(Direction.UP, i, j);
				}
				clone = cloneMatrix(boxes);
				if (moveDown(clone, i, j) && tidyUp(clone) == 0)
				{
					return new Operation(Direction.DOWN, i, j);
				}
			}
		}
		return null;
	}
	
	public void move(int stepnum, List<Operation> ops)
	{

		moveBox(matrix, stepnum, ops);
	}
}
