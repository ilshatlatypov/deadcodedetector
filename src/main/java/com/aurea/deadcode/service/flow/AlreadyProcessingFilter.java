package com.aurea.deadcode.service.flow;

import com.aurea.deadcode.service.flow.message.GitHubRepoPayload;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;

/**
 * Created by ilshat on 28.03.17.
 */
public class AlreadyProcessingFilter implements MessageSelector {
    @Override
    public boolean accept(Message<?> message) {
        return !((GitHubRepoPayload) message.getPayload()).isAlreadyProcessing();
    }
}
