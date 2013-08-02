package no.steria.quizzical;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;



public class InputCleaner {

	public String clean(String unsafe) {
		if (unsafe == null) {
			return null;
		}
		Document doc = Jsoup.parse(unsafe);
		doc = new Cleaner(Whitelist.none()).clean(doc);
		doc.outputSettings().escapeMode(EscapeMode.xhtml);
		return doc.body().html();
	}

}
