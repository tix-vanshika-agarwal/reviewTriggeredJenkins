SELECT *
FROM `${TABLE}`
WHERE TIMESTAMP_TRUNC(timestamp, DAY) = TIMESTAMP("${DATE}")
  AND user_identity = "${IDENTITY}"
  AND activity_type IN (${ACTIVITY_TYPES})
  AND request LIKE "%${REQUEST_KEY}%"
ORDER BY timestamp DESC
LIMIT ${LIMIT};