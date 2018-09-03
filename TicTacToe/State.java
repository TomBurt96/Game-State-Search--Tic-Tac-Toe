package ttt;
import java.util.LinkedList;
import java.util.List;

public class State {
	public String Board[];
	public String player;
	public State(String player, String Board[]){
		this.player = player;
		this.Board = Board;
	}
	public List<State> generteSucc(State state, String play, String opp){
		List<State> list = new LinkedList<State>();
		for(int i = 0; i < 9; i++){
			if(state.Board[i] == null){
				String tempBoard[] = copyBoard(state);
				tempBoard[i] = play;
				State temp = new State(opp, tempBoard);
				list.add(temp);
			}
		}
		return list;
	}
	public String[] copyBoard(State s){
		String temp[] = new String[9];
		for(int i = 0; i < 9; i++){
			if(s.Board[i] == null){
				
			}else{
				temp[i] = s.Board[i];
			}
		}
		return temp;
	}
}
