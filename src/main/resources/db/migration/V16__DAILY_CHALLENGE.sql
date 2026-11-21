ALTER TABLE challenges ALTER COLUMN creator_id DROP NOT NULL;   -- NULL = system-made
ALTER TABLE challenges ADD COLUMN daily_date DATE UNIQUE;       -- NULL for friend challenges