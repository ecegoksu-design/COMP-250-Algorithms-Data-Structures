/**
 * Your name here: Ece Goksu
 * Your McGill ID here: 261138642
 **/

public class SolitaireCipher {
    public Deck key;

    public SolitaireCipher (Deck key) {
        this.key = new Deck(key); // deep copy of the deck
    }

    /*
     * TODO: Generates a keystream of the given size
     */
    public int[] getKeystream(int size) {
        int[] keystream = new int[size];
        for (int i = 0; i < size; i++) {
            keystream[i] = this.key.generateNextKeystreamValue();
        }
        return keystream;
    }

    /*
     * TODO: Encodes the input message using the algorithm described in the pdf.
     */
    public String encode(String msg) {
        String message = msg.toUpperCase().replaceAll("[^A-Z]", "");
        int[] keystream = getKeystream(message.length());
        String encodedMessage = "" ;

        for (int i = 0; i < message.length(); i++) {
            char changed = (char) (((message.charAt(i) - 'A' + keystream[i]) % 26) + 'A');
            encodedMessage += changed;
        }
        return encodedMessage;
    }

    /*
     * TODO: Decodes the input message using the algorithm described in the pdf.
     */
    public String decode(String msg) {
        String decodedMessage = "";
        int[] keystream = getKeystream(msg.length());

        for (int i = 0; i < msg.length(); i++) {
            int letterIndex = msg.charAt(i) - 'A';
            int decodedIndex = (letterIndex - keystream[i] + 26) % 26;
            char decodedLetter = (char) ('A' + decodedIndex);
            decodedMessage += decodedLetter;
        }

        return decodedMessage;
    }

}
