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
    `id`         INT          NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(128) NOT NULL,
    `seq`        INT          NOT NULL,
    `is_default` VARCHAR(1)   NOT NULL DEFAULT 'N',
    `active`     VARCHAR(1)   NOT NULL DEFAULT 'Y',
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
CREATE TABLE `user`
(
    `userid`          int(11) NOT NULL,
    `loginid`         varchar(20) COLLATE utf8_bin DEFAULT NULL,
    `lastname`        varchar(25) COLLATE utf8_bin DEFAULT NULL,
    `firstname`       varchar(25) COLLATE utf8_bin DEFAULT NULL,
    `middlename`      varchar(25) COLLATE utf8_bin DEFAULT NULL,
    `altname`         varchar(50) COLLATE utf8_bin DEFAULT NULL,
    `title`           varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT 'From the global_employee(workingTitle)',
    `phone`           varchar(25) COLLATE utf8_bin DEFAULT NULL,
    `managerid`       int(11)                      DEFAULT NULL,
    `hiredate`        datetime                     DEFAULT NULL,
    `termdate`        datetime                     DEFAULT NULL,
    `def_loc`         int(11)                      DEFAULT NULL,
    `company_no`      int(11)                      DEFAULT NULL,
    `cost_center`     int(11)                      DEFAULT NULL,
    `user_loc`        int(11)                      DEFAULT NULL,
    `job_code`        int(11)                      DEFAULT NULL,
    `global_id`       int(11)                      DEFAULT NULL,
    `support_company` int(11)                      DEFAULT NULL,
    `fusion_id`       int(11)                      DEFAULT NULL,
    `email_address`   varchar(50) COLLATE utf8_bin DEFAULT NULL,
    `authoritylevel`  int(11)                      DEFAULT NULL COMMENT 'From the global_job_code(authorityLevel)',
    `project_name`    varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'From the msa_project(project_name) ',
    `shift`           varchar(10) COLLATE utf8_bin DEFAULT NULL,
    `h_version`       int(11)                      DEFAULT '0' COMMENT 'for hibernate use',
    PRIMARY KEY (`userid`),
    KEY `hyve_userI1` (`userid`) USING BTREE,
    KEY `hyve_userI2` (`managerid`) USING BTREE,
    KEY `hyve_userI3` (`loginid`) USING BTREE,
    KEY `hyve_userI4` (`def_loc`) USING BTREE,
    KEY `hyve_userI5` (`cost_center`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;
CREATE TABLE `SHELL_EXECUTOR`.`ip_table`
(
    `id`              INT          NOT NULL AUTO_INCREMENT,
    `name`            VARCHAR(128) NOT NULL,
    `ip`              VARCHAR(128) NOT NULL,
    `update_datetime` DATETIME     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `unique` (`ip` ASC),
    INDEX `index1` (`name` ASC)
);
