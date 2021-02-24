/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Daniel Mikesell
 * Created: Oct 22, 2020
 */

SELECT                                                         
    QHPTY,                                                     
    LEFT(QHROAN, 8),                                          
    TIOLCP,                                                   
    QDITM,                                                     
    QHASLE,                                                   
    QHBAY,                                                    
    QHLVL,                                                    
    QHSTS,                                                     
    QHAUSR,                                                   
    QHCTRL                                                    
FROM                                                           
    PROBASEF.WFWQH                                             
    INNER JOIN PROBASEF.WFWQD ON QHQNUM = QDQNUM          	 
       INNER JOIN PROBASEF.WFTIN ON QDINST = TIINST              
WHERE                                                          
    QHWHS = 'LB' AND                                           
    QHSRCM = 'RPLN' AND                                         
    QHSTS <> 'R' AND                                           
    QHPTY <> 99999                                             
ORDER BY QHPTY                                                 
FOR FETCH ONLY												 