package com.example.traveldiary;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TripsControllerTest {

    TripsController controller = new TripsController();

    @Test
    void testFormatBulletPoints_NormalList() {
        String input = "- slonecznie\n- banger herbatka\n- mega widoki";
        String expected = "slonecznie, banger herbatka, mega widoki";
        assertEquals(expected, controller.formatBulletPoints(input));
    }

    @Test
    void testFormatBulletPoints_EmptyInput() {
        assertEquals("", controller.formatBulletPoints(""));
        assertEquals("", controller.formatBulletPoints(null));
    }

    @Test
    void testFormatBulletPoints_MixedSpaces() {
        String input = "-    duzo spacji  \n-brak spacji";
        String expected = "duzo spacji, brak spacji";
        assertEquals(expected, controller.formatBulletPoints(input));
    }

    @Test
    void testWrapText_LongSentence() {
        String input = "To jest bardzo dlugie zdanie ktore powinno zostac zawiniete.";
        String result = controller.wrapText(input, 20);
        assertTrue(result.contains("\\n"), "Tekst powinien zawierac \\n");
    }
}