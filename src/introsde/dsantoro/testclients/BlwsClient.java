package introsde.dsantoro.testclients;

import java.net.MalformedURLException;

import introsde.dsantoro.blws.Blws;

public class BlwsClient {
	public static void main(String[] args) throws MalformedURLException {
		final String BLWS_ENDPOINT = System.getenv("BLWS_ENDPOINT");
		final String BLWS_PORT = System.getenv("BLWS_PORT");
		
		Blws blws = new Blws(BLWS_ENDPOINT, BLWS_PORT);		
		System.out.println("Number of meals retrieved: " + blws.searchFoods("orange", 5, 5).size());
	}
}
