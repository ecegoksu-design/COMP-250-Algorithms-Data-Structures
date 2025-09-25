import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class LargeDatabaseTests {
    @Test
    public void rankCalulation1() throws Exception {
        SearchEngine searchEngine = new SearchEngine("db1.xml");
        searchEngine.crawlAndIndex("www.unreal.com");
        searchEngine.assignPageRanks(0.01);
        for (String vertex : searchEngine.internet.getVertices()) {
            assertEquals(searchEngine.parser.getPageRank(vertex), searchEngine.internet.getPageRank(vertex), 0.0001);
        }
    }

    @Test
    public void getResults1() throws Exception {
        SearchEngine searchEngine = new SearchEngine("db1.xml");
        searchEngine.crawlAndIndex("www.unreal.com");
        searchEngine.assignPageRanks(0.01);
        ArrayList<String> results = searchEngine.getResults("American");


        for (int i = 0; i < 7; i++) {
            if (i != 6) {
                assertTrue(searchEngine.internet.getPageRank(results.get(i))
                        >= searchEngine.internet.getPageRank(results.get(i + 1)));
            }
        }
    }

    @Test
    public void rankCalculation2() throws Exception {
        SearchEngine searchEngine = new SearchEngine("db2.xml");
        searchEngine.crawlAndIndex("www.all-links.com");
        searchEngine.assignPageRanks(0.01);
        for (String vertex : searchEngine.internet.getVertices()) {
            assertEquals(searchEngine.parser.getPageRank(vertex), searchEngine.internet.getPageRank(vertex), 0.0001);
        }
    }

    @Test
    public void getResults2() throws Exception {
        SearchEngine searchEngine = new SearchEngine("db2.xml");
        searchEngine.crawlAndIndex("www.all-links.com");
        searchEngine.assignPageRanks(0.01);
        ArrayList<String> results = searchEngine.getResults("coMpaNy");

        for (int i = 0; i < 8; i++) {
            if (i != 7) {
                assertTrue(searchEngine.internet.getPageRank(results.get(i))
                        >= searchEngine.internet.getPageRank(results.get(i + 1)));
            }
        }

    }

    @Test
    public void rankCalculation3() throws Exception {
        SearchEngine searchEngine = new SearchEngine("db3.xml");
        searchEngine.crawlAndIndex("site55");
        searchEngine.assignPageRanks(0.01);
        for (String vertex: searchEngine.internet.getVertices()) {
            assertEquals(searchEngine.parser.getPageRank(vertex), searchEngine.internet.getPageRank(vertex), 0.0001);
        }
    }

    @Test
    public void getResults3() throws Exception {
        SearchEngine searchEngine = new SearchEngine("db3.xml");
        searchEngine.crawlAndIndex("site55");
        searchEngine.assignPageRanks(0.01);

        // should have 11 fig
        ArrayList<String> results1 = searchEngine.getResults("fig");
        assertEquals(11, results1.size());
        for (int i = 0; i < 11; i++) {
            if (i != 10) {
                assertTrue(searchEngine.internet.getPageRank(results1.get(i))
                        >= searchEngine.internet.getPageRank(results1.get(i + 1)));
            }
        }

        // should have 10 papaya
        ArrayList<String> results2 = searchEngine.getResults("papaya");
        assertEquals(10, results2.size());
        for (int i = 0; i < 10; i++) {
            if (i != 9) {
                assertTrue(searchEngine.internet.getPageRank(results2.get(i))
                        >= searchEngine.internet.getPageRank(results2.get(i + 1)));
            }
        }

        // should have 8 watermelon (2 of the sites containing watermelon are unreachable)
        ArrayList<String> results3 = searchEngine.getResults("watermelon");
        assertEquals(8, results3.size());
        for (int i = 0; i < 8; i++) {
            if (i != 7) {
                assertTrue(searchEngine.internet.getPageRank(results3.get(i))
                        >= searchEngine.internet.getPageRank(results3.get(i + 1)));
            }
        }
    }
}
