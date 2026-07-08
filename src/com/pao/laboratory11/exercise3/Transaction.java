package com.pao.laboratory11.exercise3;

public final class Transaction {
    private final int id;
    private final double amount;
    private final String date;
    private final String country;
    private final String channel;

    public Transaction(int id, double amount, String date, String country, String channel) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.country = country;
        this.channel = channel;
    }

    public int getId() { return id; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getCountry() { return country; }
    public String getChannel() { return channel; }

    @Override
    public String toString() {
        return "Tx[" + id + "] amount=" + String.format(java.util.Locale.US, "%.2f", amount)
                + " country=" + country + " channel=" + channel;
    }
}
