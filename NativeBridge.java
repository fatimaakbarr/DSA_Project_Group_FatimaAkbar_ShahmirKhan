package com.campusconnect;

public class NativeBridge {
    // Load the DLL
    static {
        System.loadLibrary("YourNativeLib"); // name without .dll
    }

    // Declare native methods
    public native void someNativeMethod();

    public void callNative() {
        someNativeMethod();
    }
}
