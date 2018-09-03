package ttt;

import java.util.LinkedList;
import java.util.List;

public class AdvState {
	TTT boards[] = new TTT[9];
	String player;
	int position;
	public AdvState(String player, TTT boards[], int position){
		this.player = player;
		this.boards = boards;
		this.position = position;
	}
	public List<AdvState> generteSucc(AdvState state, String play, String opp, int boardNum){
		List<AdvState> list = new LinkedList<AdvState>();
		
			for(int i = 0; i < 9; i++){
				TTT tempBoard[] = copyTTT(state);
			if(state.boards[i] != null && tempBoard[boardNum].TBoard[i] == null){
				tempBoard[boardNum].TBoard[i] = play;
				AdvState temp = new AdvState(opp, tempBoard, i);
				list.add(temp);
			}
		}
		
		return list;
	}
	public TTT[] copyTTT(AdvState state){
		TTT temp[] = new TTT[9];
		for(int i = 0; i < 9; i++){
			temp[i] = copyTTT(state.boards[i]);
		}
		return temp;
	}
	public TTT copyTTT(TTT s){
		String temp[] = new String[9];
		for(int i = 0; i < 9; i++){
			if(s.TBoard[i] == null){
				
			}else{
				temp[i] = s.TBoard[i];
			}
		}
		TTT fresh = new TTT();
		fresh.TBoard = temp;
		return fresh;
	}

}
