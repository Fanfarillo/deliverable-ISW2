package deliverable;

import java.io.IOException;
import java.text.ParseException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.json.JSONException;

import deliverable.control.ExecutionFlow;

public class Main {

	public static void main(String[] args) throws JSONException, IOException, ParseException, RevisionSyntaxException, GitAPIException {
		
		ExecutionFlow.collectData("avro");
		//Same with bookkeeper
		
	}

}
