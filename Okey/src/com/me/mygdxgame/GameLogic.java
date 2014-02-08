/************************************************************
 * Game logic is the core class of the game.
 * - Make placement computations
 * - Make sorting
 * - Make replacement after making series and pairs
 * 
 */
package com.me.mygdxgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.me.mygdxgame.objects.Rack;

public class GameLogic {
	
	public MyGdxGame game;
	private Rack[][] rackPacks; // holds 106 racks of the game
	public ArrayList<Rack> racks; // holds 15 racks for 1 player
	private boolean initialized = false; // has the game initialized?
	
	private int[][] board; // holds empty and occupied locations on the board
	
	public GameLogic(MyGdxGame game){
		this.game = game;
	
		// 2 for red 1 to 13
		// 2 for blue 1 to 13
		// 2 for green 1 to 13
		// 2 for black 1 to 13
		// 1 for 2 fake okey
		rackPacks = new Rack[9][13];
		racks = new ArrayList<Rack>(Constants.MAX_NB_SELECTED_RACKS);
		
		// making the board empty
		board = new int[2][17];
		for(int r = 0; r < 2; r++){
			for(int c = 0; c < 17; c++){
				board[r][c] = -1;
			}
		}
		
		generateRacks();	
		getRandomRacks();
	}
	
	public void dispose(){
		rackPacks = null;
		racks = null;
		board = null;
		series = null;
		rowCol = null;
	}
	
	/*********************************************************************************************
	 * Move a series of racks while dragging 
	 * @author yy
	 ********************************************************************************************/
	public void moveSeries(float x, float y){

		float movePos = game.getRackWidth();
		if(moveLeft)	movePos = -1*movePos;
		
		for(int i = 0; i < nbOfSeries; i++){
			move(racks.get(series[i]).okIndex, x + (movePos)*i, y);
		}
	
	}
	
	/*********************************************************************************************
	 * Place a series of racks when user dropped, for more information look place method 
	 * @author yy
	 ********************************************************************************************/
	public void placeSeries(float x, float y){
		int total = 0;
		
		float movePos = game.getRackWidth();
		if(moveLeft)	movePos = -1*movePos;
		for(int i = 0; i < nbOfSeries; i++ ){
			findSelectedRowCol(series[i], x + (movePos)*i, y);
			racks.get(series[i]).moving = false;
			
			if(rowCol[0] != -1 && rowCol[1] != -1){
				racks.get(series[i]).setRow(rowCol[0]);
				racks.get(series[i]).setColumn(rowCol[1]);
				//updateBoard();
		
				total++;
			}
		}
		
		if(total != nbOfSeries){
			for(int i = 0; i < nbOfSeries; i++)
				racks.get(series[i]).setPosition(racks.get(series[i]).oldPosition.x, racks.get(series[i]).oldPosition.y);
		}
		updateBoard();
	}

	private boolean moveLeft = false;
	public int[] series = new int[15];
	int nbOfSeries;
	public boolean haveSeries(int index){
		for(int i = 0; i < 15; i++)
			series[i] = -1;
		nbOfSeries = 0;
		
		int row = racks.get(index).getRow();
		int col = racks.get(index).getColumn();
	
		int before = -1, after = -1;
		//printBoard();
		
		if(col - 1 >= 0)	before = board[row][col-1];
		if(col +1 < 17)		after = board[row][col+1];
		
		//System.out.println("Column: "+ col);
		//System.out.println("Before: "+ before+" After: "+after);
		
		if((after != -1 && before != -1) ||(after == -1 && before == -1)){
			return false;
		}
		
		// if before == -1 (start from left)
		if(before == -1){
			moveLeft = false;
			//System.out.println("Before");
			int end = findEnd(row, col--);
			//System.out.println("END: "+ end);
			if(checkSeries())	return true;
		}
	
		// if after == -1 (start from right)
		if(after == -1){
			moveLeft = true;
			//System.out.println("After");
			int start = findStart(row, col++);
			//System.out.println("START: "+ start+" "+after);
			if(checkSeries())	return true;
		}
		return false;
	}
	
