/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  mikesda001
 * Created: Nov 3, 2020
 */

 SELECT  
    COUNT(DISTINCT(QHQNUM)) AS PALLETS 
 FROM  
    PROBASEF.WFWQH  
    INNER JOIN PROBASEF.WFWQD ON QHQNUM = QDQNUM   
    INNER JOIN PROBASEF.TFPCD ON QDINST = PAINST  
 WHERE   
    QHWHS = 'LB'  
    AND QHSRCM = 'PICK'  
    AND QHWINC = 'PAL'  
    AND NOT(QHSTS IN ('E', 'R', 'H'))  
 GROUP BY   
    QHSRCM   
 FOR FETCH ONLY