SELECT
    PTY,
    MAX(QHROAN) AS ORD,
    ''''||OLCP AS LP,
    QDITM AS PN,
    PMDESC,
    CASE WHEN ASL IS NULL THEN '100' ELSE ASL END AS ASL,
    CASE WHEN BAY IS NULL THEN 'CUT' ELSE BAY END AS BAY,
    CASE WHEN LVL IS NULL THEN '' ELSE LVL END AS LVL,
    LDOHQ AS OHQ,
    (
        SELECT
            COUNT(TIOLCP)
        FROM
            PROBASEF.WFWQH 
            INNER JOIN PROBASEF.WFWQD ON QHQNUM = QDQNUM
            INNER JOIN PROBASEF.WFTIN ON QDINST = TIINST
        WHERE
            QHWHS = 'LB'
            AND QHSRCM = 'WMM'
            AND QHZONE = 'W.CUT'
            AND TIOLCP = OLCP
        GROUP BY
            TIOLCP
        ) AS CUTSONLP,
    CASE WHEN LDOHQ - MIN(QHTQTY) <= ? THEN 'SP' ELSE '' END AS SP
FROM
    (SELECT
        QHPTY AS PTY,
        RPPTY,
        QHROAN,
        TIOLCP AS OLCP,
        QDITM,
        ASL,
        BAY,
        LVL,
        QHTQTY,
        LDOHQ,
        CASE WHEN LDOHQ - QHTQTY <= ? THEN 'SP' ELSE '' END
    FROM
        PROBASEF.WFWQH 
        INNER JOIN PROBASEF.WFWQD ON QHQNUM = QDQNUM
        INNER JOIN PROBASEF.WFTIN ON QDINST = TIINST
        INNER JOIN (
            SELECT LDLOT, LDOHQ
            FROM PROBASEF.WFLOD
            WHERE LDOHQ <> 0
        ) ON (TILOT = LDLOT)
        LEFT OUTER JOIN (
            SELECT
                QHPTY AS RPPTY,
                TIOLCP AS LP,
                QHASLE AS ASL,
                QHBAY AS BAY,
                QHLVL AS LVL
            FROM
                PROBASEF.WFWQH 
                LEFT OUTER JOIN PROBASEF.WFWQD ON QHQNUM = QDQNUM
                LEFT OUTER JOIN PROBASEF.WFTIN ON QDINST = TIINST
            WHERE
                QHWHS = 'LB'
                AND QHSRCM = 'RPLN'
        ) ON TIOLCP = LP
    WHERE
        QHWHS = 'LB'
        AND QHSRCM = 'WMM'
        AND QHZONE = 'W.CUT'
        AND QHPTY >= (
            SELECT
                QHPTY
            FROM
                PROBASEF.WFWQH 
                LEFT OUTER JOIN PROBASEF.WFWQD ON QHQNUM = QDQNUM
                LEFT OUTER JOIN PROBASEF.WFTIN ON QDINST = TIINST
            WHERE
                QHWHS = 'LB'
                AND QHSRCM = 'RPLN'
            ORDER BY
                QHPTY
            FETCH FIRST 1 ROWS ONLY
        )
        AND QHPTY BETWEEN ? AND ?
    ORDER BY
        QHPTY
    ) 
    LEFT JOIN HFADTAGC.PMP ON PMPART = QDITM
GROUP BY
    PTY,
    OLCP,
    QDITM,
    PMDESC,
    ASL,
    BAY,
    LVL,
    LDOHQ
ORDER BY
    PTY