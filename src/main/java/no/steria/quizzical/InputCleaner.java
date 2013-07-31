package no.steria.quizzical;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;


public class InputCleaner {

	public String clean(String unsafe) {
		if (unsafe == null) {
			return null;
		}
		return Jsoup.clean(unsafe, Whitelist.none());
	}

}
