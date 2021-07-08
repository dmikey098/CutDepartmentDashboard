SELECT
    PSUSER, 
    PSTEAM, 
    PSJOB,
    MIN(PSONDT),
    MIN(PSONTM),
    MAX(PSXACT)
    
FROM
    PROBASEF.WFPSI
WHERE
    PSWHS = 'LB'
    AND PSPSTS = 'A'
    AND PSTEAM <> 'IN/OUT'
GROUP BY
    PSUSER, PSTEAM, PSJOB