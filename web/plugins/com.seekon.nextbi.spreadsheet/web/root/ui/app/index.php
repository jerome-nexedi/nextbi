<?php

header('Location: /spreadsheet/root/ui/app/main.php' . ($_SERVER['QUERY_STRING'] ? '?' . $_SERVER['QUERY_STRING'] : ''));
die();

?>