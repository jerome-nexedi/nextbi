<?php

function ccmd ($ccmd, $flags = -1, $sess_id = '', $path = CFG_UB_PATH)
{
	$ch = curl_init('http://127.0.0.1:' . CFG_UB_PORT . $path . ($flags == -1 ? '' : '?' . $flags));

	curl_setopt_array($ch, array(
	  CURLOPT_HEADER => false
	//, CURLOPT_FORBID_REUSE => false
	, CURLOPT_FRESH_CONNECT => false
	, CURLOPT_POST => true
	, CURLOPT_RETURNTRANSFER => true
	, CURLOPT_FAILONERROR => true
	, CURLOPT_USERAGENT => $_SERVER['HTTP_USER_AGENT']
	, CURLOPT_HTTPHEADER => array('Content-Type: application/json; charset=utf-8; charset=UTF-8')
	));

	curl_setopt($ch, CURLOPT_POSTFIELDS, ($un = is_array($ccmd)) ? json_encode($ccmd) : $ccmd);

	if ($sess_id !== null && ($sess_id != '' || ($sess_id = session_id()) != '')){
		$cookie_file = tempnam('./temp','cookie');
		curl_setopt($ch, CURLOPT_COOKIEJAR,$cookie_file); 
		curl_setopt($ch, CURLOPT_COOKIEFILE,$cookie_file);//
		curl_setopt($ch, CURLOPT_COOKIE, 'WSS_SESS_ID=' . $sess_id . ';');
	}
 
 	if (($res = curl_exec($ch)) === false)
		$res = '[[false,3' . curl_errno($ch) . ',' . json_encode(curl_error($ch)) . ']]';

	curl_close($ch);

	return $un ? json_decode($res, true) : $res;
}

?>