package no.steria.quizzical;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class InputCleanerTest {
	private InputCleaner cleaner = new InputCleaner();
	
	@Test
	public void shouldReturnSameIfOk() {
		assertThat(cleaner.clean("someone@somewhere.com")).isEqualTo("someone@somewhere.com");
	}

	@Test
	public void shouldWashUnintendedInput() {
		assertThat(cleaner.clean("Me<script>alert('abc');</script>")).isEqualTo("Me");
	}
	
	@Test
	public void shouldHandleEmptyText() {
		assertThat(cleaner.clean("")).isEqualTo("");
	}
	
	@Test
	public void shouldHandleNull() {
		assertThat(cleaner.clean(null)).isNull();
	}
	
	@Test
	public void shouldHandleNorwegianCharacters() {
		assertThat(cleaner.clean("blåbærsyltetøy")).isEqualTo("blåbærsyltetøy");
	}
}
