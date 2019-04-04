<?php
$act=isset($_POST['act'])?$_POST['act']:null;
$stime=time();

@header('Content-Type: application/json; charset=UTF-8');

switch($act){
case 'upapp':
		$utime=isset($_POST['utime'])?$_POST['utime']:null;
		if($stime == $utime || $stime == $utime+1 || $stime == $utime+2){
		exit('{"Code":1,"Msg":"OK","Edition":"0.3","Force":1,"Date":"2019-04-04","Body":[{"Title":"【更新】正在完善中..."},{"Title":"【预计】在 2019-04-08 完成基本功能。"}]}');
		//Force为强制更新开关1为强制0为不强制
		}else{
		exit('{"Code":-1,"Msg":"No Parameter"}');
		}
	break;
case 'subject':
		$utime=isset($_POST['utime'])?$_POST['utime']:null;
		if($stime == $utime || $stime == $utime+1 || $stime == $utime+2){
		exit('{"Code":0,"Msg":"OK","Body":[{"id":133,"date":"2019-04-05","title":"660流畅"}]}');
		}else{
		exit('{"Code":-1,"Msg":"No Parameter"}');
		}
	break;
default:
		exit('{"Code":-2,"Msg":"No Act"}');
	break;
}