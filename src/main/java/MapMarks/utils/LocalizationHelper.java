package MapMarks.utils;

import com.megacrit.cardcrawl.localization.UIStrings;

public class LocalizationHelper {
    public static String getDictString(UIStrings strings, String key) {
        if(strings == null || !strings.TEXT_DICT.containsKey(key))
            return "MISSING LOCALIZATION";

        return strings.TEXT_DICT.get(key);
    }
}