	/*********************************************************************************************
	 * Check whether selected rack is a member of a series
	 * @author yy
	 ********************************************************************************************/
	private boolean checkSeries(){
		
		//System.out.println("Check series");
		/*for(int i = 0; i < nbOfSeries; i++){
	
			System.out.print(series[i]+" "); 
		}
		System.out.println();
		System.out.println("Series len:"+nbOfSeries);*/
		
		Rack rack1, rack2;
		
		// first check color series
		int total = 0;
		for (int i = 0; i < nbOfSeries-1; i++) {
			
			rack1 = racks.get(series[i]);
			rack2 = racks.get(series[i+1]);
			//System.out.println("Compare "+rack1.number+" with "+rack2.number);
		    if (rack1.color != rack2.color && rack1.number == rack2.number) {
		    	//System.out.println("different color, same number");
		    	total++;
		    }
		}
		
		total++;
		//System.out.println("Total:"+total);
		if(total == nbOfSeries) return true;
		
		total = 0;
		// first check number series-ascending
		for (int i = 0; i < nbOfSeries-1; i++) {
			
			rack1 = racks.get(series[i]);
			rack2 = racks.get(series[i+1]);
			
			if (rack1.color == rack2.color && rack1.number + 1 == rack2.number) {
				//System.out.println("same color, asc order");
				total++;
		    }
		}
		total++;
		//System.out.println("Total:"+total);
		if(total == nbOfSeries) return true;
		
		total = 0;
		// first check number series-descending
		for (int i = 0; i < nbOfSeries-1; i++) {
			
			rack1 = racks.get(series[i]);
			rack2 = racks.get(series[i+1]);
			
			if (rack1.color == rack2.color && rack1.number == rack2.number + 1) {
				//System.out.println("same color, desc order");
				total++;
		    }
		}
		total++;
		//System.out.println("Total:"+total);
		if(total == nbOfSeries) return true;
		
		System.out.println();
		return false;
	}
	private int findEnd(int row, int index){
		int i;
		int j = 0;
		nbOfSeries = 0;
		for(i = index; i < 17; i++){
			
			if(j<15){
				series[j] = board[row][i];
				if(series[j] != -1)
					nbOfSeries++;
				j++;
			}
			
			if(board[row][i] == -1){
				return i;
			}
		}
		return i;
	}
	private int findStart(int row, int index){
		int i;
		int j = 0;
	
		for(i = index; i >= 0; i--){
			if(j<15){
				series[j] = board[row][i];
				if(series[j] != -1)
					nbOfSeries++;
				j++;
			}
			if(board[row][i] == -1){
				return i;
			}		
		}
		return i;
	}
	
	/*********************************************************************************************
	 * Move method gets the index of the rack and move the rack on the stage according to x and y
	 * coordinates
	 * @author yy
	 ********************************************************************************************/
	
	public void move(int index, float x, float y){
		// if rack is started to drag, hold the old position in order to
		// make rack attachable to the board if the user drag the rack
		// outside the board. If that situation is occured, rack is placed
		// to the old position.
		if(!racks.get(index).moving){
			// while moving, selected rack goes under the stable racks
			// by changind its depth, now the rack is ready to move
			racks.get(index).setZIndex(20);
			racks.get(index).moving = true;
			racks.get(index).oldPosition.x = racks.get(index).position.x;
			racks.get(index).oldPosition.y = racks.get(index).position.y;
		}
		
		racks.get(index).setPosition(x, y);
	}
	
	/*********************************************************************************************
	 * Put method is used when dragging is over. This methods finds the correct row and column
	 * that the rack is put. If the desired position is not suitable, rack is put on its 
	 * old position.
	 * @author yy
	 ********************************************************************************************/
	
	public void place(int index, float x, float y){
	
		findSelectedRowCol(index, x, y);
		racks.get(index).moving = false;
		
		if(rowCol[0] != -1 && rowCol[1] != -1){
			racks.get(index).setRow(rowCol[0]);
			racks.get(index).setColumn(rowCol[1]);
			updateBoard();
	
			return;
		}
			
		racks.get(index).setPosition(racks.get(index).oldPosition.x, racks.get(index).oldPosition.y);	
	}
	
