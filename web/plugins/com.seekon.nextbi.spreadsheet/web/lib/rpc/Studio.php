<?php

/*
 * \brief Studio RPC routines
 *
 * \file Studio.php
 *
 * Copyright (C) 2006-2010 Jedox AG
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License (Version 2) as published
 * by the Free Software Foundation at http://www.gnu.org/copyleft/gpl.html.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * You may obtain a copy of the License at
 *
 * <a href="http://www.jedox.com/license_palo_bi_suite.txt">
 *   http://www.jedox.com/license_palo_bi_suite.txt
 * </a>
 *
 * If you are developing and distributing open source applications under the
 * GPL License, then you are free to use Palo under the GPL License.  For OEMs,
 * ISVs, and VARs who distribute Palo with their products, and do not license
 * and distribute their source code under the GPL, Jedox provides a flexible
 * OEM Commercial License.
 *
 * \author
 * Srdjan Vukadinovic <srdjan.vukadinovic@develabs.com>
 * Andrej Vrhovac <andrej.vrhovac@develabs.com>
 * Mladen Todorovic <mladen.todorovic@develabs.com>
 * Drazen Kljajic <drazen.kljajic@develabs.com>
 * Predrag Malicevic <predrag.malicevic@develabs.com>
 *
 * \version
 * SVN: $Id: Studio.php 3062 2010-03-30 18:38:55Z predragm $
 *
 */

class Studio
{
	const UNDEFINED = -1;
	const SESSION_STORE_NAME = 'Studio';
	const WB_EXTENSION = '.wss';
	const PALO_SUBSET_GRAMMAR = '../../lib/rpc/subset_grammar.xslt';

	public static $errMsg = array(
		1 => 'Exception Msg: ',

		111 => 'Connection failed!',
		122 => 'Getting  data failed!',
		123 => 'Setting data failed!',
		132 => 'Element list failed!',
		133 => 'Adding element failed!',
		134 => 'Removing element failed!',
		135 => 'Element already exists.',
		136 => 'Renaming element failed!',
		142 => 'Element already exists!',

		1001 => 'Dimension used by some Cubes.',
		1002 => 'Dimension doesn\'t exist.',
		1003 => 'Database name is not correct.',
		1004 => 'Dimension already exists.',
		1005 => 'Cube doesn\'t exist.',
		1006 => 'Element already exists.',
		1007 => 'Workbook/Sheet is not correct.',
		1008 => 'Server not found.',
		1009 => 'Subset not saved.',
		1010 => 'Subset not deleted.',
		1011 => 'Subset not renamed.',

		1000 => ''
	);

	private $currGroup;
	private $currHierarchy;

	private $plugins;

	private $accessPolicy;

	public function __construct ()
	{
		if (isset($_SESSION[self::SESSION_STORE_NAME]))
		{
			$tmp = $_SESSION[self::SESSION_STORE_NAME];

			foreach ($tmp as $variable_name => $variable_value)
				$this->$variable_name = $variable_value;
		}
		else
		{
			$this->currGroup = array('file' => -1, 'report' => -1, 'template' => -1, 'wss' => -1, 'prefs_file' => -1, 'prefs_report' => -1, 'wss_report' => -1);
			$this->currHierarchy = array();

			$this->plugins = array('fs' => new W3S_FSPlugin());
		}

		$this->accessPolicy = $_SESSION['accessPolicy'];
	}

	public function __destruct ()
	{
		unset($this->accessPolicy);

		$_SESSION[self::SESSION_STORE_NAME] = $this;
	}

	public function __wakeup ()
	{
		$this->accessPolicy = $_SESSION['accessPolicy'];
	}

	/*
	 * \brief verifies that the Core session is still alive
	 *
	 * \author
	 * Predrag Malicevic <predrag.malicevic@develabs.com>
	 *
	 */

	public function verifySess ()
	{
		$res = json_decode(ccmd('[["ilog"]]', -1, $_SESSION['WSSU_SESSID']), true);

		return $res[0][0] ? $res[0][1] : false;
	}

	/*
	 * \brief instructs Core to refresh Palo connection database
	 *
	 * \author
	 * Predrag Malicevic <predrag.malicevic@develabs.com>
	 *
	 */

	public function syncPaloConnData ()
	{
		ccmd('[["tee","palo_wss3",0.0]]', -1, $_SESSION['WSSU_SESSID']);
	}

	public function getUserCreds ()
	{
		return array(true, array('user' => $this->accessPolicy->getUser(), 'pass' => $this->accessPolicy->getPass()));
	}


	//====================== Palo Dispatcher ===============================================

	private function _get_connection ()
	{
		$conn = $this->accessPolicy->getConn();

		return is_resource($conn) ? $conn : array('!', 111);
	}

	private function _getData($connection, $dbName, $cubeName, $elemName, $coordinates){
		/*
		 *  palo_datav (string $connection, string $db_name, array $cube_name,  $elemName, $coordinate1)
		 * */
		try {
			$result = palo_datav($connection, $dbName, $cubeName, $elemName, $coordinates);
			return $result;
		}
		catch (Exception $e) {
			return array('!', 122);
		}
	}

	private function _setData($connection, $dbName, $cubeName, $dimensions, $values) {
		/*
		 * palo_setdataa  (mixed $value, mixed $splash, string $connection_and_db, string $cube_name, array $dimensions)
		 * */
		try {
			$br=count($dimensions);
			for($i=0; $i<$br; $i++)
				$result=palo_setdataa($values[$i], false, $connection, $dbName, $cubeName, $dimensions[$i]);

			return $result;
		}
		catch (Exception $e) {
			return array('!', 123);
		}
	}

	private function _getElementsList($connection, $dbName, $dimName){
		/*
		 *  palo_dimension_list_elements  (resource $connection, string $db_name, string $dimension_name)
		 * */
		try {
			$result = palo_dimension_list_elements($connection, $dbName, $dimName, true);

			return $result;
		}
		catch (Exception $e) {
			return array('!', 132);
		}
	}

	private function _addElement($connection, $dbName, $dimName, $elemType, $elemName, $parentElemName, $consolidFactor, $clear){
		/*
		 * bool palo_eadd(resource $connection, string $db_name, string $dimension_name, string $element_type, string $element_name, string $parent_element_name, double $consolidation_factor, bool $clear, boolean $empty_string)
		 */
		try {
			$result = palo_eadd($connection, $dbName, $dimName, $elemType, $elemName, $parentElemName, $consolidFactor, $clear, true);

			if (!$result)
				throw new Exception();

			return $result;
		}
		catch (Exception $e) {
			return array('!', 133);
		}
	}

	private function _deleteElement($connection, $dbName, $dimName, $elemName){
		/*
		 *  palo_edelete  (resource $connection, string $db_name, string $dimension, string $element, boolean $emtpy_string)
		 * */
		try {
			$result = palo_edelete($connection, $dbName, $dimName, $elemName, true);

			if (!$result)
				throw new Exception();

			return $result;
		}
		catch (Exception $e) {
			return array('!', 134);
		}
	}

	private function _isElement($connection, $dbName, $dimName, $elemName){
		/*
		 *  palo_eindex(resource $connection, string $db_name, string $dimension, string $element, boolean $emtpy_string)
		 * */
		try {
			$result =  palo_eindex($connection, $dbName, $dimName, $elemName, true);

			if (!$result)
				throw new Exception();

			return true;
		}
		catch (Exception $e) {
			return false;
		}
	}

	public function isElement($userName){

		$connection = $this->_get_connection();
	    $dbName = 'System';
	    $dimName = '#_USER_';
	    $elemName = $userName;

		$result = $this->_isElement($connection, $dbName, $dimName, $elemName);

		return $result;

	}

	private function _renameElement($connection, $dbName, $dimName, $elemName, $newName){
		/*
		 * bool palo_erename(resource $connection, string $db_name, string $dimension_name, string $element_name, string $new_element_name,  $empty_string)
		 * */
		try {
			$result = palo_erename($connection, $dbName, $dimName, $elemName, $newName, true);

			if (!$result)
				throw new Exception();

			return $result;
		}
		catch (Exception $e) {
			return array('!', 136);
		}
	}

	private function _checkAndRenameElement($connection, $dbName, $dimName, $oldName, $newName){
		/*
		 * bool palo_erename(resource $connection, string $db_name, string $dimension_name, string $element_name, string $new_element_name,  $empty_string)
		 * */
		try {
			$result = $this->_getElementsList($connection, $dbName, $dimName);

			$names=array();

			for($i=0; $i<count($result); $i++)
					array_push($names, $result[$i]['name']);

			if ($oldName != $newName)
				if (in_array($oldName, $names))
					if (!in_array($newName, $names)){
						$result = $this->_renameElement($connection, $dbName, $dimName, $oldName, $newName);

						if ($result[0] == "!" && count($result)==2)
							throw new Exception();
					}

					else
						throw new Exception();

			return $result;

		}
		catch (Exception $e) {
			return array('!', 142);
		}
	}

	private function _isError($result){
		try {

			if ($result[0] == "!" && count($result)==2)
				throw new Exception();
			return false;

		}
		catch(Exception $e){
			return true;
		}
	}

	private function _getUUID(){
		$seed = 'JvKnrQWPsThuJteNQAuH';
		$hash = sha1(uniqid($seed . mt_rand(), true));
		return $hash;
	}


	/*
	 * #############
	 * ### PREFS ###
	 * #############
	 */

	public function getPrefs ($level, $name = null, $path = null)
	{
		$prefs = $level == Prefs::LEVEL_USER && $name === null ? $_SESSION['prefs'] : new Prefs($this->accessPolicy, $level, $name);

		 return $prefs->get($path);
	}

	public function setPrefs ($level, $name, $data, $path = null) // eg. (2, 'smith', { general: { l10n: 'de_DE' }, wss: { interface: 'toolbar' } });
	{
		$prefs = new Prefs($this->accessPolicy, $level, $name);

		$prefs->set($path, $data);

		return $prefs->save($this->accessPolicy);
	}

	public function removePrefs ($level, $name, $path1) // , ..., $pathN)
	{
		$prefs = new Prefs($this->accessPolicy, $level, $name);

		$paths = func_get_args();
		array_splice($paths, 0, 2);

		foreach ($paths as $path)
			$prefs->remove($path);

		return $prefs->save($this->accessPolicy);
	}

	public function removeUserPrefGroupsFromSession(){
		$this->removeFromSessionCurrGH('prefs_file');
		$this->removeFromSessionCurrGH('prefs_report');
	}


	//================================================== Connections =================================================================

		public function getAllConnections($prop){

		try {
//		    $connection = $this->_get_connection();
			$connection = $this->accessPolicy->getConn();
		    $dbName = 'Config';
			$cubeName = '#_connections';
			$dimName = 'connections';
			$properties = array(count($prop), 1);

			for($i=0; $i<count($prop); $i++)
				array_push($properties, $prop[$i]);

			$result = $this->_getElementsList($connection, $dbName, $dimName);

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);

			$connectionObjs=$result;
			$coordinates = array(1, count($connectionObjs));

			for($i=0; $i<count($connectionObjs); $i++)
				array_push($coordinates, $connectionObjs[$i]['name']);

			$result = $this->_getData($connection, $dbName, $cubeName, $properties, $coordinates);
			$connectionNames = array_slice($coordinates, 2); //list of connections

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);

			$icons=$this->getConnectionIcons();

			if ($this->_isError($icons))
				throw new Exception($icons[0], $icons[1]);

			$perms = $this->accessPolicy->calcPerms($dbName, $dimName, $connectionNames, false, 'dimension element', true);

			$result = $this->formatConnections(array_slice($result, 2), $icons, $perms);

