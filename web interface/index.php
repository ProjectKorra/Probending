<!DOCTYPE html>
<html lang="en">
<head>
<title>Probending Web Interface</title>
<link rel = "stylesheet" type = "text/css" href = "css/bootstrap.css">
</head>
<body>
<div class = "container">
<center><img src = "img/probendinglogo.png"></center>
<table class = "table table-striped table-bordered" style = "text-aling: center">
<tr><th>Team Name</th><th>Team Owner</th><th>Airbender</th><th>Waterbender</th><th>Earthbender</th><th>Firebender</th><th>Chiblocker</th><th>Wins</th><th>Losses</th></tr>
<?php
require ('config.php');
$handle = mysql_connect($mysql_host, $mysql_user, $mysql_pass) or die($connect_error);
mysql_select_db($mysql_db, $handle) or die ($connect_error);

if (!(isset($pagenum))) {
	$pagenum = 1;
}
$data = mysql_query("SELECT * FROM probending_teams") or die (mysql_error());
$rows = mysql_num_rows($data);

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

$data_p = mysql_query("SELECT * FROM probending_teams ORDER BY wins DESC $max") or die (mysql_error());

while ($info = mysql_fetch_array($data_p)) {
	echo '<tr>';
	echo '<td>'.$info["team"].'</td>';
	echo '<td>'.$info["owner"].'</td>';
	echo '<td>'.$info["Air"].'</td>';
	echo '<td>'.$info["Water"].'</td>';
	echo '<td>'.$info["Earth"].'</td>';
	echo '<td>'.$info["Fire"].'</td>';
	echo '<td>'.$info["Chi"].'</td>';
	echo '<td>'.$info["wins"].'</td>';
	echo '<td>'.$info["losses"].'</td>';
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