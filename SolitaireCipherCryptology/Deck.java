import java.util.Random;

public class Deck {
    public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
    public static Random gen = new Random();

    public int numOfCards; // contains the total number of cards in the deck
    public Card head; // contains a pointer to the card on the top of the deck

    /*
     * TODO: Initializes a Deck object using the inputs provided
     */
    public Deck(int numOfCardsPerSuit, int numOfSuits) {
        if (numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13)
            throw new IllegalArgumentException("Number of cards per suit must be between 1 and 13");
        if (numOfSuits < 1 || numOfSuits > suitsInOrder.length)
            throw new IllegalArgumentException("Number of suits must be between 1 and suits in order");

        // Initialize head
        this.head = new PlayingCard(suitsInOrder[0],1);
        Card prevCard = this.head;

        for (int i = 0; i < numOfSuits; i++) {
            for (int j = 1; j <= numOfCardsPerSuit; j++) {
                if (i ==0 && j==1) {
                    continue;
                }
                PlayingCard newCard = new PlayingCard(suitsInOrder[i], j);
                prevCard.next = newCard;
                newCard.prev = prevCard;
                prevCard = newCard;
            }
        }

        // Add red joker
        Joker redJoker = new Joker("red");
        prevCard.next = redJoker;
        redJoker.prev = prevCard;
        prevCard = redJoker;

        // Add black joker
        Joker blackJoker = new Joker("black");
        prevCard.next = blackJoker;
        blackJoker.prev = prevCard;

        // Circular doubly linked list
        blackJoker.next = this.head;
        this.head.prev = blackJoker;

        numOfCards = numOfSuits * numOfCardsPerSuit + 2;

    }

    /*
     * TODO: Implements a copy constructor for Deck using Card.getCopy().
     * This method runs in O(n), where n is the number of cards in d.
     */
    public Deck(Deck d) {
        if (d.head == null) {
            this.head = null;
            this.numOfCards = 0;
            return;

        } else {

            this.head = d.head.getCopy();
            Card prevCardCopy = this.head;
            Card currentCard = d.head.next;

            while (currentCard != d.head) {
                Card currentCardCopy = currentCard.getCopy();
                prevCardCopy.next = currentCardCopy;
                currentCardCopy.prev = prevCardCopy;
                prevCardCopy = currentCardCopy;
                currentCard = currentCard.next;
            }

            prevCardCopy.next = this.head;
            this.head.prev = prevCardCopy;

            this.numOfCards = d.numOfCards;

        }
    }

    /*
     * For testing purposes we need a default constructor.
     */
    public Deck() {}

    /*
     * TODO: Adds the specified card at the bottom of the deck. This
     * method runs in $O(1)$.
     */
    public void addCard(Card c) {
        if (this.head == null) {
            this.head = c;
            c.next = c;
            c.prev = c;

        } else {
            c.prev = this.head.prev;
            c.next = this.head;
            this.head.prev.next = c;
            this.head.prev = c;
        }

        this.numOfCards++;

    }

    /*
     * TODO: Shuffles the deck using the algorithm described in the pdf.
     * This method runs in O(n) and uses O(n) space, where n is the total
     * number of cards in the deck.
     */
    public void shuffle() {
        if (this.head == null || this.numOfCards < 1) {
            return;
        }

        Card[] shuffledDeck;
        shuffledDeck = new Card[this.numOfCards];
        Card current = this.head;
        for (int i = 0; i < this.numOfCards; i++) {
            shuffledDeck[i] = current;
            current = current.next;
        }

        for (int i = this.numOfCards - 1; i > 0; i--) {
            int randomInt = gen.nextInt(i +1);

            Card temp = shuffledDeck[i];
            shuffledDeck[i] = shuffledDeck[randomInt];
            shuffledDeck[randomInt] = temp;
        }

        this.head = shuffledDeck[0];
        this.head.prev = shuffledDeck[this.numOfCards - 1];

        for (int i = 1; i < numOfCards; i++) {
            shuffledDeck[i].prev = shuffledDeck[i - 1];
            shuffledDeck[i -1].next = shuffledDeck[i];
        }

        shuffledDeck[this.numOfCards - 1].next = this.head;

    }

