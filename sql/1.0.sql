DROP TABLE IF EXISTS `SHELL_EXECUTOR`.`task`;
CREATE TABLE `SHELL_EXECUTOR`.`task`
(
    `id`            INT           NOT NULL AUTO_INCREMENT,
    `name`          VARCHAR(128)  NOT NULL,
    `seq`           INT           NOT NULL,
    `description`   VARCHAR(1024) NULL,
    `script`        VARCHAR(512)  NULL,
    `success_msg`   VARCHAR(256)  NULL,
    `group_id`      INT           NOT NULL,
    `active`        VARCHAR(1)    NOT NULL DEFAULT 'Y',
    `last_run_time` datetime      NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `index1` (`seq` ASC, `group_id` ASC)
);

DROP TABLE IF EXISTS `SHELL_EXECUTOR`.`task_group`;
CREATE TABLE `SHELL_EXECUTOR`.`task_group`
(
    `id`     INT          NOT NULL AUTO_INCREMENT,
    `name`   VARCHAR(128) NOT NULL,
    `seq`    INT          NOT NULL,
    `is_default` VARCHAR(1)   NOT NULL DEFAULT 'N',
    `active` VARCHAR(1)   NOT NULL DEFAULT 'Y',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC),
    UNIQUE INDEX `seq_UNIQUE` (`seq` ASC)
);

DROP TABLE IF EXISTS `SHELL_EXECUTOR`.`config`;
CREATE TABLE `SHELL_EXECUTOR`.`config`
(
    `id`    INT          NOT NULL AUTO_INCREMENT,
    `key`   VARCHAR(128) NOT NULL,
    `value` VARCHAR(256) NOT NULL,
    `seq`   INT          NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `index1` (`key` ASC, `seq` ASC)
);

DROP TABLE IF EXISTS `SHELL_EXECUTOR`.`task_param`;
CREATE TABLE `SHELL_EXECUTOR`.`task_param`
(
    `id`              INT           NOT NULL AUTO_INCREMENT,
    `task_id`         INT           NOT NULL,
    `name`            VARCHAR(128)  NOT NULL,
    `type`            VARCHAR(128)  NOT NULL,
    `available_value` VARCHAR(1024) NULL,
    `default_value`   VARCHAR(128)  NULL,
    `seq`             INT           NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `index1` (`task_id` ASC, `seq` ASC)
);

DROP TABLE IF EXISTS `SHELL_EXECUTOR`.`task_history`;
CREATE TABLE `SHELL_EXECUTOR`.`task_history`
(
    `id`       BIGINT       NOT NULL AUTO_INCREMENT,
    `task_id`  INT          NOT NULL,
    `user`     VARCHAR(128) NULL,
    `ip`       VARCHAR(128) NOT NULL,
    `datetime` DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `SHELL_EXECUTOR`.`history_param`;
CREATE TABLE `SHELL_EXECUTOR`.`history_param`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `history_id` BIGINT       NOT NULL,
    `param_id`   INT          NOT NULL,
    `value`      VARCHAR(128) NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `unique` (`history_id` ASC, `param_id` ASC)
);
