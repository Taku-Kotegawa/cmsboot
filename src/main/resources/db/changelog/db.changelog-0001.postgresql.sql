-- liquibase formatted sql

-- changeset taku:1628246859811-1
CREATE TABLE document_index (id BIGINT NOT NULL, no INTEGER NOT NULL, announce_date VARCHAR(255), body TEXT, body_plain TEXT, charge_person_for_creation VARCHAR(255), charge_person_for_publish VARCHAR(255), content TEXT, created_by VARCHAR(255) NOT NULL, created_date VARCHAR(255) NOT NULL, customer_public VARCHAR(255), department_for_creation VARCHAR(255), department_for_publish VARCHAR(255), doc_category1 VARCHAR(255), doc_category2 VARCHAR(255), doc_service1 VARCHAR(255), doc_service2 VARCHAR(255), doc_service3 VARCHAR(255), document_number VARCHAR(255), file_memo VARCHAR(255), file_uuid VARCHAR(255), invalidation_date VARCHAR(255), last_modified_by VARCHAR(255) NOT NULL, last_modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, last_modified_date_str VARCHAR(255) NOT NULL, last_revised_date VARCHAR(255), pdf_uuid VARCHAR(255), public_scope VARCHAR(255), published_date VARCHAR(255), reason_for_change VARCHAR(255), remark VARCHAR(255), responsible_person_for_publish VARCHAR(255), save_revision BOOLEAN NOT NULL, status VARCHAR(255), title VARCHAR(255), type VARCHAR(255), version BIGINT NOT NULL, version_number VARCHAR(255), CONSTRAINT document_index_pkey PRIMARY KEY (id, no));

-- changeset taku:1628246859811-2
CREATE TABLE document_revision_use_stage (document_revision_rid BIGINT NOT NULL, use_stage VARCHAR(255));

