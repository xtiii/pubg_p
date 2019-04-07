<?php

$act=isset($_POST['act'])?$_POST['act']:null;
$stime=time();

@header('Content-Type: application/json; charset=UTF-8');

switch($act){
case 'upapp':
		$utime=isset($_POST['utime'])?$_POST['utime']:null;
		if($stime == $utime || $stime == $utime+1 || $stime == $utime+2){
		//Force为强制更新开关1为强制0为不强制
		exit('{"Code":1,"Msg":"OK","Edition":"0.5_beta","Force":0,"Date":"2019-04-07","Body":[{"Title":"【功能】正在完善中..."},{"Title":"【预计】在 2019-04-08 完成基本功能。"}]}');
		}else{
		exit('{"Code":-1,"Msg":"No Parameter"}');
		}
	break;
case 'subject':
		exit('{"Code":0,"Msg":"OK","Body":[{"Id":"133","Date":"2019-04-05","Title":"骁龙660流畅，极限帧率"}]}');
	break;
default:
		exit('{"Code":-2,"Msg":"No Act"}');
	break;
}

?>