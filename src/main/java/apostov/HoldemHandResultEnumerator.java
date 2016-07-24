package apostov;

public class HoldemHandResultEnumerator extends HandResultEnumerator<HoldemHolecardHand> {

	public HoldemHandResultEnumerator() {
		super(new HoldemShowdownEvaluator());
	}

}
