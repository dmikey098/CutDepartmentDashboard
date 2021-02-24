/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Daniel Mikesell
 * Created: Nov 2, 2020
 */

SELECT 
    THHSEQ,
    THDATS, 
    THUSRS,
    THLOT,
    THOLCP,
    THASLE,
    THBAY,
    THLVL,
    LDOHQ,
    COMT,
    QTY,
    QTY - LDOHQ 
FROM 
    PROBASEF.WFTRH 
    INNER JOIN 
        (SELECT 
            TCHSEQ, 
            SUBSTR(TCCOMT, LOCATE(' ',TCCOMT) + 1) AS QTY, 
            TCCOMT AS COMT 
        FROM  
            PROBASEF.WFTRC 
        WHERE 
            TCCOMT <> ''     
            AND SUBSTR(TCCOMT, LOCATE(' ',TCCOMT) + 1) <> '' 
        ) ON (THHSEQ = TCHSEQ) 
    INNER JOIN PROBASEF.WFLOD ON (THLOT = LDLOT) 
WHERE 
    THWHS = 'LB' 
    AND THDATS > 1201014
    AND THUSRS IN ('LBQUDJR', 'LBQUDWM', 'LBQUZMG') 
    AND THASLE <> '100' 
    AND THASLE <> 'SRD' 
    AND THTTYP = 'LOCA' 
    AND THLOT <> '' 
    AND QTY - LDOHQ <> 0
    AND LDOHQ <> 0
    AND NOT(THHSEQ IN (<THHSEQ>))