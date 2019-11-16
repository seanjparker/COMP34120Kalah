
public class MiniMax {
	public int minimax(State state, int depth, boolean maximizingPlayer) {
		if(depth==0 || state.children()==null) {
			return valueFunction(state);
		}
		if(maximizingPlayer==true) {
			int value=-1000000000;
			State[] children = state.children();
			for(int i=0;i<children.length();i++) {
				value=max(value,minimax(children[i],depth-1,false));
			}
			return value;
		}else{
			int value=+1000000000;
			State[] children = state.children();
			for(int i=0;i<children.length();i++) {
				value=min(value,minimax(children[i],depth-1,false));
			}
			return value;
		}
	 }
}

//The assumptions are:
//We have the 'state' passed in from the API.
//We have the function called 'children' which returns all the states
//that can be accessed from this chosen state.
