/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Daniel Mikesell
 * Created: Nov 3, 2020
 */

SELECT  
    COUNT(QHQNUM) AS CASES  
FROM  
    PROBASEF.WFWQH  
    INNER JOIN PROBASEF.WFWQD ON QHQNUM = QDQNUM   
    INNER JOIN PROBASEF.TFPCD ON QDINST = PAINST  
WHERE   
    QHWHS = 'LB'  
    AND QHSRCM = 'PICK'  
    AND NOT(QHSTS IN ('H', 'E', 'R'))  
    AND QHWINC = 'CAS'  
GROUP BY   
    QHSRCM   
FOR FETCH ONLY