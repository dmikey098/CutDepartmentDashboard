/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  mikesda001
 * Created: Oct 25, 2020
 */

SELECT 
    QHPTY,
    QHROAN,    
    QDITM,
    QDQTY,
    QHCR,
    QHWVE,
    QHSTS,
    QHAUSR,
    PAORLN,
    QDASLE,    
    QDBAY,
    QDLVL,
    T5.F2 AS OD,
    PMPKG,
    CASE
        WHEN PMCMPN = 'THN' AND PMALPH <> 'PV' THEN 'BW'
        WHEN PMCMPN = 'ED' THEN 'CORD'
        WHEN PMCMPN LIKE 'ACC%' THEN 'FIBER'
        WHEN PMALPH = 'PV' THEN 'PV'
    END AS TYPE
FROM 
    PROBASEF.WFWQH 
    INNER JOIN PROBASEF.WFWQD ON QHQNUM = QDQNUM 
    INNER JOIN PROBASEF.TFPCD ON QDINST = PAINST  
    INNER JOIN HFADTAGC.PMP ON QDITM = PMPART
    INNER JOIN BBQYXXX.CABLESIZE3 T5 ON PMPART = T5.F1
WHERE  
    QHWHS = 'LB' 
    AND QHSRCM = 'PICK' 
    AND QHWINC = 'PAL'
    AND QHSTS <> 'R' 
ORDER BY
    QHPTY
FOR FETCH ONLY