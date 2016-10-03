import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class Board extends JPanel implements MouseInputListener {
	public Cell[][] cells;
	public Cell_2[][] cells2;
	public GenCell[][] display;
	private int size; //these are the numbers of cells in the board, NOT the graphical dimensions of the board
	private static final int EXTRA_BOARD_SPACE = 50;
	private Color c;
	public Color polarity;
	private int cellSize;
	private Agent[] agents;
	
	public Board(int width, int height, int size, int typeBoard) {
		//set preferred graphical dimensions of the board
		setPreferredSize(new Dimension(width, height));
		//HOW DID I FORGET THIS EARLIER
		this.size = size;
		display = new GenCell[size*2][size];
		//set the graphical dimensions of the cells themselves
		//the cells are always square, but the space they take up is constrained by the width and height of the board
		//and by the number of cells.
		cellSize = ((width/2)-2*EXTRA_BOARD_SPACE)/size; //board space in the middle?
		int agentSize = (int)(cellSize*0.7);
		//gap between the two changes depending on if the size is 100 vs 5
		
		//layer 1
		cells = new Cell[size][size];
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				if (typeBoard==0)
				{
					int rand = (int) (Math.random()*2);
					if (rand == 0)
					{
						c = Color.black;
					}
					else
					{
						c = Color.white;
					}
					cells[row][col] = new Cell(EXTRA_BOARD_SPACE+row*cellSize, EXTRA_BOARD_SPACE+col*cellSize, cellSize, c);
				}
				else if (typeBoard==1)
				{
					if (row%2 == col%2)
					{
						c = Color.black;
					}
					else
					{
						c = Color.white;
					}
					cells[row][col] = new Cell(EXTRA_BOARD_SPACE+row*cellSize, EXTRA_BOARD_SPACE+col*cellSize, cellSize, c);

				}

			}

		}
		
		//generate the swarm; kirsch suggested, say, 30 agents, so we're trying 10 right now
		//we've tried moving the swarm agents every frame with mousedragged... it can handle at least 500 with no
		//visible slowdown.
		agents = new Agent[500];
		for (int i = 0; i < agents.length; i++) {
			//these generate in a random spot on the board itself, with a random vector that makes no effing sense yet
			agents[i] = new Agent((int)(EXTRA_BOARD_SPACE+Math.random()*width-2*EXTRA_BOARD_SPACE), (int)(EXTRA_BOARD_SPACE+Math.random()*width-2*EXTRA_BOARD_SPACE), agentSize, new Point2D.Double(Math.random()*10-5, Math.random()*10-5));
		}
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		//In regards to polarity, a checker board with black in the top left is red, the other is blue.
		if(cells[0][0].getColor()==Color.BLACK)
		{
			polarity = Color.RED;
		}
		else
		{
			polarity = Color.BLUE;
		}
		
		//layer 2 initial construction
		layer2(polarity);
		
	}
	
	protected void layer2(Color polarity)
	{

		cells2 = new Cell_2[size][size];


		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {

				if(polarity == Color.RED)
					//if the top left is black
				{
					if(cells[row][col].getColor() == Color.BLACK)
						//if the layer 1 cell is black
					{
						if(col%2 == row%2)
							//if its in a spot that should be black
						{
							cells2[row][col] = new Cell_2(800+EXTRA_BOARD_SPACE+row*cellSize, EXTRA_BOARD_SPACE+col*cellSize, cellSize, Color.RED);
							//then you are the same polarity as cell[0][0]
						}
						else
							//if its in a spot that SHOULDN'T be black
						{
							cells2[row][col] = new Cell_2(800+EXTRA_BOARD_SPACE+row*cellSize, EXTRA_BOARD_SPACE+col*cellSize, cellSize, Color.BLUE);
							//then you are in the opposite polarity than cells[0][0]
						}
					}
					else
						//if the layer 1 cell is white
					{
						if(col%2 == row%2)
							//if its in a spot that SHOULDN'T be 
						{
							cells2[row][col] = new Cell_2(800+EXTRA_BOARD_SPACE+row*cellSize, EXTRA_BOARD_SPACE+col*cellSize, cellSize, Color.BLUE);
							// then its in the opposite polarity than cells[0]
						}
						else
							//if its in a spot that should be white
						{
							cells2[row][col] = new Cell_2(800+EXTRA_BOARD_SPACE+row*cellSize, EXTRA_BOARD_SPACE+col*cellSize, cellSize, Color.RED);
							//then its in the same polarity as cells[0][0]
						}
					}
				}
				else
				{
					if(cells[row][col].getColor() == Color.WHITE)
					{
						if(col%2 == row%2)
						{
							cells2[row][col] = new Cell_2(800+EXTRA_BOARD_SPACE+row*cellSize, EXTRA_BOARD_SPACE+col*cellSize, cellSize, Color.RED);
						}
						else
						{
							cells2[row][col] = new Cell_2(800+EXTRA_BOARD_SPACE+row*cellSize, EXTRA_BOARD_SPACE+col*cellSize, cellSize, Color.BLUE);
						}
					}
					else
					{
						if(col%2 == row%2)
						{
							cells2[row][col] = new Cell_2(800+EXTRA_BOARD_SPACE+row*cellSize, EXTRA_BOARD_SPACE+col*cellSize, cellSize, Color.BLUE);
						}
						else
						{
							cells2[row][col] = new Cell_2(800+EXTRA_BOARD_SPACE+row*cellSize, EXTRA_BOARD_SPACE+col*cellSize, cellSize, Color.RED);
						}
					}
				}
			}
		}
	}
	
	protected void paintComponent(Graphics arg0) {
		super.paintComponent(arg0);
		
		Graphics2D g = (Graphics2D)arg0;
		
		for (int row = 0; row < size*2; row++) {
			for (int col = 0; col <size; col++) {
				if (row<size)
				{
					display[row][col] = cells[row][col];
				}
				else
				{
					display[row][col] = cells2[row-size][col];
				}
				display[row][col].draw(g);
			}
		}
		
//		for (int row = 0; row < cells2.length; row++) {
//			for (int col = 0; col < cells2[row].length; col++) {
//				cells2[row][col].draw(g2);
//			}
//		}
		
		for (int i = 0; i < agents.length; i++) {
			agents[i].draw(g);
		}
	}
	
	public void step() {
		//initialize temp array of colors
		Color[][] result = new Color[size][size];
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				int tally = 0;
				//check how many neighbors are alive
				for (Cell n : getNeighbors(cells, row, col)) {
					//the array may have nulls in it ;)
					if (n != null) {
						if (n.getColor() == Color.BLACK) {
							tally++;
						}
					}
				}
				//if three of your neighbors are alive, you come alive or stay alive,
				//or if two of your neighbors are alive and you're alive, you stay alive,
				//otherwise you die
				if (tally == 3 || (tally == 2 && cells[row][col].getColor() == Color.BLACK)) {
					//result[row][col] = new Cell(cells[row][col].getX(), cells[row][col].getY(), cells[row][col].getWidth(), Color.BLACK);
					result[row][col] = Color.BLACK;
				}
				else {
					//result[row][col] = new Cell(cells[row][col].getX(), cells[row][col].getY(), cells[row][col].getWidth(), Color.WHITE);
					result[row][col] = Color.WHITE;
				}
			}
		}
		
		//populate the old array with the results of the temp array
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				cells[row][col].setColor(result[row][col]);
			}
		}
		layer2(polarity);
		
		//move the agents too!
		for (Agent agent : agents) {
			agent.step();
		}
	}
	
	public Cell[] getNeighbors(Cell[][] cells, int rowNum, int colNum) {
		//each cell only has 8 neighbors! for now at least.... :(
		Cell[] neighbors = new Cell[8];
		//this is pretty cool
		int rowMax = cells.length-1;
		int colMax = cells[rowMax-1].length-1;
		
		//THIS IS AWFUL AND NEEDS SO MUCH REVISION IT'S NOT EVEN FUNNY
		
		//top left
		if (rowNum == 0 && colNum == 0) {
			neighbors[4] = cells[rowNum][colNum+1];
			neighbors[6] = cells[rowNum+1][colNum];
			neighbors[7] = cells[rowNum+1][colNum+1];
		}
		
		//bottom left
		if (rowNum == rowMax && colNum == 0) {
			neighbors[1] = cells[rowNum-1][colNum];
			neighbors[2] = cells[rowNum-1][colNum+1];
			neighbors[4] = cells[rowNum][colNum+1];
		}
		
		//top right
		if (rowNum == 0 && colNum == cells[0].length-1) {
			neighbors[3] = cells[rowNum][colNum-1];
			neighbors[5] = cells[rowNum+1][colNum-1];
			neighbors[6] = cells[rowNum+1][colNum];
		}
		
		//bottom right
		if (rowNum == rowMax && colNum == colMax) {
			neighbors[0] = cells[rowNum-1][colNum-1];
			neighbors[1] = cells[rowNum-1][colNum];
			neighbors[3] = cells[rowNum][colNum-1];
		}
		
		//top
		if (rowNum == 0 && colNum > 0 && colNum < colMax) {
			neighbors[3] = cells[rowNum][colNum-1];
			neighbors[4] = cells[rowNum][colNum+1];
			neighbors[5] = cells[rowNum+1][colNum-1];
			neighbors[6] = cells[rowNum+1][colNum];
			neighbors[7] = cells[rowNum+1][colNum+1];
		}
		
		//bottom
		if (rowNum == rowMax && colNum > 0 && colNum < colMax) {
			neighbors[0] = cells[rowNum-1][colNum-1];
			neighbors[1] = cells[rowNum-1][colNum];
			neighbors[2] = cells[rowNum-1][colNum+1];
			neighbors[3] = cells[rowNum][colNum-1];
			neighbors[4] = cells[rowNum][colNum+1];
		}
		
		//left
		if (rowNum > 0 && rowNum < rowMax && colNum == 0) {
			neighbors[1] = cells[rowNum-1][colNum];
			neighbors[2] = cells[rowNum-1][colNum+1];
			neighbors[4] = cells[rowNum][colNum+1];
			neighbors[6] = cells[rowNum+1][colNum];
			neighbors[7] = cells[rowNum+1][colNum+1];
		}
		
		//right
		if (rowNum > 0 && rowNum < rowMax && colNum == colMax) {
			neighbors[0] = cells[rowNum-1][colNum-1];
			neighbors[1] = cells[rowNum-1][colNum];
			neighbors[3] = cells[rowNum][colNum-1];
			neighbors[5] = cells[rowNum+1][colNum-1];
			neighbors[6] = cells[rowNum+1][colNum];
		}
		
		//middle cells obviously get everything
		if (rowNum > 0 && rowNum < rowMax && colNum > 0 && colNum < colMax) {
			neighbors[0] = cells[rowNum-1][colNum-1];
			neighbors[1] = cells[rowNum-1][colNum];
			neighbors[2] = cells[rowNum-1][colNum+1];
			neighbors[3] = cells[rowNum][colNum-1];
			neighbors[4] = cells[rowNum][colNum+1];
			neighbors[5] = cells[rowNum+1][colNum-1];
			neighbors[6] = cells[rowNum+1][colNum];
			neighbors[7] = cells[rowNum+1][colNum+1];
		}
		
		return neighbors;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//i commented this out for now so you can test the step procedure by clicking
		//the behavior is boring atm because checkerboard is a bad starting pattern (almost everything
		//has four neighbors, so everything dies immediately, except the border cells, which all
		//spring alive)
//		for (int row = 0; row < cells.length; row++) {
//			for (int col = 0; col < cells[row].length; col++) {
//				if (cells[row][col].contains(arg0.getPoint())) {
//					cells[row][col].flipColor();
//				}
//			}
//		}
		step();
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
		//for each agent, have the agent decide randomly whether to flip its cell's color
		for (Agent agent : agents) {
			if (Math.random() < 0.1) {
				//to decide which cell the agent is in... this is bad :( need to possibly flip it around, or decide
				//which cell each agent is in each time they move, in fact this could be determined using each agent's
				//x and y rather than searching all the cells... but this should be runnable for now
				for (int row = 0; row < cells.length; row++) {
					for (int col = 0; col < cells[row].length; col++) {
						if (cells[row][col].contains(agent.getCenterX(), agent.getCenterY())) {
							cells[row][col].flipColor();
						}
					}
				}
			}
		}

		//This is a temporary thing
		step();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
