<!DOCTYPE html>
<html lang="en">
<head>
<title>Probending Web Interface</title>
<link rel = "stylesheet" type = "text/css" href = "css/bootstrap.css">
</head>
<body>
<div class = "container">
<center><img src = "img/probendinglogo.png"></center>
<table class = "table table-striped table-bordered table-hover">
<?php
require ('config.php');
$handle = mysql_connect($mysql_host, $mysql_user, $mysql_pass) or die($connect_error);
mysql_select_db($mysql_db, $handle) or die ($connect_error);

if (!(isset($pagenum))) {
	$pagenum = 1;
}
$data = mysql_query("SELECT * FROM probending_teams") or die (mysql_error());
$rows = mysql_num_rows($data);

echo "<tr>";
echo "<th>Team Name</th>";
echo "<th>Team Owner</th>";
if ($ShowAir != false) {
	echo "<th>Airbender</th>";
}
if ($ShowWater != false) {
	echo "<th>Waterbender</th>";
}
if ($ShowEarth != false) {
	echo "<th>Earthbender</th>";
}
if ($ShowFire != false) {
	echo "<th>Firebender</th>";
}
if ($ShowChi != false) {
	echo "<th>Chiblocker</th>";
}

if ($ShowWins != false) {
	echo "<th>Wins</th>";
}
if ($ShowLosses != false) {
	echo "<th>Losses</th>";
}
if ($ShowPoints != false) {
	echo "<th>Points</th>";
}

// Rows per page
$page_rows = 10;

// Page number of last page
$last = ceil($rows/$page_rows);

if ($pagenum < 1) {
	$pagenum = 1;
} else if ($pagenum > $last) {
	$pagenum = $last;
}

$max = 'limit ' .($pagenum - 1) * $page_rows .',' .$page_rows;

$data_p = mysql_query("SELECT * FROM probending_teams ORDER BY points DESC $max") or die (mysql_error());

while ($info = mysql_fetch_array($data_p)) {
	echo '<tr>';
	echo '<td>'.$info["team"].'</td>';
	echo '<td>'.$info["owner"].'</td>';
	if ($ShowAir != false) {
	echo '<td>'.$info["Air"].'</td>';
	}
	if ($ShowWater != false) {
	echo '<td>'.$info["Water"].'</td>';
	}
	if ($ShowEarth != false) {
	echo '<td>'.$info["Earth"].'</td>';
	}
	if ($ShowFire != false) {
	echo '<td>'.$info["Fire"].'</td>';
	}
	if ($ShowChi != false) {
	echo '<td>'.$info["Chi"].'</td>';
	}
	if ($ShowWins != false) {
	echo '<td>'.$info["wins"].'</td>';
	}
	if ($ShowLosses != false) {
	echo '<td>'.$info["losses"].'</td>';
	}
	if ($ShowPoints != false) {
	echo '<td>'.$info["points"].'</td>';
	
	echo '</tr>';
}
echo "</table>";
echo "<p>";

if ($pagenum == 1) {
	echo "<ul class = 'pager'>";

}
else {
	echo "<ul class = 'pager'>";
	//echo "<li><a href = '{$_SERVER['PHP_SELF']}?pagenum=1'><<</a>";

 $previous = $pagenum-1;

 echo " <li><a href='{$_SERVER['PHP_SELF']}?pagenum=$previous'>Previous Page</a> ";

 } 


 //just a spacer
 
 if ($pagenum == $last) 

 {

 } 

 else {

 $next = $pagenum+1;
 echo "<li><a href = '{$_SERVER['PHP_SELF']}?pagenum=$next'>Next Page</a>";
 //echo "<li><a href='{$_SERVER['PHP_SELF']}?pagenum=$last'>>></a>";
 echo "</ul>";
 echo "<center>-- Page $pagenum of $last --</center>";

 } 

 ?> 
 
</div>
<div class = "footer">
<hr />
<!-- Don't be a jerk, leave this footnote here -->
<center><p>Website Designed by MistPhizzle</p></center>
</div>
</body>
</html>