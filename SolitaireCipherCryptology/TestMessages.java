import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestMessages {

    @Test
    @DisplayName("Test decode")
    public void testEncode() {
        Deck key = new Deck(13, 4);
        SolitaireCipher keyGen = new SolitaireCipher(key);
        String encodedMsg = keyGen.encode("add me: @not_a_real_username123:P");
        String expectedMsg = "EANKMMNLAAMHGIZCUMCUFU";
        assertEquals(expectedMsg, encodedMsg);
    }
    @Test
    @DisplayName("Test decode")
    public void testEncode2() {
        Deck key = new Deck(13, 4);
        SolitaireCipher keyGen = new SolitaireCipher(key);
        String encodedMsg = keyGen.encode("c00l 1s my m1dd13 n4m3");
        String expectedMsg = "GICKGLCVNV";
        assertEquals(expectedMsg, encodedMsg);
    }
    @Test
    @DisplayName("Test decode")
    public void testEncode3() {
        Deck key = new Deck(13, 4);
        SolitaireCipher keyGen = new SolitaireCipher(key);
        String encodedMsg = keyGen.encode("McGill is MY university!!!!!!");
        String expectedMsg = "QZQGTKHKMHCUDJLPVHVG";
        assertEquals(expectedMsg, encodedMsg);
    }
    @Test
    @DisplayName("Test decode")
    public void testEncode4() {
        Deck key = new Deck(9, 2);
        SolitaireCipher keyGen = new SolitaireCipher(key);
        String encodedMsg = keyGen.encode("b0rn t0 y4p, m4d3 t0 l0ck 1n...");
        String expectedMsg = "FNQNELPJKFEEG";
        assertEquals(expectedMsg, encodedMsg);
    }

    @Test
    @DisplayName("Test decode")
    public void testEncode5() {
        Deck key = new Deck(2, 4);
        SolitaireCipher keyGen = new SolitaireCipher(key);
        String encodedMsg = keyGen.encode("mclennan 2 is so. very. depressing.");
        String expectedMsg = "BQMGPOOOKTGCXSTAEFDGGTHWBV";
        assertEquals(expectedMsg, encodedMsg);
    }

    @Test
    @DisplayName("Test decode")
    public void testDecode() {
        Deck key = new Deck(13, 4);
        SolitaireCipher keyGen = new SolitaireCipher(key);
        String decodedMsg = keyGen.decode("GLWNCSDJSLQLIQLGVQGIMQGKNM");
        String expectedMsg = "COMPUTERSCIENCEISREALLYFUN";
        assertEquals(expectedMsg, decodedMsg);
    }

    @Test
    @DisplayName("Test decode")
    public void testDecode2() {
        Deck key = new Deck(13, 4);
        SolitaireCipher keyGen = new SolitaireCipher(key);
        String decodedMsg = keyGen.decode("MEYNMXNMAAMNZHAGQFGVPZOMLKOZT");
        String expectedMsg = "IHOPEYOUAREGETTINGENOUGHSLEEP";
        assertEquals(expectedMsg, decodedMsg);
    }

    @Test
    @DisplayName("Test decode")
    public void testDecode3() {
        Deck key = new Deck(9, 2);
        SolitaireCipher keyGen = new SolitaireCipher(key);
        String decodedMsg = keyGen.decode("LE");
        String expectedMsg = "HI";
        assertEquals(expectedMsg, decodedMsg);
    }

    @Test
    @DisplayName("Test decode")
    public void testDecode4() {
        Deck key = new Deck(6, 3);
        SolitaireCipher keyGen = new SolitaireCipher(key);
        String decodedMsg = keyGen.decode("LVPLLCGXSOHDTAOQTNGIMQMOX");
        String expectedMsg = "HEYWHYARENTYOUANSWERINGME";
        assertEquals(expectedMsg, decodedMsg);
    }

    @Test
    @DisplayName("Test decode")
    public void testDecode5() {
        Deck key = new Deck(13, 4);
        SolitaireCipher keyGen = new SolitaireCipher(key);
        String decodedMsg = keyGen.decode("KBDRQMFSNJQUXCTNZGCBMNSJBSCCENJCTU");
        String expectedMsg = "GETTINGANAINCOMPWHATLIKEITSHARDYES";
        assertEquals(expectedMsg, decodedMsg);
    }

    @Test
    @DisplayName("Test decode")
    public void testDecode6() {
        Deck key = new Deck(1, 1);
        SolitaireCipher keyGen = new SolitaireCipher(key);
        String decodedMsg = keyGen.decode("NBOJGFTUJOHTGPSFWFSZPOF");
        String expectedMsg = "MANIFESTINGSFOREVERYONE";
        assertEquals(expectedMsg, decodedMsg);
    }
}
