<?php
$act=isset($_POST['act']);
$stime=time();

@header('Content-Type: application/json; charset=UTF-8');

switch($act){
case 'upapp':
		$utime=isset($_POST['utime']);
		if($stime == $utime || $stime == $utime+1 || $stime == $utime+2){
		exit('{"code":0,"msg":"成功！"}');
		}else{
		exit('{"code":-1,"msg":"失败！"}');
		}
	break;
}