DROP TABLE IF EXISTS artefact;

CREATE TABLE artefacts (
-- id uuid default random_uuid()
  id UUID DEFAULT RANDOM_UUID() NOT NULL PRIMARY KEY,
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
  user_id VARCHAR(250),
  category VARCHAR(250),
  description VARCHAR(250)
);

INSERT INTO artefacts(user_id, category, description) VALUES
  ('1', 'Food', 'Nice food'),
  ('2', 'Food', 'Bad food'),
  ('2', 'Water', 'Oil');

DROP TABLE IF EXISTS commentaries;

CREATE TABLE commentaries (
  id UUID DEFAULT RANDOM_UUID() NOT NULL PRIMARY KEY,
  artefact_id UUID,
  foreign key (artefact_id) references artefacts(id),
  user_id VARCHAR(250),
  content VARCHAR(250)
);

SET @first_artifact = SELECT ID FROM artefacts limit 1;

INSERT INTO commentaries (artefact_id, user_id, content) VALUES
(@first_artifact, '1', 'Left'),
(@first_artifact, '2', 'Right');