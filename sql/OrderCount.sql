SELECT 
	MIN(QHPTY), 
	QHROAN, 
    QHSTS,
    QHZONE,    
	COUNT(QHROAN) 
FROM 
	PROBASEF.WFWQH 
WHERE 
	QHWHS = 'LB' 
	AND QHSRCM = 'WMM' 
GROUP BY 
    QHROAN, QHSTS, QHZONE
ORDER BY
    MIN(QHPTY)