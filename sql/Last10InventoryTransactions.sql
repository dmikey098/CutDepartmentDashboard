/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Daniel Mikesell
 * Created: Dec 8, 2020
 */

SELECT
    THASLE,
    THBAY,
    THLVL,
    THITEM,
    THOLCP,
    THLOT,
    CASE WHEN THTEAM = 'CUTTER' THEN 'CUT' ELSE THRSRT END,
    THTTYP,
    THUSRS,
    THTQTY,
    THDATS,
    THTIMS
FROM
    PROBASEF.WFTRH
WHERE
    THWHS = 'LB'
    AND (THRSRT IN ('STR','PNC','CRD','CYC') OR THTEAM = 'CUTTER')
    AND THTTYP IN ('PCKV', 'PUTV')
ORDER BY
    THHSEQ DESC
LIMIT 10