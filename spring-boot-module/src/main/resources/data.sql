DROP TABLE IF EXISTS AuditItem;
DROP TABLE IF EXISTS Item;
CREATE TABLE IF NOT EXISTS Item
(
    id      BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name    VARCHAR(40)                    NOT NULL,
    status  VARCHAR(15)                    NOT NULL,
    deleted BOOLEAN DEFAULT FALSE          NOT NULL
);
CREATE TABLE IF NOT EXISTS AuditItem
(
    id     BIGINT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    action VARCHAR(15)                    NOT NULL,
    date   DATETIME                       NOT NULL
);
ALTER TABLE AuditItem
    ADD item_id BIGINT UNSIGNED NOT NULL,
    ADD FOREIGN KEY (item_id) REFERENCES Item (id);