-- Create tasks table
CREATE TABLE tasks (
                       id BIGINT NOT NULL ,
                       name VARCHAR(100) NOT NULL,
                       description VARCHAR(500),
                       created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                       category VARCHAR(10) NOT NULL,
                       status VARCHAR(20) NOT NULL,
                       user_id BIGINT,

    -- Columns for Bug subclass
                       severity VARCHAR(20),
                       steps_to_reproduce TEXT,

    -- Columns for Feature subclass
                       deadline DATE,
                       business_value VARCHAR(500),

                       CONSTRAINT pk_tasks PRIMARY KEY (id),
                       CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create indexes
CREATE INDEX idx_tasks_user_id ON tasks(user_id);
CREATE INDEX idx_tasks_category ON tasks(category);
CREATE INDEX idx_tasks_status ON tasks(status);