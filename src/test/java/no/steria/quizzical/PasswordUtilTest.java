package no.steria.quizzical;


import static org.fest.assertions.Assertions.assertThat;

import no.steria.quizzical.admin.PasswordUtil;
import org.junit.Test;

public class PasswordUtilTest {
	@Test
	public void shouldTestPassword() throws Exception {
		PasswordUtil passwordUtil = new PasswordUtil();
		byte[] salt = passwordUtil.generateSalt();
		byte[] crypt = passwordUtil.getEncryptedPassword("secret", salt);

		assertThat(passwordUtil.authenticate("secret", crypt, salt)).isTrue();
		assertThat(passwordUtil.authenticate("false", crypt, salt)).isFalse();
	}
}
