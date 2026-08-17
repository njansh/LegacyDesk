-- Baseline system initialization migration
CREATE TABLE system_metadata (
                                 id VARCHAR(50) NOT NULL PRIMARY KEY,
                                 system_version VARCHAR(20) NOT NULL,
                                 initialized_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

INSERT INTO system_metadata (id, system_version)
VALUES ('SYSTEM_INIT', '1.0.0');