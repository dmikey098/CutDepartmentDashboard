/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Daniel Mikesell
 * Created: Oct 22, 2020
 * 02/07/2021 - Added WLU(QHWLLP) column for returning the WLU.
 */

SELECT
    QHPTY,
    LEFT(QHROAN, 8),
    QDITM,
    QHTQTY,
    QHWVE,
    QHSTS,
    QHAUSR,
    CASE
        WHEN PMCMPN = 'THN' AND PMALPH <> 'PV' THEN 'BW'
        WHEN PMCMPN = 'ED' THEN 'CORD'
        WHEN PMCMPN LIKE 'ACC%' THEN 'FIBER'
        WHEN PMALPH = 'PV' THEN 'PV'
    END AS TYPE,
    PAORLN,
    PCPATP AS Reel,
    CMNAME,
    QHWLLP
FROM
    PROBASEF.WFWQH
    LEFT JOIN PROBASEF.WFWQD ON QHQNUM = QDQNUM
    LEFT OUTER JOIN PROBASEF.TFPCD ON QDINST = PAINST
    LEFT OUTER JOIN PROBASEF.TFPCH ON PAPANM = PCPANM
    LEFT OUTER JOIN HFADTAGC.PMP ON QDITM = PMPART
    LEFT JOIN HFADTAGC.CMP ON QHSHAC = CMPRKY
WHERE
    QHWHS = 'LB' AND
    QHSRCM = 'WMM' AND
    QHZONE = 'W.P&D' AND
    QHSTS <> 'R' AND
    QDSEQ = '10'
ORDER BY QHPTY
FOR FETCH ONLY