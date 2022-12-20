-- -------------------------------------------------------------------------------------------------
-- list match
SELECT * FROM wc_match
WHERE DATE <= 20221129
ORDER BY DATE DESC, start_time DESC;

-- list match with teams information
SELECT m.date, t1.name, t2.name, m.team1goals, m.team2goals, m.slack_message_id
FROM wc_match AS m
         INNER JOIN wc_team t1 ON m.team1id = t1.id
         INNER JOIN wc_team t2 ON m.team2id = t2.id
WHERE DATE > 20221129
ORDER BY DATE DESC, start_time DESC;

-- list user's bet
SELECT m.id AS match_id, m.date AS DATE, m.start_time AS startTime, t1.name AS team1Name, t2.name AS team2Name, m.result AS matchResult, mub.result AS betResult, mub.bet AS userBet, mub.status AS betStatus, mub.created_at AS betAt
FROM wc_match_user_bet mub
    INNER JOIN wc_match m ON mub.match_id = m.id
    INNER JOIN wc_team t1 ON m.team1Id = t1.id
    INNER JOIN wc_team t2 ON m.team2Id = t2.id
WHERE mub.user_id = 'U025CNW6RNJ'
ORDER BY m.date DESC, m.start_time DESC;

-- list user's bet with bet team's name
SELECT m.id AS match_id, m.date AS DATE, m.start_time AS startTime, t1.name AS team1Name, t2.name AS team2Name, m.result AS matchResult, mub.result AS betResult, mub.bet AS userBet, tx.name AS userBetTeam, mub.status AS betStatus, mub.created_at AS betAt
FROM wc_match_user_bet mub
    INNER JOIN wc_match m ON mub.match_id = m.id
    INNER JOIN wc_team t1 ON m.team1Id = t1.id
    INNER JOIN wc_team t2 ON m.team2Id = t2.id
    LEFT JOIN wc_team tx ON mub.bet = tx.id
WHERE mub.user_id = 'U025CNW6RNJ'
ORDER BY m.date DESC, m.start_time DESC;

-- -------------------------------------------------------------------------------------------------
-- UPDATE wc_team SET created_at = UNIX_TIMESTAMP() * 1000, updated_at = UNIX_TIMESTAMP() * 1000;
-- UPDATE wc_tournament SET created_at = UNIX_TIMESTAMP() * 1000, updated_at = UNIX_TIMESTAMP() * 1000;
-- UPDATE wc_tournament_team SET created_at = UNIX_TIMESTAMP() * 1000, updated_at = UNIX_TIMESTAMP() * 1000;
-- UPDATE wc_match SET created_at = UNIX_TIMESTAMP() * 1000, updated_at = UNIX_TIMESTAMP() * 1000;