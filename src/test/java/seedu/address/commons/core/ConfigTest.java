package seedu.address.commons.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConfigTest {

    private static final String DEFAULT_BOT_TOKEN = "339790464:AAGUN2BmhnU0I2B2ULenDdIudWyv1d4OTqY";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toString_defaultObject_stringReturned() {
        String defaultConfigAsString = "App title : Ark\n"
                + "Current log level : INFO\n"
                + "Preference file Location : preferences.json\n"
                + "Bot Authentication Token: " + DEFAULT_BOT_TOKEN + "\n"
                + "Bot Username: ArkBot";

        assertEquals(defaultConfigAsString, new Config().toString());
    }

    @Test
    public void equalsMethod() {
        Config defaultConfig = new Config();
        assertNotNull(defaultConfig);
        assertTrue(defaultConfig.equals(defaultConfig));
    }


}
