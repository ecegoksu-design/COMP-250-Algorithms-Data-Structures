import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (String, ArrayList of Strings)	
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}
	
	/* 
	 * This does an exploration of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 * 
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	public void crawlAndIndex(String url) throws Exception {
		HashMap<String, Boolean> visitedVertices = new HashMap<>();
		crawl(url, visitedVertices);
	}

	private void crawl(String url, HashMap<String, Boolean> visitedVertices) throws Exception {
		if (visitedVertices.putIfAbsent(url, true) != null) {
			return;
		}

		ArrayList<String> links = parser.getLinks(url);
		ArrayList<String> words = parser.getContent(url);

		// Update web graph
		internet.addVertex(url);
		for (String link : links) {
			internet.addVertex(link);
			internet.addEdge(url, link);
		}

		// Update word index
		for (String word : words) {
			String lowerCase = word.toLowerCase();
			wordIndex.computeIfAbsent(lowerCase, k -> new ArrayList<>());
			if (!wordIndex.get(lowerCase).contains(url)) {
				wordIndex.get(lowerCase).add(url);
			}
		}

		// Visit linked URLs
		for (String link : links) {
			if (!visitedVertices.containsKey(link)) {
				crawl(link, visitedVertices);
			}
		}
	}


	/* 
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex(). 
	 * To implement this method, refer to the algorithm described in the 
	 * assignment pdf. 
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		// Initialize rank to 1
		ArrayList<String> vertices = internet.getVertices();
		for (String vertex : vertices) {
			internet.setPageRank(vertex, 1.0);
		}

		boolean converged;
		do {
			converged = true;
			// Call to computeRanks to determine new ranks
			ArrayList<Double> newRanks = computeRanks(internet.getVertices());

			// Update ranks
			for (int i = 0; i < internet.getVertices().size(); i++) {
				String url = internet.getVertices().get(i);
				double oldRank = internet.getPageRank(url);
				double newRank = newRanks.get(i);
				internet.setPageRank(url, newRank);

				// Stop when ranks converge
				if (Math.abs(oldRank - newRank) >= epsilon) {
					converged = false;
				}
			}
		} while (!converged);
	}


	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 * Note that the double in the output list is matched to the url in the input list using 
	 * their position in the list.
	 * 
	 * This method will probably fit in about 20 lines.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		double dampingFactor = 0.5;
		ArrayList<Double> ranks = new ArrayList<Double>();

		for (String vertex : vertices) {
			// Initialize
			double rankSum = 0;

			// Iterate over linked vertices
			for (String linkedVertex : internet.getEdgesInto(vertex)) {
				double linkedRank = internet.getPageRank(linkedVertex);
				int outDegree = internet.getOutDegree(linkedVertex);

				// Avoid division by zero
				if (outDegree != 0) {
					rankSum += linkedRank / outDegree;
				}
			}

			// Damping factor formula
			double newRank = (1 - dampingFactor) + (dampingFactor * rankSum);

			// Add new rank to the list of ranks
			ranks.add(newRank);
		}

		return ranks;
	}


	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 * 
	 * This method will probably fit in about 10-15 lines.
	 */
	public ArrayList<String> getResults(String query) {
		// Lowercase
		String lowerCase = query.toLowerCase();
		ArrayList<String> urls = wordIndex.getOrDefault(lowerCase, new ArrayList<>());

		// Return empty list
		if (urls.isEmpty()) {
			return urls;
		}

		// Create a HashMap to hold urls and corresponding ranks
		HashMap<String, Double> ranks = new HashMap<>();
		for (String url : urls) {
			ranks.put(url, internet.getPageRank(url));
		}

		// Sort urls by rank
		return Sorting.fastSort(ranks);
	}
}
