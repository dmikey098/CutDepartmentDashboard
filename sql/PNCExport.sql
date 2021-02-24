/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  mikesda001
 * Created: Nov 4, 2020
 */


SELECT
    SUBSTRING(THDATS, 2 , 2) AS AFYR,
    SUBSTRING(THDATS, 4 , 4) AS MMDD,
    SUBSTRING(LPAD(THTIMS, 6, '0'), 1, 2)||':'||
    SUBSTRING(LPAD(THTIMS, 6, '0'), 3, 2)||':'||
    SUBSTRING(LPAD(THTIMS, 6, '0'), 5, 2),
    THITEM,
    THTQTY,
    THLOT,
    THRSRT,
    PMCMPN,
    PMALPH
FROM
    PROBASEF.WFTRH
    LEFT JOIN HFADTAGC.PMP ON THITEM = PMPART
WHERE
    THWHS = 'LB'
    AND THRSRT = 'PNC'
    AND THTTYP IN ('PCKV','PUTV')
    AND THDATS >= ?
ORDER BY
    THDATS DESC, THTIMS DESC