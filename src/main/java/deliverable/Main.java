package deliverable;

import java.io.IOException;
import java.text.ParseException;

import org.json.JSONException;

import deliverable.control.ExecutionFlow;

public class Main {

	public static void main(String[] args) throws JSONException, IOException, ParseException {
		
		ExecutionFlow.collectData("avro");
		//ExecutionFlow.collectData("bookkeeper");
		
		return;
		
	}

}
