-- -------------------------------------------------------------------------------------------------
SELECT m.date, t1.name, t2.name, m.team1goals, m.team2goals, m.slack_message_id
FROM wc_match AS m
         INNER JOIN wc_team t1 ON m.team1id = t1.id
         INNER JOIN wc_team t2 ON m.team2id = t2.id
WHERE DATE <= 20221129
ORDER BY DATE DESC, start_time DESC;

SELECT * FROM wc_match
WHERE DATE <= 20221129
ORDER BY DATE DESC, start_time DESC;

-- -------------------------------------------------------------------------------------------------
UPDATE wc_team SET created_at = UNIX_TIMESTAMP() * 1000, updated_at = UNIX_TIMESTAMP() * 1000;
UPDATE wc_tournament SET created_at = UNIX_TIMESTAMP() * 1000, updated_at = UNIX_TIMESTAMP() * 1000;
UPDATE wc_tournament_team SET created_at = UNIX_TIMESTAMP() * 1000, updated_at = UNIX_TIMESTAMP() * 1000;
UPDATE wc_match SET created_at = UNIX_TIMESTAMP() * 1000, updated_at = UNIX_TIMESTAMP() * 1000;