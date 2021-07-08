/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Daniel Mikesell
 * Created: Oct 9, 2020
 * 11/01/2020 - Updated join for TFOOH to return a single record
 * 02/07/2021 - Switched from OHQ from transaction instruction (PAPCCA) to current OHQ from WFLOD (LDOHQ)
 *			  - Added WLU(QHWLLP) column for returning the WLU of complete items
 * 03/15/2021 - Added column for standard length available for allocation
 * 03/26/2021 - Removed OHDARA from group by statement - it was returning multiple rows for the same order line
 */

 SELECT
    QHPTY AS "Priority",								/* Priority */
    LEFT(QHROAN, 8),									/* Order Number */
    CASE WHEN PALCPL IS NULL THEN 0 ELSE PALCPL END,	/* License Plate */
    QDITM,												/* Part Number */
    QHTQTY,												/* Transaction Qty */
    (SELECT LDOHQ 							
    	FROM PROBASEF.WFLOD 
    	WHERE LDWHS = 'LB' AND LDOHQ <> 0 AND LDLOT = TILOT 
    	LIMIT 1
	),													/* On-hand Qty */
    QHCR,												/* Carrier */
    QHWVE,												/* Wave */
    QHSTS,												/* Status */
    CASE WHEN QHAUSR = '' THEN QDUSR ELSE QHAUSR END,	/* Active User */
    CASE	
        WHEN PMCMPN = 'THN' AND PMALPH <> 'PV' THEN 'BW'
        WHEN PMCMPN = 'ED' THEN 'CORD'
        WHEN PMCMPN LIKE 'ACC%' THEN 'FIBER'
        WHEN PMALPH = 'PV' THEN 'PV'
    END AS TYPE,										/* Product Type */
    CMNAME,												/* Customer Name */
    PAORLN,												/* Order Line */
    T5.F2 AS OD,										/* Diameter */
    PCPATP AS Reel,										/* Reel Size */
    OHSHDT,												/* Ship on Date */														
    CMPRKY,												/* Customer ID */
    QHCTRL,												/* Control Number */
    PAPANM,												/*  */	
    QHWLLP,												/* Work Load Unit */
    CASE WHEN PMCMPN = 'THN' AND PMALPH <> 'PV' THEN (
    	SELECT 
            CASE WHEN SUM(LDAVLQ) IS NULL THEN 0 ELSE SUM(LDAVLQ) END
    	FROM PROBASEF.WFLOD
    	WHERE 
            LDWHS = 'LB' 
            AND LDITEM LIKE REPLACE(QDITM, 'XX', '%') 
            AND LDPCCA = QHTQTY
            AND LDITEM <> QDITM
            AND LDISTS = 'AVL'
    ) ELSE 0 END,                                       /* TESTING - SHOULD SHOW STANDARD LENGTH AVAILABLE */
    OHDARA												/* Dock Area */
 FROM
    PROBASEF.WFWQH 										/* Queue Header */
    LEFT JOIN PROBASEF.WFWQD ON QHQNUM = QDQNUM 		/* Queue Header */
    LEFT JOIN PROBASEF.WFTIN ON QDINST = TIINST 		/* Transaction Intruction */
    LEFT JOIN PROBASEF.TFPCD ON QDINST = PAINST 		/*  */
    LEFT JOIN (
        SELECT OHPOOR, MAX(OHSHDT) AS OHSHDT, MAX(OHDARA) AS OHDARA  
        FROM PROBASEF.TFOOH    
        GROUP BY OHPOOR
    ) ON QHROAN = OHPOOR 								/* Order Header */
    LEFT JOIN HFADTAGC.PMP ON QDITM = PMPART 			/* HFA Part Master */
    LEFT JOIN HFADTAGC.CMP ON QHSHAC = CMPRKY 			/* HFA Customer Master */
    LEFT JOIN HFADTAGC.XPMP ON PMPART = XMITM 			/* HFA Part Master Extension */
    LEFT OUTER JOIN PROBASEF.TFPCH ON PAPANM = PCPANM 	/* Parcel */
    LEFT JOIN BBQYXXX.CABLESIZE3 T5 ON PMPART = T5.F1 	/* Cable Diameter for Part */
 WHERE
    QHWHS = 'LB'										/* Warehouse LB */
    AND QHSRCM = 'WMM'									/* WMM Source Module */
    AND (
        (QHSTS <> 'R' AND QHZONE = 'W.CUT') OR			/* W.CUT Zone for open items */
        (QHSTS = 'R' AND NOT(PAORG IS NULL))			/* Any zone for complete items */
    )											
 ORDER BY QHPTY											/* Order by Priority */
 FOR FETCH ONLY