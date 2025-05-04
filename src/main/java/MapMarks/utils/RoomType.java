package MapMarks.utils;


public enum RoomType {

    MONSTER, ELITE, EVENT, BOSS, SHOP, TREASURE, REST, UNKNOWN_TYPE;

    public static RoomType fromSymbol(String symbol) {
        if (symbol == null)
            return UNKNOWN_TYPE;

        if (symbol.equals("M"))
            return MONSTER;
        else if (symbol.equals("?"))
            return EVENT;
        else if (symbol.equals("B"))
            return BOSS;
        else if (symbol.equals("E"))
            return ELITE;
        else if (symbol.equals("R"))
            return REST;
        else if (symbol.equals("$"))
            return SHOP;
        else if (symbol.equals("T"))
            return TREASURE;
        else
            return UNKNOWN_TYPE;
    }

    public static String toSymbol(RoomType type) {
        switch (type) {
            case MONSTER:
                return "M";
            case EVENT:
                return "?";
            case BOSS:
                return "B";
            case ELITE:
                return "E";
            case REST:
                return "R";
            case SHOP:
                return "$";
            case TREASURE:
                return "T";
        }

        return null;
    }

    public static RoomType fromLegendItemConstructorIndex(int index)
    {
        switch(index)
        {
            case 0:
                return EVENT;
            case 1:
                return SHOP;
            case 2:
                return TREASURE;
            case 3:
                return REST;
            case 4:
                return MONSTER;
            case 5:
                return ELITE;
            // Note: Boss not included
        }

        return UNKNOWN_TYPE;
    }
}
