INSERT INTO WO_MST_MENU (
	MENU_ID, NAME_IN, NAME_EN, ACTION, PARENT_ID, MENU_LEVEL, MENU_ORDER,
	FONTAWESOME, DESCRIPTION, ENABLED_FLAG, CREATION_DATE, CREATED_BY, DEL_ID
)
SELECT wo_mst_menu_seq.NEXTVAL,
	'History Notaris',
	'History Notaris',
	'/pages/notary/notaryHistory.faces',
	p.PARENT_ID,
	p.MENU_LEVEL,
	NVL(p.MENU_ORDER, 0) + 1,
	p.FONTAWESOME,
	'History perubahan daftar notaris',
	'Y',
	SYSDATE,
	'SYSTEM',
	0
FROM WO_MST_MENU p
WHERE UPPER(p.NAME_IN) = UPPER('Daftar Notaris')
AND p.ENABLED_FLAG = 'Y'
AND LOWER(p.ACTION) LIKE '%/pages/notary/notary.faces%'
AND ROWNUM = 1;
