/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  mikesda001
 * Created: Jan 8, 2021
 */

SELECT
    THUSRS, 
    THTEAM,
    CASE WHEN THTEAM = 'LLRRPL' THEN COUNT(THHSEQ) / 2 ELSE COUNT(THHSEQ) END
FROM
	PROBASEF.WFTRH
WHERE
    THWHS = 'LB'
    AND NOT(THTEAM IN ('', 'WIM301'))
    AND THTEAM IN ('CUTTER', 'SHUTTL', 'LLRRPL')
    AND THUSRS = ?
    AND (
            (
                THUSRS IN ('LBQULBM','LBQUJJS','LBQUZAG','LBQUDWM','LBQUDJR','LBQUZMG')
                AND ((THDATS = ? AND THTIMS > 120000) OR (THDATS = ? AND THTIMS < 115959))
            ) OR (
                NOT(THUSRS IN ('LBQULBM','LBQUJJS','LBQUZAG','LBQUDWM','LBQUDJR','LBQUZMG'))
                AND THDATS = ?
            )
    )
GROUP BY
    THUSRS, THTEAM
ORDER BY
    THTEAM, COUNT(THHSEQ)