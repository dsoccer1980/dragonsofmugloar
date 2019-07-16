CREATE SEQUENCE global_seq START 100;

CREATE TABLE Message
(
  message_id  INT PRIMARY KEY DEFAULT nextval('global_seq'),
  ad_id       VARCHAR(255),
  message     VARCHAR(255),
  reward      VARCHAR(255),
  expires_in  INT,
  probability VARCHAR(255)
);

CREATE TABLE Ad_Solution
(
  ad_id   VARCHAR(255) PRIMARY KEY,
  success INT,
  fail    INT
);
