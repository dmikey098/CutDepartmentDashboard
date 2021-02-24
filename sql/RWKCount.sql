/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Daniel Mikesell
 * Created: Nov 10, 2020
 */

SELECT
    SUM(CASE WHEN PMPKG <> 1 THEN LDOHQ / PMPKG ELSE 1 END)
FROM
    PROBASEF.WFLOD
    LEFT JOIN (SELECT PMPKG, PMPART FROM HFADTAGC.PMP) ON LDITEM = PMPART
WHERE
    LDWHS = 'LB'
    AND LDASLE = 'RWK'
    AND LDBAY = '002'
    AND LDISTS = 'RPK'
GROUP BY
    LDASLE, LDBAY