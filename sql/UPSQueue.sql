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
    QHROAN
FROM
    PROBASEF.WFWQH
WHERE
    QHWHS = 'LB'
    AND QHSTS <> 'R'
    AND QHSRCM = 'PICK'
    AND QHWINC = 'CAS'
    AND QHCR LIKE '%UPS%'