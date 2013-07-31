<?php
$connect_error = 'Could not connect to the database!';

// Database Information
$mysql_host = 'localhost';
$mysql_user = 'root';
$mysql_pass = '';
$mysql_db = 'minecraft';

// Table Options
$ShowAir = true;
$ShowWater = true;
$ShowEarth = true;
$ShowFire = true;
$ShowChi = true;
$ShowWins = true;
$ShowLosses = true;
$ShowPoints = true;

if (!mysql_connect($mysql_host, $mysql_user, $mysql_pass)||!mysql_select_db($mysql_db)) {
	die($connect_error);
}
?>