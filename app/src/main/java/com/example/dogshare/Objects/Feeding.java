package com.example.dogshare.Objects;

import java.util.Map;

public class Feeding {
    private String dogId;
    private long timeFed;
    private String fedByUserId;

    private double kgRemaining;
    private Map<Integer, String> status;  // 2:scheduled / 1:completed / 0:canceled
}
