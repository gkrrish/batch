package com.batch.reader;

import org.springframework.batch.item.ItemReader;

public class CustomItemReader implements ItemReader<String> {
    @Override
    public String read() {
        // Custom logic to read the next item
        return null;
    }
}