    /*
     * TODO: Returns a reference to the joker with the specified color in
     * the deck. This method runs in O(n), where n is the total number of
     * cards in the deck.
     */
    public Joker locateJoker(String color) {
        Card inspected = this.head;
        while (true) {
            if (inspected instanceof Joker) {
                Joker joker = (Joker) inspected;
                if (joker.getColor().equalsIgnoreCase(color)) {
                    return joker;
                }
            }

            inspected = inspected.next;
            if (inspected == this.head || inspected == null) {
                break;
            }
        }
        return null;
    }

    /*
     * TODO: Moved the specified Card, p positions down the deck. You can
     * assume that the input Card does belong to the deck (hence the deck is
     * not empty). This method runs in O(p).
     */
    public void moveCard(Card c, int p)
    {
        if (c == null || this.head == null || this.numOfCards <= 1) {
            return;
        }

        Card inspected = this.head;
        int position = 0;
        while (inspected != c) {
            inspected = inspected.next;
            position++;
        }
        int newPosition = position + p;
        if (newPosition == 0 ) {
            return;
        }
        if (inspected != this.head) {

            c.prev.next = c.next;
            c.next.prev = c.prev;
            Card current = this.head;
            for (int i = 0; i < newPosition -1; i++) {
                current = current.next;
            }
            c.prev = current;
            c.next = current.next;
            current.next.prev = c;
            current.next = c;
        }
        else {

            Card nextCard = inspected.next;
            Card[] removedDeck = new Card[p];
            for (int i = 0; i < p; i++) {
                Card removed = nextCard;
                removed.prev.next = removed.next;
                removed.next.prev = removed.prev;
                removedDeck[i] = removed;
                this.numOfCards -= 1;
                nextCard = removed.next;
                this.addCard(removedDeck[i]);
            }
        }

    }

    /*
     * TODO: Performs a triple cut on the deck using the two input cards. You
     * can assume that the input cards belong to the deck and the first one is
     * nearest to the top of the deck. This method runs in O(1)
     */
    public void tripleCut(Card firstCard, Card secondCard) {
        if (this.head == null || firstCard == null || secondCard == null || firstCard == secondCard)
            return;

        Card beforeJoker = firstCard.prev;
        Card afterJoker = secondCard.next;
        Card tail = this.head.prev;

        if (firstCard == this.head) {
            this.head = secondCard.next;
            tail = secondCard;
        }

        else if (secondCard == tail) {
            this.head = firstCard;
            tail = firstCard.prev;

        } else {

            tail.next = null;
            this.head.prev = null;
            firstCard.prev = null;
            secondCard.next = null;
            beforeJoker.next = null;
            afterJoker.prev = null;

            secondCard.next = this.head;
            this.head.prev = secondCard;

            firstCard.prev = tail;
            tail.next = firstCard;

            this.head = afterJoker;
            tail = beforeJoker;

            this.head.prev = tail;
            tail.next = this.head;
        }

    }

    /*
     * TODO: Performs a count cut on the deck. Note that if the value of the
     * bottom card is equal to a multiple of the number of cards in the deck,
     * then the method should not do anything. This method runs in O(n).
     */
    public void countCut() {

        Card tail = this.head.prev;
        int a = tail.getValue() % this.numOfCards;

        if (a == 0) {
            return;
        }

        Card top = this.head;

        for (int i = 1; i < a; i++) {
            top = top.next;
        }

        Card prevTail = tail.prev;

        prevTail.next = this.head;
        this.head.prev = prevTail;
        this.head = top.next;
        this.head.prev = tail;

        top.next = tail;
        tail.prev = top;
        tail.next = this.head;

    }



    /*
     * TODO: Returns the card that can be found by looking at the value of the
     * card on the top of the deck, and counting down that many cards. If the
     * card found is a Joker, then the method returns null, otherwise it returns
     * the Card found. This method runs in O(n).
     */
    public Card lookUpCard() {

        Card top = this.head;
        if (top == null || this.numOfCards == 0) {
            return null;
        }

        int count = this.head.getValue() % this.numOfCards;

        for (int i = 0; i < count; i++) {
            top = top.next;
        }

        if (top instanceof Joker) {
            return null;
        }

        return top;
    }

