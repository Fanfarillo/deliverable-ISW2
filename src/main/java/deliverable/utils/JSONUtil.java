package deliverable.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {
	
	//This private constructor is meant to hide the public one: utility classes do not have to be instantiated.
	private JSONUtil() {
		throw new IllegalStateException("This class does not have to be instantiated.");
	}
	
	private static String readAll(Reader rd) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		
		return sb.toString();
	}

	public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
		
	    try(InputStream is = new URL(url).openStream())
	    {
	    	BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
	        String jsonText = readAll(rd);
	        return new JSONArray(jsonText);

	    }
	    
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		
	    try(InputStream is = new URL(url).openStream())
	    {
	    	BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
	        String jsonText = readAll(rd);
	        return new JSONObject(jsonText);

	    }
	    
	}

}
