package logic;

public class Levels {
	public static final int[][][] LVL = {
			{
				// some squares
				{0, 0}, {1, 0}, {0, 1}, {1, 1},
				{0, 4}, {1, 4}, {0, 5}, {1, 5},
				{12, 0}, {13, 0}, {12, 1}, {13, 1},
				{12, 4}, {13, 4}, {12, 5}, {13, 5},
				// a line
				{2, 8}, {3, 8}, {4, 8}, {5, 8}, {6, 8}, 
				{7, 8}, {8, 8}, {9, 8}, {10, 8}, {11, 8}
			},
			{
				{0, 0}, {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, 
				{7, 0}, {8, 0}, {9, 0}, {10, 0}, {11, 0}, {12, 0}, {13, 0},
				
				{0, 2}, {1, 2}, {2, 2}, {3, 2}, {4, 2}, {5, 2}, {6, 2}, 
				{7, 2}, {8, 2}, {9, 2}, {10, 2}, {11, 2}, {12, 2}, {13, 2},
				
				{0, 4}, {1, 4}, {2, 4}, {3, 4}, {4, 4}, {5, 4}, {6, 4}, 
				{7, 4}, {8, 4}, {9, 4}, {10, 4}, {11, 4}, {12, 4}, {13, 4}
			}
			
	};
	
	public static final int[][][] INTRO = {
			{
				// B
				{5, 2}, {6, 2}, {7, 2},
				{5, 3}, {8, 3},
				{5, 4}, {8, 4},
				{5, 5}, {8, 5},
				{5, 6}, {6, 6}, {7, 6},
				{5, 7}, {8, 7},
				{5, 8}, {8, 8},
				{5, 9}, {8, 9},
				{5, 10}, {6, 10}, {7, 10}
			},
			
			{
				// R
				{5, 2}, {6, 2}, {7, 2},
				{5, 3}, {8, 3},
				{5, 4}, {8, 4},
				{5, 5}, {8, 5},
				{5, 6}, {6, 6}, {7, 6},
				{5, 7}, {8, 7},
				{5, 8}, {8, 8},
				{5, 9}, {8, 9},
				{5, 10}, {8, 10}
			},
			
			{
				// E
				{5, 2}, {6, 2}, {7, 2}, {8, 2},
				{5, 3},
				{5, 4},
				{5, 5},
				{5, 6}, {6, 6}, {7, 6},
				{5, 7},
				{5, 8},
				{5, 9},
				{5, 10}, {6, 10}, {7, 10}, {8, 10}
			},
			
			{
				// A
				{6, 2}, {7, 2},
				{5, 3}, {8 ,3},
				{5, 4}, {8 ,4},
				{5, 5}, {8 ,5},
				{5, 6}, {6, 6}, {7, 6}, {8, 6},
				{5, 7}, {8 ,7},
				{5, 8}, {8 ,8},
				{5, 9}, {8 ,9},
				{5, 10}, {8, 10}
			},
			
			{
				// K
				{5, 2}, {8, 2},
				{5, 3}, {8 ,3},
				{5, 4}, {7 ,4},
				{5, 5}, {7 ,5},
				{5, 6}, {6, 6},
				{5, 7}, {7 ,7},
				{5, 8}, {7 ,8},
				{5, 9}, {8 ,9},
				{5, 10}, {8, 10}
			},
			
			{
				// O
				{6, 2}, {7, 2},
				{5, 3}, {8 ,3},
				{5, 4}, {8 ,4},
				{5, 5}, {8 ,5},
				{5, 6}, {8, 6},
				{5, 7}, {8 ,7},
				{5, 8}, {8 ,8},
				{5, 9}, {8 ,9},
				{6, 10}, {7, 10}
			},
			
			{
				// U
				{5, 2}, {8, 2},
				{5, 3}, {8 ,3},
				{5, 4}, {8 ,4},
				{5, 5}, {8 ,5},
				{5, 6}, {8, 6},
				{5, 7}, {8 ,7},
				{5, 8}, {8 ,8},
				{5, 9}, {8 ,9},
				{6, 10}, {7, 10}
			},
			
			{
				// T
				{5, 2}, {6, 2}, {7, 2}, {8, 2}, {9, 2},
				{7 ,3},
				{7 ,4},
				{7 ,5},
				{7, 6},
				{7 ,7},
				{7 ,8},
				{7 ,9},
				{7, 10}
				
			}

	};
	
	public static final int[][][] GAMEOVER = {
			{
				// G
				{6, 2}, {7, 2}, {8, 2},
				{5, 3},
				{5, 4},
				{5, 5},
				{5, 6}, {7, 6}, {8, 6},
				{5, 7}, {8, 7},
				{5, 8}, {8, 8},
				{5, 9}, {8, 9},
				{6, 10}, {7, 10}
			},
			
			{
				// A
				{6, 2}, {7, 2},
				{5, 3}, {8 ,3},
				{5, 4}, {8 ,4},
				{5, 5}, {8 ,5},
				{5, 6}, {6, 6}, {7, 6}, {8, 6},
				{5, 7}, {8 ,7},
				{5, 8}, {8 ,8},
				{5, 9}, {8 ,9},
				{5, 10}, {8, 10}
			},
			
			{
				// M
				{5, 2}, {6, 2}, {8, 2}, {9, 2},
				{5, 3}, {7, 3}, {9, 3},
				{5, 4}, {7, 4}, {9, 4},
				{5, 5}, {9, 5},
				{5, 6}, {9, 6},
				{5, 7}, {9, 7},
				{5, 8}, {9, 8},
				{5, 9}, {9, 9},
				{5, 10}, {9, 10}
			},
			
			{
				// E
				{5, 2}, {6, 2}, {7, 2}, {8, 2},
				{5, 3},
				{5, 4},
				{5, 5},
				{5, 6}, {6, 6}, {7, 6},
				{5, 7},
				{5, 8},
				{5, 9},
				{5, 10}, {6, 10}, {7, 10}, {8, 10}
			},
			
			{
				// O
				{6, 2}, {7, 2},
				{5, 3}, {8 ,3},
				{5, 4}, {8 ,4},
				{5, 5}, {8 ,5},
				{5, 6}, {8, 6},
				{5, 7}, {8 ,7},
				{5, 8}, {8 ,8},
				{5, 9}, {8 ,9},
				{6, 10}, {7, 10}
			},
			
			{
				// V
				{5, 2}, {8, 2},
				{5, 3}, {8 ,3},
				{5, 4}, {8 ,4},
				{5, 5}, {8 ,5},
				{5, 6}, {8, 6},
				{5, 7}, {8 ,7},
				{5, 8}, {8 ,8},
				{6, 9}, {7 ,9},
				{6, 10}, {7, 10}
				
			},
			
			{
				// E
				{5, 2}, {6, 2}, {7, 2}, {8, 2},
				{5, 3},
				{5, 4},
				{5, 5},
				{5, 6}, {6, 6}, {7, 6},
				{5, 7},
				{5, 8},
				{5, 9},
				{5, 10}, {6, 10}, {7, 10}, {8, 10}
			},
			
			{
				// R
				{5, 2}, {6, 2}, {7, 2},
				{5, 3}, {8, 3},
				{5, 4}, {8, 4},
				{5, 5}, {8, 5},
				{5, 6}, {6, 6}, {7, 6},
				{5, 7}, {8, 7},
				{5, 8}, {8, 8},
				{5, 9}, {8, 9},
				{5, 10}, {8, 10}
			}
			

	};
	
	public static final int[][][] YOU_WIN = {
			{
				//Y
				{5, 2}, {9, 2},
				{5, 3}, {9 ,3},
				{5, 4}, {9 ,4},
				{6, 5}, {8 ,5},
				{6, 6}, {8, 6},
				{7, 7},
				{7, 8},
				{7 ,9},
				{7, 10}
			},
			
			{
				//O
				{6, 2}, {7, 2},
				{5, 3}, {8 ,3},
				{5, 4}, {8 ,4},
				{5, 5}, {8 ,5},
				{5, 6}, {8, 6},
				{5, 7}, {8 ,7},
				{5, 8}, {8 ,8},
				{5, 9}, {8 ,9},
				{6, 10}, {7, 10}
			},
			
			{
				//U
				{5, 2}, {8, 2},
				{5, 3}, {8 ,3},
				{5, 4}, {8 ,4},
				{5, 5}, {8 ,5},
				{5, 6}, {8, 6},
				{5, 7}, {8 ,7},
				{5, 8}, {8 ,8},
				{5, 9}, {8 ,9},
				{6, 10}, {7, 10}
			},
			
			/*{
				//Whitespace
				
			},*/
			
			{
				//W
				{5, 2}, {9, 2},
				{5, 3}, {9 ,3},
				{5, 4}, {9 ,4},
				{5, 5}, {9 ,5},
				{5, 6}, {9, 6},
				{5, 7}, {9 ,7},
				{5, 8}, {7, 8}, {9 ,8},
				{5 ,9}, {7, 9}, {9, 9},
				{6, 10}, {8, 10}
				
			},
			
			{
				//I
				{7, 2},
				{7, 3}, 
				{7, 4}, 
				{7, 5}, 
				{7, 6}, 
				{7, 7}, 
				{7, 8}, 
				{7 ,9}, 
				{7, 10}
				
			},
			
			{
				//N
				{5, 2}, {8, 2},
				{5, 3}, {8 ,3},
				{5, 4}, {6, 4}, {8 ,4},
				{5, 5}, {6, 5}, {8 ,5},
				{5, 6}, {6, 6}, {8, 6},
				{5, 7}, {7, 7}, {8 ,7},
				{5, 8}, {7, 8}, {8 ,8},
				{5 ,9}, {7, 9}, {8, 9},
				{5, 10}, {8, 10}
				
			}
	
			
	};
	
}
