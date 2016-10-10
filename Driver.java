/*		Author: Zak Gray, Nick Corrado and Tim Dobeck
 * 		Description: This is the class to kick start the program to run. It is creating the instance of the Board to be called depending on whatever parameters
 * 					 the user types in. The first input determines the size of the board (x by x) and the second input determines if the board will be a 
 * 					 checkerboard or random. This keeps looping through until there is a valid response. The try/catch block is for catching error in the responses
 * 					 We were torn between using an IllegalArgumentExceptiona and a TypeMismatchException because we need to catch both so that is why we left Exception()
 */

import java.util.InputMismatchException;
import java.util.Scanner;

import javax.swing.JFrame;

public class Driver {

	public static boolean validResponse = false;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		while (validResponse==false) //keep looping through until repsonse is valid, then draw it
		{
			try
			{
				Scanner sc = new Scanner(System.in);
				System.out.print("Enter the number for the size of the board: ");
				int input = sc.nextInt();
				System.out.println("Please indicate if you wish to start with a checkerboard by typing \"1\" or start randomly by typing \"0\": ");
				int typeBoard = sc.nextInt();
				if ((typeBoard != 0 && typeBoard != 1) || input < 1 || input > 500) //500 should be the very maximum of what the board size will be, even then, it is super slow to update
				{
					throw new Exception(); //We were torn between using an IllegalArgumentExceptiona and a TypeMismatchException because we need to catch both so that is why we left Exception();
				}
				else
				{
					validResponse = true;
				}
				//as Tim noted, we should have a try/catch here, but the long term goal is not to need the scanner actually
				sc.close();

				Board board = new Board(1800, 800, input, typeBoard);
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(board);
				frame.pack();
				frame.setVisible(true);
			}
			catch (Exception ex) 
			{
				System.out.println("Invalid response");
			}
		}
	}
}
