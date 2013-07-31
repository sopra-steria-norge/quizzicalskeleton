package no.steria.quizzical;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;


public class InputCleaner {

	public String clean(String unsafe) {
		return Jsoup.clean(unsafe, Whitelist.none());
	}

}
