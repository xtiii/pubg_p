<?php
$act=isset($_POST['act'])?$_POST['act']:null;
$stime=time();

@header('Content-Type: application/json; charset=UTF-8');

switch($act){
case 'upapp':
		$utime=isset($_POST['utime'])?$_POST['utime']:null;
		if($stime == $utime || $stime == $utime+1 || $stime == $utime+2){
		exit('{"Code":0,"Msg":"OK"}');
		}else{
		exit('{"Code":-1,"Msg":"No Parameter"}');
		}
	break;
case 'subject':
		$utime=isset($_POST['utime'])?$_POST['utime']:null;
		if($stime == $utime || $stime == $utime+1 || $stime == $utime+2){
		exit('{"Code":0,"Msg":"OK","Body":[{"id":133,"title":"660流畅"}]}');
		}else{
		exit('{"Code":-1,"Msg":"No Parameter"}');
		}
	break;
default:
		exit('{"Code":-2,"Msg":"No Act"}');
	break;
}