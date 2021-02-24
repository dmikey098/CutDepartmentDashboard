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
    QHWVE,
    QHSTS,
    QHROAN,
    QHCR,
    QDITM,
    QDASLE,
    QDBAY,
    QDLVL,
    QDWGT,
    QDQTY,
    PAORLN,
    QDSTS
FROM 
    PROBASEF.WFWQH 
    INNER JOIN PROBASEF.WFWQD ON QHQNUM = QDQNUM 
    INNER JOIN PROBASEF.TFPCD ON QDINST = PAINST  
WHERE  
    QHWHS = 'LB' 
    AND QHSRCM = 'PICK' 
    AND QHWINC = 'CAS'
    AND QHSTS <> 'R' 
FOR FETCH ONLY