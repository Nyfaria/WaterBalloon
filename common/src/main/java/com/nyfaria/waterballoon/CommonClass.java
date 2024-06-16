package com.nyfaria.waterballoon;

import com.nyfaria.waterballoon.init.EntityInit;
import com.nyfaria.waterballoon.init.ItemInit;
import com.nyfaria.waterballoon.init.RecipeInit;

public class CommonClass {

    public static void init() {
        ItemInit.loadClass();
        EntityInit.loadClass();
        RecipeInit.loadClass();
    }
}