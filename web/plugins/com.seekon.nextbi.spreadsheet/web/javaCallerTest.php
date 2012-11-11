<?php
	import com.seekon.nextbi.spreadsheet.test.JavaCaller;
	
	$javaCaller = new JavaCaller();
	echo $javaCaller->getGreetString("undyliu");
	
	echo " ";
	
	echo getGreetStringFromModule("undyliu");
?>