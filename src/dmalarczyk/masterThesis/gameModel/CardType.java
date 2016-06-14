package dmalarczyk.masterThesis.gameModel;

public enum CardType {
    guard, priest, baron, handmaid, prince, king, countess, princess;

    public static int getStrength(CardType cardType){
        switch(cardType){
            case guard:
                return 1;
            case priest:
                return 2;
            case baron:
                return 3;
            case handmaid:
                return 4;
            case prince:
                return 5;
            case king:
                return 6;
            case countess:
                return 7;
            case princess:
                return 8;
        }
        return 0;
    }
}
