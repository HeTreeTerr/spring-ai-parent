CREATE TABLE IF NOT EXISTS SPRING_AI_CHAT_MEMORY(
    `conversation_id` VARCHAR(36) NOT NULL,
    `content` text NOT NULL,
    `type` VARCHAR(10) NOT NULL,
    `timestamp` datetime NOT NULL,
    INDEX `idx_conversation_id_timestamp` (`conversation_id`,`timestamp`)
);