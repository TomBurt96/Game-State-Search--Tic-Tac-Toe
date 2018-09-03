package ttt;

import java.util.List;
import java.util.Scanner;

public class TTT {
	public String TBoard[] = new String[9];
	public State current;
	public String CompPlayer;
	public String personChar;
	Scanner scan = new Scanner(System.in);
	
	public TTT(){
		
	}
	public void drawBoard(){
		for(int i = 0; i < 9; i++){
			if(i == 0 || i == 3 || i == 6){
				System.out.println("");
			}
			if(TBoard[i]==null){
				System.out.print(Integer.toString(i));
			}else{
				System.out.print(TBoard[i]);
			}		
		}
		System.out.println("");
	}
	public boolean checkTerm(State s, String player){
		if(s.Board[0] == player && s.Board[0] == s.Board[1] && s.Board[1] == s.Board[2]){
			return true;
		}if(s.Board[3] == player && s.Board[3] == s.Board[4] && s.Board[4] == s.Board[5]){
			return true;
		}if(s.Board[6] == player && s.Board[6] == s.Board[7] && s.Board[7] == s.Board[8]){
			return true;
		}if(s.Board[0] == player && s.Board[0] == s.Board[3] && s.Board[3] == s.Board[6]){
			return true;
		}if(s.Board[1] == player && s.Board[1] == s.Board[4] && s.Board[4] == s.Board[7]){
			return true;
		}if(s.Board[2] == player && s.Board[2] == s.Board[5] && s.Board[5] == s.Board[8]){
			return true;
		}if(s.Board[0] == player && s.Board[0] == s.Board[4] && s.Board[4] == s.Board[8]){
			return true;
		}if(s.Board[2] == player && s.Board[2] == s.Board[4] && s.Board[4] == s.Board[6]){
			return true;
		}
		return false;
	}
	public boolean checkDraw(State state){
		boolean truth = true;
		for(int i = 0; i < 9; i++){
			if(state.Board[i] == null){
				truth = false;
			}
		}
		return truth;
	}
	public void AImove(){
		State temp = current;
		double val = -10.0;
		List<State> list = temp.generteSucc(current, CompPlayer, personChar);
		for(State s : list){
			double potential = Min(s);
			if(val < potential){
				val = potential;
				temp = s;
			}
			TBoard = temp.Board;
			current = temp;
		}
		if(checkTerm(current, CompPlayer) == true){
			drawBoard();
			System.out.println("The computer has won");
		}else if(checkTerm(current, personChar) == true){
			drawBoard();
			System.out.println("The computer has won");
		}else if(checkDraw(current)){
			System.out.println("The game is a draw");
			drawBoard();
		}
		else{
			TBoard = temp.Board;
			current = temp;
			playerMove();
		}
	}
	public void playerMove(){
		drawBoard();
		System.out.println("");
		System.out.println("Where would you like to go next");
		int move = scan.nextInt();
		if(move > 8 || move < 0 || TBoard[move] != null){
			System.out.println("This is not a legal move");
			playerMove();
		}
		TBoard[move] = personChar;
		current.Board = TBoard;
		if(checkTerm(current, CompPlayer) == true){
			drawBoard();
			System.out.println("The computer has won");
		}if(checkTerm(current, personChar) == true){
			drawBoard();
			System.out.println("The computer has won");
		}if(checkDraw(current)){
			System.out.println("The game is a draw");
			drawBoard();
		}else{
			AImove();
		}
	}
	public double Max(State state){
		double ut = -2.0;
		
		if(checkTerm(state, personChar) || checkTerm(state, CompPlayer) || checkDraw(state)){
			return getUtil(state);
		}
		else{
			State temp1 = state;
			for(State s : state.generteSucc(temp1, CompPlayer, personChar)){
				double temp = Min(s);
				if(ut < temp){
					ut = temp;
				}
			}
		}
		return ut;
	}
	public double getUtil(State state){
		double ut = 0.0;
		if(checkTerm(state, personChar)){
			ut = -1.0;
		}else if(checkTerm(state, CompPlayer)){
			return 1.0;
		}else if(checkDraw(state)){
			return 0.0;
		}
		return ut;
	}
	public double Min(State state){
		double ut = 2.0;
		if(checkTerm(state, personChar) || checkTerm(state, CompPlayer) || checkDraw(state)){
			return getUtil(state);
		}else{
			for(State s : state.generteSucc(state, personChar, CompPlayer)){
				double temp = Max(s);
				if(ut > temp){
					ut = temp;
				}
			}
		}
		return ut;
	}
	
	public void StartGame(){
		System.out.println("Welcome to Tic Tac Toe");
		System.out.println("Would you like to be X or O");
		personChar = scan.nextLine();
		if(personChar.equals("X")){
			CompPlayer = "O";
			current = new State(personChar, TBoard);
			playerMove();
		}else{
			CompPlayer = "X";
			current = new State(CompPlayer, TBoard);
			AImove();
		}
	}
}