	private int[] rowCol = new int[2];
	/*********************************************************************************************
	 * find selected row and column for putting the rack
	 ********************************************************************************************/
	private void findSelectedRowCol(int index, float x, float y){
		rowCol[0] = -1;// row
		rowCol[1] = -1;
		
		float yLimit = Gdx.graphics.getHeight() - game.getBoardHeight();
		// if y coordinate is outside the board regions, rack is placed
		// on its old position/
		if(y <  yLimit)	
			return;
		
		
		// decide column
		rowCol[0] = (y > yLimit + (game.getRackHeight() - 40 )*2)?1:0;	
		rowCol[1] = (int)(x / game.getRackWidth()); 
		//System.out.println("Mod "+ x % game.getRackWidth());
		
		// As there is maximum 17 columns on the board, if the calculated row
		// is greater than 16, rack is placed on its old position.
		if(rowCol[1] > 16){
			rowCol[1] = -1;
			return;
		}
		
		// If desired position is empty, put the rack on its new position
		if(board[rowCol[0]][rowCol[1]] == -1)
			return;
		
		
		/****************************************************************************************** 
		* If the player tried to put the rack between two racks, following algorithm is used:
		* 	1 - If there is an available position after the rack group, find the available index
		*		and move all racks that place after the selected index one column right.
		*   2 - If there is an available position before the rack group, find the available index
		*		and move all racks that place before the selected index one column left.
		******************************************************************************************/
		
		int ind1, ind2;// racks that user want to insert a rack btw them
		if( x % game.getRackWidth() < 30){
			
			ind1 = rowCol[1] - 1;
			ind2 = rowCol[1];
			
			rowCol[1] = ind2;
			// available in afterwards
			for(int i = ind2; i < 16; i++){
				if(board[rowCol[0]][i] == -1){
					//System.out.println("After Found pos "+ i);
					rowCol[1] = ind2;
					
					for(int m = ind2; m < i; m++){
						//System.out.println(m);
						int k = board[rowCol[0]][m];
						racks.get(k).setColumn(racks.get(k).getColumn()+1);
					}
					return;
				}
			}
			
			// available in backwards
			for(int i = ind1; i > 0; i--){
				
				if(board[rowCol[0]][i] == -1){
					
					//System.out.println("Backward Found pos "+ i);
					rowCol[1] = ind1;
					for(int m = ind1; m > i; m--){
						//System.out.println("Change "+ m);
						int k = board[rowCol[0]][m];
						racks.get(k).setColumn(racks.get(k).getColumn()-1);
					}
					
					return;
				}
			}	
		}
		
		// If the player tries to swap the rack with another rack:
		int rowTemp;
		int colTemp;
		int availableInd = board[rowCol[0]][rowCol[1]];
		
		if( availableInd != -1){
			rowTemp = racks.get(index).getRow();
			colTemp = racks.get(index).getColumn();
			
			racks.get(availableInd).setRow(rowTemp);
			racks.get(availableInd).setColumn(colTemp);

			return;
		}
	
	}
	/*********************************************************************************************
	 * updates the available and occupied positions of the board
	 ********************************************************************************************/
	private void updateBoard(){
		for(int r = 0; r < 2; r++){
			for(int c = 0; c < 17; c++){
				board[r][c] = -1;
			}
		}
		
		for(int i = 0; i < 15; i++){
			board[racks.get(i).getRow()][racks.get(i).getColumn()] = i; 	
		}
	}
	