    /*
     * TODO: Uses the Solitaire algorithm to generate one value for the keystream
     * using this deck. This method runs in O(n).
     */
    public int generateNextKeystreamValue()
    {
        Card tail = this.head.prev;
        while (true) {
            Card redJoker = locateJoker("red");
            if (redJoker == tail && redJoker != this.head.next) {
                Card nextStart = this.head.next;
                Card prevJoker = redJoker.prev;

                this.head.next = redJoker;
                redJoker.prev = this.head;
                redJoker.next = nextStart;
                nextStart.prev = redJoker;

                tail = prevJoker;
                tail.next = this.head;
            }
            else{
                this.moveCard(redJoker,1);}

            Card blackJoker = locateJoker("black");

            if (blackJoker == tail && blackJoker != this.head.next) {
                if (blackJoker != this.head.next.next) {
                    Card second = this.head.next.next;
                    this.head.next.next = blackJoker;
                    blackJoker.prev.next = blackJoker.next;
                    blackJoker.next.prev = blackJoker.prev;
                    blackJoker.next = second;
                    blackJoker.prev = this.head.next;
                    second.prev = blackJoker;
                }
            } else if (blackJoker == tail.prev && blackJoker != this.head.next) {
                Card afterHead = this.head.next;
                this.head.next = blackJoker;
                blackJoker.prev.next = blackJoker.next;
                blackJoker.next.prev = blackJoker.prev;
                blackJoker.next = afterHead;
                blackJoker.prev = this.head;
                afterHead.prev = blackJoker;

            } else {
                this.moveCard(blackJoker, 2);
            }

            if (tail == blackJoker) {
                tail = blackJoker.prev;
            }

            Joker firstJoker= locateFirstJoker();
            Joker secondJoker;

            if (firstJoker.getColor().equalsIgnoreCase("black")){
                secondJoker= (Joker) redJoker;
            } else {
                secondJoker= (Joker) blackJoker;
            }

            this.tripleCut(firstJoker,secondJoker);

            this.countCut();


            Card key = this.lookUpCard();

            if (key == null){
                continue;
            }
            else{
                return key.getValue();
            }
        }
    }
    private Joker locateFirstJoker() {
        Card found = this.head;
        do {
            if (found instanceof Joker) {
                return (Joker) found;
            }
            found = found.next;
        } while (found != this.head);
        return null;
    }



    public abstract class Card {
        public Card next;
        public Card prev;

        public abstract Card getCopy();
        public abstract int getValue();

    }

    public class PlayingCard extends Card {
        public String suit;
        public int rank;

        public PlayingCard(String s, int r) {
            this.suit = s.toLowerCase();
            this.rank = r;
        }

        public String toString() {
            String info = "";
            if (this.rank == 1) {
                //info += "Ace";
                info += "A";
            } else if (this.rank > 10) {
                String[] cards = {"Jack", "Queen", "King"};
                //info += cards[this.rank - 11];
                info += cards[this.rank - 11].charAt(0);
            } else {
                info += this.rank;
            }
            //info += " of " + this.suit;
            info = (info + this.suit.charAt(0)).toUpperCase();
            return info;
        }

        public PlayingCard getCopy() {
            return new PlayingCard(this.suit, this.rank);
        }

        public int getValue() {
            int i;
            for (i = 0; i < suitsInOrder.length; i++) {
                if (this.suit.equals(suitsInOrder[i]))
                    break;
            }

            return this.rank + 13*i;
        }

    }

    public class Joker extends Card{
        public String redOrBlack;

        public Joker(String c) {
            if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black"))
                throw new IllegalArgumentException("Jokers can only be red or black");

            this.redOrBlack = c.toLowerCase();
        }

        public String toString() {
            //return this.redOrBlack + " Joker";
            return (this.redOrBlack.charAt(0) + "J").toUpperCase();
        }

        public Joker getCopy() {
            return new Joker(this.redOrBlack);
        }

        public int getValue() {
            return numOfCards - 1;
        }

        public String getColor() {
            return this.redOrBlack;
        }
    }

}
