package org.example.service.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 探针状态记录
 */
@Data
public class ProbeStatus {
    private final String probeId;
    private volatile boolean isOnline;
    private volatile LocalDateTime lastHeartbeat;
    private final AtomicLong collectedMetrics = new AtomicLong();

    public ProbeStatus(String probeId) {
        this.probeId = probeId;
        this.isOnline = true;
        this.lastHeartbeat = LocalDateTime.now();
    }
}