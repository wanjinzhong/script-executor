package com.synnex.shellexecutor.enums;

public enum YorN {
    Y, N;
    public static YorN of(String s) {
        if (s == null || s.trim().length() == 0) {
            return N;
        } else {
            return YorN.valueOf(s.trim().toUpperCase());
        }
    }
}
