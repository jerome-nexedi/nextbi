<?php

	$conn = palo_init("127.0.0.1", "7921", "admin", "admin");
	if(is_resource($conn)){
		//echo "connection is resource.";
	}
	palo_ping($conn);
	palo_use_unicode(true);
	
	$res = palo_dimension_list_elements($conn, "System", "#_GROUP_");
	echo json_encode($res);
	
	if (!is_array($res) || !count($res)){
	}
	foreach ($res as &$entry){
		$all_groups[] = $entry['name'];
	}
	
	$res = palo_datav($conn, 'System', '#_USER_GROUP', 'admin', array_merge(array(count($all_groups), 1), $all_groups));
	echo json_encode($res);
	
	array_splice($res, 0, 2);
	foreach ($res as $idx => $entry){
		if ($entry == '1'){
			$all_user_groups[] = $all_groups[$idx];
		}
	}
	
	// active user's groups
	$res = palo_datav($conn, 'System', '#_GROUP_GROUP_PROPERTIES', array_merge(array(1, count($all_user_groups)), $all_user_groups), 'accountStatus');
	echo json_encode($res);
	
	if (is_array($res))
	{
		array_splice($res, 0, 2);
		
		foreach ($res as $idx => $entry)
			if ($entry == '1')
				$groups[] = $all_user_groups[$idx];
	}
	else{
		$groups = $all_user_groups;
	}
	
	$groups_num = count($groups);
	

	// roles

	// all roles
	$res = palo_dimension_list_elements($conn, 'System', '#_ROLE_');
	echo json_encode($res);
	
	foreach ($res as &$entry)
		$all_roles[] = $entry['name'];

	$all_roles_num = count($all_roles);


	// groups' roles
	$res = palo_datav($conn, 'System', '#_GROUP_ROLE', array_merge(array(1, $groups_num), $groups), array_merge(array($all_roles_num, 1), $all_roles));
	echo json_encode($res);
	
	array_splice($res, 0, 2);

	for ($r = 0; $r < $all_roles_num; ++$r)
		for ($g = 0; $g < $groups_num; ++$g)
			if ($res[$r * $groups_num + $g] == '1')
			{
				$roles[] = $all_roles[$r];
				break;
			}

	$roles_num = count($roles);


	// rules
	if (!is_array($rules) || !count($rules))
	{
		$res = palo_dimension_list_elements($conn, 'System', '#_RIGHT_OBJECT_');

		foreach ($res as &$entry)
			$rules[] = $entry['name'];
	}

	$rules_num = count($rules);


	// rules effective perms
	$res = palo_datav($conn, 'System', '#_ROLE_RIGHT_OBJECT', array_merge(array(1, $roles_num), $roles), array_merge(array($rules_num, 1), $rules));

	echo json_encode($res);
	array_splice($res, 0, 2);
	
	//-------------------------------------------------------------------------------
	
	$dbs = palo_root_list_databases($conn);
	echo json_encode($dbs);	
	
	foreach ($dbs as $db)
	{
			if (substr($db, 0, 6) == 'System')
				continue;

			$dims = palo_database_list_dimensions($conn, $db, 0);
			echo json_encode($dims);
			
			if (!in_array('meta', $dims))
				continue;

			$meta = palo_datav($conn, $db, '#_meta', array(1, 2, 'type', 'data'), array(1, 1, 'group'));
			echo json_encode($meta);
			
			$type = $meta[2];

			if ($types !== null && !in_array($type, $types))
				continue;
		}	
	palo_disconnect($conn);
?>