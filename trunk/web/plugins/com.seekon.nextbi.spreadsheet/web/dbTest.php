<?php

	$con = mysql_connect("localhost:3306", "wss", "wss");
	if(con && mysql_ping($con)){
		echo "database connection is ok.";
		mysql_close($con);
	}
	
	$con = mysqli_connect("java:comp/env/jdbc/spreadsheet");
	if(con){
		echo "<table border='1'>
				<tr>
					<th>userid</th>
					<th>userName</th>
				</tr>";

		$sql = " select * from up_user ";
		if ($result = mysqli_query($con, $sql)){
			while ($row = mysqli_fetch_array($result)){
				echo "<tr>";
  				echo "<td>" . $row[0] . "</td>";
  				echo "<td>" . $row[1] . "</td>";
  				echo "</tr>";
			}
		}
		echo "</table>";
	}
	mysqli_close($con);
?>