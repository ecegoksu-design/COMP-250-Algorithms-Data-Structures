import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MiniTesterA2 {

    @Test
    public void AddCard_AllRef() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2); //2C
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); //3C
        deck.addCard(c1);
        deck.addCard(c2);
        deck.addCard(c3);
        boolean c1ref = c1.next == c2 && c1.prev == c3;
        boolean c2ref = c2.next == c3 && c2.prev == c1;
        boolean c3ref = c3.next == c1 && c3.prev == c2;

        assertTrue(c1ref && c2ref && c3ref, "The next and prev references are not set correctly");

    }

    @Test
    public void AddCard_CheckHead() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card c8 = deck.new PlayingCard(Deck.suitsInOrder[0], 8); //8C
        deck.addCard(c1);
        deck.addCard(c8);
        Deck.Card head = deck.head;

        assertSame(head, c1, "The head is not set correctly. " +
                "addCard should add the input card to the bottom of the deck.\n" +
                "Expected head to be " + c1 + " but got " + head);

    }

    @Test
    public void AddCard_Circular() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2); //2C
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); //3C
        Deck.Card c8 = deck.new PlayingCard(Deck.suitsInOrder[0], 8); //8C
        deck.addCard(c1);
        deck.addCard(c2);
        deck.addCard(c3);
        deck.addCard(c8);

        assertTrue(c1.prev == c8 && c8.next == c1, "Circular references are not correctly set up.");
    }

    @Test
    public void AddCard_NumOfCards() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2); //2C
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); //3C
        Deck.Card d11 = deck.new PlayingCard(Deck.suitsInOrder[1], 11); //JD
        deck.addCard(c1);
        deck.addCard(c2);
        deck.addCard(d11);
        deck.addCard(c3);
        int expected = 4;
        int result = deck.numOfCards;

        assertEquals(expected, result, "numOfCards is not correctly updated.");
    }

    @Test
    public void AddCard_SingleCard() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        deck.addCard(c1);

        assertTrue(c1.prev == c1 && c1.next == c1, "Card references are not correctly set up when the deck contains only ONE card.");
    }

    @Test
    public void DeepCopy_CheckRefs() {
        HashSet<Deck.Card> cardSet = new HashSet<>();
        Deck deck = new Deck();
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[0], 1));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[0], 3));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[0], 5));
        cardSet.add(deck.new Joker("black"));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[1], 2));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[2], 4));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[3], 6));

        for(Deck.Card c: cardSet) {
            deck.addCard(c);
        }

        Deck copy = new Deck(deck); // should do a deep copy

        Deck.Card cur = copy.head;

        for (int i = 0; i < cardSet.size(); i++) {
            assertFalse(cardSet.contains(cur),"Deep copy must create new object.");

            cur = cur.next;
        }

    }

    @Test
    public void DeepCopy_CircularNext() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) {
            deck.addCard(c);
        }

        Deck copy = new Deck(deck); // should do a deep copy

        Deck.Card cur = copy.head;

        for (int i = 0; i < cards.length; i++) {
            assertNotNull(cur,"Either head or one of the next pointers is null.");


            // Either one is Joker and other is PlayingCard or vice versa
            assertEquals(cards[i].getClass(), cur.getClass(), "The card at the next position of ."
                    + i + " from head, has type: " + cur.getClass().getName()
                    + " but expected: " + cards[i].getClass().getName());


            // both are PlayingCard
            if (cur instanceof Deck.PlayingCard) {
                assertEquals(cards[i].getClass(), cur.getClass(), "The card at the next position of ."
                        + i + " from head must have value: " + cards[i].getValue() + " but got: " + cur.getValue());

                // both are Joker
            } else {
                String cardColor = ((Deck.Joker) cards[i]).getColor();
                String curColor = ((Deck.Joker) cur).getColor();
                assertEquals(cardColor, curColor, "The joker card at the next position of ."
                        + i + " from head must have color: " + cardColor + " but got: " + curColor);

            }
            cur = cur.next;
        }

        if (cur != copy.head) {
            fail("The last card's next does not point to the head.");
        }

    }

    @Test
    public void DeepCopy_CircularPrev() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) {
            deck.addCard(c);
        }

        Deck copy = new Deck(deck); // should do a deep copy

        Deck.Card cur = copy.head;
        for (int j = 0; j < cards.length; j++) {
            int i = Math.floorMod(-j, cards.length); // i goes 0, n-1, n-2, ..., 1
            assertNotNull(cur,"Either head or one of the prev pointers is null.");


            // Either one is Joker and other is PlayingCard or vice versa
            assertEquals(cards[i].getClass(), cur.getClass(), "The card at the prev position of ."
                    + j + " from head, has type: " + cur.getClass().getName()
                    + " but expected: " + cards[i].getClass().getName());


            if (cur instanceof Deck.PlayingCard) { // both are PlayingCard
                assertEquals(cards[i].getValue(),cur.getValue(),"The card at the prev position of ."
                        + j + " from head must have value: " + cards[i].getValue() + " but got: " + cur.getValue());

            } else { // both are Joker
                String cardColor = ((Deck.Joker) cards[i]).getColor();
                String curColor = ((Deck.Joker) cur).getColor();
                assertEquals(cardColor, curColor, "The joker card at the prev position of ."
                        + j + " from head must have color: " + cardColor + " but got: " + curColor);

            }
            cur = cur.prev;
        }

        if (cur != copy.head) {
            fail("The last card's prev does not point to the head.");
        }
    }

    @Test
    public void LocateJoker_Test1() {
        Deck tdeck = new Deck();
        Deck.Card c1 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card c2 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 2); //2C
        Deck.Card c3 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 3); //3C
        Deck.Card expected = tdeck.new Joker("red");

        tdeck.addCard(c1);
        tdeck.addCard(c2);
        tdeck.addCard(c3);
        tdeck.addCard(expected);

        Deck.Card received = tdeck.locateJoker("red");

        assertEquals(expected, received, "The reference returned was incorrect." +
                "Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                +" but instead got the card " + received + " with reference " + received.hashCode());
    }

    @Test
    public void LocateJoker_Test2() {
        Deck tdeck = new Deck();
        Deck.Card c1 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card c2 = tdeck.new Joker("red");
        Deck.Card c3 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 2); //2C
        Deck.Card c4 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 3); //3C

        Deck.Card expected = tdeck.new Joker("black");

        tdeck.addCard(c1);
        tdeck.addCard(c2);
        tdeck.addCard(c3);
        tdeck.addCard(c4);
        tdeck.addCard(expected);

        Deck.Card received = tdeck.locateJoker("black");

        assertEquals(expected, received, "The reference returned was incorrect." +
                "Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                +" but instead got the card " + received + " with reference " + received.hashCode());
    }

    @Test
    public void LocateJoker_Test3() {
        Deck tdeck = new Deck();
        Deck.Card c1 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card expected = tdeck.new Joker("red");
        Deck.Card c2 = tdeck.new Joker("black");
        Deck.Card c3 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 2); //2C
        Deck.Card c4 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 3); //3C

        tdeck.addCard(c1);
        tdeck.addCard(expected);
        tdeck.addCard(c2);
        tdeck.addCard(c3);
        tdeck.addCard(c4);


        Deck.Card received = tdeck.locateJoker("red");

        assertEquals(expected, received, "The reference returned was incorrect." +
                "Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                +" but instead got the card " + received + " with reference " + received.hashCode());
    }

    @Test
    public void LookUpCard_Test1() {

        Deck tdeck = new Deck();
        Deck.Card c1 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card expected = tdeck.new PlayingCard(Deck.suitsInOrder[0], 2); //2C
        Deck.Card c3 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 3); //3C

        tdeck.addCard(c1);
        tdeck.addCard(expected);
        tdeck.addCard(c3);

        Deck.Card received = tdeck.lookUpCard();

        assertEquals(expected, received, "The reference returned was incorrect." +
                "Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                +" but instead got the card " + received + " with reference " + received.hashCode());

    }

    @Test
    public void LookUpCard_Test2() {
        Deck tdeck = new Deck();
        Deck.Card c1 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 6); //6C
        Deck.Card c2 = tdeck.new PlayingCard(Deck.suitsInOrder[1], 2); //2D
        Deck.Card c3 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 3); //3H
        Deck.Card c4 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 4); //4H
        Deck.Card c5 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 5); //5H
        Deck.Card c6 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 6); //6H
        Deck.Card expected = tdeck.new PlayingCard(Deck.suitsInOrder[2], 7); //7H
        Deck.Card c7 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 8); //8H
        Deck.Card c8 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 9); //9H

        tdeck.addCard(c1);
        tdeck.addCard(c2);
        tdeck.addCard(c3);
        tdeck.addCard(c4);
        tdeck.addCard(c5);
        tdeck.addCard(c6);
        tdeck.addCard(expected);
        tdeck.addCard(c7);
        tdeck.addCard(c8);

        Deck.Card received = tdeck.lookUpCard();

        assertEquals(expected, received, "The reference returned was incorrect." +
                "Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                +" but instead got the card " + received + " with reference " + received.hashCode());

    }

    @Test
    public void LookUpCard_Test3() {
        Deck tdeck = new Deck();
        Deck.Card c1 = tdeck.new PlayingCard(Deck.suitsInOrder[0], 8); //6C
        Deck.Card c2 = tdeck.new PlayingCard(Deck.suitsInOrder[1], 2); //2D
        Deck.Card c3 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 3); //3H
        Deck.Card c4 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 4); //4H
        Deck.Card c5 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 5); //5H
        Deck.Card c6 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 6); //6H
        Deck.Card c7 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 7); //7H
        Deck.Card c8 = tdeck.new PlayingCard(Deck.suitsInOrder[2], 8); //8H
        Deck.Card c9 = tdeck.new Joker("red"); //JR

        tdeck.addCard(c1);
        tdeck.addCard(c2);
        tdeck.addCard(c3);
        tdeck.addCard(c4);
        tdeck.addCard(c5);
        tdeck.addCard(c6);
        tdeck.addCard(c7);
        tdeck.addCard(c8);
        tdeck.addCard(c9);


        Deck.Card received = tdeck.lookUpCard();

        assertNull(received, "Null should be returned in case a Joker is found.");
    }

    @Test
    public void MoveCard_CheckNext1() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) {
            deck.addCard(c);
        }

        Deck.Card[] expected = new Deck.Card[]{
                cards[0], cards[1], cards[3], cards[4],
                cards[5], cards[2], cards[6]};

        deck.moveCard(cards[2], 3);

        Deck.Card cur = deck.head;

        for (int i = 0; i < expected.length; i++) {
            // System.out.println(cur);
            assertEquals(expected[i], cur, "Expect card: " + expected[i] + " but got: " + cur);
            cur = cur.next;
        }
    }

    @Test
    public void MoveCard_CheckNext2() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) {
            deck.addCard(c);
        }

        Deck.Card[] expected = new Deck.Card[]{
                cards[0], cards[3], cards[1], cards[2],
                cards[4], cards[5], cards[6]};

        deck.moveCard(cards[3], 4);

        Deck.Card cur = deck.head;
        for (int i = 0; i < expected.length; i++) {
            // System.out.println(cur);
            assertEquals(expected[i],cur,"Expect card: " + expected[i] + " but got: " + cur);

            cur = cur.next;
        }
    }

    @Test
    public void MoveCard_CheckPrev1() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) {
            deck.addCard(c);
        }

        Deck.Card[] expected = new Deck.Card[]{
                cards[0], cards[1], cards[3], cards[4],
                cards[5], cards[2], cards[6]};

        deck.moveCard(cards[2], 3);

        Deck.Card cur = deck.head;
        for (int j = 0; j < expected.length; j++) {
            int i = Math.floorMod(-j, expected.length); // i goes 0, n-1, n-2, ..., 1
            // System.out.println(cur);
            assertEquals(expected[i],cur,"Expect card: " + expected[i] + " but got: " + cur);

            cur = cur.prev;
        }
    }

    @Test
    public void MoveCard_CheckPrev2() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) {
            deck.addCard(c);
        }

        Deck.Card[] expected = new Deck.Card[]{
                cards[0], cards[3], cards[1], cards[2],
                cards[4], cards[5], cards[6]};

        deck.moveCard(cards[3], 4);

        Deck.Card cur = deck.head;
        for (int j = 0; j < expected.length; j++) {
            int i = Math.floorMod(-j, expected.length); // i goes 0, n-1, n-2, ..., 1
            // System.out.println(cur);
            assertEquals(expected[i],cur,"Expect card: " + expected[i] + " but got: " + cur);

            cur = cur.prev;
        }
    }

    @Test
    public void Shuffle_Empty() {
        Deck deck = new Deck();
        deck.shuffle();

        assertNull(deck.head,"Deck should be empty.");

    }

    @Test
    public void Shuffle_Example() {
        Deck deck = new Deck();
        // example in instruction pdf
        // AC 2C 3C 4C 5C AD 2D 3D 4D 5D RJ BJ
        Deck.Card[] arrDeck = new Deck.Card[12];
        for (int i = 0; i < 10; i++) {
            int suit = i/5;
            int rank = i%5 + 1;
            Deck.Card card = deck.new PlayingCard(Deck.suitsInOrder[suit], rank);
            arrDeck[i] = card;
            deck.addCard(card);
        }
        Deck.Card rj = deck.new Joker("red");
        Deck.Card bj = deck.new Joker("black");
        arrDeck[10] = rj;
        arrDeck[11] = bj;
        deck.addCard(rj);
        deck.addCard(bj);

        int seed = 10;
        Deck.gen.setSeed(seed);
        deck.shuffle();

        // expected result
        // 3C 3D AD 5C BJ 2C 2D 4D AC RJ 4C 5D

        int[] shuffledIndex = {2,7,5,4,11,1,6,8,0,10,3,9};

        // .next references
        Deck.Card cur = deck.head;
        for (int i = 0; i < 12; i++) {
            Deck.Card expected = arrDeck[shuffledIndex[i]];
            assertEquals(expected.getValue(),cur.getValue(),"Deck is not correctly shuffled.\n" +
                    "Forward references are not correctly set up. " +
                    "Expected card at index " + i + " iterating using .next is " + expected + " but got " + cur);

            cur = cur.next;
        }

        // .prev references
        cur = deck.head.prev;
        for (int i = 11; i >=0; i--) {
            Deck.Card expected = arrDeck[shuffledIndex[i]];
            assertEquals(expected.getValue(),cur.getValue(),"Deck is not correctly shuffled.\n" +
                    "Backward references are not correctly set up. " +
                    "Expected card at index " + i + " iterating using .prev is " + expected + " but got " + cur);

            cur = cur.prev;
        }
    }

    @Test
    public void Shuffle_FullDeck() {
        Deck deck = new Deck();
        // all 54 cards
        Deck.Card[] arrDeck = new Deck.Card[54];
        for (int i = 0; i < 52; i++) {
            int suit = i/13;
            int rank = i%13 + 1;
            Deck.Card card = deck.new PlayingCard(Deck.suitsInOrder[suit], rank);
            arrDeck[i] = card;
            deck.addCard(card);
        }
        Deck.Card rj = deck.new Joker("red");
        Deck.Card bj = deck.new Joker("black");
        arrDeck[52] = rj;
        arrDeck[53] = bj;
        deck.addCard(rj);
        deck.addCard(bj);

        int seed = 10;
        Deck.gen.setSeed(seed);
        deck.shuffle();

        // expected result
        // 7S QD 7H JH KH KD 8C 4C 9S JD KC 9C 5C QC 2S 5S 10H 10D
        // 4S 5D 6H 4D 9D 8D 3H 6D 4H 7C RJ 9H 3C 2D JC 6C 8H JS 5H
        // AH BJ 3S 6S 3D QS AS 7D 2C AD KS 10S 8S 10C QH AC 2H
        int[] shuffledIndex = {
                45, 24, 32, 36, 38, 25, 7, 3, 47, 23, 12, 8, 4, 11, 40, 43, 35, 22,
                42, 17, 31, 16, 21, 20, 28, 18, 29, 6, 52, 34, 2, 14, 10, 5, 33, 49, 30,
                26, 53, 41, 44, 15, 50, 39, 19, 1, 13, 51, 48, 46, 9, 37, 0, 27};

        // .next references
        Deck.Card cur = deck.head;
        for (int i = 0; i < 54; i++) {
            Deck.Card expected = arrDeck[shuffledIndex[i]];
            assertEquals(expected.getValue(),cur.getValue(),"Deck is not correctly shuffled.\n" +
                    "Forward references are not correctly set up. " +
                    "Expected card at index " + i + " is " + expected + " but got " + cur);

            cur = cur.next;
        }

        // .prev references
        cur = deck.head.prev;
        for (int i = 53; i >=0; i--) {
            Deck.Card expected = arrDeck[shuffledIndex[i]];
            assertEquals(expected.getValue(),cur.getValue(),"Deck is not correctly shuffled.\n" +
                    "Backward references are not correctly set up. " +
                    "Expected card at index " + i + " iterating using .prev is " + expected + " but got " + cur);

            cur = cur.prev;
        }
    }

    @Test
    public void Shuffle_NewCard() {
        Deck deck = new Deck();
        // example in instruction pdf
        // AC 2C 3C 4C 5C AD 2D 3D 4D 5D RJ BJ
        Set<Deck.Card> cardSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            int suit = i/5;
            int rank = i%5 + 1;
            Deck.Card card = deck.new PlayingCard(Deck.suitsInOrder[suit], rank);
            deck.addCard(card);
            cardSet.add(card);
        }
        Deck.Card rj = deck.new Joker("red");
        Deck.Card bj = deck.new Joker("black");
        deck.addCard(rj);
        deck.addCard(bj);
        cardSet.add(rj);
        cardSet.add(bj);

        int seed = 10;
        Deck.gen.setSeed(seed);
        deck.shuffle();

        Deck.Card cur = deck.head;
        // forward ref
        for (int i = 0; i < 12; i++) {
            assertTrue(cardSet.contains(cur),"Shuffle should not create new cards.");

            cur = cur.next;
        }
        assertEquals(deck.head, cur,"Deck is not correctly shuffled. " +
                "Tail does not connect to head or new cards were added.");


        // backward ref
        for (int i = 11; i >= 0; i--) {
            cur = cur.prev;
        }
        assertEquals(deck.head, cur,"Deck is not correctly shuffled. " +
                "Backward references are not correctly set up.");

    }

    @Test
    public void Shuffle_SingleCard() {
        Deck deck = new Deck();
        Deck.Card c = deck.new Joker("red");
        deck.addCard(c);

        deck.shuffle();

        assertTrue((deck.head.getValue() == c.getValue() &&
                c.next.getValue() == c.getValue() && c.prev.getValue() == c.getValue()),"Deck is not correctly shuffled when it only has one card.");

    }

    @Test
    public void Shuffle_Three() {
        Deck deck = new Deck();
        // AC 2C 3C 4C 5C
        Deck.Card[] arrDeck = new Deck.Card[5];
        for (int i = 0; i < 5; i++) {
            Deck.Card card = deck.new PlayingCard(Deck.suitsInOrder[0], i+1);
            arrDeck[i] = card;
            deck.addCard(card);
        }

        int seed = 250;
        Deck.gen.setSeed(seed);
        deck.shuffle();
        deck.shuffle();
        deck.shuffle();

        // expected first pass
        // AC, 4C, 5C, 3C, 2C

        // expected second pass
        // 5C, AC, 4C, 2C, 3C

        // expected third pass
        // AC, 5C, 3C, 2C, 4C

        int[] shuffledIndex = {0, 4, 2, 1, 3};

        // .next references
        Deck.Card cur = deck.head;
        for (int i = 0; i < 5; i++) {
            Deck.Card expected = arrDeck[shuffledIndex[i]];
            assertEquals(expected.getValue(),cur.getValue(),"Deck is not correctly shuffled.\n" +
                    "Forward references are not correctly set up. " +
                    "Expected card at index " + i + " is " + expected + " but got " + cur);

            cur = cur.next;
        }

        // .prev references
        cur = deck.head.prev;
        for (int i = 4; i >=0; i--) {
            Deck.Card expected = arrDeck[shuffledIndex[i]];
            assertEquals(expected.getValue(),cur.getValue(),"Deck is not correctly shuffled.\n" +
                    "Backward references are not correctly set up. " +
                    "Expected card at index " + i + " iterating using .prev is " + expected + " but got " + cur);

            cur = cur.prev;
        }
    }
}
