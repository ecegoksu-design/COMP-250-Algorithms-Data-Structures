import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) < 0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
	public static <K, V extends Comparable<V>> ArrayList<K> fastSort(HashMap<K, V> results) {
		ArrayList<Entry<K, V>> vertices = new ArrayList<>(results.entrySet());
		ArrayList<Entry<K, V>> temporary = new ArrayList<>(vertices.size());
		for (int i = 0; i < vertices.size(); i++) {
			temporary.add(null);
		}
		mergeSort(vertices, temporary, 0, vertices.size() - 1);
		ArrayList<K> sorted = new ArrayList<>();
		for (Entry<K, V> vertex : vertices) {
			sorted.add(vertex.getKey());
		}
		return sorted;
	}

	private static <K, V extends Comparable<V>> void mergeSort(ArrayList<Entry<K, V>> vertices, ArrayList<Entry<K, V>> temporary, int left, int right) {
		if (left >= right)
			return;
		int middle = (left + right)/2;
		mergeSort(vertices, temporary, left, middle);
		mergeSort(vertices, temporary, middle + 1, right);
		merge(vertices, temporary, left, middle, right);
	}

	private static <K, V extends Comparable<V>> void merge(ArrayList<Entry<K, V>> vertices, ArrayList<Entry<K, V>> temporary, int left, int middle, int right) {
		for (int k = left; k <= right; k++) {
			temporary.set(k, vertices.get(k));
		}
		int i = left;
		int j = middle + 1;

		for (int k = left; k <= right; k++) {
			if (i > middle)
				vertices.set(k, temporary.get(j++));
			else if (j > right)
				vertices.set(k, temporary.get(i++));
			else if (temporary.get(j).getValue().compareTo(temporary.get(i).getValue()) > 0)
				vertices.set(k, temporary.get(j++));
			else
				vertices.set(k, temporary.get(i++));
		}
	}
}