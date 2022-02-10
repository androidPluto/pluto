package com.sampleapp;

import com.pluto.Pluto;

public class JavaTest {

    public void showNotch(boolean state) {
        Pluto.INSTANCE.showNotch(state);
    }

    public void open(String id) {
        Pluto.INSTANCE.open(id);
    }

    public void open() {
        Pluto.INSTANCE.open();
    }
}
