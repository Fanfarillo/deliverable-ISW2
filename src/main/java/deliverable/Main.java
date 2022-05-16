package deliverable;

import deliverable.control.ExecutionFlow;

public class Main {

	public static void main(String[] args) throws Exception {
		
		ExecutionFlow.collectData("avro");
		ExecutionFlow.collectData("bookkeeper");
		
	}

}
