import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Final Project Minitester")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MiniTester_FinalProject {

 private static final String[] TEST_FILE_NAMES = {};

 private static String XML_FILES_PATH;
 private static final double RUNTIME_FACTOR = 4.0;
 private static final int NUM_TIME_ATTEMPTS = 10;

 private static HashMap<Integer, Integer> map = new HashMap<>();
 private static boolean fastSortCorrectTest1 = false;

 private static long median(long[] arr) {
  Arrays.sort(arr);
  int pos = (int) Math.ceil(NUM_TIME_ATTEMPTS / 2.0);
  return arr[pos - 1];
 }

 // method that compares the running time of students' solutions
 // with the running time of the expected solution.
 private long runAndTime(int mapSize) throws IOException {
  //expected average runtime

  long[] expectedRunTimes = new long[NUM_TIME_ATTEMPTS];
  long[] actualRunTimes = new long[NUM_TIME_ATTEMPTS];


  for (int i = 0; i < NUM_TIME_ATTEMPTS; i++) {

   //run time of our solution
   long expectedStartTime = System.nanoTime();
   simulateProfAns(mapSize);
   long expectedEndTime = System.nanoTime();
   expectedRunTimes[i] = expectedEndTime - expectedStartTime;

   //actual runtime of the student
   long actualStartTime = System.nanoTime();
   simulateStudentAns(mapSize);
   long actualEndTime = System.nanoTime();
   actualRunTimes[i] = actualEndTime - actualStartTime;
  }

  long expectedMedianRuntime = median(expectedRunTimes);
  long actualMedianRuntime = median(actualRunTimes);

  return actualMedianRuntime / expectedMedianRuntime;
 }


 private static void simulateStudentAns(int mapSize) {
  map.clear();
  for (int j = 0; j <= mapSize; j++) {
   int val = j + (int)(Math.random() * (mapSize-j+1));
   map.put(val, val);
  }

  Sorting.fastSort(map);
 }

 private static void simulateProfAns(int mapSize) {
  map.clear();
  for (int j = 0; j <= mapSize; j++) {
   int val = j + (int)(Math.random() * (mapSize-j+1));
   map.put(val, val);
  }

  //Sorting_Solutions.fastSort(map);
  Collections.sort(new ArrayList<Integer>(map.values()));
 }


 @BeforeAll
 static void setup() throws IOException {
  XML_FILES_PATH = new File(".").getCanonicalPath() + "/Data/";
 }


 @DisplayName("addVertex() test 1")
 @Tag("score:2")
 @Tag("hidden")
 @Order(1)
 @Test
 void addVertex_test1() {
  // set up
  MyWebGraph web = new MyWebGraph();
  boolean[] result = new boolean[1];

  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    result[0] = web.addVertex("url_1");
   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");

  // verify results
  MyWebGraph.WebVertex v = web.vertexList.get("url_1");
  assertTrue(v!=null, "No vertex has been added to the graph with the given url");
  assertTrue(v.toString().contains("url_1"), "The vertex added does not have the correct url");
  assertEquals(0, v.getNeighbors().size(), "Unwanted edges have been added to the graph");
  assertTrue(web.vertexList.size()<=1, "There's more than one vertex in the graph");
  assertTrue(result[0], "The method did not return true");
 }

 @DisplayName("addVertex() test 2")
 @Tag("score:1")
 @Tag("hidden")
 @Order(2)
 @Test
 void addVertex_test2() {
  // set up
  MyWebGraph web = new MyWebGraph();
  boolean[] result = new boolean[3];

  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    result[0] = web.addVertex("url_1");
    result[1] = web.addVertex("url_2");
    result[2] = web.addVertex("url_3");
   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");

  // verify results
  assertEquals(3, web.vertexList.size(), "The graph does not have 3 vertices.");
  assertTrue(result[0] && result[1] && result[2], "The method returned false instead of true.");
 }

 @DisplayName("addVertex() test 3")
 @Tag("score:2")
 @Tag("hidden")
 @Order(3)
 @Test
 void addVertex_test3() {
  // set up
  MyWebGraph web = new MyWebGraph();
  boolean[] result = new boolean[1];

  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    web.addVertex("url_1");
    web.setPageRank("url_1", 5);
    result[0] = web.addVertex("url_1");
   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");

  // verify results
  assertEquals(1, web.vertexList.size(), "The size of the graph has changed");
  assertTrue(!result[0], "The method returned true");
  assertEquals(5, web.getPageRank("url_1"), "The method updated the existing vertex");
 }


 @DisplayName("addEdge() test 1")
 @Tag("score:3")
 @Tag("hidden")
 @Order(4)
 @Test
 void addEdge_test1() {
  // set up
  MyWebGraph web = new MyWebGraph();
  boolean[] result = new boolean[1];

  MyWebGraph.WebVertex v1 = web.new WebVertex("url_1");
  MyWebGraph.WebVertex v2 = web.new WebVertex("url_2");
  web.vertexList.put("url_1", v1);
  web.vertexList.put("url_2", v2);


  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    result[0] = web.addEdge("url_1", "url_2");

   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");

  // verify results
  assertEquals(2, web.vertexList.size(), "The number of vertices changed");
  assertTrue(v1.containsEdge("url_2"), "The edge has not been added correctly");
  assertEquals(0, v2.getNeighbors().size(), "An extra edge has been added to the graph.");
  assertTrue(result[0], "The method did not return true.");
 }

 @DisplayName("addEdge() test 2")
 @Tag("score:1")
 @Tag("hidden")
 @Order(5)
 @Test
 void addEdge_test2() {
  // set up
  MyWebGraph web = new MyWebGraph();
  boolean[] result = new boolean[1];

  MyWebGraph.WebVertex v1 = web.new WebVertex("url_1");
  MyWebGraph.WebVertex v2 = web.new WebVertex("url_2");
  web.vertexList.put("url_1", v1);
  web.vertexList.put("url_2", v2);


  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    result[0] = web.addEdge("url_3", "url_4");

   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");

  // verify results
  assertEquals(2, web.vertexList.size(), "The number of vertices changed");
  assertEquals(0, v1.getNeighbors().size(), "An unwanted edge has been added to the graph.");
  assertEquals(0, v2.getNeighbors().size(), "An unwanted edge has been added to the graph.");
  assertTrue(!result[0], "The method did not return false.");
 }


 @DisplayName("addEdge() test 3")
 @Tag("score:1")
 @Tag("hidden")
 @Order(6)
 @Test
 void addEdge_test3() {
  // set up
  MyWebGraph web = new MyWebGraph();
  boolean[] result = new boolean[2];

  MyWebGraph.WebVertex v1 = web.new WebVertex("url_1");
  MyWebGraph.WebVertex v2 = web.new WebVertex("url_2");
  web.vertexList.put("url_1", v1);
  web.vertexList.put("url_2", v2);


  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    result[0] = web.addEdge("url_1", "url_4");
    result[1] = web.addEdge("url_4", "url_1");

   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");

  // verify results
  assertEquals(2, web.vertexList.size(), "The number of vertices changed");
  assertEquals(0, v1.getNeighbors().size(), "An unwanted edge has been added to the graph.");
  assertEquals(0, v2.getNeighbors().size(), "An unwanted edge has been added to the graph.");
  assertTrue(!result[0] && !result[1], "The method did not return false.");
 }

 @DisplayName("getVertices() test 1")
 @Tag("score:1")
 @Tag("hidden")
 @Order(7)
 @Test
 void getVertices_test1() throws IOException {
  // set up
  MyWebGraph web = new MyWebGraph();
  ArrayList<ArrayList<String>> vertices = new ArrayList<>();

  MyWebGraph.WebVertex v1 = web.new WebVertex("url_1");
  MyWebGraph.WebVertex v2 = web.new WebVertex("url_2");
  web.vertexList.put("url_1", v1);
  web.vertexList.put("url_2", v2);

  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    vertices.add(web.getVertices());
   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");

  // verify results
  if (vertices.size()==0 || vertices.get(0)==null)
   fail("The method did not execute correctly or was not implemented");

  ArrayList<String> urls = vertices.get(0);

  assertEquals(2, urls.size(), "The number of urls returned is incorrect");
  assertTrue(urls.contains("url_1") && urls.contains("url_2"), "The url returned are not correct");
  assertEquals(2, web.vertexList.size(), "The graph has been modified");

 }

 @DisplayName("getVertices() test 2")
 @Tag("score:1")
 @Tag("hidden")
 @Order(8)
 @Test
 void getVertices_test2() throws IOException {
  // set up
  MyWebGraph web = new MyWebGraph();
  ArrayList<ArrayList<String>> vertices = new ArrayList<>();


  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    vertices.add(web.getVertices());
   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");

  // verify results
  if (vertices.size()==0 || vertices.get(0)==null)
   fail("The method did not execute correctly or was not implemented");

  ArrayList<String> urls = vertices.get(0);

  assertEquals(0, urls.size(), "A non-empty list has been returned");
 }

 @DisplayName("crawlAndIndex() web graph test 1")
 @Tag("score:2")
 @Tag("hidden")
 @Order(9)
 @Test
 void crawlAndIndexWebGraphTest1() throws Exception {
  // set up
  SearchEngine searchEngine = new SearchEngine("testAcyclic.xml");


  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    searchEngine.crawlAndIndex("siteA");
   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");

  // verify results
  assertEquals(4, searchEngine.internet.vertexList.size(), "The number of vertices added while crawling is incorrect.");
 }

 @DisplayName("crawlAndIndex() web graph test 2")
 @Tag("score:1")
 @Tag("hidden")
 @Order(10)
 @Test
 void crawlAndIndexWebGraphTest2() throws Exception {
  // set up
  SearchEngine searchEngine = new SearchEngine("testAcyclic.xml");
  String[] expectedUrls = {"siteA", "siteC", "siteD", "siteE"};

  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    searchEngine.crawlAndIndex("siteA");
   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");

  // verify results
  for (String s: expectedUrls) {
   if (!searchEngine.internet.vertexList.containsKey(s))
    fail("Expected to find " + s + " in the graph, but it is not there");
  }
 }

 @DisplayName("crawlAndIndex() web graph test 3")
 @Tag("score:1")
 @Tag("hidden")
 @Order(11)
 @Test
 void crawlAndIndexWebGraphTest3() throws Exception {
  // set up
  SearchEngine searchEngine = new SearchEngine("testAcyclic.xml");

  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    searchEngine.crawlAndIndex("siteA");
   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");

  // verify results
  int numEdges = 0;

  for (String s: searchEngine.internet.vertexList.keySet()) {
   numEdges += searchEngine.internet.getOutDegree(s);
  }

  assertEquals(4, numEdges, "The number of edges added while crawling is incorrect");
 }

 @DisplayName("crawlAndIndex() word index test 1")
 @Tag("score:2")
 @Tag("hidden")
 @Order(12)
 @Test
 void crawlAndIndexWordIndexTest1() throws Exception {
  // set up
  SearchEngine searchEngine = new SearchEngine("testAcyclic.xml");

  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    searchEngine.crawlAndIndex("siteA");
   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");

  // verify results
  assertEquals(40, searchEngine.wordIndex.size(), "The number of words in the index is incorrect.");
 } 


 @DisplayName("crawlAndIndex() word index test 2")
 @Tag("score:2")
 @Tag("hidden")
 @Order(13)
 @Test
 void crawlAndIndexWordIndexTest2() throws Exception {
  // set up
  SearchEngine searchEngine = new SearchEngine("testAcyclic.xml");
  String[] expectedUrls = {"siteA", "siteC", "siteD", "siteE"};

  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    searchEngine.crawlAndIndex("siteA");
   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");

  // verify results
  ArrayList<String> urls = new ArrayList<String>();

  for (ArrayList<String> links : searchEngine.wordIndex.values()) {
   urls.addAll(links);
  }

  for(String s: expectedUrls) {
   if (!urls.contains(s))
    fail("Expected to find " + s + " in the word index, but it is not there");
  }
 }

 @DisplayName("crawlAndIndex() word index test 3")
 @Tag("score:1")
 @Tag("hidden")
 @Order(14)
 @Test
 void crawlAndIndexWordIndexTest3() throws Exception {
  // set up
  SearchEngine searchEngine = new SearchEngine("testAcyclic.xml");
  HashMap<String, String[]> urlToWords = new HashMap<>();
  urlToWords.put("siteA", new String[]{"3740770036", "0952663565", "0663674451", "5822227676", "5928627295", 
    "1519254007", "7150009686", "7614423011", "6586008889", "4939957667"});
  urlToWords.put("siteC", new String[] {"9068347470", "7635743784", "9736248449", "0285275009", "6583993610", 
    "0548427363", "4757636609", "6795855698", "5843111770", "5494122122"});
  urlToWords.put("siteD", new String[] {"7871509127", "3335050628", "4579875335", "0480627822", "4580968426", 
    "0823656729", "5496960782", "6010933711", "7179029061", "3507354539"});
  urlToWords.put("siteE", new String[] {"2393368125", "0765457981", "2078273722", "7218447788", "5243054669", 
    "2807185277", "9534956158", "8258010535", "6452353679", "9599444132"});


  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    searchEngine.crawlAndIndex("siteA");
   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");

  // verify results
  for (Entry<String, String[]> entry : urlToWords.entrySet()) {
   String url = entry.getKey();
   for (String w : entry.getValue()) {
    if (!searchEngine.wordIndex.get(w).get(0).equals(url))
     fail("Expected to see " + w + " map to " + url + " but it instead maps to " 
       + searchEngine.wordIndex.get(w).get(0));
   }
  }

 }


 @DisplayName("computeRanks() test1")
 @Tag("score:4")
 @Tag("hidden")
 @Order(15)
 @Test
 void computeRanks_test1() throws Exception {
  // set up
  String comment = "";
  boolean result = true;

  HashMap<String, Double> expectedResults = new HashMap<String, Double>();
  expectedResults.put("siteA", 0.5);
  expectedResults.put("siteC", 0.75);
  expectedResults.put("siteD", 0.75);
  expectedResults.put("siteE", 1.5);

  //SearchEngine searchEngine = new SearchEngine(XML_FILES_PATH + "testAcyclic.xml");
  SearchEngine searchEngine = new SearchEngine("testAcyclic.xml");  
  searchEngine.internet = new MyWebGraph();

  // create vertices with appropriate links
  MyWebGraph.WebVertex A = searchEngine.internet.new WebVertex("siteA");
  MyWebGraph.WebVertex C = searchEngine.internet.new WebVertex("siteC");
  MyWebGraph.WebVertex D = searchEngine.internet.new WebVertex("siteD");
  MyWebGraph.WebVertex E = searchEngine.internet.new WebVertex("siteE");
  C.addEdge("siteE");
  D.addEdge("siteE");
  A.addEdge("siteC");
  A.addEdge("siteD");

  // store them in the graph
  searchEngine.internet.vertexList.put("siteA", A);
  searchEngine.internet.vertexList.put("siteC", C);
  searchEngine.internet.vertexList.put("siteD", D);
  searchEngine.internet.vertexList.put("siteE", E);

  ArrayList<String> vertices = new ArrayList<>();
  vertices.add("siteA");
  vertices.add("siteC");
  vertices.add("siteD");
  vertices.add("siteE");


  // set initial values for the ranks
  for (String v : vertices)
   searchEngine.internet.setPageRank(v, 1.0);


  ArrayList<ArrayList<Double>> rankAfterOneIteration = new ArrayList<ArrayList<Double>>();
  // call method to be tested
  Assertions.assertTimeoutPreemptively(Duration.ofMillis(750), () -> {
   try{
    rankAfterOneIteration.add(searchEngine.computeRanks(vertices));
   } catch (Exception e) {
    fail("Unexpected exception: " + e);
   }
  }, "Took too long / Infinite loop");


  // verify results

  if (rankAfterOneIteration.size() != 1) {
   fail("ComputeRanks did not properly execute.");
  }

  ArrayList<Double> studentComputedRanks = rankAfterOneIteration.get(0);

  if (studentComputedRanks == null || studentComputedRanks.size() != vertices.size()) {
   fail("The arraylist returned is not of the correct size");
  }

  for (int i = 0; i < vertices.size(); i++) {
   String vertex = vertices.get(i);
   Double rank = studentComputedRanks.get(i);
   Double expectedRank = expectedResults.get(vertex);

   if (Math.abs(expectedRank - rank) > 1e-5) {
    comment = comment + " Expected Page rank for " + vertex + " is " + expectedRank + ", evaluated rank is " + rank + "\n";
    result = false;
   }
  }

  assertTrue (result, comment);
 }

 
 @DisplayName("getResults() test 1")
 @Tag("score:3")
 @Tag("hidden")
 @Order(16)
 @Test
 void getResults_test1() throws Exception {
  // set up

  //SearchEngine searchEngine = new SearchEngine(XML_FILES_PATH + "testAcyclic.xml");
  SearchEngine searchEngine = new SearchEngine("testAcyclic.xml");  
  searchEngine.internet = new MyWebGraph();

  // create vertices with appropriate ranks
  MyWebGraph.WebVertex A = searchEngine.internet.new WebVertex("siteA");
  MyWebGraph.WebVertex C = searchEngine.internet.new WebVertex("siteC");
  MyWebGraph.WebVertex D = searchEngine.internet.new WebVertex("siteD");
  MyWebGraph.WebVertex E = searchEngine.internet.new WebVertex("siteE");
  
  searchEngine.internet.vertexList.put("siteA", A);
  searchEngine.internet.vertexList.put("siteC", C);
  searchEngine.internet.vertexList.put("siteD", D);
  searchEngine.internet.vertexList.put("siteE", E);
  
  searchEngine.internet.setPageRank("siteA", 1.5);
  searchEngine.internet.setPageRank("siteC", 0.5);
  searchEngine.internet.setPageRank("siteD", 1);
  searchEngine.internet.setPageRank("siteE", 1.2);
  
  ArrayList<String> urls = new ArrayList<>();
  urls.add("siteA");
  urls.add("siteC");
  urls.add("siteD");
  
  searchEngine.wordIndex.put("test", urls);

  ArrayList<String> expectedResult = new ArrayList<>();
  expectedResult.add("siteA");
  expectedResult.add("siteD");
  expectedResult.add("siteC");
  

  // call method to be tested
  ArrayList<String> rankedUrls = searchEngine.getResults("test");

  // verify results
  assertEquals(3, rankedUrls.size(), "The method does not return the correct number of elements");
  assertEquals(expectedResult, rankedUrls, "The method does not return the urls in the correct order");
 }

 @DisplayName("getResults() test 2")
 @Tag("score:1")
 @Tag("hidden")
 @Order(17)
 @Test
 void getResults_test2() throws Exception {
  // set up

  //SearchEngine searchEngine = new SearchEngine(XML_FILES_PATH + "testAcyclic.xml");
  SearchEngine searchEngine = new SearchEngine("testAcyclic.xml");  
  searchEngine.internet = new MyWebGraph();

  // create vertices with appropriate ranks
  MyWebGraph.WebVertex A = searchEngine.internet.new WebVertex("siteA");
  MyWebGraph.WebVertex C = searchEngine.internet.new WebVertex("siteC");
  MyWebGraph.WebVertex D = searchEngine.internet.new WebVertex("siteD");
  MyWebGraph.WebVertex E = searchEngine.internet.new WebVertex("siteE");
  
  searchEngine.internet.vertexList.put("siteA", A);
  searchEngine.internet.vertexList.put("siteC", C);
  searchEngine.internet.vertexList.put("siteD", D);
  searchEngine.internet.vertexList.put("siteE", E);
  
  searchEngine.internet.setPageRank("siteA", 1.5);
  searchEngine.internet.setPageRank("siteC", 0.5);
  searchEngine.internet.setPageRank("siteD", 1);
  searchEngine.internet.setPageRank("siteE", 1.2);
  
  ArrayList<String> urls = new ArrayList<>();
  urls.add("siteA");
  urls.add("siteC");
  urls.add("siteD");
  
  searchEngine.wordIndex.put("test", urls);

  ArrayList<String> expectedResult = new ArrayList<>();
  expectedResult.add("siteA");
  expectedResult.add("siteD");
  expectedResult.add("siteC");
  

  // call method to be tested
  ArrayList<String> rankedUrls = searchEngine.getResults("TeST");

  // verify results
  assertEquals(3, rankedUrls.size(), "The method does not return the correct number of elements");
  assertEquals(expectedResult, rankedUrls, "The method does not return the urls in the correct order");
 }
 
 
 @DisplayName("fastSort() correctness test 1")
 @Tag("score:2")
 @Tag("hidden")
 @Order(18)
 @Test
 void fastSortCorrectnessTest1() {
  // set up
  ArrayList<Integer> results;
  ArrayList<Integer> actualResults = new ArrayList<>();
  HashMap<Integer, Integer> toSort = new HashMap<>();

  // check if fastSort was implemented
  for (int j = 0; j < 100; j++) {
   toSort.put(j, j);
  }
  results = Sorting.fastSort(toSort);
  if (results == null) {
   fail("fastSort() was not implemented");
  }

  // test output
  for (int j = 0; j < 100; j++) {
   toSort.put(j, j);
   actualResults.add(100-j-1);
  }

  results = Sorting.fastSort(toSort);

  // verify results
  assertEquals(results, actualResults, "fastSort did not successfully sort in decreasing order.");
  
  fastSortCorrectTest1 = true;
 }
 

 @DisplayName("fastSort() stress test 1")
 @Tag("score:1")
 @Tag("hidden")
 @Order(19)
 @Test
 void fastSort_stressTest1() throws IOException {
  if (!fastSortCorrectTest1)
   fail("fastSort was not correctly implemented");
  
  double actualRunTimeFactor = runAndTime(1000);
  assertTrue( actualRunTimeFactor <= RUNTIME_FACTOR,
    "RUNTIME EXCEEDED: expected your code to run at most " + RUNTIME_FACTOR +
    " times slower than the solution but was " + actualRunTimeFactor +
    " times slower than the solution"
    );
 }

 @DisplayName("fastSort() stress test 2")
 @Tag("score:2")
 @Tag("hidden")
 @Order(20)
 @Test
 void fastSort_stressTest2() throws IOException {
  if (!fastSortCorrectTest1)
   fail("fastSort was not correctly implemented");
  
  double actualRunTimeFactor = runAndTime(10000);
  assertTrue( actualRunTimeFactor <= RUNTIME_FACTOR,
    "RUNTIME EXCEEDED: expected your code to run at most " + RUNTIME_FACTOR +
    " times slower than the solution but was " + actualRunTimeFactor +
    " times slower than the solution"
    );
 }

 @DisplayName("fastSort() stress test 3")
 @Tag("score:3")
 @Tag("hidden")
 @Order(21)
 @Test
 void fastSort_stressTest3() throws IOException {
  if (!fastSortCorrectTest1)
   fail("fastSort was not correctly implemented");
  
  double actualRunTimeFactor = runAndTime(100000);
  assertTrue( actualRunTimeFactor <= RUNTIME_FACTOR,
    "RUNTIME EXCEEDED: expected your code to run at most " + RUNTIME_FACTOR +
    " times slower than the solution but was " + actualRunTimeFactor +
    " times slower than the solution"
    );
 }

 @DisplayName("fastSort() stress test 4")
 @Tag("score:3")
 @Tag("hidden")
 @Order(22)
 @Test
 void fastSort_stressTest4() throws IOException {
  if (!fastSortCorrectTest1)
   fail("fastSort was not correctly implemented");
  
  double actualRunTimeFactor = runAndTime(1000000);
  assertTrue( actualRunTimeFactor <= RUNTIME_FACTOR,
    "RUNTIME EXCEEDED: expected your code to run at most " + RUNTIME_FACTOR +
    " times slower than the solution but was " + actualRunTimeFactor +
    " times slower than the solution"
    );
 }



}


