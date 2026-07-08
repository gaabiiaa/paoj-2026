package com.pao.laboratory11.exercise3;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class Snapshot {
    private final Map<String, Long> countByCountry;
    private final Map<String, Long> countByChannel;
    private final double totalAmount;
    private final List<Transaction> topTransactions;

    public Snapshot(Map<String, Long> countByCountry,
                    Map<String, Long> countByChannel,
                    double totalAmount,
                    List<Transaction> topTransactions) {
        this.countByCountry = Collections.unmodifiableMap(countByCountry);
        this.countByChannel = Collections.unmodifiableMap(countByChannel);
        this.totalAmount = totalAmount;
        this.topTransactions = List.copyOf(topTransactions);
    }

    public Map<String, Long> getCountByCountry() { return countByCountry; }
    public Map<String, Long> getCountByChannel() { return countByChannel; }
    public double getTotalAmount() { return totalAmount; }
    public List<Transaction> getTopTransactions() { return topTransactions; }
}
