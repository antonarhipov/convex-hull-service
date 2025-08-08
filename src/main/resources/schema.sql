create extension if not exists "pgcrypto";

create table if not exists executions (
    id uuid primary key default gen_random_uuid(),
    created_at timestamptz not null,
    algorithm text not null,
    input text not null,
    result text not null,
    duration_ms bigint not null
);

create index if not exists idx_executions_created_at on executions (created_at desc);
