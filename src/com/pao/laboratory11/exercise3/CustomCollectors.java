package com.pao.laboratory11.exercise3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class CustomCollectors {
    private CustomCollectors() {}

    public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
        class Agg {
            Map<String, Long> byCountry = new HashMap<>();
            Map<String, Long> byChannel = new HashMap<>();
            double total = 0.0;
            List<Transaction> all = new ArrayList<>();
        }

        Supplier<Agg> supplier = Agg::new;

        BiConsumer<Agg, Transaction> accumulator = (agg, tx) -> {
            agg.byCountry.put(tx.getCountry(), agg.byCountry.getOrDefault(tx.getCountry(), 0L) + 1L);
            agg.byChannel.put(tx.getChannel(), agg.byChannel.getOrDefault(tx.getChannel(), 0L) + 1L);
            agg.total += tx.getAmount();
            agg.all.add(tx);
        };

        BinaryOperator<Agg> combiner = (a, b) -> {
            b.byCountry.forEach((k, v) -> a.byCountry.put(k, a.byCountry.getOrDefault(k, 0L) + v));
            b.byChannel.forEach((k, v) -> a.byChannel.put(k, a.byChannel.getOrDefault(k, 0L) + v));
            a.total += b.total;
            a.all.addAll(b.all);
            return a;
        };

        Function<Agg, Snapshot> finisher = agg -> {
            // compute topN by amount desc, id asc
            agg.all.sort((t1, t2) -> {
                int cmp = Double.compare(t2.getAmount(), t1.getAmount());
                if (cmp != 0) return cmp;
                return Integer.compare(t1.getId(), t2.getId());
            });
            List<Transaction> top = agg.all.subList(0, Math.min(topN, agg.all.size()));
            // make defensive copies of maps
            Map<String, Long> byCountry = new HashMap<>(agg.byCountry);
            Map<String, Long> byChannel = new HashMap<>(agg.byChannel);
            return new Snapshot(byCountry, byChannel, agg.total, top);
        };

        return Collector.of(supplier, accumulator, combiner, finisher);
    }
}
