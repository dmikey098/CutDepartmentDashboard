/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  mikesda001
 * Created: Jan 14, 2021
 */

SELECT
    O5ORD#,
    O5SUFX,
    FLOOR(O5ITEM),
    ODTEXT
FROM
    HFADTAGC.O5P
WHERE
    O5WHB = 'LB'
    AND O5ORD#||LPAD(O5SUFX, 2, '0') = ?
ORDER BY
    O5ITEM