-- changeset taku:1628246859811-3
CREATE TABLE file_managed (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, created_by VARCHAR(255) NOT NULL, created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, last_modified_by VARCHAR(255) NOT NULL, last_modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, version BIGINT NOT NULL, file_mime VARCHAR(255), file_size BIGINT, file_type VARCHAR(255), original_filename VARCHAR(255), status VARCHAR(255) DEFAULT '0', uri VARCHAR(255), uuid VARCHAR(255), CONSTRAINT file_managed_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-4
CREATE TABLE simple_entity_revision_combobox03 (simple_entity_revision_rid BIGINT NOT NULL, combobox03 VARCHAR(255));

-- changeset taku:1628246859811-5
CREATE TABLE select_table (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, created_by VARCHAR(255) NOT NULL, created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, last_modified_by VARCHAR(255) NOT NULL, last_modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, version BIGINT NOT NULL, select01 VARCHAR(255), select02 VARCHAR(255), status VARCHAR(255) NOT NULL, CONSTRAINT select_table_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-6
CREATE TABLE email_change_request (token VARCHAR(255) NOT NULL, expiry_date TIMESTAMP WITHOUT TIME ZONE, new_mail VARCHAR(255), secret VARCHAR(255), username VARCHAR(255), CONSTRAINT email_change_request_pkey PRIMARY KEY (token));

-- changeset taku:1628246859811-7
CREATE TABLE mail_send_history (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, body VARCHAR(255), created_by VARCHAR(255), email VARCHAR(255), username VARCHAR(255), send_time TIMESTAMP WITHOUT TIME ZONE, subject VARCHAR(255), CONSTRAINT mail_send_history_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-8
CREATE TABLE failed_email_change_request (attempt_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, token VARCHAR(255) NOT NULL, CONSTRAINT failed_email_change_request_pkey PRIMARY KEY (attempt_date, token));

-- changeset taku:1628246859811-9
CREATE TABLE person (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, created_by VARCHAR(255) NOT NULL, created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, last_modified_by VARCHAR(255) NOT NULL, last_modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, version BIGINT NOT NULL, age INTEGER, attached_file01uuid VARCHAR(255), code VARCHAR(255), content TEXT, name VARCHAR(255), status VARCHAR(255) NOT NULL, memo VARCHAR(255), CONSTRAINT person_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-10
CREATE TABLE select_max_rev (id BIGINT NOT NULL, rid BIGINT NOT NULL, CONSTRAINT select_max_rev_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-11
CREATE TABLE simple_entity_combobox03 (simple_entity_id BIGINT NOT NULL, combobox03 VARCHAR(255));

-- changeset taku:1628246859811-12
CREATE TABLE simple_entity_max_rev (id BIGINT NOT NULL, rid BIGINT NOT NULL, CONSTRAINT simple_entity_max_rev_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-13
CREATE TABLE simple_entity_revision (rid BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, created_by VARCHAR(255), created_date TIMESTAMP WITHOUT TIME ZONE, last_modified_by VARCHAR(255), last_modified_date TIMESTAMP WITHOUT TIME ZONE, rev_type INTEGER NOT NULL, version BIGINT NOT NULL, attached_file01uuid VARCHAR(255), checkbox01 VARCHAR(255), combobox01 VARCHAR(255), combobox02 VARCHAR(255), date01 date, datetime01 TIMESTAMP WITHOUT TIME ZONE, id BIGINT NOT NULL, radio01 BOOLEAN, radio02 VARCHAR(255), select01 VARCHAR(255), select03 VARCHAR(255), status VARCHAR(255) NOT NULL, text01 VARCHAR(255), text02 INTEGER, text03 FLOAT4, text04 BOOLEAN, textarea01 VARCHAR(255), CONSTRAINT simple_entity_revision_pkey PRIMARY KEY (rid));

-- changeset taku:1628246859811-14
CREATE TABLE simple_entity (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, created_by VARCHAR(255) NOT NULL, created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, last_modified_by VARCHAR(255) NOT NULL, last_modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, version BIGINT NOT NULL, attached_file01uuid VARCHAR(255), checkbox01 VARCHAR(255), combobox01 VARCHAR(255), combobox02 VARCHAR(255), date01 date, datetime01 TIMESTAMP WITHOUT TIME ZONE, radio01 BOOLEAN, radio02 VARCHAR(255), select01 VARCHAR(255), select03 VARCHAR(255), status VARCHAR(255) NOT NULL, text01 VARCHAR(255), text02 INTEGER, text03 FLOAT4, text04 SMALLINT, textarea01 VARCHAR(255), CONSTRAINT simple_entity_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-15
CREATE TABLE account (username VARCHAR(255) NOT NULL, created_by VARCHAR(255) NOT NULL, created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, last_modified_by VARCHAR(255) NOT NULL, last_modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, version BIGINT NOT NULL, allowed_ip VARCHAR(255), api_key VARCHAR(255), department VARCHAR(255), email VARCHAR(255), first_name VARCHAR(255), image_uuid VARCHAR(255), last_name VARCHAR(255), password VARCHAR(255), profile VARCHAR(1000), status VARCHAR(255), url VARCHAR(255), CONSTRAINT account_pkey PRIMARY KEY (username));

-- changeset taku:1628246859811-16
CREATE TABLE password_reissue_info (token VARCHAR(255) NOT NULL, created_by VARCHAR(255) NOT NULL, expiry_date TIMESTAMP WITHOUT TIME ZONE, secret VARCHAR(255), username VARCHAR(255), CONSTRAINT password_reissue_info_pkey PRIMARY KEY (token));

-- changeset taku:1628246859811-17
CREATE TABLE simple_entity_checkbox02 (simple_entity_id BIGINT NOT NULL, checkbox02 VARCHAR(255));

-- changeset taku:1628246859811-18
CREATE TABLE simple_entity_revision_text05 (simple_entity_revision_rid BIGINT NOT NULL, text05 VARCHAR(255));

-- changeset taku:1628246859811-19
CREATE TABLE password_history (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, created_by VARCHAR(255) NOT NULL, password VARCHAR(255), use_from TIMESTAMP WITHOUT TIME ZONE, username VARCHAR(255), CONSTRAINT password_history_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-20
CREATE TABLE document_index_use_stage (document_index_id BIGINT NOT NULL, document_index_no INTEGER NOT NULL, use_stage VARCHAR(255));

-- changeset taku:1628246859811-21
CREATE TABLE temp_file (id VARCHAR(255) NOT NULL, body OID, original_name VARCHAR(255), uploaded_date TIMESTAMP WITHOUT TIME ZONE, CONSTRAINT temp_file_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-22
CREATE TABLE simple_entity_text05 (simple_entity_id BIGINT NOT NULL, text05 VARCHAR(255));

-- changeset taku:1628246859811-23
CREATE TABLE document_access (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, access_date date, document_id BIGINT, username VARCHAR(255), CONSTRAINT document_access_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-24
CREATE TABLE document_max_rev (id BIGINT NOT NULL, rid BIGINT NOT NULL, CONSTRAINT document_max_rev_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-25
CREATE TABLE account_roles (account_username VARCHAR(255) NOT NULL, roles VARCHAR(255));

-- changeset taku:1628246859811-26
CREATE TABLE document_use_stage (document_id BIGINT NOT NULL, use_stage VARCHAR(255));

-- changeset taku:1628246859811-27
CREATE TABLE simple_entity_revision_select02 (simple_entity_revision_rid BIGINT NOT NULL, select02 VARCHAR(255));

-- changeset taku:1628246859811-28
CREATE TABLE simple_entity_revision_select04 (simple_entity_revision_rid BIGINT NOT NULL, select04 VARCHAR(255));

-- changeset taku:1628246859811-29
CREATE TABLE document_revision_files (document_revision_rid BIGINT NOT NULL, file_uuid VARCHAR(255), file_memo VARCHAR(255), pdf_uuid VARCHAR(255), type VARCHAR(255));

-- changeset taku:1628246859811-30
CREATE TABLE document (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, created_by VARCHAR(255) NOT NULL, created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, last_modified_by VARCHAR(255) NOT NULL, last_modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, version BIGINT NOT NULL, announce_date date, body TEXT, charge_person_for_creation VARCHAR(255), charge_person_for_publish VARCHAR(255), customer_public VARCHAR(255) NOT NULL, department_for_creation VARCHAR(255), department_for_publish VARCHAR(255), doc_category1 VARCHAR(255), doc_category2 VARCHAR(255), doc_service1 VARCHAR(255), doc_service2 VARCHAR(255), doc_service3 VARCHAR(255), document_number VARCHAR(255), intended_reader VARCHAR(255), invalidation_date date, last_revised_date date, public_scope VARCHAR(255) NOT NULL, published_date date, reason_for_change VARCHAR(255), remark VARCHAR(255), responsible_person_for_publish VARCHAR(255), save_revision BOOLEAN NOT NULL, status VARCHAR(255) NOT NULL, title VARCHAR(255), version_number VARCHAR(255), CONSTRAINT document_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-31
CREATE TABLE simple_entity_line_items (simple_entity_id BIGINT NOT NULL, item_name VARCHAR(255), item_no BIGINT, item_number INTEGER, unit_prise INTEGER);

-- changeset taku:1628246859811-32
CREATE TABLE access_counter (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, created_by VARCHAR(255) NOT NULL, created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, last_modified_by VARCHAR(255) NOT NULL, last_modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, version BIGINT NOT NULL, count BIGINT, status VARCHAR(255) NOT NULL, url VARCHAR(1000), CONSTRAINT access_counter_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-33
CREATE TABLE select_revision (rid BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, created_by VARCHAR(255), created_date TIMESTAMP WITHOUT TIME ZONE, last_modified_by VARCHAR(255), last_modified_date TIMESTAMP WITHOUT TIME ZONE, rev_type INTEGER NOT NULL, version BIGINT NOT NULL, id BIGINT NOT NULL, select01 VARCHAR(255) NOT NULL, select02 VARCHAR(255) NOT NULL, status VARCHAR(255) NOT NULL, CONSTRAINT select_revision_pkey PRIMARY KEY (rid));

-- changeset taku:1628246859811-34
CREATE TABLE simple_entity_revision_checkbox02 (simple_entity_revision_rid BIGINT NOT NULL, checkbox02 VARCHAR(255));

-- changeset taku:1628246859811-35
CREATE TABLE failed_authentication (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, authentication_timestamp TIMESTAMP WITHOUT TIME ZONE, username VARCHAR(255), CONSTRAINT failed_authentication_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-36
CREATE TABLE variable (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, created_by VARCHAR(255) NOT NULL, created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, last_modified_by VARCHAR(255) NOT NULL, last_modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, version BIGINT NOT NULL, code VARCHAR(255) NOT NULL, date1 date, date2 date, date3 date, date4 date, date5 date, file1uuid VARCHAR(255), remark VARCHAR(255), status VARCHAR(255), textarea TEXT, type VARCHAR(255) NOT NULL, valint1 INTEGER, valint2 INTEGER, valint3 INTEGER, valint4 INTEGER, valint5 INTEGER, value1 VARCHAR(255), value10 VARCHAR(255), value2 VARCHAR(255), value3 VARCHAR(255), value4 VARCHAR(255), value5 VARCHAR(255), value6 VARCHAR(255), value7 VARCHAR(255), value8 VARCHAR(255), value9 VARCHAR(255), CONSTRAINT variable_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-37
CREATE TABLE simple_entity_select04 (simple_entity_id BIGINT NOT NULL, select04 VARCHAR(255));

-- changeset taku:1628246859811-38
CREATE TABLE successful_authentication (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, authentication_timestamp TIMESTAMP WITHOUT TIME ZONE, created_by VARCHAR(255) NOT NULL, username VARCHAR(255), CONSTRAINT successful_authentication_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-39
CREATE TABLE simple_entity_select02 (simple_entity_id BIGINT NOT NULL, select02 VARCHAR(255));

-- changeset taku:1628246859811-40
CREATE TABLE simple_entity_revision_line_items (simple_entity_revision_rid BIGINT NOT NULL, item_name VARCHAR(255), item_no BIGINT, item_number INTEGER, unit_prise INTEGER);

-- changeset taku:1628246859811-41
CREATE TABLE permission_role (permission VARCHAR(255) NOT NULL, role VARCHAR(255) NOT NULL, CONSTRAINT permission_role_pkey PRIMARY KEY (permission, role));

-- changeset taku:1628246859811-42
CREATE TABLE document_files (document_id BIGINT NOT NULL, file_uuid VARCHAR(255), file_memo VARCHAR(255), pdf_uuid VARCHAR(255), type VARCHAR(255), files_order INTEGER NOT NULL, CONSTRAINT document_files_pkey PRIMARY KEY (document_id, files_order));

-- changeset taku:1628246859811-43
CREATE TABLE failed_password_reissue (attempt_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, token VARCHAR(255) NOT NULL, created_by VARCHAR(255) NOT NULL, CONSTRAINT failed_password_reissue_pkey PRIMARY KEY (attempt_date, token));

-- changeset taku:1628246859811-44
CREATE TABLE document_revision (rid BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, created_by VARCHAR(255), created_date TIMESTAMP WITHOUT TIME ZONE, last_modified_by VARCHAR(255), last_modified_date TIMESTAMP WITHOUT TIME ZONE, rev_type INTEGER NOT NULL, version BIGINT NOT NULL, announce_date date, body TEXT, charge_person_for_creation VARCHAR(255), charge_person_for_publish VARCHAR(255), customer_public VARCHAR(255), department_for_creation VARCHAR(255), department_for_publish VARCHAR(255), doc_category1 VARCHAR(255), doc_category2 VARCHAR(255), doc_service1 VARCHAR(255), doc_service2 VARCHAR(255), doc_service3 VARCHAR(255), document_number VARCHAR(255), id BIGINT, intended_reader VARCHAR(255), invalidation_date date, last_revised_date date, public_scope VARCHAR(255), published_date date, reason_for_change VARCHAR(255), remark VARCHAR(255), responsible_person_for_publish VARCHAR(255), save_revision BOOLEAN NOT NULL, status VARCHAR(255), title VARCHAR(255), version_number VARCHAR(255), CONSTRAINT document_revision_pkey PRIMARY KEY (rid));

-- changeset taku:1628246859811-45
CREATE TABLE workflow (id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, created_by VARCHAR(255) NOT NULL, created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, last_modified_by VARCHAR(255) NOT NULL, last_modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, version BIGINT NOT NULL, employee_id BIGINT NOT NULL, entity_id BIGINT NOT NULL, entity_type VARCHAR(255) NOT NULL, step_no INTEGER NOT NULL, step_status INTEGER NOT NULL, weight INTEGER DEFAULT 0 NOT NULL, CONSTRAINT workflow_pkey PRIMARY KEY (id));

-- changeset taku:1628246859811-46
ALTER TABLE document_index_use_stage ADD CONSTRAINT fkleu7m83uqab8mmh7ed4odv9qj FOREIGN KEY (document_index_id, document_index_no) REFERENCES document_index (id, no) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-47
ALTER TABLE file_managed ADD CONSTRAINT idx_02 UNIQUE (uri);

-- changeset taku:1628246859811-48
ALTER TABLE account_roles ADD CONSTRAINT fksa8fblleof2c8aqce4x3n0yy5 FOREIGN KEY (account_username) REFERENCES account (username) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-49
ALTER TABLE simple_entity_checkbox02 ADD CONSTRAINT fkrmv44v1sgjx7l30nrw3erf0re FOREIGN KEY (simple_entity_id) REFERENCES simple_entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-50
ALTER TABLE simple_entity_revision_text05 ADD CONSTRAINT fkkmxcf81bd8vgmasf649wlg1ux FOREIGN KEY (simple_entity_revision_rid) REFERENCES simple_entity_revision (rid) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-51
ALTER TABLE simple_entity_text05 ADD CONSTRAINT fkeaiyyh79glf138bqsneolhd9k FOREIGN KEY (simple_entity_id) REFERENCES simple_entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-52
ALTER TABLE simple_entity_revision_select02 ADD CONSTRAINT fks6pq89gb97b3v5bdebnkp2dda FOREIGN KEY (simple_entity_revision_rid) REFERENCES simple_entity_revision (rid) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-53
ALTER TABLE simple_entity_revision_select04 ADD CONSTRAINT fkoxks4uuxq3yxse7iydlvfvd5w FOREIGN KEY (simple_entity_revision_rid) REFERENCES simple_entity_revision (rid) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-54
ALTER TABLE document_files ADD CONSTRAINT fk1b767qp1gkma8nuq8en8oygen FOREIGN KEY (document_id) REFERENCES document (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-55
ALTER TABLE document_use_stage ADD CONSTRAINT fk6e2wqg1fhhig392vq9edjr0g7 FOREIGN KEY (document_id) REFERENCES document (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-56
ALTER TABLE simple_entity_line_items ADD CONSTRAINT fk9p2ff74vnn6dpq4gu0edaw9iw FOREIGN KEY (simple_entity_id) REFERENCES simple_entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-57
ALTER TABLE access_counter ADD CONSTRAINT idx_01 UNIQUE (url);

-- changeset taku:1628246859811-58
ALTER TABLE simple_entity_revision_checkbox02 ADD CONSTRAINT fkjj2hm7b5c3xv6foi51n20wy06 FOREIGN KEY (simple_entity_revision_rid) REFERENCES simple_entity_revision (rid) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-59
ALTER TABLE simple_entity_select04 ADD CONSTRAINT fkg0f3xp1brlfmr8etthrvaq2el FOREIGN KEY (simple_entity_id) REFERENCES simple_entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-60
ALTER TABLE simple_entity_select02 ADD CONSTRAINT fk221mpw54qo7ostpnqtecb4jv5 FOREIGN KEY (simple_entity_id) REFERENCES simple_entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-61
ALTER TABLE simple_entity_revision_line_items ADD CONSTRAINT fkm74nkmtffqq3pnaffiwumrk5h FOREIGN KEY (simple_entity_revision_rid) REFERENCES simple_entity_revision (rid) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-62
ALTER TABLE document_revision_use_stage ADD CONSTRAINT fkeadlw1pajfi4lne0evc3edewq FOREIGN KEY (document_revision_rid) REFERENCES document_revision (rid) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-63
ALTER TABLE document_revision_files ADD CONSTRAINT fkfb6pqvb7xwca2c56ubwpjmt8i FOREIGN KEY (document_revision_rid) REFERENCES document_revision (rid) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-64
ALTER TABLE simple_entity_revision_combobox03 ADD CONSTRAINT fkos8picdxl023m189uqeyut1yr FOREIGN KEY (simple_entity_revision_rid) REFERENCES simple_entity_revision (rid) ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset taku:1628246859811-65
ALTER TABLE simple_entity_combobox03 ADD CONSTRAINT fkilbmxju7vidne5iwurrto4fn2 FOREIGN KEY (simple_entity_id) REFERENCES simple_entity (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
