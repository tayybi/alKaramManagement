package com.aktechzone.propertydeal.FontSetting;

public final class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "lato_light.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "lato_light.ttf");
    }
}
