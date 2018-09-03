import java.util.Scanner;

public class Game {

	public static void Run(){
		Scanner scan = new Scanner(System.in);
		System.err.println("(A = Advanced, B = Basic)");
		String dec = scan.nextLine();
		if(dec.equals("A")){
			AdvancedTTT game = new AdvancedTTT();
			game.StartGame();
		}else if(dec.equals("B")){
			TTT game = new TTT();
			game.StartGame();
		}else{
			System.err.println("That was not an A or B input an A or B");
			Run();
		}
	}

	
	public static void main(String args[]){
		System.err.println("Let's play TicTacToe");
		System.err.println("Would you like to play the Basic version or Advanced version?");
		Run();
	}
}