	/*********************************************************************************************
	 * print available and occupied positions of the board
	 ********************************************************************************************/
	private void printBoard(){
		System.out.println("-----------------------------------------------------------------");
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 17; j++){
				if(board[i][j] != -1)
					System.out.print(racks.get(board[i][j]).number+" ");
				else{
					System.out.print("-1 ");
				}
			}
			System.out.println();
		}	
		System.out.println();
	}
	
	/*********************************************************************************************
	 * generate rack pack: 106 racks are available in the game
	 * RED, BLUE, GREEN and BLACK - 4 rack groups
	 * 2 for fake okey rack
	 ********************************************************************************************/
	public void generateRacks(){
		
		for(int row = 0; row < 9; row++){
			for(int col = 0; col < 13; col++){
				if(row < 2){
					rackPacks[row][col] = new Rack(this, Color.RED, col+1, game.debugMode);
				}else if(row < 4){
					rackPacks[row][col] = new Rack(this, Color.BLUE, col+1, game.debugMode);
				}else if(row < 6){
					rackPacks[row][col] = new Rack(this, Color.GREEN, col+1, game.debugMode);
				}else if(row < 8){
					rackPacks[row][col] = new Rack(this, Color.BLACK, col+1, game.debugMode);
				}else if(row < 9 && col < 2){
					rackPacks[row][col] = new Rack(this, Color.BLUE, 100, game.debugMode);
				}else{
					rackPacks[row][col] = null;
				}
			}	
		}
	}
	
	/*********************************************************************************************
	 * Make series 
	 * ******************************************************************************************/
	public void  makeSeries(){

		Collections.sort(racks);
	
		for(int i = 0; i < Constants.MAX_NB_SELECTED_RACKS; i++)
		{
		    racks.get(i).setColumn(i);
		    racks.get(i).setRow(0);
		    racks.get(i).okIndex = i;
		}
		
		updateBoard();
	}
	
	/*********************************************************************************************
	 * Reset rack
	 * ******************************************************************************************/
	public void resetRack(){
		
		for(int row = 0; row < 9; row++){
			for(int col = 0; col < 13; col++){
				if(rackPacks[row][col] != null)
					rackPacks[row][col].selected  = false;
			}
		}
		
		getRandomRacks();
		makeSeries();
		//updateAvailability();
		//printBoard();
		
	}
	
	/*********************************************************************************************
	 * Get 15 random racks from the generated rack pack.
	 * ******************************************************************************************/
	public void getRandomRacks(){

		int randomCol;
		int randomRow;
		Random generator = new Random();
	
		for(int i = 0; i < Constants.MAX_NB_SELECTED_RACKS; i++){
			randomCol = generator.nextInt(13); // 0 - 12
			randomRow = generator.nextInt(9);  // 0 - 8
			if(rackPacks[randomRow][randomCol] != null 
					&& !rackPacks[randomRow][randomCol].selected){
					
				if(initialized){
					rackPacks[randomRow][randomCol].selected = true;
					racks.get(i).setColumn(i);
					racks.get(i).setRow(0);
					racks.get(i).okIndex = 0;
					racks.get(i).selected = true;
					racks.get(i).moving = false;
					racks.get(i).number = rackPacks[randomRow][randomCol].number;
					racks.get(i).color = rackPacks[randomRow][randomCol].color;
					racks.get(i).oldPosition.x = 0;
					racks.get(i).oldPosition.y = 0;
					
				}else{
					rackPacks[randomRow][randomCol].setColumn(i);
					rackPacks[randomRow][randomCol].setRow(0);
					rackPacks[randomRow][randomCol].selected = true;
					rackPacks[randomRow][randomCol].okIndex = i;
					rackPacks[randomRow][randomCol].moving = false;
					racks.add(i, rackPacks[randomRow][randomCol]);
				}
				
				//System.out.println("col: "+i+" num: "+rackPacks[randomRow][randomCol].number);
			}else{
				i--;
			}
		}	
		updateBoard();
		initialized = true;
	}

	/*********************************************************************************************
	 * Make pairs
	 * ******************************************************************************************/
	public void  makePairs(){
		
		makeSeries();
		
		int[] pairs = new int[15];
		
		int pairLen = 0;
		boolean hasPair = false;
		Rack rack1, rack2;
		
		for(int i = 0; i < 15; i++){
			rack1 = racks.get(i);
			
			for(int j = 0; j < 15; j++){
				rack2 = racks.get(j);
			
				if(i !=j && rack1.selected && rack2.selected && rack1.equals(rack2)){
				   racks.get(j).selected = false;
				   pairs[pairLen] = j;
				   pairLen++;
				   hasPair = true;
				  
				}
				
			}
			if(hasPair){
				racks.get(i).selected = false;
				pairs[pairLen] = i;
				hasPair = false;
				pairLen++;
			}	
		}
		
		
		int ind = 0;
		for(int m = 0; m < pairLen; m++){
			racks.get(pairs[m]).selected = true;
			racks.get(pairs[m]).setRow(1);
			racks.get(pairs[m]).setColumn(ind);
			board[1][ind] = pairs[m];
			ind++;
		}
		
		// reposition
		int tmpIndex = 0;
		for(int i = 0; i < 15; i++){
			if(racks.get(i).getRow() != 1){
				racks.get(i).setColumn(tmpIndex);
				board[0][tmpIndex] = i;
				tmpIndex++;
			}		
		}
		updateBoard();
	}
		
}