			return $result;
		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}

	}

	public function setConnectionPermission($connectionName, $accessGroup, $permission){
		/*
		 *  palo_datav (string $connection, string $db_name, array $cube_name,  $coordinate1)
		 * */
		try {
			$connection = $this->accessPolicy->getConn();
		    $dbName = 'Config';
			$cubeName = '#_GROUP_DIMENSION_DATA_connections';

			$dimensions = array(array($accessGroup, $connectionName));
			$values=array($permission);

			$result = $this->_setData($connection, $dbName, $cubeName, $dimensions, $values);
			palo_disconnect($connection);

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);

			return array(true, $result);

		}
		catch (Exception $e) {
			return array(false, $e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function getAllPaloConnections($prop, $incCurr){

		try {
		    $connection = $this->_get_connection();
		    $dbName = 'Config';
			$cubeName = '#_connections';
			$dimName = 'connections';
			if (!$prop) $prop = array("name", "type");
			$properties = array(count($prop), 1);

			for($i=0; $i<count($prop); $i++)
				array_push($properties, $prop[$i]);

			$result = $this->_getElementsList($connection, $dbName, $dimName);

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);

			$connectionNames=$result;
			$coordinates = array(1, count($connectionNames));

			for($i=0; $i<count($connectionNames); $i++)
				array_push($coordinates, $connectionNames[$i]['name']);

			$result = $this->_getData($connection, $dbName, $cubeName, $properties, $coordinates);

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);


			$result = array_slice($result, 2);
			$connectons = array();

			$rowNmb = count($result)/count($prop);

			for ($i=0; $i<$rowNmb; $i++){
				if ($result[$rowNmb+$i]=='palo')
				{
					$tmConn = array();
					for ($j=0; $j<count($prop); $j++){
						array_push($tmConn, $result[$i+$rowNmb*$j]);
					}
					array_push($connectons, $tmConn);
				}
			}
			if ($incCurr){
				return array('connections' => $connectons, 'currConn' => isset($_SESSION['paloConnData']) ? $_SESSION['paloConnData']['name'] : null);
			}

			return $connectons;
		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}

	}

	public function setPaloConnData ($connName = null)
	{
		if ($connName == null)
		{
			unset($_SESSION['paloConnData']);
			return true;
		}

		$err_codes = array(1 => 'Studio-err_no_conn', 2 => 'no_connection_rights');

		try
		{
			$connData = $this->getConnection($connName);
			$connData = array('name' => $connName, 'host' => $connData[3], 'port' => $connData[4], 'username' => $connData[5], 'password' => $connData[6]);

			$apol = new AccessPolicy($connData['host'], $connData['port'], $connData['username'], $connData['password'], $connData['username'], $connData['password'], false);

			if (!is_resource($apol->getSuperConn()))
				return $err_codes[1];

			if (!$apol->reload())
				return $err_codes[2];

			$rules = $apol->getRules();

			if ($rules['user'] < AccessPolicy::PERM_DELETE || $rules['password'] < AccessPolicy::PERM_DELETE || $rules['group'] < AccessPolicy::PERM_DELETE || $rules['rights'] < AccessPolicy::PERM_DELETE)
				return $err_codes[2];

			$_SESSION['paloConnData'] = $connData;

			return true;
		}
		catch (Exception $e)
		{
			return $err_codes[1];
		}
	}

	public function formatConnections($result, $icons, $perms){
		$rowNmb = count($result)/2;
		for ($i=0; $i<$rowNmb; $i++){
			$result[$i+$rowNmb] = $icons[$result[$i+$rowNmb]];
			$result[$i+2*$rowNmb] = $perms[$result[$i]];
		}
		return $result;
	}

	public function getConnectionIcons (){
		try {
			$types=$this->getConnectionTypes(array("icon"));
			//return $types;
				if ($this->_isError($types))
					throw new Exception($types[0], $types[1]);

				$rowNmb = count($types)/2;

				for ($i=0; $i<$rowNmb; $i++)
					$icons[$types[$i]]=$types[$i+$rowNmb];

				return $icons;

		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function getConnection($connectionName){
		/*
		 *  palo_datav (string $connection, string $db_name, array $cube_name,  $coordinate1)
		 * */
		try {
		    $connection = $this->_get_connection();
		    $dbName = 'Config';
			$cubeName = '#_connections';
			$elemName = $connectionName;
			$coordinates = array("name", "type", "description", "host", "port", "userName", "password", "useLoginCred", "active");

			array_unshift($coordinates,  1, count($coordinates));

			$result = $this->_getData($connection, $dbName, $cubeName, $coordinates, $elemName);
			palo_disconnect($connection);

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);

			return array_slice($result, 2);
		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function getConnectionTypes($prop){
		/*
		 *  palo_datav (string $connection, string $db_name, array $cube_name,  $coordinate1)
		 * */
		try {
		    $connection = $this->_get_connection();
		    $dbName = 'Config';
			$cubeName = '#_connection_types';
			$dimName = 'connection_types';
			$properties = array(count($prop), 1);

			for($i=0; $i<count($prop); $i++)
				array_push($properties, $prop[$i]);

			$result = $this->_getElementsList($connection, $dbName, $dimName);

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);

			$connectionTypes=$result;
			$coordinates = array(1, count($connectionTypes));

			for($i=0; $i<count($connectionTypes); $i++)
				array_push($coordinates, $connectionTypes[$i]['name']);

			$result = $this->_getData($connection, $dbName, $cubeName, $properties, $coordinates);

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);

			return array_merge(array_slice($coordinates, 2), array_slice($result, 2));
		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function getConnectionType($typeName){
		/*
		 *  palo_datav (string $connection, string $db_name, array $cube_name,  $coordinate1)
		 * */
		try {
			$connection = $this->_get_connection();
		    $dbName = 'Config';
			$cubeName = '#_connection_types';
			$dimName = 'connection_types';

			$elemName = $typeName;
			$coordinates = array("icon");
			array_unshift($coordinates,  1, count($coordinates));

			$result = $this->_getData($connection, $dbName, $cubeName, $coordinates, $elemName);
			palo_disconnect($connection);

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);

			return array_slice($result, 2);
		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function addConnection($connectionName, $type, $description, $host, $port, $userName, $password, $useLoginCred, $active){
		/*
		 * bool palo_setdataa (string $value, string $splash, string $connection, array $db_name, mixed $cube_name, mixed $dimensions)
		 * */
		try {
		    $connection = $this->_get_connection();
		    $dbName = 'Config';
		    $dimName = 'connections';
			$elemType = 'S';
		    $elemName = $connectionName;
		    $parentElemName = '';
		    $consolidFactor = 1;
		    $clear = FALSE;

			$result = $this->_isElement($connection, $dbName, $dimName, $elemName);

			if ($result)
				throw new Exception("!", 135);

			$result = $this->_addElement($connection, $dbName, $dimName, $elemType, $elemName, $parentElemName, $consolidFactor, $clear);
			palo_ping($connection);

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);

			$cubeName = '#_connections';
			$dimensions = array(array('uuid', $connectionName), array('name', $connectionName), array('type', $connectionName), array('description', $connectionName), array('host', $connectionName), array('port', $connectionName), array('username', $connectionName), array('password', $connectionName), array('useLoginCred', $connectionName), array('active', $connectionName));
			$values=array($this->_getUUID(), $connectionName, $type, $description, $host, $port, $userName, $password, $useLoginCred, $active);

			$result = $this->_setData($connection, $dbName, $cubeName, $dimensions, $values);
			palo_disconnect($connection);

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);

			if ($type == 'palo')
				$this->syncPaloConnData();

			return $result;
		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function updateConnection($oldConnectionName, $newConnectionName, $type, $description, $host, $port, $userName, $password, $useLoginCred, $active){
		/*
		 * bool palo_setdataa (string $value, string $splash, string $connection, array $db_name, mixed $cube_name, mixed $dimensions)
		 * */
		try {

			$connection = $this->_get_connection();
			$dbName = 'Config';
		    $dimName = 'connections';
			$oldName = $oldConnectionName;
			$newName = $newConnectionName;

			if ($oldName != $newName){
				$result = $this->_checkAndRenameElement($connection, $dbName, $dimName, $oldName, $newName);

				if ($this->_isError($result))
					throw new Exception($result[0], $result[1]);
			}

			$connectionName = $newName;
			$cubeName = '#_connections';
			$dimensions = array(array('name', $connectionName), array('type', $connectionName), array('description', $connectionName), array('host', $connectionName), array('port', $connectionName), array('username', $connectionName), array('password', $connectionName), array('useLoginCred', $connectionName), array('active', $connectionName));
			$values=array($connectionName, $type, $description, $host, $port, $userName, $password, $useLoginCred, $active);

			$result = $this->_setData($connection, $dbName, $cubeName, $dimensions, $values);
			palo_disconnect($connection);

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);
			if ($type == 'palo')
				$this->syncPaloConnData();

			return $result;
		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function removeConnection($connectionName){
		/*
		 *  palo_edelete  (resource $connection, string $db_name, string $dimension, string $element, boolean $emtpy_string)
		 * */
		try {
			$connection = $this->_get_connection();
			$dbName = 'Config';
			$dimName = 'connections';
			$elemName = $connectionName;

			//check type of connection (because of sync with core)
			$cubeName = '#_connections';
			$coordinates = array("type");
			array_unshift($coordinates,  1, count($coordinates));
			$type = array_slice($this->_getData($connection, $dbName, $cubeName, $coordinates, $elemName), 2);

			$result = $this->_deleteElement($connection, $dbName, $dimName, $elemName);

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);

			if ($type[0] == 'palo')
				$this->syncPaloConnData();

			return $result;
		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	/*
	 * \brief returns connection perms for a given user group
	 *
	 * \author
	 * Predrag Malicevic <predrag.malicevic@develabs.com>
	 *
	 */

	public function getConnPerms4UGroup ($user_group, $conn)
	{
		$apol = clone $this->accessPolicy;
		$apol->reload(array($user_group), array('dimension element'));

		$rule = $apol->getRule('dimension element');

		$perm = $apol->calcPerms('Config', 'connections', array($conn), true);
		$perm = $perm[$conn];

		return array('eff' => $perm & $rule, 'own' => $perm, 'def' => $rule, 'max' => $rule);
	}


	//====================================================== Files ==================================================

	public function getDefaultLocalPath(){
		/*
		 *  palo_datav (string $connection, string $db_name, array $cube_name,  $coordinate1)
		 * */
		try {
		  $connection = $this->_get_connection();
		  $dbName = 'Config';
			$cubeName = '#_config';
			$elemName = 'local';

			// workaround for older Config database
			if (!in_array('config', palo_database_list_dimensions($connection, $dbName)))
			{
				palo_disconnect($connection);
				return array('');
			}

			$coordinates = array('value');
			array_unshift($coordinates,  1, count($coordinates));

			$result = $this->_getData($connection, $dbName, $cubeName, $coordinates, $elemName);
			palo_disconnect($connection);

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);

			return array_slice($result, 2);
		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}


	public function getHierarchyPropertiesData($type, $h){
		try {
			$group = $this->currGroup[$type];
			$group->apol = $this->accessPolicy;
			$guid = $group->getUID();
			$hierarchy = $group->getHierarchy($h);
			$hData = $hierarchy->getData();
			$name = $hData->getName();
			$description = $hData->getDescription();
			$studioType = $hData->getType();

			$relPath = "";
			$sysPath = "";
			$size = "";
			$props = null;

			if ($studioType == 'file'){
				$sysPath = $hData->getBackend()->getLocation();
				$props = $hierarchy->getProperties();
			}

			$result = array("group"=>$guid, "hierarchy"=>$h, "node"=>$h, "sysPath"=>$sysPath, "relPath"=>$relPath, "name"=>$name, "description"=>$description, "type"=>ucwords($studioType) . " repository", "t"=>$type, 'props'=>$this->getFSProps($props));
			return $result;

		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function setHierarchyPropertiesData($type, $g, $h, $name, $desc, $path){
		try {
			$group = $this->currGroup[$type];
			$group->apol = $this->accessPolicy;
			$hierarchy = $group->getHierarchy($h);

			if ($hierarchy->getData()->getName() != $name) {
				$group->renameHierarchy($hierarchy, $name);
				$hierarchy = $group->getHierarchy($h);
			}

			$hData = $hierarchy->getData();

			$old_name = $hData->getName();
			$hData->setName($name);
			$hData->setDescription($desc);
			$hData->setBackend(new W3S_Backend(array('type' => 'local', 'location' => $path)));
	//		$hData->getBackend()->setLocation($path);

			$result = $hierarchy->saveData();
			return $result;

		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}


	public function getGroupPropertiesData($type){
		try {
			$group = $this->currGroup[$type];
			$group->apol = $this->accessPolicy;
			$guid = $group->getUID();
			$gData = $group->getData();
			$name = $gData->getName();
			$description = $gData->getDescription();
			$studioType = $gData->getType();
			$relPath = '';
			$sysPath = '';


			$result = array("group"=>$guid, "hierarchy"=>"", "node"=>"group", "sysPath"=>$sysPath, "relPath"=>$relPath, "name"=>$name, "description"=>$description, "type"=>ucwords($studioType) . " group", "t"=>$type);
			return $result;
		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function setGroupPropertiesData($type, $g, $name, $desc){
		$group = $this->currGroup[$type];
		$group->apol = $this->accessPolicy;

		$gData = $group->getData();

		$gData->setName($name);
		$gData->setDescription($desc);

		$group->saveData();
	}

	public function getCurrHierarchy($type){
		$group = $this->currGroup[$type];
		$group->apol = $this->accessPolicy;

		$h = $this->currHierarchy[$type];
		$hierarchy = $group->getHierarchy($h);

		return $hierarchy;
	}

	public function removeFromSessionCurrGH($type){
		$this->currGroup[$type] = -1;
		$this->currHierarchy[$type] = null;
	}

	public function getElementPath($group, $hierarchy, $element){
		try {
			$connection = $this->_get_connection();
		    $db_name = $group;
		    $dimension_name = $hierarchy;
		    $element_name = $element;
			$parent = $element;

			$path = array();

			while (true){
				$parent = palo_eparentname($connection, $db_name, $dimension_name, $parent, 1, true);
				if (!$parent)
					break;

				//array_push($path, $parent);
				array_push($path, $hierarchy . "_" . $parent);
			}

			if (sizeof($path) == 0){
				//check if is dimension (it has no parent)
				$result = palo_eindex($connection, $db_name, $dimension_name, $element_name, true);
				if (!$result)
					throw new WSS_Exception('Studio-err_no_parent', array('element_name' => $element_name), 'Unable to find parent for: ' . $element_name);

				return array(true, $hierarchy);
			}

			$result = $hierarchy . "/" . join("/", array_reverse($path));
			//print_r($result);
			return array(true, $result);
		}
		catch (WSS_Exception $wsse)
		{
			return array(false, $wsse->getId(), $wsse->getParams());
		}
		catch (Exception $e)
		{
			return array(false, 'Studio-err_no_parent', array('element_name' => $element_name));
		}

	}

	//test

	public function getCurrH($type){
		print_r($this->currHierarchy[$type]);
	}

	public function getCurrG($type){
		print_r($this->currGroup[$type]);
	}


// ============================================= node properties data and access list ===================================

	public function getReferenceNode($g, $h, $n){
		$conn = $this->_get_connection();
		$node = W3S_NodeData::getNodeData($conn, $g, $h, $n);
		$ref = $node->getReference();
		return array('g'=>$ref->getGroup(), 'h'=>$ref->getHierarchy(), 'n'=>$ref->getNode());
	}

	public function getHyperlinkURL($type, $g, $h, $n, $path = null){

		try {

			$conn = $this->_get_connection();

			if($type == 'report' && substr($g, 0, 1) == 'r'){
				$r_node = W3S_NodeData::getNodeData($conn, $g, $h, $n);
				$ref = $r_node->getReference();
				$f_node = W3S_NodeData::getNodeData($conn, $ref->getGroup(), $ref->getHierarchy(), $ref->getNode());
			}
			else{
				$f_node = W3S_NodeData::getNodeData($conn, $g, $h, $n);
			}

			$url = $f_node->getHyperlink()->getLink();

			//add to recent
			$location = array('group'=>$g, 'hierarchy'=>$h, 'node'=>$n, 'path'=>$path);
			$this->addRecent($type, 'hyperlink', $location, 'hyperlink');

			return $url;

		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function setHyperlinkPropertiesData($type, $g, $h, $n, $data){

		try {
			$group = $this->currGroup[$type];
			$group->apol = $this->accessPolicy;

			$h = $this->currHierarchy[$type];
			$hierarchy = $group->getHierarchy($h);
			$node = $hierarchy->getNode($n);
			$ndata = $node->getData();
			$ndata->setDescription($data['desc']);
			$ndata->getHyperlink()->setLink($data['url'], $data['target']);
			$node->saveData();

		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function setURLPluginPropertiesData($type, $g, $h, $n, $desc, $params){

		try {
			$group = $this->currGroup[$type];
			$group->apol = $this->accessPolicy;

			$h = $this->currHierarchy[$type];
			$hierarchy = $group->getHierarchy($h);
			$node = $hierarchy->getNode($n);
			$ndata = $node->getData();
			$ndata->setDescription($desc);
			$ndata->setParams($params);
			$node->saveData();

		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function getURLPluginURL ($type, $g, $h, $n, $path = null)
	{
		try
		{
			$this->addRecent($type, 'hyperlink', array('group' => $g, 'hierarchy' => $h, 'node' => $n, 'path' => $path), 'ahview');

			$params = '&viewid=';

			if ($type == 'report')
			{
				$conn = $this->accessPolicy->getConn();
				$nodeData = W3S_NodeData::getNodeData($conn, $g, $h, $n);

				if (substr($g, 0, 1) != 'r')
					$params .= $viewid = $g . '-' . $h . '-' . $n;
				else
				{
					$ref = $nodeData->getReference();
					$ref_g = $ref->getGroup();
					$ref_h = $ref->getHierarchy();
					$ref_n = $ref->getNode();

					$nodeData = W3S_NodeData::getNodeData($conn, $ref_g, $ref_h, $ref_n);
					$params .= $viewid = $ref_g . '-' . $ref_h . '-' . $ref_n;
				}

				foreach ($nodeData->getParams() as $key => $val)
					$params .= '&'. $key . '=' . $val;
			}
			else
				$params .= $viewid = $g . '-' . $h . '-' . $n;


			$user = $this->accessPolicy->getUser();
			$pass = $this->accessPolicy->getPass();

			$td = mcrypt_module_open('rijndael-128', '', 'cfb', '');

			$block_size = mcrypt_enc_get_block_size($td);
			$pass_len = strlen($pass);
			$pass = str_pad($pass, $pass_len + $block_size - $pass_len % $block_size);

			mcrypt_generic_init($td, CFG_SECRET, md5($user . $viewid, true));
			$pass = urlencode(base64_encode(mcrypt_generic($td, $pass)));
			mcrypt_generic_deinit($td);

			mcrypt_module_close($td);


			$prefs = $_SESSION['prefs'];
			$params = 'user=' . urlencode($user) . '&pass=' . $pass . '&locale=' . $prefs->search('general/l10n') . '&theme=' . $prefs->search('general/theme') . $params;

			return array('type' => 'url', 'url' => CFG_AHVIEWER_PATH . '?' . $params . '&cksum=' . sha1($params . CFG_SECRET), 'target' => 'self');
		}
		catch (Exception $e)
		{
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function getNodeName($g, $h, $n){

		try {
			$conn = $this->_get_connection();
			$nodeData = W3S_NodeData::getNodeData($conn, $g, $h, $n);
			$nodeName =  $nodeData->getName();

			return $nodeName;
			}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function getNodePropertiesData($type, $g, $h, $n){

		try {
			$group = $this->currGroup[$type];
			$group->apol = $this->accessPolicy;

			$h = $this->currHierarchy[$type];
			$hierarchy = $group->getHierarchy($h);
			$node = $hierarchy->getNode($n);

			$name = $node->getData()->getName();
			$description = $node->getData()->getDescription();
			$studioType = $node->getType();

			$relPath = $node->getRelPath($node, false);
			$sysPath = '';
			$size = '0.00 B';
			$url = '';
			$urlPluginParams = '';
			$extension = '';
			$props = null;

			switch ($studioType) {
				case "template":
				case "rstatic":
					$ghn = $this->getTWorkbook($n);
					$group = new W3S_Group($this->accessPolicy, $ghn['grp']);

					if ($group->getType() == 'file' && $this->plugins['fs'] instanceof W3S_Plugin)
						$group->registerPlugin($this->plugins['fs'], true);

					$hierarchy = $group->getHierarchy($ghn['hr']);
					$node = $hierarchy->getNode($ghn['node']);

					$props = $node->getProperties();
					break;

				case "workbook":
					$extension = ".wss";
					$relPath = $relPath . $extension;

					$props = $node->getProperties();
					break;

				case "folder":
					$sysPath = $node->getSysPath() . $extension;

					$props = $node->getProperties();
					break;

				case "hyperlink":
					$url = $node->getData()->getHyperlink()->getLink();
					break;

				case 'urlplugin':
				case 'rurlplugin':
					$urlPluginParams = $node->getData()->getParams();
					break;
				case "rfolder":
					break;
				case "static":
					$props = $node->getProperties();
					break;
				case "rhyperlink":
					break;
			}

			$result = array("group"=>$g, "hierarchy"=>$h, "node"=>$n ,"sysPath"=>$sysPath, "relPath"=>$relPath, "type"=>ucwords($studioType), "name"=>$name, "description"=>$description, "url"=>$url, "urlPluginParams"=>$urlPluginParams, "t"=>$type, "props"=>$this->getFSProps($props));
			return $result;

		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}

	}


	public function getFSProps($props){

			if(isset($props) && is_array($props)){

				for($i = 0, $count = count($props); $i<$count; $i++){
					if ($props[$i]['type'] == 'fs'){
						$fs =  $props[$i]['props'];
						$fs_props = array(
							'ctime'=>date('d.m.Y H:i:s', $fs['ctime']),
							'mtime'=>date('d.m.Y H:i:s', $fs['mtime']),
							'atime'=>date('d.m.Y H:i:s', $fs['atime']),
							'size'=>$fs['size'],
							'blksize'=>$fs['blksize']
							);

						return $fs_props;
						break; //for now no other properties implemented
					}
				}
			}

			return null;
	}

	public function getExternURI (array $params, array $creds = null)
	{
		$srv = &$_SERVER;
		$uri = 'http' . ($srv['HTTPS'] && $srv['HTTPS'] != 'off' ? 's' : '') . '://' . $srv['SERVER_NAME'] . (intval($srv['SERVER_PORT']) == 80 ? '' : ':' . $srv['SERVER_PORT'])
				 . '/spreadsheet/root/ui/studio/?_=' . urlencode(base64_encode(gzcompress(json_encode($params))));

		if (!is_array($creds))
			return $uri;

		if ($creds['flag'] == 1)
		{
			$creds['user'] = $this->accessPolicy->getUser();
			$creds['pass'] = $this->accessPolicy->getPass();
		}

		if (!isset($creds['user']) || !isset($creds['pass']))
			return $uri;

		$pass = $creds['pass'];

		$td = mcrypt_module_open('rijndael-128', '', 'cfb', '');

		$block_size = mcrypt_enc_get_block_size($td);
		$pass_len = strlen($pass);
		$pass = str_pad($pass, $pass_len + $block_size - $pass_len % $block_size);

		mcrypt_generic_init($td, CFG_SECRET, md5($creds['user'], true));
		$pass = mcrypt_generic($td, $pass);
		mcrypt_generic_deinit($td);

		mcrypt_module_close($td);

		return $uri . '&user=' . urlencode($creds['user']) . '&pass=' . urlencode(base64_encode($pass));
	}

	public function addRecent($context, $type, $location, $subtype){ //$context = files, $type = hyperlink, $location = array(group:group, hierarchy:hierarchy, node:node), $subtype = ahview

		if($context == 'file' || substr($location['group'], 0, 1) == 'f'){
			// Group
			if (get_class($this->currGroup[$context]) == 'W3S_Group' && $location["group"] == $this->currGroup[$context]->getUID()){
				$group = $this->currGroup[$context];
				$group->apol = $this->accessPolicy;
			}
			else {
				$group = new W3S_Group($this->accessPolicy, $location["group"]);
			}

			$hierarchy = $group->getHierarchy($location["hierarchy"]);
			$node = $hierarchy->getNode($location["node"]);

			$path = '//' . $group->getData()->getName() . '/' . $hierarchy->getData()->getName() . '/' . $node->getRelPath($node, false);
			$location["path"] = $path;
		}

		$recent = new Recent($this->accessPolicy);
		$recent->add($context . 's', $type, $location, $subtype);
		$recent->save();

	}


	/*
	 * ###########
	 * ### ACL ###
	 * ###########
	 */

	/*
	 * \brief returns max and effective perm (g, h or n level) for a given user group
	 *
	 * \author
	 * Predrag Malicevic <predrag.malicevic@develabs.com>
	 *
	 */

	public function getPerms4UGroup ($user_group, $type, $h = null, $n = null)
	{
		// create list of only needed rules
		$rules = array('database');

		if ($h)
		{
			$rules[] = 'dimension';

			if ($n)
				$rules[] = 'dimension element';
		}


		// clone access policy and reload it with given user group and list of rules
		$apol = clone $this->accessPolicy;
		$apol->reload(array($user_group), $rules);


		// find out perm_g/perm_h
		$group = $this->currGroup[$type];
		$g = $group->getUID();

		list ($perm, $perm_g, $perm_h) = W3S_Group::_calcPerms($apol, $g);

		$rule_g = $apol->getRule('database');

		// bail out if asking only for perm_g
		if (!$h)
			return array('eff' => $perm_g, 'own' => $perm, 'def' => $rule_g, 'max' => $rule_g);


		// find out perm_h/perm_n
		$perm = $apol->calcPerms($g, 'meta', array($h));
		$perm = $perm[$h];

		$def = $perm_h;

		$rule_h = $apol->getRule('dimension');
		$rule_n = $apol->getRule('dimension element');

		if ($perm != -1)
		{
			$perm_h = $perm & $rule_h;
			$perm_n = $perm & $rule_n;
		}
		else
			$perm_n = $perm_h & $rule_n;

		// bail out if asking only for perm_h
		if (!$n)
			return array('eff' => $perm_h, 'own' => $perm, 'def' => $def, 'max' => $rule_h);


		// asking for perm_n, will need to construct consolidation path of given node
		$hierarchy = $group->getHierarchy($h);
		$node = $hierarchy->getNode($n);

		$path = array($n);
		$node->getSuperUIDs($path);

		// find out perm_n's for all nodes in path
		$perms = $apol->calcPerms($g, $h, $path, true);

		// remove the node from path
		array_shift($path);

		$def = $perm_n;

		foreach ($path as $uid)
			if (($perm = $perms[$uid]) != -1)
			{
				$def = $perm & $rule_n;
				break;
			}

		$perm_n = ($perm = $perms[$n]) != -1 ? $perm & $rule_n : $def;

		return array('eff' => $perm_n, 'own' => $perm, 'def' => $def, 'max' => $rule_n);
	}


	public function getNodePermission($g, $h, $n){
		try {
			$perm = -1;
			if (($group = new W3S_Group($this->accessPolicy, $g)) instanceof W3S_Group){
				if (($hierarchy = $group->getHierarchy($h)) instanceof W3S_Hierarchy){
					if (($node = $hierarchy->getNode($n)) instanceof W3S_Node){
						$perm = $node->getPermN();
					}
				}
			}

			return $perm;
		}
		catch (Exception $e) {
			return array(false, $e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function setNodePemission($group, $hierarchy, $node, $accessGroup, $perrmision, $type){
		/*
		 *  palo_datav (string $connection, string $db_name, array $cube_name,  $coordinate1)
		 * */
		try {
		    $connection = $this->_get_connection();
		    $dbName = $group;
			$cubeName = '#_GROUP_DIMENSION_DATA_' . $hierarchy;

			$dimensions = array(array($accessGroup, $node));
			$values=array($perrmision);

			$result = $this->_setData($connection, $dbName, $cubeName, $dimensions, $values);
			palo_disconnect($connection);

			if ($this->_isError($result))
				throw new Exception($result[0], $result[1]);

			$refresh = $this->refreshNodes($group, $hierarchy, $node, $accessGroup, $type);

			return array(true, $refresh);

		}
		catch (Exception $e) {
			return array(false, $e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	public function refreshNodes($g, $h, $n, $accessGroup, $type){

		try {

			$refresh = false;

			if (in_array($accessGroup, $this->accessPolicy->getGroups())){

				$refresh = true;
				$group = $this->currGroup[$type];
				$group->apol = $this->accessPolicy;

				if ($h == 'meta'){
					if ($n == 'group'){
						$group->calcPerms();
					}
					else {
						$h = $this->currHierarchy[$type];
						$hierarchy = $group->getHierarchy($h);
						$hierarchy->calcPerms();
					}
				}
				else {
					$h = $this->currHierarchy[$type];
					$hierarchy = $group->getHierarchy($h);
//					$hierarchy->calcPerms();
					$node = $hierarchy->getNode($n);
					$node->calcPerms();
				}
			}

			return $refresh;

		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}
	}

	// WSS onInsertHyperlink
	public function getNodeHyperlinkPropertiesData($g, $h, $n){

		try {
			$type = 'wss';
			$group = $this->currGroup[$type];
			$group->apol = $this->accessPolicy;

			$h = $this->currHierarchy[$type];
			$hierarchy = $group->getHierarchy($h);

			$node = $hierarchy->getNode($n);

			$path =  '/' . $group->getData()->getName() . '/' . $hierarchy->getData()->getName() . $node->getRelPath($node, false, true);

			$result = array("g"=>$g, "h"=>$h, "n"=>$n ,"p"=>$path, "t"=>$relPath, "t"=>$node->getData()->getEffectiveType());

			return $result;
		}
		catch (Exception $e) {
			return array($e->getMessage(), self::$errMsg[$e->getCode()]);
		}

	}

	public function treePasteNodes ($type)
	{
		if (!isset($this->currGroup[$type]) || !(($group = $this->currGroup[$type]) instanceof W3S_Group) || !(($hierarchy = $group->getHierarchy($this->currHierarchy[$type])) instanceof W3S_Hierarchy))
			return false;

		$group->apol = $this->accessPolicy;

		$args = func_get_args();

		return call_user_func_array(array($hierarchy, 'node'), array_slice($args, 1));
	}


	/*
	 * ####################################################
	 * ### GROUP, HIERARCHY & TREE MANIPULATION METHODS ###
	 * ####################################################
	 */

	/*
	 * \brief sets the currently selected group for a given type (file, report, ...)
	 *
	 * \author
	 * Predrag Malicevic <predrag.malicevic@develabs.com>
	 *
	 */

	public function treeSetGroup ($type, $uid)
	{
		if (!isset($this->currGroup[$type]))
			return false;

		$group = new W3S_Group($this->accessPolicy, $uid);

		$this->currGroup[$type] = $group;

		if ($group->getType() == 'file' && $this->plugins['fs'] instanceof W3S_Plugin)
			$group->registerPlugin($this->plugins['fs'], true);

		$this->currHierarchy[$type] = ($hierarchy = $group->getFirstHierarchy()) instanceof W3S_Hierarchy ? $hierarchy->getUID() : '';

		return true;
	}

	/*
	 * \brief gets the currently selected group for a given type (file, report, ...)
	 *
	 * \author
	 * Predrag Malicevic <predrag.malicevic@develabs.com>
	 *
	 */

	public function treeGetGroup ($type)
	{
		if (!isset($this->currGroup[$type]) || !(($group = $this->currGroup[$type]) instanceof W3S_Group))
			return false;

		return $group->getUID();
	}

	/*
	 * \brief sets the currently selected hierarchy for a given type (file, report, ...)
	 *
	 * \author
	 * Predrag Malicevic <predrag.malicevic@develabs.com>
	 *
	 */

	public function treeSetHierarchy ($type, $uid)
	{
		if (!isset($this->currGroup[$type]) || !(($group = $this->currGroup[$type]) instanceof W3S_Group))
			return false;

		$group->apol = $this->accessPolicy;

		if (!(($hierarchy = $group->getHierarchy($uid)) instanceof W3S_Hierarchy))
			return false;

		$this->currHierarchy[$type] = $uid;

		return true;
	}

	/*
	 * \brief gets the currently selected hierarchy for a given type (file, report, ...)
	 *
	 * \author
	 * Predrag Malicevic <predrag.malicevic@develabs.com>
	 *
	 */

	public function treeGetHierarchy ($type)
	{
		if (!isset($this->currGroup[$type]) || !(($group = $this->currGroup[$type]) instanceof W3S_Group) || !(($hierarchy = $group->getHierarchy($uid = $this->currHierarchy[$type])) instanceof W3S_Hierarchy))
			return '';

		return $uid;
	}

	/*
	 * \brief proxies calls to W3S_Group static methods
	 *
	 * \author
	 * Predrag Malicevic <predrag.malicevic@develabs.com>
	 *
	 */

	public function treeMngGroup ($method)
	{
		if (!is_callable($callback = 'W3S_Group::' . $method))
			return false;

		$args = func_get_args();
		$args[0] = $this->accessPolicy;

		$res = call_user_func_array($callback, $args);

		return ($res instanceof W3S_Group) ? $res->getUID() : $res;
	}

	/*
	 * \brief proxies calls to currently selected group (of a given type)
	 *
	 * \author
	 * Predrag Malicevic <predrag.malicevic@develabs.com>
	 *
	 */

	public function treeMngHierarchy ($type)
	{
		if (!isset($this->currGroup[$type]) || !(($group = $this->currGroup[$type]) instanceof W3S_Group))
			return false;

		$group->apol = $this->accessPolicy;

		$args = func_get_args();

		return call_user_func_array(array($group, 'hierarchy'), array_slice($args, 1));
	}

	/*
	 * \brief proxies calls to currently selected hierarchy (of a given type)
	 *
	 * \author
	 * Predrag Malicevic <predrag.malicevic@develabs.com>
	 *
	 */

	public function treeMngNode ($type)
	{
		if (!isset($this->currGroup[$type]) || !(($group = $this->currGroup[$type]) instanceof W3S_Group) || !(($hierarchy = $group->getHierarchy($this->currHierarchy[$type])) instanceof W3S_Hierarchy))
			return false;

		$group->apol = $this->accessPolicy;

		$args = func_get_args();
		array_splice($args, 0, 1);

		if (count($args) > 1)
			return call_user_func_array(array($hierarchy, 'node'), $args);

		if (!is_array($args[0]))
			return false;

		$res = array();

		foreach ($args[0] as &$c_args)
		{
			try
			{
				$res[] = $res_tmp = call_user_func_array(array($hierarchy, 'node'), $c_args);
			}
			catch (Exception $e)
			{
				$res[] = false;
				break;
			}

			if ($res_tmp === false)
				break;
		}

		return $res;
	}

	public function treeNodeDelMap ($node, $tvar) {

		 $type = 'report';

	    if (!isset($this->currGroup[$type]) || !(($group = $this->currGroup[$type]) instanceof W3S_Group) || !(($hierarchy = $group->getHierarchy($this->currHierarchy[$type])) instanceof W3S_Hierarchy)
	        || !(($node = $hierarchy->getNode($node)) instanceof W3S_Node))
	      return false;

	    $group->apol = $this->accessPolicy;
	    $node->getData()->deleteMap($tvar);

	    $node->saveData();

	    return true;

	}

	public function treeNodeMap ($m_type, $node, array $map, $defVal, $tplVar)
	  {
	    $type = 'report';

	    if (!isset($this->currGroup[$type]) || !(($group = $this->currGroup[$type]) instanceof W3S_Group) || !(($hierarchy = $group->getHierarchy($this->currHierarchy[$type])) instanceof W3S_Hierarchy)
	        || !(($node = $hierarchy->getNode($node)) instanceof W3S_Node))
	      return false;

	    $group->apol = $this->accessPolicy;

	    //	choose Palo List / Const List / Const

	    if ($m_type==0) {
	      $node->getData()->setMap(new W3S_PaloList($map[0]['conn'], $map[0]['db'], $map[0]['dim'], $map[0]['subs']), $defVal, $tplVar);
	    } else if ($m_type==1) {
	      $node->getData()->setMap(new W3S_ConstList($map), $defVal, $tplVar);
	    } else if ($m_type==2) {
	      $node->getData()->setMap(new W3S_ConstValue($map[0]), $defVal, $tplVar);
	    } else if ($m_type==3) {
	      $node->getData()->setMap(new W3S_LinkList($map[0]), $defVal, $tplVar);
	    } else if ($m_type==4) {
	      $node->getData()->setMap(new W3S_PaloSubsetList(json_encode($map)), $defVal, $tplVar);
	      // here link! --> see how to implement
	    } else if ($m_type == 'none')
	    	$node->getData()->deleteMap($tplVar);

	    $node->saveData();

	    return true;
	  }

	  public function treeNodeGetMap ($node, $tvar)
	  {
	    $type = 'report';
	    if (!$tvar) $tvar = null;

	    if (!isset($this->currGroup[$type]) || !(($group = $this->currGroup[$type]) instanceof W3S_Group) || !(($hierarchy = $group->getHierarchy($this->currHierarchy[$type])) instanceof W3S_Hierarchy)
	        || !(($node = $hierarchy->getNode($node)) instanceof W3S_Node))
	      return false;

	    $group->apol = $this->accessPolicy;

	    $map = $node->getData()->getMap($tvar);

	    if (!$map) return false;

	    //print_r($map);
	    $defval = $map['defval'];

	    //	need to check for type!
	    if (!$type) return false;
	    if ($map['list']) $type = $map['list']->getType();

	    if ($type == 'palo')
	      return array('type'=>$type, 'conn' => $map['list']->getConnection(), 'db' => $map['list']->getDb(), 'list' => $map['list']->getList(), 'defval' => $defval, 'ordnum' => $map['ordnum']);
	    else if ($type == 'clist')
	      return array('type'=>$type, 'values' => $map['list']->getValues(), 'defval' => $defval, 'ordnum' => $map['ordnum']);
	    else if ($type == 'cval')
	      return array('type'=>$type, 'value' => $map['list']->getValue(), 'defval' => $defval, 'ordnum' => $map['ordnum']);
		else if ($type == 'link')
	      return array('type'=>$type, 'value' => $map['list']->getElement(), 'defval' => $defval, 'ordnum' => $map['ordnum']);
	    else if ($type == 'palosubset')
	      return array('type'=>$type, 'value' => $map['list']->getData(), 'defval' => $defval, 'ordnum' => $map['ordnum']);
	  }

	/*
	 * \brief dumps the structure of a given hierarchy (used directly by Ext's TreeLoader)
	 *
	 * \author
	 * Predrag Malicevic <predrag.malicevic@develabs.com>
	 *
	 */

	public function treeDump ()
	{
		if (!isset($_POST['type']))
			return array();

		$type = $_POST['type'];

		if (!isset($this->currGroup[$type]) || !(($group = $this->currGroup[$type]) instanceof W3S_Group))
			return array();

		if (isset($_POST['hierarchy']))
		{
			if (!$this->treeSetHierarchy($type, $_POST['hierarchy']))
				return array();

			$hierarchy = $group->getHierarchy($this->currHierarchy[$type]);
		}
		else if (!(($hierarchy = $group->getHierarchy($this->currHierarchy[$type])) instanceof W3S_Hierarchy))
			return array();

		if (!isset($_POST['node']) || !(($node = $hierarchy->getNode($_POST['node'])) instanceof W3S_Node))
			$node = $hierarchy->getRoot();

		$level = isset($_POST['level']) ? intval($_POST['level']) : -1;
		$filter = isset($_POST['filter']) ? split(',', $_POST['filter']) : null;
		$multi_h = isset($_POST['multi_h']) ? ($_POST['multi_h'] == 'true') : false;

		return $node->dump($level, $filter, $multi_h);
	}

	private function getLinks($link_type, $nodes) {
		// Refresh group stored in session
		$type = 'file';
		$g = $this->currGroup[$type]->getUID();
		$h = $this->currHierarchy[$type];
		$this->treeSetGroup($type, $g);
		$this->treeSetHierarchy($type, $h);

		$group = $this->currGroup[$type];
		$group->apol = $this->accessPolicy;
		$h = $this->currHierarchy[$type];
		$hierarchy = $group->getHierarchy($h);

	    $nodes_cnt = count($nodes);
	    $all_links = array();

	    foreach ($nodes as $n) {
		    $node = $hierarchy->getNode($n);

		    $links = array();
		    $node->getLinks($link_type, $links, $nodes_cnt > 1 ? $node : null);

		    if (!count($links))
		    	continue;

			foreach ($links as $link)
				foreach ($link['links'] as $node_link)
					$link_struct[$node_link['group']][$node_link['hierarchy']][$node_link['node']] = 'unknown';

			$all_links = array_merge($all_links, $links);
	    }

	    if (!count($all_links))
		    return $all_links;

		foreach ($link_struct as $grp => $hrcs) {
			if (!(($group = new W3S_Group($this->accessPolicy, $grp)) instanceof W3S_Group))
				continue;

			foreach ($hrcs as $hrc => $nds) {
				if (!(($hierarchy = $group->getHierarchy($hrc)) instanceof W3S_Hierarchy))
					continue;

				foreach ($nds as $nd => $path) {
					if (!(($node = $hierarchy->getNode($nd)) instanceof W3S_Node))
						continue;

					$link_struct[$grp][$hrc][$nd] = '//' . $group->getData()->getName() . '/' . $hierarchy->getData()->getName() . $node->getRelPath($node, false);

					unset($node);
				}

				unset($hierarchy);
			}

			unset($group);
		}

		$is_resource = $link_type == W3S_NodeData::$resource;

		if (count($all_links) == 1 && !strlen($all_links[0]['path'])) {
			foreach ($all_links[0]['links'] as $node_link) {
				$larr = array($link_struct[$node_link['group']][$node_link['hierarchy']][$node_link['node']]);

				if ($is_resource) {
					$larr[] = $node_link['group']; $larr[] = $node_link['hierarchy']; $larr[] = $node_link['node'];
				}

				$res_arr[] = $larr;
			}
		} else {
			foreach ($all_links as $link)
				foreach ($link['links'] as $node_link) {
					$larr = array($link['path'], $link_struct[$node_link['group']][$node_link['hierarchy']][$node_link['node']]);

					if ($is_resource) {
						$larr[] = $node_link['group']; $larr[] = $node_link['hierarchy']; $larr[] = $node_link['node'];
					}

					$res_arr[] = $larr;
				}
		}

	    return $res_arr;
	}

	public function getReferrers($nodes) {
		return $this->getLinks(W3S_NodeData::$referrer, $nodes);
	}

	public function getResources($nodes) {
		return $this->getLinks(W3S_NodeData::$resource, $nodes);
	}

	public function getDependents($nodes) {
		return $this->getLinks(W3S_NodeData::$dependent, $nodes);
	}

	public function setResources($n, $old_r, $new_r){
		$type = 'file';
		$g = $this->currGroup[$type]->getUID();
		$group = $this->currGroup[$type];
		$group->apol = $this->accessPolicy;

		$h = $this->currHierarchy[$type];
		$hierarchy = $group->getHierarchy($h);
		$node = $hierarchy->getNode($n);

		$d_res = array_diff_key($old_r, $new_r);
		$a_res = array_diff_key($new_r, $old_r);


		//remove resource - dependent link
		foreach ($d_res as $k => $v){
			$result = $this->removeDependent($v, array($g, $h, $n));
			if ($result)
				$node->removeResource($v[0], $v[1], $v[2]);
		}

		//add new resource - dependent link
		foreach ($a_res as $k => $v){
			$result = $this->addDependent($v, array($g, $h, $n));
			if ($result)
				$node->addResource($v[0], $v[1], $v[2]);
		}

		$node->saveData();

	}

	function removeDependent($d, $r){

		try {

			if (($group = new W3S_Group($this->accessPolicy, $d[0])) instanceof W3S_Group){
				if (($hierarchy = $group->getHierarchy($d[1])) instanceof W3S_Hierarchy){
					if (($node = $hierarchy->getNode($d[2])) instanceof W3S_Node){
						$node->removeDependent($r[0], $r[1], $r[2]);
						$node->saveData();

						return true;
					}
				}
			}

			return false;
		}
		catch (Exception $e) {
			return false;;
		}
	}

	function addDependent($d, $r){

		try {

			if (($group = new W3S_Group($this->accessPolicy, $d[0])) instanceof W3S_Group){
				if (($hierarchy = $group->getHierarchy($d[1])) instanceof W3S_Hierarchy){
					if (($node = $hierarchy->getNode($d[2])) instanceof W3S_Node){
						$node->addDependent($r[0], $r[1], $r[2]);
						$node->saveData();

						return true;
					}
				}
			}

			return false;
		}
		catch (Exception $e) {
			return false;
		}
	}


	//===================================================================================================================================================
	private function _getConn($connName)
	{
		try
		{
			$connData = $this->getConnection($connName);

			if (strtoupper($connData[1]) != 'PALO')
				throw new WSS_Exception('Studio-err_bad_palo_conn_type', array('conn_name' => $connName), 'Bad PALO connection type for connection name: ' . $connName);

			$tmpErrRep = ini_get('error_reporting');
			error_reporting(0);
			if ($connData[7] == 0)
				$conn = palo_init($connData[3], $connData[4], $connData[5], $connData[6]);
			else
				$conn = palo_init($connData[3], $connData[4], $this->accessPolicy->getUser(), $this->accessPolicy->getPass());
			error_reporting($tmpErrRep);

			if ($conn === '#NULL!')
				throw new Exception();

			palo_use_unicode(true);

			return $conn;
		}
		catch (WSS_Exception $wsse)
		{
			throw $wsse;
		}
		catch (Exception $e)
		{
			throw new WSS_Exception('Studio-err_no_conn', array('conn_name' => $connName), 'Unable to make connection for connection name: ' . $connName);
		}
	}

	public function getDatabases($connName)
	{
		try
		{
			$conn = $this->_getConn($connName);
			$listDB = palo_root_list_databases($conn, true, true);
			palo_disconnect($conn);

			return array(true, $listDB);
		}
		catch (WSS_Exception $wsse)
		{
			return array(false, $wsse->getId(), $wsse->getParams());
		}
		catch (Exception $e)
		{
			return array(false, 'Studio-err_no_list_of_databases', array('conn_name' => $connName));
		}
	}

	public function getDimensions($connName, $dbName)
	{
		try
		{
			$conn = $this->_getConn($connName);
			$listDims = palo_database_list_dimensions($conn, $dbName, 0); // dimension type 0 = normal
			palo_disconnect($conn);

			$unlistedDims = array('#viewcolumns', '#viewrows', '#subsetrows', '#subsetcolumns', '#views');
			$retList = array();
			foreach ($listDims as $dim)
				if (!in_array($dim, $unlistedDims))
					 $retList[] = $dim;

			return array(true, $retList);
		}
		catch (WSS_Exception $wsse)
		{
			return array(false, $wsse->getId(), $wsse->getParams());
		}
		catch (Exception $e)
		{
			return array(false, 'Studio-err_no_list_of_dimensions', array('conn_name' => $connName, 'db_name' => $dbName));
		}
	}

	public function getDimElements($connName, $dbName, $dimName)
	{
		try
		{
			$conn = $this->_getConn($connName);
			$listElems = palo_dimension_list_elements($conn, $dbName, $dimName, true);
			palo_disconnect($conn);

			$retElems = array();
			foreach ($listElems as $elem)
				$retElems[] = $elem['name'];

			return array(true, $retElems);
		}
		catch (WSS_Exception $wsse)
		{
			return array(false, $wsse->getId(), $wsse->getParams());
		}
		catch (Exception $e)
		{
			return array(false, 'Studio-err_no_list_of_elements', array('conn_name' => $connName, 'db_name' => $dbName, 'dim_name' => $dimName));
		}
	}

	public function getSubsets($connName, $dbName, $dimName)
	{
		try
		{
			$conn = $this->_getConn($connName);
			$listSubset = palo_dimension_list_elements($conn, $dbName, '#_SUBSET_', true);

			$listDimGlobalSubsets = array();
			$listDimLocalSubsets = array();
			foreach($listSubset as $subset)
			{
				if (palo_data($conn, $dbName, '#_SUBSET_GLOBAL', $dimName, $subset['name']) != null)
					$listDimGlobalSubsets[] = $subset['name'];
				if (palo_data($conn, $dbName, '#_SUBSET_LOCAL', $dimName, $_SESSION['servid_' . $serv_id]['username'], $subset['name']) != null)
					$listDimLocalSubsets[] = $subset['name'];
			}

			return array(true, array('global_subsets' => $listDimGlobalSubsets, 'local_subsets' => $listDimLocalSubsets));
		}
		catch (WSS_Exception $wsse)
		{
			return array(false, $wsse->getId(), $wsse->getParams());
		}
		catch (Exception $e)
		{
			return array(false, 'Studio-err_no_list_of_subsets', array('conn_name' => $connName, 'db_name' => $dbName, 'dim_name' => $dimName));
		}
	}

	// get Database/Dimension/Subsets Nodes
	public function getDDSTreeNodes()
	{
		$elemID = isset($_POST['node']) ? stripslashes($_POST['node']) : null;
		$paloPath = isset($_POST['_palo_path']) ? json_decode(stripslashes($_POST['_palo_path']), true) : null;
		$connName = isset($_GET['connName']) ? $_GET['connName'] : null;
		$connName = isset($_POST['_conn_name']) ? $_POST['_conn_name'] : $connName;

//		$elemID = 'root'; // TEST ONLY
//		$elemID = '_test_'; // TEST ONLY
//		$paloPath = array('db' => 'Demo', 'dim' => 'Months'); // TEST ONLY
//		$connName = 'Palo local'; // TEST ONLY

		$nodes = array();
		if (($elemID != null) && ($connName != null))
		{
			try
			{
				// Testing is connection to Connection name correct
				$conn = $this->_getConn($connName);

				if (strtoupper($elemID) == strtoupper('root'))
				{
					$rootData = $this->getDatabases($connName);
					if ($rootData[0])
						for ($i=0; $i<count($rootData[1]); $i++)
							$nodes[] = array('text' => $rootData[1][$i], 'iconCls' => 'w3s_database', 'leaf' => false, '_palo_path' => array('db' => $rootData[1][$i]));
				}
				else if (count($paloPath) == 1)
				{
					$dbData = $this->getDimensions($connName, $paloPath['db']);
					if ($dbData[0])
						for ($i=0; $i<count($dbData[1]); $i++)
							$nodes[] = array('text' => $dbData[1][$i], 'iconCls' => 'w3s_dimension', 'leaf' => false, '_palo_path' => array('db' => $paloPath['db'], 'dim' => $dbData[1][$i]));
				}
				else if (count($paloPath) == 2)
				{
					$dimData = $this->getSubsets($connName, $paloPath['db'], $paloPath['dim']);
					if ($dimData[0])
						for ($i=0; $i<count($dimData[1]['global_subsets']); $i++)
							$nodes[] = array('text' => $dimData[1]['global_subsets'][$i], 'iconCls' => 'w3s_subset', 'leaf' => true, '_palo_path' => array('db' => $paloPath['db'], 'dim' => $paloPath['dim'], 'ss' => $dimData[1]['global_subsets'][$i]));
				}
			}
			catch (WSS_Exception $wsse)
			{
				$nodes[] = array('text' => '', 'iconCls' => 'w3s_error', 'leaf' => true, '_error' => array('id' => $wsse->getId(), 'params' => $wsse->getParams()));
			}
			catch (Exception $e)
			{}
		}

		return $nodes;
	}

	public function getSubsetList($connName, $dbName, $dimName, $ssName, $ssType =0)
	{
		try
		{
			$conn = $this->_getConn($connName);

			$retStr = '';
			if ($ssType == 0)
				$retStr = palo_data($conn, $dbName, '#_SUBSET_GLOBAL', $dimName, $ssName);
			else if ($ssType == 1)
			{
				$connData = $this->getConnection($connName);
				$retStr = palo_data($conn, $dbName, '#_SUBSET_LOCAL', $dimName, $connData[5], $ssName);
			}

			if ($retStr != '')
			{
				$subsetInfo = new DOMDocument();
				$subsetInfo->preserveWhiteSpace = false;
				$subsetInfo->loadXML($retStr);

				$subsetDXPath = new DOMXPath($subsetInfo);
				$subsetDXPath->registerNameSpace('subset', 'http://www.jedox.com/palo/SubsetXML');
				$wssPalo = new Palo();
				$execArr = $wssPalo->execPHPPaloSubsetFunc($conn, $dbName, $dimName, PaloConf::$paloSubsetDesc, $subsetDXPath);
				palo_disconnect($conn);

				$retArr = array();
				for ($i=0; $i<count($execArr); $i++)
					$retArr[] = $execArr[$i]['name'];

				return array(true, $retArr);
			}
			else
				return array(false, 'Studio-err_no_subset_list_empty_subset', array('conn_name' => $connName, 'db_name' => $dbName, 'dim_name' => $dimName, 'ss_name' => $ssName, 'ss_type' => $ssType));
		}
		catch (WSS_Exception $wsse)
		{
			return array(false, $wsse->getId(), $wsse->getParams());
		}
		catch (Exception $e)
		{
			return array(false, 'Studio-err_no_subset_list', array('conn_name' => $connName, 'db_name' => $dbName, 'dim_name' => $dimName, 'ss_name' => $ssName, 'ss_type' => $ssType));
		}
	}

	private function _getSubsetListByXML($inArr)
	{
		try
		{
			// +++ init vars +++ //
			$settings = $inArr[0];
			$dataArr = $inArr[1];

			$serv_id = $settings[0];
			$dbName = $settings[1];
			$dimName = $settings[2];

			$hierOrder = false;
			// --- init vars --- //

			$conn = $this->_getConn($serv_id);

			$xmlDoc = new DOMDocument();
			$xmlDoc->preserveWhiteSpace = false;
			$xmlStr = $this->_genPaloSubsetXML($dataArr, array('conn' => $conn, 'db_name' => $dbName, 'dim_name' => $dimName));

			$xmlDoc->loadXML($xmlStr);

			$subsetDXPath = new DOMXPath($xmlDoc);
			$subsetDXPath->registerNameSpace('subset', 'http://www.jedox.com/palo/SubsetXML');
			$wssPalo = new Palo();
			$execArr = $wssPalo->execPHPPaloSubsetFunc($conn, $dbName, $dimName, PaloConf::$paloSubsetDesc, $subsetDXPath);

			$retArr = array();
			for ($i=0; $i<count($execArr); $i++)
				$retArr[] = $execArr[$i]['name'];

			return array(true, $retArr);
		}
		catch (WSS_Exception $wsse)
		{
			throw $wsse;
		}
		catch (Exception $e)
		{
			throw new WSS_Exception('Studio-err_no_ad_hoc_subset_list', array('conn_name' => $serv_id, 'db_name' => $dbName, 'dim_name' => $dimName));
		}
	}

	private function _genPaloSubsetXML(&$dataArr, $setupArr =null)
	{
		$xmlDoc = new DOMDocument();
		$xmlDoc->preserveWhiteSpace = false;
		Palo::fixPaloSubsetInObject($dataArr, $setupArr);
		$this->_genXmlFromArr($xmlDoc, $xmlDoc, $dataArr);

		$docXslt = new DOMDocument();
		$xsl = new XSLTProcessor();

		if (is_file(self::PALO_SUBSET_GRAMMAR))
			$docXslt->load(self::PALO_SUBSET_GRAMMAR);
		else
			throw new WSS_Exception('Studio-err_bad_subset_grammar_file', array('file_name' => PaloConf::PALO_SUBSET_GRAMMAR), 'Can not find ' . self::PALO_SUBSET_GRAMMAR . ' file!');
		$xsl->importStyleSheet($docXslt);

		$xmlDoc = $xsl->transformToDoc($xmlDoc);
		$xmlDoc->removeChild($xmlDoc->firstChild);
		$tmpNode = $xmlDoc->createProcessingInstruction('palosubset', 'version="1.0"');
		$xmlDoc->insertBefore($tmpNode, $xmlDoc->firstChild);

		return $xmlDoc->saveXML();
	}

	private function _genXmlFromArr($xmlDoc, $node, $arr)
	{
		foreach($arr as $key => $val)
		{
			$tagName = (is_numeric($key)) ? 'elem' : $key;

			$tmpElem = $xmlDoc->createElement($tagName);
			$newNode = $node->appendChild($tmpElem);
			$newNode->setAttribute('id', $key);

			if (is_array($val))
				$this->_genXmlFromArr($xmlDoc, $newNode, $val);
			else
				$newNode->setAttribute('value', $val);
		}
	}

	public function getListByPaloPath($connName, $paloPath)
	{
		try
		{
			if (count($paloPath) == 2)
				return $this->getDimElements($connName, $paloPath['db'], $paloPath['dim']);
			else if (count($paloPath) == 3)
				return $this->getSubsetList($connName, $paloPath['db'], $paloPath['dim'], $paloPath['ss']);
		}
		catch (Exception $e)
		{}

		return array(false, 'Studio-err_no_list_by_palo_path', array('conn_name' => $connName, 'palo_path' => $paloPath));
	}

	public function getMappedTreeNodes()
	{
		try
		{
			$elemID = isset($_POST['node']) ? stripslashes($_POST['node']) : null;
			$connName = isset($_POST['cn']) ? stripslashes($_POST['cn']) : null;
			$dbName = isset($_POST['g']) ? stripslashes($_POST['g']) : null;
			$dimName = isset($_POST['h']) ? stripslashes($_POST['h']) : null;
			$nodeInfo = isset($_POST['_ni']) ? json_decode(stripslashes($_POST['_ni']), true) : null;

			if ($connName !== null)
				$conn = $this->_getConn($connName);
			else
				$conn = $this->accessPolicy->getConn();

			$retList = array();
			if (strtoupper($elemID) == strtoupper('root'))
			{
				// ROOT
				$listElems = palo_dimension_list_elements2($conn, $dbName, $dimName, true);
				$cubeName = '#_' . $dimName;

				foreach($listElems as $elem)
					if ($elem['num_parents'] == 0)
					{
						$nodeData = new W3S_NodeData();
						$nodeData->import(palo_data($conn, $dbName, $cubeName, 'data', $elem['name']));
						$isDTemp = false;
						if (!in_array($nodeData->getType(), array('rstatic', 'rhyperlink', 'rurlplugin')))
						{
							$tmpMap = $nodeData->getMap();
							$isDTemp = (!empty($tmpMap) && ($nodeData->getType() == 'template'));
							if ($isDTemp)
							{
								$allLinks = true;
								foreach($tmpMap as $map)
									if ($map['list']->getType() != 'link')
									{
										$allLinks = false;
										break;
									}

								if ($allLinks)
									$isDTemp = false;
							}
						}

						$nodeArr = array(
							'text' => $nodeData->getName(),
							//'qtip' => $nodeData->getDescription(), --- until complete implementation of quick tips, only name of item will be used
							'qtip' => $nodeData->getName(),
							'iconCls' => 'w3s_' . $nodeData->getEffectiveType(),
							'leaf' => (($isDTemp || (!in_array($nodeData->getType(), array('rstatic', 'rhyperlink', 'rurlplugin', 'template')))) ? false : true),
							'_ni' => array('n' => $elem['name'])
						);

						if (in_array($nodeData->getType(),array('template', 'rhyperlink', 'rstatic', 'rurlplugin')))
						{
							$nodeLink = $nodeData->getReference();
							$nodeArr['_wb'] = array('g' => $nodeLink->getGroup(), 'h' => $nodeLink->getHierarchy(), 'n' => $nodeLink->getNode());
						}

						$retList[] = $nodeArr;
					}
			}
			else if (isset($nodeInfo['n']) && isset($nodeInfo['elem_n']))
			{
				// dim nodes
				$listElems =  palo_element_list_consolidation_elements($conn, $dbName, $dimName, $nodeInfo['n']);
				$cubeName = '#_' . $dimName;

				foreach ($listElems as $elem)
				{
					$nodeData = new W3S_NodeData();
					$nodeData->import(palo_data($conn, $dbName, $cubeName, 'data', $elem['name']));
					$isDTemp = false;
					if (!in_array($nodeData->getType(), array('rstatic', 'rhyperlink', 'rurlplugin')))
					{
						$tmpMap = $nodeData->getMap();
						$isDTemp = (!empty($tmpMap) && ($nodeData->getType() == 'template'));
						if ($isDTemp)
						{
							$allLinks = true;
							foreach($tmpMap as $map)
								if ($map['list']->getType() != 'link')
								{
									$allLinks = false;
									break;
								}

							if ($allLinks)
								$isDTemp = false;
						}
					}

					$nodeArr = array(
						'text' => $nodeData->getName(),
						//'qtip' => $nodeData->getDescription(), --- until complete implementation of quick tips, only name of item will be used
						'qtip' => $nodeData->getName(),
						'iconCls' => 'w3s_' . $nodeData->getEffectiveType(),
						'leaf' => (($isDTemp || (!in_array($nodeData->getType(), array('rstatic', 'rhyperlink', 'rurlplugin', 'template')))) ? false : true),
						'_ni' => array('n' => $elem['name'])
					);

						if (in_array($nodeData->getType(),array('template', 'rhyperlink', 'rstatic', 'rurlplugin')))
						{
							$nodeLink = $nodeData->getReference();
							$nodeArr['_wb'] = array('g' => $nodeLink->getGroup(), 'h' => $nodeLink->getHierarchy(), 'n' => $nodeLink->getNode());
						}

					$retList[] = $nodeArr;
				}

				// getting map
				$nodeData = new W3S_NodeData();
				$nodeData->import(palo_data($conn, $dbName, $cubeName, 'data', $nodeInfo['n']));
				$mappedData = $nodeData->getMap();

				// generate nodes for map
				$isTempl = ($nodeData->getType() == 'template');
				if (!empty($mappedData))
				{
					$map = $mappedData;
					$addVar = false;
					if ($nodeData->getType() == 'template')
					{
						$addVar = true;
						$map = $map[$nodeInfo['v_n']];
						$varName = $nodeInfo['v_n'];

						$mappedData = $this->_getOrderedVariables($mappedData);
						$varNames = array_keys($mappedData);
						$keyIndex = array_search($nodeInfo['v_n'], $varNames);

						if ($keyIndex == (count($varNames) - 1))
						{
							$nodeData = new W3S_NodeData();
							$nodeData->import(palo_data($conn, $dbName, $cubeName, 'data', $nodeInfo['n']));
							$nodeLink = $nodeData->getReference();
							$retList[] = array(
								'text' => $nodeData->getName(),
								//'qtip' => $nodeData->getDescription(), --- until complete implementation of quick tips, only name of item will be used
								'qtip' => $nodeData->getName(),
								'iconCls' => 'w3s_' . $nodeData->getEffectiveType(),
								'leaf' => true,
								'_ni' => array('n' => $nodeInfo['n']),
								'_wb' => array('g' => $nodeLink->getGroup(), 'h' => $nodeLink->getHierarchy(), 'n' => $nodeLink->getNode())
							);
						}
					}

					$map = $map['list'];
					if ($map->getType() == 'palo')
					{
						$dimMapList = $map->getList();
						if ($dimMapList['type'] == 'dimension')
						{
							if ($map->getConnection() === $connName)
								$dim_conn = $conn;
							else
								$dim_conn = $this->_getConn($map->getConnection());
							$dim_dbName = $map->getDb();

							$listElems = palo_element_list_consolidation_elements($dim_conn, $dim_dbName, $dimMapList['dimension'], $nodeInfo['elem_n']);

							if (count($listElems) > 0)
							{
								$addVar = false;

								foreach($listElems as $elem)
								{
									$ni = array('n' => $nodeInfo['n'], 'elem_n' => $elem['name']);
									if ($isTempl)
										$ni['v_n'] = $varName;

									$retList[] = array(
										'text' => $elem['name'],
										'iconCls' => 'w3s_rfolder_c',
										'leaf' => false,
										'_ni' => $ni
									);
								}
							}
						}
					}

					if ($addVar)
					{
						$mappedData = $this->_getOrderedVariables($mappedData);
						$varNames = array_keys($mappedData);
						$keyIndex = array_search($nodeInfo['v_n'], $varNames);

						while ($keyIndex < (count($varNames) - 1))
						{
							$varName = $varNames[++$keyIndex];
							$map = $mappedData[$varName]['list'];

							if ($map->getType() != 'link')
							{
								// next templated mapped variable
								$retList[] = array(
									'text' => $varName,
									'iconCls' => 'w3s_variable',
									'leaf' => false,
									'_ni' => array('n' => $nodeInfo['n'], 'v_n' => $varName)
								);
								break;
							}
							else if ($keyIndex == (count($varNames) - 1))
							{
								$nodeData = new W3S_NodeData();
								$nodeData->import(palo_data($conn, $dbName, $cubeName, 'data', $nodeInfo['n']));
								$nodeLink = $nodeData->getReference();
								$retList[] = array(
									'text' => $nodeData->getName(),
									//'qtip' => $nodeData->getDescription(), --- until complete implementation of quick tips, only name of item will be used
									'qtip' => $nodeData->getName(),
									'iconCls' => 'w3s_' . $nodeData->getEffectiveType(),
									'leaf' => true,
									'_ni' => array('n' => $nodeInfo['n']),
									'_wb' => array('g' => $nodeLink->getGroup(), 'h' => $nodeLink->getHierarchy(), 'n' => $nodeLink->getNode())
								);
								break;
							}
						}
					}
				}
			}
			else if (isset($nodeInfo['n']))
			{
				// hier nodes
				$listElems =  palo_element_list_consolidation_elements($conn, $dbName, $dimName, $nodeInfo['n']);

				$nodeData = new W3S_NodeData();
				$cubeName = '#_' . $dimName;

				// getting map
				$nodeData = new W3S_NodeData();
				$nodeData->import(palo_data($conn, $dbName, $cubeName, 'data', $nodeInfo['n']));
				$map = $nodeData->getMap();

				// generate nodes for map
				$isTempl = ($nodeData->getType() == 'template');
				if ($isTempl && !empty($map) && !isset($nodeInfo['v_n']))
				{
					$mappedData = $map;
					$mappedData = $this->_getOrderedVariables($mappedData);
					$varNames = array_keys($mappedData);
					$keyIndex = -1;

					while ($keyIndex < (count($varNames) - 1))
					{
						$varName = $varNames[++$keyIndex];
						$map = $mappedData[$varName]['list'];

						if ($map->getType() != 'link')
						{
							// next templated mapped variable
							$retList[] = array(
								'text' => $varName,
								'iconCls' => 'w3s_variable',
								'leaf' => false,
								'_ni' => array('n' => $nodeInfo['n'], 'v_n' => $varName)
							);
							break;
						}
						else if ($keyIndex == (count($varNames) - 1))
						{
							$nodeData = new W3S_NodeData();
							$nodeData->import(palo_data($conn, $dbName, $cubeName, 'data', $nodeInfo['n']));
							$nodeLink = $nodeData->getReference();
							$retList[] = array(
								'text' => $nodeData->getName(),
								//'qtip' => $nodeData->getDescription(), --- until complete implementation of quick tips, only name of item will be used
								'qtip' => $nodeData->getName(),
								'iconCls' => 'w3s_' . $nodeData->getEffectiveType(),
								'leaf' => true,
								'_ni' => array('n' => $elem['name']),
								'_wb' => array('g' => $nodeLink->getGroup(), 'h' => $nodeLink->getHierarchy(), 'n' => $nodeLink->getNode())
							);
							break;
						}
					}
				}
				else if (!empty($map))
				{
					if ($isTempl)
					{
						$map = $map[$nodeInfo['v_n']];
						$varName = $nodeInfo['v_n'];
					}

					$map = $map['list'];
					$listType = strtolower($map->getType());

					if ($listType == 'palo')
					{
						$dimMapList = $map->getList();
						if ($dimMapList['type'] == 'dimension')
						{
							if ($map->getConnection() === $connName)
								$dim_conn = $conn;
							else
								$dim_conn = $this->_getConn($map->getConnection());
							$dim_dbName = $map->getDb();

							$listElems = palo_dimension_list_elements2($dim_conn, $dim_dbName, $dimMapList['dimension'], true);

                            foreach($listElems as $elem)
                            {
                                // if element has no parents then it's root element
                                if($elem['num_parents'] > 0)
                                    continue;
                                else
                                {
									$ni = array('n' => $nodeInfo['n'], 'elem_n' => $elem['name']);
									if ($isTempl)
										$ni['v_n'] = $varName;

									$retList[] = array(
										'text' => $elem['name'],
										'iconCls' => 'w3s_rfolder_c',
										'leaf' => false,
										'_ni' => $ni
									);
								}
							}
						}
						else if ($dimMapList['type'] == 'subset')
						{
							$ssList = $this->getSubsetList($map->getConnection(), $map->getDb(), $dimMapList['dimension'], $dimMapList['subset']);

							if ($ssList[0])
							{
								$ssList = $ssList[1];

								foreach($ssList as $ssName)
								{
									$ni = array('n' => $nodeInfo['n'], 'elem_n' => $ssName);
									if ($isTempl)
										$ni['v_n'] = $varName;

									$retList[] = array(
										'text' => $ssName,
										'iconCls' => 'w3s_rfolder_c',
										'leaf' => false,
										'_ni' => $ni
									);
								}
							}
						}
					}
					else if ($listType == 'clist')
					{
						$constList = $map->getValues();

						foreach($constList as $constName)
						{
							$ni = array('n' => $nodeInfo['n'], 'elem_n' => $constName);
							if ($isTempl)
								$ni['v_n'] = $varName;

							$retList[] = array(
								'text' => $constName,
								'iconCls' => 'w3s_rfolder_c',
								'leaf' => false,
								'_ni' => $ni
							);
						}
					}
					else if ($listType == 'cval')
					{
						$constVal = $map->getValue();

						$ni = array('n' => $nodeInfo['n'], 'elem_n' => $constVal);
						if ($isTempl)
							$ni['v_n'] = $varName;

						$retList[] = array(
							'text' => $constVal,
							'iconCls' => 'w3s_rfolder_c',
							'leaf' => false,
							'_ni' => $ni
						);
					}
					else if ($listType == 'palosubset')
					{
						$ssList = $this->_getSubsetListByXML(json_decode($map->getData(), true));

						if ($ssList[0])
						{
							foreach($ssList[1] as $elemName)
							{
								$ni = array('n' => $nodeInfo['n'], 'elem_n' => $elemName);
								if ($isTempl)
									$ni['v_n'] = $varName;

								$retList[] = array(
									'text' => $elemName,
									'iconCls' => 'w3s_rfolder_c',
									'leaf' => false,
									'_ni' => $ni
								);
							}
						}
					}
				}
				else
				{
					// not mapped node
					$listElems =  palo_element_list_consolidation_elements($conn, $dbName, $dimName, $nodeInfo['n']);
					$cubeName = '#_' . $dimName;

					foreach ($listElems as $elem)
					{
						$nodeData = new W3S_NodeData();
						$nodeData->import(palo_data($conn, $dbName, $cubeName, 'data', $elem['name']));
						$isDTemp = false;
						if (!in_array($nodeData->getType(), array('rstatic', 'rhyperlink', 'rurlplugin')))
						{
							$tmpMap = $nodeData->getMap();
							$isDTemp = (!empty($tmpMap) && ($nodeData->getType() == 'template'));
							if ($isDTemp)
							{
								$allLinks = true;
								foreach($tmpMap as $map)
									if ($map['list']->getType() != 'link')
									{
										$allLinks = false;
										break;
									}

								if ($allLinks)
									$isDTemp = false;
							}
						}

						$nodeArr = array(
							'text' => $nodeData->getName(),
							//'qtip' => $nodeData->getDescription(), --- until complete implementation of quick tips, only name of item will be used
							'qtip' => $nodeData->getName(),
							'iconCls' => 'w3s_' . $nodeData->getEffectiveType(),
							'leaf' => (($isDTemp || (!in_array($nodeData->getType(), array('rstatic', 'rhyperlink', 'template', 'rurlplugin')))) ? false : true),
							'_ni' => array('n' => $elem['name'])
						);

						if (in_array($nodeData->getType(),array('template', 'rhyperlink', 'rstatic', 'rurlplugin')))
						{
							$nodeLink = $nodeData->getReference();
							$nodeArr['_wb'] = array('g' => $nodeLink->getGroup(), 'h' => $nodeLink->getHierarchy(), 'n' => $nodeLink->getNode());
						}

						$retList[] = $nodeArr;
					}
				}
			}
		}
		catch (Exception $e)
		{}

		return  $retList;
	}

	private function _getOrderedVariables($vars)
	{
		$indexesArr = array();
		$keysArr = array();
		foreach ($vars as $key => $val)
		{
			array_push($indexesArr, $val['ordnum']);
			if (isset($keysArr[$val['ordnum']]))
				array_push($keysArr[$val['ordnum']], $key);
			else
				$keysArr[$val['ordnum']] = array($key);
		}

		$orderedVars = array();
		if (count($indexesArr) > 0)
		{
			sort($indexesArr, SORT_STRING);
			if (intval($indexesArr[0]) == 0)
			{
				$zeroIndex = array_shift($indexesArr);
				array_push($indexesArr, $zeroIndex);
			}

			foreach($indexesArr as $iVal)
			{
				sort($keysArr[$iVal], SORT_STRING);
				foreach($keysArr[$iVal] as $kVal)
					$orderedVars[$kVal] = $vars[$kVal];
			}
		}

		return ((count($orderedVars) > 0) ? $orderedVars : $vars);
	}

	public function getUsedVariables($name) {

	    $group = $this->currGroup['report'];
	    $hierarchy = $group->getHierarchy($this->currHierarchy['report']);

	    $node = $hierarchy->getNode($name);

//	    $put = $node->getData()->getName() . ' (/' . $hierarchy->getData()->getName() . '/' . $node->getRelPath($node) . ')';
//	    print_r($put);

	    $workbook = $node->getData()->getReference();

	    if (!isset($workbook))
	      return array();

	    $wb_gr = $workbook->getGroup();
	    $wb_hr = $workbook->getHierarchy();
	    $wb_nd = $workbook->getNode();

	    //	now call to "real" template
	    $t_group = new W3S_Group($this->accessPolicy, $wb_gr);
	    $t_hierarchy = $t_group->getHierarchy($wb_hr);

	    $template = $t_hierarchy->getNode($wb_nd);

	    if (!isset($template))
	      return null;

	    $path = $template->getSysPath() . self::WB_EXTENSION;

	    $var_list = $this->getUsedVariablesEx($path);
		//if ($var_list)
	    foreach ($var_list as $var)
	      $maps[$var] = $this->treeNodeGetMap($name, $var);

	    //print_r($maps);
	    return $maps;
	  }

	public function getUsedVariablesEx ($path)
	{
		$res = json_decode(ccmd('[["logi",' . json_encode($this->accessPolicy->getUser()) . ',""],["oadd",0,"' . ('A' . mt_rand()) . '"]]', -1, null), true);
		$sess_id = $res[0][1];
		$app_uid = $res[1][1];

		$res = json_decode(ccmd('[["osel",0,"' . $app_uid . '"],["gbvl",' . json_encode($path) . '],["odel",0,"' . $app_uid . '"],["logo"]]', -1, $sess_id), true);
		$var_list = $res[1][1];
		return $var_list;
	}

	public function getSNodeInScope($g, $h, $n) {
		$group = new W3S_Group($this->accessPolicy, $g);
		$hierarchy = $group->getHierarchy($h);
		$node = $hierarchy->getNode($n);
		return $node;
	}

	public function getNodeInScope($type, $node_name) {
		// Group
		$group = $this->currGroup[$type];
		$group->apol = $this->accessPolicy;

		// Hierarchy
		$h = $this->currHierarchy[$type];
		$hierarchy = $group->getHierarchy($h);
		$node = $hierarchy->getNode($node_name);

		if ($type == 'report') {
			$ref = $node->getData()->getReference();
			$node = $this->getSNodeInScope($ref->getGroup(), $ref->getHierarchy(), $ref->getNode());
			return (get_class($node) == 'W3S_Node') ? $node : null;
		}

		return (get_class($node) == 'W3S_Node') ? $node : null;
	}

	public function getNodeInThisScope($type, $g, $h, $n) {

		// Group
		if (get_class($this->currGroup[$type]) == 'W3S_Group' && $g == $this->currGroup[$type]->getUID()){
			$group = $this->currGroup[$type];
			$group->apol = $this->accessPolicy;
		}
		else {
			$group = new W3S_Group($this->accessPolicy, $g);
		}

		$hierarchy = $group->getHierarchy($h);
		$node = $hierarchy->getNode($n);


		if ($type == 'report') {
			$ref = $node->getData()->getReference();
			$node = $this->getSNodeInScope($ref->getGroup(), $ref->getHierarchy(), $ref->getNode());
		}

		return (get_class($node) == 'W3S_Node') ? $node : null;
	}

	public function getTWorkbook ($template) {
		$conn = $this->_get_connection();
	    $group = $this->currGroup['report'];
	    $hierarchy = $group->getHierarchy($this->currHierarchy['report']);

	    $node = $hierarchy->getNode($template);
	    $workbook = $node->getData()->getReference();

	    if (!isset($workbook))
	      return array();

	    $wb_gr = $workbook->getGroup();
	    $wb_hr = $workbook->getHierarchy();
	    $wb_nd = $workbook->getNode();

	    return array('grp' => $wb_gr, 'hr' => $wb_hr, 'node' => $wb_nd);
	}

	public function getChildTempVars ($folder) {
		$conn = $this->_get_connection();
	    $group = $this->currGroup['report'];
	    $hierarchy = $group->getHierarchy($this->currHierarchy['report']);

	    $node = $hierarchy->getNode($folder);

	    $allCh = array();
	    $toReturn = array();
	    $node->getSubUIDs($allCh);

	    foreach ($allCh as $nod) {
	    	$tnode = $hierarchy->getNode($nod);
	    	if ($tnode->getType() == 'template') {

		    	$path = $tnode -> getRelPath($tnode, false);
		    	$path = split('[/-]', $path);

		    	$rpath = '/';

		    	for ($i=1; $i < count($path); $i+=1) {
		    		if ($i!=1)$rpath .= '/'; $rpath .= str_replace('+', ' ', $path[$i]);
		    	}
		    	//print_r($rpath);
		    	$vars = $this -> getNodeVars($nod);
		    	$maps = $tnode -> getData() -> getMap();
		    	$tname = $tnode -> getData() -> getName();

		    	$mapsArray = array();

		    	foreach ($maps as $mp => $val) {
		    		$mtype = $maps[$mp]['list']->getType();
		    		if ($mtype == 'link') $mlist = $maps[$mp]['list'] -> getElement();

		    		$mapsArray[$mp] = array('type' => $mtype, 'list' => $mlist);
		    	}
		    	$toReturn[$nod] = array('name' => $tname, 'path' => $rpath, 'vars' => $vars, 'maps' => $mapsArray, 'node' => $nod);
	    	}
	    }
	    return($toReturn);
	}

	public function getNodeVars($tpl) {
	    $group = $this->currGroup['report'];
	    $hierarchy = $group->getHierarchy($this->currHierarchy['report']);

	    $node = $hierarchy->getNode($tpl);

	    $workbook = $node->getData()->getReference();

	    if (!isset($workbook))
	      return array();

	    $wb_gr = $workbook->getGroup();
	    $wb_hr = $workbook->getHierarchy();
	    $wb_nd = $workbook->getNode();

	    $t_group = new W3S_Group($this->accessPolicy, $wb_gr);
	    $t_hierarchy = $t_group->getHierarchy($wb_hr);

	    $template = $t_hierarchy->getNode($wb_nd);

	    $n = $template -> getData() -> getVars();
	    return $n;
	}

	public function sortVariables($node, array $ord) {

	    $type = 'report';

	    if (!isset($this->currGroup[$type]) || !(($group = $this->currGroup[$type]) instanceof W3S_Group) || !(($hierarchy = $group->getHierarchy($this->currHierarchy[$type])) instanceof W3S_Hierarchy)
	        || !(($node = $hierarchy->getNode($node)) instanceof W3S_Node))
	      return false;

	    $group->apol = $this->accessPolicy;

	    $node->getData()->rearrangeMaps($ord);
	    $node->saveData();
		return true;
	}


	/*
	 * #####################
	 * ### HOMEPAGE MGMT ###
	 * #####################
	 */

	public function getDefRepsByGroup ()
	{
		$list = palo_dimension_list_elements($this->accessPolicy->getSuperConn(), 'System', '#_GROUP_', true);
		$res = array();

		foreach ($list as &$entry)
		{
			$prefs = new Prefs($this->accessPolicy, Prefs::LEVEL_GROUP, $group = $entry['name']);
			$defRep = $prefs->get('studio/default/reports');

			if (!$defRep || !isset($defRep['node']) || !isset($defRep['path']))
			{
				$res[$group] = false;
				continue;
			}

			$vars = array();

			if (isset($defRep['var']))
			{
				if (isset($defRep['var']['name']))
				{
					$var = &$defRep['var'];
					$vars[$var['type']][$var['name']] = $var['val'];
				}
				else
					foreach ($defRep['var'] as &$var)
						$vars[$var['type']][$var['name']] = $var['val'];
			}

			$res[$group] = array('g' => $defRep['group'], 'h' => $defRep['hierarchy'], 'n' => $defRep['node'], 'p' => $defRep['path'], 'v' => $vars);
		}

		return $res;
	}

	public function setDefRepsByGroup ($defRep, $list = null)
	{
		if (isset($defRep['v']))
		{
			if (is_array($defRep['v']['nodes']))
				foreach ($defRep['v']['nodes'] as $name => $val)
					$defRep['var'][] = array('type' => 'nodes', 'name' => $name, 'val' => $val);

			if (is_array($defRep['v']['vars']))
				foreach ($defRep['v']['vars'] as $name => $val)
					$defRep['var'][] = array('type' => 'vars', 'name' => $name, 'val' => $val);

			unset($defRep['v']);
		}

		if ($list)
			foreach ($list as $group => $set)
			{
				$prefs = new Prefs($this->accessPolicy, Prefs::LEVEL_GROUP, $group);

				if ($set)
					$prefs->set('studio/default/reports', $defRep);
				else
				{
					$prefs->remove('studio/default/reports/node');
					$prefs->remove('studio/default/reports/path');
					$prefs->remove('studio/default/reports/var');
				}

				$prefs->save($this->accessPolicy);
			}
		else
		{
			$prefs = new Prefs($this->accessPolicy, Prefs::LEVEL_SERVER);
			$prefs->set('studio/default/reports', $defRep);
			$prefs->save($this->accessPolicy);
		}
	}

}

?>