package com.example.dogshare.Objects;

import java.util.Map;

public class Walk {
    private String walkId;
    private String dogId;
    private String assignedWalkerId;
    private long scheduledTime;
    private String pickupLocation;
    private Map<Integer, String> status;  // 2:scheduled / 1:completed / 0:canceled
}
