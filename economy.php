<html>
<head>
<title>Survival Server Economy Rankings</title>
<style type = "text/css">
html {
background-image:url('images/bg.gif');
color: #FFF;
font-family: "Georgia", "Lucida Grande", Sans-Serif;
}
.footer {
display:block;
position: fixed;
bottom: 0px;
width: 350px;
}
#minimalist {
font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
font-size: 12px;
text-align: left;
border-collapse: collapse;
margin: 30px 30px 30px 15px;
}
#minimalist th {
font-weight: normal;
font-size: 14px;
color: #FFF;
padding: 8px 2px;
}
#minimalist td {
border-right: 30px solid transparent;
border-left: 30px solid transparent;
color: #FFF;
padding: 12px 2px 0;
}

div.pagination {
		padding: 3px;
		margin: 3px;
		text-align:center;
	}
	
	div.pagination a {
		padding: 2px 5px 2px 5px;
		margin: 2px;
		border: 1px solid #AAAADD;
		
		text-decoration: none; /* no underline */
		color: #000099;
	}
	div.pagination a:hover, div.digg a:active {
		border: 1px solid #000099;

		color: #000;
	}
	div.pagination span.current {
		padding: 2px 5px 2px 5px;
		margin: 2px;
		border: 1px solid #000099;
		
		font-weight: bold;
		background-color: #000099;
		color: #FFF;
	}
	div.pagination span.disabled {
		padding: 2px 5px 2px 5px;
		margin: 2px;
		border: 1px solid #EEE;
	
		color: #DDD;
	}
</style>
</head>

<div class = "footer">
<p>EtriaCraft Survival Server Economy Stats<br>v1.0</p>
</div>

<center>
<?php
require ('survivalconfig.php');
$handle = mysql_connect($mysql_host, $mysql_user, $mysql_pass) or die($connect_error);
mysql_select_db($mysql_db, $handle) or die ($connect_error);

$rank = 1;

$sum = mysql_query("SELECT SUM(money) FROM fe_accounts");
$sum2 = mysql_fetch_array($sum);
echo "<strong><font style = 'color:white'>Total Coins in Circulation</strong>: $sum2[0]</font></strong>
<center>
<table border = '0' cellpadding = '3' cellspacing = '10' id = 'minimalist'>
<tr>
<td width = '30'><strong>Rank</strong></td>
<td width = '30'><strong>Username</strong</td>
<td width = '30'><strong>Coins</strong></td>
</tr>";

if (!(isset($pagenum))) {
	$pagenum = 1;
	}
$data = mysql_query("SELECT * FROM fe_accounts ORDER BY money DESC");
$rows = mysql_num_rows($data);

$page_rows = 10;
$last = ceil($rows/$page_rows);

if ($pagenum < 1) {
	$pagenum = 1;
	}
elseif ($pagenum > $last) {
	$pagenum = $last;
}

$max = 'limit ' .($pagenum - 1) * $page_rows .',' .$page_rows;

$data_p = mysql_query("SELECT * FROM fe_accounts ORDER BY money DESC $max");

while ($info = mysql_fetch_array( $data_p )) {
Print $info['Name'];

echo "<br>";
}

echo "<p>";

echo " --Page $pagenum of $last-- <p>";

if ($pagenum == 1) {}
else {
echo "<a href='{$_SERVER['PHP_SELF']}?pagenum=1'> <<-First</a> ";

echo " ";

$previous = $pagenum-1;

echo " <a href='{$_SERVER['PHP_SELF']}?pagenum=$previous'> <-Previous</a> ";
}

echo " ---- ";

if ($pagenum == $last) 

 {

 } 

 else {

 $next = $pagenum+1;

 echo " <a href='{$_SERVER['PHP_SELF']}?pagenum=$next'>Next -></a> ";

 echo " ";

 echo " <a href='{$_SERVER['PHP_SELF']}?pagenum=$last'>Last ->></a> ";

 } 

 ?> 
<?php
//while ($finaldata = mysql_fetch_assoc($data_p)) {
// Table
echo '<tr>';
echo '<td><strong>'.$rank++.'</strong></td>';
echo '<td><strong>'.$data_p["name"].'</strong></td>';
echo '<td><strong>'.$data_p["money"].'</strong></td></tr>';
//}
?>
</table></center> 