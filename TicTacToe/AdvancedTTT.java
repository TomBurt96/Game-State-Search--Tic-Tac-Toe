package ttt;

import java.util.List;
import java.util.Scanner;

public class AdvancedTTT {
	TTT Boards[] = new TTT[9];
	Scanner scan = new Scanner(System.in);
	String playerChar, compChar;
	AdvState current;
	int boardNum;
	public void drawBoard(){
		for(int i = 0; i < 9; i++){
			Boards[i].drawBoard();
			System.out.println("");
		}
	}
	public void Playermove(){
		drawBoard();
		System.out.println("You have to place on board: " + current.position);
		System.out.println("What is your move?");
		int move = scan.nextInt();
		if(move < 0|| move > 8 ||Boards[current.position].TBoard[move] != null){
			System.out.println("This is not a legal move.");
			Playermove();
		}
		Boards[current.position].TBoard[move] = playerChar;
		current.boards = Boards;
		current.position = move;
		if(checkAdvTerm(current, compChar)){
			drawBoard();
			System.out.println("The computer has won");
		}else if(checkAdvTerm(current, playerChar)){
			drawBoard();
			System.out.println("The player has won");
		}
		else if(checkAdvDraw(current) == true){
			drawBoard();
			System.out.println("The game is a draw");
		}else{
			AIMove();
		}
	}
	public void AIMove(){
		AdvState temp = current;
		double val = -1000.0;
		List<AdvState> list = temp.generteSucc(current, compChar, playerChar, temp.position);
		for(AdvState s : list){
			double potential = HMin(s, 1, -100.0, 100.0);
			System.out.println("action: "+potential);
			if(val < potential){
				val = potential;
				temp = s;
			}
			Boards = temp.boards;
			current = temp;
		}
		System.out.println("value: "+ val);
		current.position = temp.position;
		if(checkAdvTerm(current, compChar)){
			drawBoard();
			System.out.println("The computer has won");
		}else if(checkAdvTerm(current, playerChar)){
			drawBoard();
			System.out.println("The player has won");
		}
		else if(checkAdvDraw(current) == true){
			drawBoard();
			System.out.println("The game is a draw");
		}
		else{
			Boards = temp.boards;
			current = temp;
			Playermove();
		}
	}
	public void setupBoards(){
		for(int i = 0; i < 9; i++){
			Boards[i] = new TTT();
		}
	}
	public void StartGame(){
		setupBoards();
		System.out.println("Welcome to Advanced Tic Tac Toe");
		System.out.println("Would you like to be X or O");
		playerChar = scan.nextLine();
		if(playerChar.equals("X")){
			compChar = "O";
			current = new AdvState(playerChar, Boards, boardNum);
			System.out.println("What board do you want to place on?");
			boardNum = scan.nextInt();
			current = new AdvState(playerChar, Boards, boardNum);
			Playermove();
		}else{
			compChar = "X";
			current = new AdvState(compChar, Boards, 0);
			boardNum = 0;
			AIMove();
		}
	}
	public double HMax(AdvState state, int depth, double alpha, double beta){
		if(checkAdvTerm(state, compChar)|| checkAdvTerm(state, playerChar) ||checkAdvDraw(state)|| depth == 4){
			return getUtil(state);
		}else{
			double ut = -100.0;
			for(AdvState s: state.generteSucc(state, compChar, playerChar, state.position)){
				double temp = HMin(s, depth+1, alpha, beta);
				if(temp > ut){
					ut = temp;
				}
				if(ut >= beta){
					return ut;
				}
				if(alpha > ut){
					alpha = ut;
				}
			}
			return ut;
		}
	}
	public double HMin(AdvState state, int depth, double alpha, double beta){
		if(checkAdvTerm(state, compChar)|| checkAdvTerm(state, playerChar) ||checkAdvDraw(state)|| depth == 4){
			return getUtil(state);
		}else{
			double ut = 100.0;
			for(AdvState s: state.generteSucc(state, playerChar, compChar, state.position)){
				double temp = HMax(s, depth+1, alpha, beta);
				if(temp < ut){
					ut = temp;
				}
				if(ut <= alpha){
					return ut;
				}
				if(beta < ut){
					beta = ut;
				}
			}
			return ut;
		}
	}
	public boolean checkAdvTerm(AdvState state, String player){
		boolean terminal = false;
		for(int i = 0; i < 9; i++){
			if(state.boards[i].checkTerm(new State(state.player, state.boards[i].TBoard), player)){
				terminal = true;
			}
		}
		return terminal;
	}
	public boolean checkAdvDraw(AdvState state){
		boolean draw = false;
		for(int i = 0; i < 9; i++){
			if(state.boards[i].checkDraw(new State(state.player, state.boards[i].TBoard))){
				draw = true;
			}
		}
		return draw;
	}
	public double getUtil(AdvState s){
		double util = 0.0;
		if(checkAdvTerm(s, compChar)){
			util = 1000.0;
		}
		else if(checkAdvTerm(s, playerChar)){
			util = -1000.0;
		}
		else if(checkAdvDraw(s)){
			util = 0.0;
		}
		else{
			TTT t[] = s.boards;
			for(int i = 0; i < 9; i++){
				util += boardVal(t[i],compChar,playerChar);
				util -= boardVal(t[i],playerChar,compChar);
			}
		}
		return util;
	}
	public double boardVal(TTT t, String max, String opponent){
		double sum = 0.0;
		if(t.TBoard[0] == max && t.TBoard[1] != opponent && t.TBoard[2] != opponent){
			sum += 5.0;
		}if(t.TBoard[0] == max && t.TBoard[3] != opponent && t.TBoard[6] != opponent){
			sum += 5.0;
		}if(t.TBoard[0] == max && t.TBoard[4] != opponent && t.TBoard[8] != opponent){
			sum += 5.0;
		}if(t.TBoard[1] == max && t.TBoard[4] != opponent && t.TBoard[7] != opponent){
			sum += 5.0;
		}if(t.TBoard[2] == max && t.TBoard[5] != opponent && t.TBoard[8] != opponent){
			sum += 5.0;
		}if(t.TBoard[2] == max && t.TBoard[4] != opponent && t.TBoard[6] != opponent){
			sum += 5.0;
		}if(t.TBoard[3] == max && t.TBoard[4] != opponent && t.TBoard[5] != opponent){
			sum += 5.0;
		}if(t.TBoard[6] == max && t.TBoard[7] != opponent && t.TBoard[8] != opponent){
			sum += 5.0;
		}
		if(t.TBoard[0] == max && t.TBoard[1] == max && t.TBoard[2] != opponent){
			sum += 10.0;
		}if(t.TBoard[0] == max && t.TBoard[3] == max && t.TBoard[6] != opponent){
			sum += 10.0;
		}if(t.TBoard[0] == max && t.TBoard[4] == max && t.TBoard[8] != opponent){
			sum += 10.0;
		}if(t.TBoard[1] == max && t.TBoard[4] == max && t.TBoard[7] != opponent){
			sum += 10.0;
		}if(t.TBoard[2] == max && t.TBoard[5] == max && t.TBoard[8] != opponent){
			sum += 10.0;
		}if(t.TBoard[2] == max && t.TBoard[4] == max && t.TBoard[6] != opponent){
			sum += 10.0;
		}if(t.TBoard[3] == max && t.TBoard[4] == max && t.TBoard[5] != opponent){
			sum += 10.0;
		}if(t.TBoard[6] == max && t.TBoard[7] == max && t.TBoard[8] != opponent){
			sum += 10.0;
		}
		if(t.TBoard[0] == max && t.TBoard[1] != opponent && t.TBoard[2] == max){
			sum += 10.0;
		}if(t.TBoard[0] == max && t.TBoard[3] != opponent && t.TBoard[6] == max){
			sum += 10.0;
		}if(t.TBoard[0] == max && t.TBoard[4] != opponent && t.TBoard[8] == max){
			sum += 10.0;
		}if(t.TBoard[1] == max && t.TBoard[4] != opponent && t.TBoard[7] == max){
			sum += 10.0;
		}if(t.TBoard[2] == max && t.TBoard[5] != opponent && t.TBoard[8] == max){
			sum += 10.0;
		}if(t.TBoard[2] == max && t.TBoard[4] != opponent && t.TBoard[6] == max){
			sum += 10.0;
		}if(t.TBoard[3] == max && t.TBoard[4] != opponent && t.TBoard[5] == max){
			sum += 10.0;
		}if(t.TBoard[6] == max && t.TBoard[7] != opponent && t.TBoard[8] == max){
			sum += 10.0;
		}
		return sum;
	}
}
