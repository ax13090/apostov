package apostov;

public class OmahaHandResultEnumerator extends HandResultEnumerator<OmahaHolecardHand> {

	public OmahaHandResultEnumerator() {
		super(new OmahaShowdownEvaluator());
	}

}
