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
			//System.out.println("");
		}
	}
	public void Playermove(){
		drawBoard();
		System.err.println("where do you want to place on the board");
		int b = scan.nextInt();
		int move = 0;
		b--;
		if(current.position == 100){//check if player has the first move
			current.position = b;
		}
		if(b != current.position){//checks to see if you are choosing the correct board you must plau on
			System.err.println("You have to place your symbol on position: "+ (current.position+1));
			Playermove();
		}
		
		if(Boards[current.position].checkDraw() == true){//checks if the board you have to play on is full
			System.err.println("The board you are suppose to play on is full. Choose a board: ");
			b = scan.nextInt();
			b--;
			System.err.println("What is your move?");
			move = scan.nextInt();
			if(move < 1|| move > 9 ||Boards[current.position].TBoard[move-1] != null){//checks if the move is valid
				System.err.println("This is not a legal move.");
				Playermove();
			}
			move--;
			Boards[b].TBoard[move] = playerChar;
		}
		else{//this is for if the board is not full then do this
			System.err.println("What is your move?");
			move = scan.nextInt();
			if(move < 1|| move > 9 ||Boards[current.position].TBoard[move-1] != null){
				System.err.println("This is not a legal move.");
				Playermove();
			}
			move--;
			Boards[current.position].TBoard[move] = playerChar;
		}
		current.boards = Boards;
		current.position = move;

		if(checkAdvTerm(current, compChar)){//check for terminal state
			drawBoard();
			System.err.println("The computer has won");
		}else if(checkAdvTerm(current, playerChar)){//checks for terminal state
			drawBoard();
			System.err.println("The player has won");
		}
		else if(checkAdvDraw(current) == true){//check for terminal state
			drawBoard();
			System.err.println("The game is a draw");
		}else{
			AIMove();
		}
	}
	public void AIMove(){//computer's move
		AdvState temp = current;
		int boardPos = current.position;
		double val = -1000.0;
		List<AdvState> list = temp.generteSucc(current, compChar, playerChar, temp.position);//generate list of possible states
		for(AdvState s : list){//for loop to find greatest min value of list of successors
			double potential = HMin(s, 1, -100.0, 100.0);
			if(val < potential){
				val = potential;
				temp = s;
			}
		}
		Boards = temp.boards;
		current = temp;
		current.position = temp.position;
		if(checkAdvTerm(current, compChar)){
			drawBoard();
			System.err.println("The computer has won");
		}else if(checkAdvTerm(current, playerChar)){
			drawBoard();
			System.err.println("The player has won");
		}
		else if(checkAdvDraw(current) == true){
			drawBoard();
			System.err.println("The game is a draw");
		}
		else{
			System.err.println("The Computer has to play at board: " + (boardPos+1));
			System.err.println("The Computer chose position: " + (current.position+1));
			Playermove();
		}
	}
	public void setupBoards(){
		for(int i = 0; i < 9; i++){
			Boards[i] = new TTT();
		}
	}
	public void StartGame(){//starts the game
		setupBoards();
		System.err.println("Would you like to be X or O");
		playerChar = scan.nextLine();
		if(playerChar.equals("X")||playerChar.equals("x")){
			playerChar = "X";
			compChar = "O";
			current = new AdvState(playerChar, Boards, 100);
			Playermove();
		}else if(playerChar.equals("O") || playerChar.equals("o")){
			playerChar = "O";
			compChar = "X";
			current = new AdvState(compChar, Boards, 0);
			boardNum = 0;
			AIMove();
		}else{
			System.err.println("That is not an upper or lower case x or o");
			StartGame();
		}
	}
	public double HMax(AdvState state, int depth, double alpha, double beta){//title speaks for itself
		if(checkAdvTerm(state, compChar)|| checkAdvTerm(state, playerChar) ||checkAdvDraw(state)|| depth == 4){
			return getUtil(state);
		}else{
			double ut = -100.0;
			for(AdvState s: state.generteSucc(state, compChar, playerChar, state.position)){
				double temp = HMin(s, depth+1, alpha, beta);
				if(temp > ut){
					ut = temp;
				}
				if(ut >= beta){//pruning here
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
				if(ut <= alpha){//pruning here
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
