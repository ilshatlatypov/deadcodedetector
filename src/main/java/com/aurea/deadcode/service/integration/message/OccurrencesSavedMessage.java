package com.aurea.deadcode.service.integration.message;

/**
 * Created by ilshat on 29.03.17.
 */
public class OccurrencesSavedMessage {
    public Long id;

    public OccurrencesSavedMessage(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
