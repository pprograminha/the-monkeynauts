CREATE EXTENSION IF NOT EXISTS "pg_cron" WITH SCHEMA extensions;

SELECT cron.schedule('0 0 * * *',
    $$UPDATE themonkeynauts."monkeynaut" SET current_energy = (CASE WHEN (current_energy + max_energy/2) > max_energy THEN max_energy ELSE (current_energy + max_energy/2) END)$$);

SELECT cron.schedule('0 12 * * *',
    $$UPDATE themonkeynauts."monkeynaut" SET current_energy = (CASE WHEN (current_energy + max_energy/2) > max_energy THEN max_energy ELSE (current_energy + max_energy/2) END)$$);

SELECT cron.schedule('0 0 * * *',
    $$UPDATE themonkeynauts."user" SET last_bounty_hunting = NULL$$);
