package com.example.smellgood.provider;

import android.provider.BaseColumns;
public interface Provider {
    public interface Note extends BaseColumns {
        public static final String TABLE_NAME = "scoreboard";
        public static final String NICKNAME = "nickname";
        public static final String TIME = "time";
    }
}


