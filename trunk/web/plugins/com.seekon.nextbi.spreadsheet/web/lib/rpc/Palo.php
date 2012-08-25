<?php

/*
 * @brief ajax
 *
 * @file Group.js
 *
 * Copyright (C) 2006-2009 Jedox AG
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
 * Mladen Todorovic <mladen.todorovic@develabs.com>
 * Predrag Malicevic <predrag.malicevic@develabs.com>
 *
 * \version
 * SVN: $Id: Palo.php 3030 2010-03-29 13:25:32Z mladent $
 *
 */

class Palo
{
	private $wss_ajax;
	private $accessPolicy;

	public function __construct($wss_ajax = null)
	{
		$this->wss_ajax = $wss_ajax;

		if (!isset($_SESSION['palo_data']))
			$_SESSION['palo_data'] = array();

		if (!isset($_SESSION['wss_page_setup']))
			$_SESSION['wss_page_setup'] = array();

		if ($wss_ajax != null)
		{
			$wss_ajax->cb_register('palo_handlerPasteView', array($this, 'handlerPasteView'));
			$wss_ajax->cb_register('palo_handlerExpandCollapsePasteView', array($this, 'handlerExpandCollapsePasteView'));
			$wss_ajax->cb_register('palo_handlerSelectElements', array($this, 'handlerSelectElements'));
			$wss_ajax->cb_register('palo_handlerPutValuesOnGrid', array($this, 'handlerPutValuesOnGrid'));
			$wss_ajax->cb_register('palo_handlerChooseElements', array($this, 'handlerChooseElements'));
			$wss_ajax->cb_register('palo_handlerPasteDataFunctions', array($this, 'handlerPasteDataFunctions'));
			$wss_ajax->cb_register('palo_handlerGetGeneratedSubsetFunc', array($this, 'handlerGetGeneratedSubsetFunc'));
			$wss_ajax->cb_register('palo_handlerImportPaloDataFunc', array($this, 'handlerImportPaloDataFunc'));
		}

		$this->accessPolicy = $_SESSION['accessPolicy'];
	}

	private function _get_config_conn()
	{
		try
		{
			$conn = $this->accessPolicy->getConn();
			if (!is_resource($conn))
				throw new Exception();

			return $conn;
		}
		catch (Exception $e)
		{
			throw new WSS_Exception('Palo-err_no_config_conn', null, 'Error at generating palo connection to config server.');
		}
	}

	private function _get_username_for_conn($connName)
	{
		try
		{
			 // read connection data
			$connection = $this->_get_config_conn();
			$coordinates = array('name', 'type', 'description', 'host', 'port', 'userName', 'password', 'active', 'useLoginCred');
			array_unshift($coordinates,  1, count($coordinates));
			$connData = palo_datav($connection, 'Config', '#_connections', $coordinates, $connName);
			$this->_palo_disconnect($connection);

			// test is connection type proper palo connection
			if (strtoupper($connData[3]) != 'PALO')
				throw new WSS_Exception('Palo-err_bad_palo_conn_type', array('conn_name' => $connName), 'Bad PALO connection type for connection name: ' . $connName);

			return $connData[7];
		}
		catch (WSS_Exception $wsse)
		{
			throw $wsse;
		}
		catch (Exception $e)
		{
			throw new WSS_Exception('Palo-err_no_user_for_conn', array('conn_name' => $connName), 'Unable to get user for connection name: ' . $connName);
		}
	}

	private function _palo_init($connName)
	{
		try
		{
//			$conn = $this->accessPolicy->getConn();
//
//			return is_resource($conn) ? $conn : array('!', 111);

			// read connection data
			$connection = $this->_get_config_conn();
			$coordinates = array('name', 'type', 'description', 'host', 'port', 'userName', 'password', 'active', 'useLoginCred');
			array_unshift($coordinates,  1, count($coordinates));
			$connData = palo_datav($connection, 'Config', '#_connections', $coordinates, $connName);
			$this->_palo_disconnect($connection);

			// make conenction
			if (strtoupper($connData[3]) != 'PALO')
				throw new WSS_Exception('Palo-err_bad_palo_conn_type', array('conn_name' => $connName), 'Bad PALO connection type for connection name: ' . $connName);

			$user = ($connData[10] == 0) ? $connData[7] : $_SESSION['accessPolicy']->getUser();
			$pass = ($connData[10] == 0) ? $connData[8] : $_SESSION['accessPolicy']->getPass();

			$tmpErrRep = ini_get('error_reporting');
			error_reporting(0);
			$conn = palo_init($connData[5], $connData[6], $user, $pass);
			palo_ping($conn);
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
			throw new WSS_Exception('Palo-err_no_conn', array('conn_name' => $connName), 'Unable to make connection for connection name: ' . $connName);
		}
	}

	private function _palo_disconnect($conn)
	{
		palo_ping($conn);
		palo_disconnect($conn);
	}

	// *** global ***
	public function getServList()
	{
		try
		{
		    $connection = $this->_get_config_conn();
		    $dbName = 'Config';
			$cubeName = '#_connections';
			$dimName = 'connections';
			$prop = array('name', 'active', 'type');
			$properties = array(count($prop), 1);

			for ($i=0; $i<count($prop); $i++)
				array_push($properties, $prop[$i]);
			$result = palo_dimension_list_elements($connection, $dbName, $dimName, true);

			$connectionNames = $result;
			$coordinates = array(1, count($connectionNames));
			for ($i=0; $i<count($connectionNames); $i++)
				array_push($coordinates, $connectionNames[$i]['name']);

			$result = palo_datav($connection, $dbName, $cubeName, $properties, $coordinates);
			$this->_palo_disconnect($connection);

			$result = array_slice($result, 2);
			$connectons = array();
			$rowNmb = count($result)/3;
			for ($i=0; $i<$rowNmb; $i++)
				if (strtoupper($result[2*$rowNmb+$i]) == 'PALO')
					array_push($connectons, array($result[$i], '', (!!$result[$i+$rowNmb]), 'server', $result[$i]));

			if (isset($_SESSION['palo_data']) && isset($_SESSION['palo_data']['palo_preselection']))
				return array(true, $connectons, $_SESSION['palo_data']['palo_preselection']);
			else
				return array(true, $connectons);
		}
		catch (WSS_Exception $wsse)
		{
			return array(false, $wsse->getId(), $wsse->getParams());
		}
		catch (Exception $e)
		{
			return array(false, 'Palo-err_no_list_of_servers');
		}
	}

	public function getDBs($serv_id)
	{
		try
		{
			$conn = $this->_palo_init($serv_id);
			$listDB = palo_root_list_databases($conn);
			$this->_palo_disconnect($conn);

			if (!is_array($listDB))
				return array(false, 'Palo-err_no_list_of_databases', array('conn_name' => $serv_id));

			array_unshift($listDB, $serv_id);

			return array(true, $listDB);
		}
		catch (Exception $e)
		{
			return array(false, 'Palo-err_no_list_of_databases', array('conn_name' => $serv_id));
		}
	}

	public function setPreselectServDb($serv_id, $db_name)
	{
		try
		{
			$_SESSION['palo_data']['palo_preselection'] = array($serv_id, $db_name);

			return array(true);
		}
		catch (Exception $e)
		{
			return array(false, 'Palo-err_unable_to_set_preselection', array('conn_name' => $serv_id));
		}
	}

	// *** Dimension ***
	public function getDims($serv_id, $dbName, $dimType)
	{
		try
		{
			// $dimType .. data, user, att
			$dimType = ($dimType == 2) ? 0 : $dimType;
			if ($dbName == 'System')
				$dimType = 1;

			if ($dbName != null)
			{
				$conn = $this->_palo_init($serv_id);
				$listDims = palo_database_list_dimensions($conn, $dbName, $dimType);
				$this->_palo_disconnect($conn);

				$retList = array();
				foreach ($listDims as $dim)
					if (!in_array($dim, PaloConf::$paloConf['filterDims']))
						 $retList[] = $dim;

				$_SESSION['servid_' . $serv_id][$dbName] = null;

				return array('', $retList, false, '');
			}

			return array('', '', true, '');
		}
		catch (Exception $e)
		{
			return array('', '', true, 'Func [getDims|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
		}
	}

	public function addDim($serv_id, $dbName, $dimName)
	{
		if ($dbName != null)
		{
			try
			{
				$conn = $this->_palo_init($serv_id);
				$listDims =  palo_database_list_dimensions($conn, $dbName);
				if (in_array($dimName, $listDims))
				{
					$this->_palo_disconnect($conn);
					return array('', false, true, 'Func [addDim|1004]: ' . PaloConf::$errStr[1004]);
				}
				else
				{
					$result = palo_database_add_dimension($conn, $dbName, $dimName);
					$this->_palo_disconnect($conn);
				}

				if ($result)
					return array($dimName, $result, false, '');
				else
					return array($dimName, $result, true, 'Func [addDim|101]: ' . PaloConf::$errStr[101]);
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [addDim|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [addDim|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function renameDim($serv_id, $dbName, $dimName, $newDimName)
	{
		if ($dbName != null)
		{
			try
			{
				$conn = $this->_palo_init($serv_id);
				$listDims =  palo_database_list_dimensions($conn, $dbName);
				if (in_array($dimName, $listDims))
				{
					$result = palo_database_rename_dimension($conn, $dbName, $dimName, $newDimName);
					$this->_palo_disconnect($conn);
				}
				else
				{
					$this->_palo_disconnect($conn);
					return array('', false, true, 'Func [renameDim|1002]: ' . PaloConf::$errStr[1002]);
				}

				if ($result)
					return array($newDimName, $result, false, '');
				else
					return array($newDimName, $result, true, 'Func [renameDim|101]: ' . PaloConf::$errStr[101]);
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [renameDim|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [renameDim|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function deleteDim($serv_id, $dbName, $dimName)
	{
		if ($dbName != null)
		{
			try
			{
				$conn = $this->_palo_init($serv_id);
				$listDims =  palo_database_list_dimensions($conn, $dbName);
				$listCubes =  palo_dimension_list_cubes($conn, $dbName, $dimName);

				if (in_array($dimName, $listDims) && (count($listCubes) < 3))
				{
					$result = palo_database_delete_dimension($conn, $dbName, $dimName);
					$this->_palo_disconnect($conn);
				}
				else
				{
					$this->_palo_disconnect($conn);
					if (count($listCubes) > 2)
						return array('', false, true, 'Func [deleteDim|1001]: ' . PaloConf::$errStr[1001]);
					else
						return array('', false, true, 'Func [deleteDim|1002]: ' . PaloConf::$errStr[1002]);
				}

				if ($result)
					return array($dimName, $result, false, '');
				else
					return array($dimName, $result, true, 'Func [deleteDim|101]: ' . PaloConf::$errStr[101]);
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [deleteDim|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [deleteDim|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function getDimInfo($serv_id, $dbName, $dimName)
	{
		if ($dbName != null)
		{
			try
			{
				// adjusting dimName for different working modes
				$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
				$conn = $this->_palo_init($serv_id);
				$listElems = palo_dimension_list_elements2($conn, $dbName, $dimName);
				$dimInfo = palo_dimension_info($conn, $dbName, $dimName);
				$this->_palo_disconnect($conn);

				$elemNames = '';
				$numOfElems = count($listElems);
				$countC = 0;
				$countS = 0;
				$countN = 0;
				$maxLevel = 0;
				$maxIndent = 0;
				$maxDepth = 0;
				$elemNames = '';

				foreach ($listElems as $elem)
				{
					if ($elem['type'] == 'numeric')
						$countN++;
					else if ($elem['type'] == 'string')
						$countS++;
					else if ($elem['type'] == 'consolidated')
						$countC++;

					if ($elem['level'] > $maxLevel)
						$maxLevel = $elem['level'];

					if ($elem['indent'] > $maxIndent)
						$maxIndent = $elem['indent'];

					if ($elem['depth'] > $maxDepth)
						$maxDepth = $elem['depth'];

					$elemNames .= $elem['name'] . ',';
				}
				if (strlen($elemNames) > 0)
					$elemNames = substr($elemNames, 0, strlen($elemNames) - 1);

				return array('', array($dimInfo[0], $dimName, $numOfElems, $elemNames, $countN, $countS, $countC, $maxLevel, $maxIndent, $maxDepth), false, '');
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [getDimInfo|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [getDimInfo|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function getDimStringAttrs($serv_id, $dbName, $dimName)
	{
		if ($dbName != null)
		{
			try
			{
				if (!(strpos($dimName, '#_') === 0))
					$dimName = '#_' . $dimName . '_'; // settings for dimension attributes
				else
					return array('', array(), false, '');

				$conn = $this->_palo_init($serv_id);
				$listElems = palo_dimension_list_elements($conn, $dbName, $dimName);
				$this->_palo_disconnect($conn);

				$listElemsOut = array();
				for ($i=0; $i<count($listElems); $i++)
					if ($listElems[$i]['type'] == 'string')
						$listElemsOut[] = array('id' => $listElems[$i]['identifier'], 'name' => $listElems[$i]['name']);

				return array('', $listElemsOut, false, '');
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [getDimStringAttrs|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [getDimStringAttrs|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function getDimAttrs($serv_id, $dbName, $dimName)
	{
		if ($dbName != null)
		{
			try
			{
				if (!(strpos($dimName, '#_') === 0))
					$dimName = '#_' . $dimName . '_'; // settings for dimension attributes
				else
					return array('', array(), false, '');

				$conn = $this->_palo_init($serv_id);
				$listElems = palo_dimension_list_elements($conn, $dbName, $dimName);
				$this->_palo_disconnect($conn);

				$listElemsOut = array();
				for ($i=0; $i<count($listElems); $i++)
					$listElemsOut[] = array('id' => $listElems[$i]['identifier'], 'name' => $listElems[$i]['name']);

				return array('', $listElemsOut, false, '');
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [getDimAttrs|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [getDimAttrs|1003]: ' . PaloConf::$errStr[1003]);
	}

	// *** Cube ***
	public function getCubeNames($serv_id, $dbName, $cubeType, $onlyWithAttr =false)
	{
		if ($dbName != null)
		{
			try
			{
				$conn = $this->_palo_init($serv_id);
				$listCubes = palo_database_list_cubes($conn, $dbName, $cubeType);

				if ($onlyWithAttr && (($cubeType == 2) || ($cubeType == 1)))
				{
					$newCubeList = array();
					for ($i=0; $i<count($listCubes); $i++)
					{
						$cInfo =  palo_cube_info($conn, $dbName, $listCubes[$i]);
						if ($cInfo['number_cells'] > 0)
							$newCubeList[] = $listCubes[$i];
					}

					$listCubes = $newCubeList;
				}
				$this->_palo_disconnect($conn);

				$newCubeList = array();
				for ($i=0; $i<count($listCubes); $i++)
					if (!in_array($listCubes[$i], PaloConf::$paloConf['filterCubes']))
						$newCubeList[] = $listCubes[$i];

				return array('', $newCubeList, false, '');
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [getCubeNames|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [getCubeNames|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function getCubes($serv_id, $dbName, $cubeType)
	{
		if ($dbName != null)
		{
			try
			{
				if ($dbName == 'System')
					$cubeType = 1;

				$conn = $this->_palo_init($serv_id);
				$listCubes = palo_database_list_cubes($conn, $dbName, $cubeType);

				if ($cubeType == 0)
				{
				    // show also GPU type cubes when displaying normal cubes
				    $listCubesGPU = palo_database_list_cubes($conn, $dbName, 4);
				    $listCubes = array_merge($listCubes, $listCubesGPU);
				}

				$newListOfCubes = array();
				for ($i=0; $i<count($listCubes); $i++)
				{
					$cubeName = $listCubes[$i];
					if (!in_array($listCubes[$i], PaloConf::$paloConf['filterCubes']))
					{
						$dimList = palo_cube_list_dimensions($conn, $dbName, $cubeName);
						$newListOfCubes[] = array($cubeName, $dimList);
					}
				}
				$this->_palo_disconnect($conn);

				return array('', $newListOfCubes, false, '');
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [getCubes|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [getCubes|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function deleteCube($serv_id, $dbName, $cubeName)
	{
		if ($dbName != null)
		{
			try
			{
				$conn = $this->_palo_init($serv_id);
				$result = palo_database_delete_cube($conn, $dbName, $cubeName);
				$this->_palo_disconnect($conn);

				if ($result)
					return array($cubeName, $result, false, '');
				else
					return array($cubeName, $result, true, 'Func [deleteCube|101]: ' . PaloConf::$errStr[101]);
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [deleteCube|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [deleteCube|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function renameCube($serv_id, $dbName, $cubeName, $newCubeName)
	{
		if ($dbName != null)
		{
			try
			{
				$conn = $this->_palo_init($serv_id);
				$result = palo_rename_cube($conn, $dbName, $cubeName, $newCubeName);
				$this->_palo_disconnect($conn);

				if ($result)
					return array($newCubeName, $result, false, '');
				else
					return array($newCubeName, $result, true, 'Func [deleteCube|101]: ' . PaloConf::$errStr[101]);
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [deleteCube|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [deleteCube|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function addCube($serv_id, $dbName, $cubeName, $dims)
	{
		if ($dbName != null)
		{
			try
			{
				$conn = $this->_palo_init($serv_id);
				$result = palo_database_add_cube($conn, $dbName, $cubeName, $dims);
				$this->_palo_disconnect($conn);

				if ($result)
					return array('', $result, false, '');
				else
					return array('', $result, true, 'Func [addCube|101]: ' . PaloConf::$errStr[101]);
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [addCube|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [addCube|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function getCubeInfo($serv_id, $dbName, $cubeName)
	{
		if ($dbName != null)
		{
			try
			{
				$conn = $this->_palo_init($serv_id);
				$cubeInfo = palo_cube_info($conn, $dbName, $cubeName);
				$this->_palo_disconnect($conn);

				return array('', array($cubeInfo['identifier'], $cubeName, $cubeInfo['number_dimensions'], $cubeInfo['dimensions'], $cubeInfo['number_cells'], $cubeInfo['number_filled_cells'], (($cubeInfo['number_cells'] != 0) ? round($cubeInfo['number_filled_cells']*100/$cubeInfo['number_cells'], 2) : '0.00') . '%', $cubeInfo['status']), false, '');
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [getCubeInfo|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [getCubeInfo|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function getCubeDims($serv_id, $dbName, $cubeName)
	{
		if ($dbName != null)
		{
			try
			{
				$conn = $this->_palo_init($serv_id);
				$listDims = palo_cube_list_dimensions($conn, $dbName, $cubeName);
				$this->_palo_disconnect($conn);

				return array('', $listDims, false, '');
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [getCubeDims|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [getCubeDims|1003]: ' . PaloConf::$errStr[1003]);
	}

	// *** Element ***
	public function getDimElems($serv_id, $dbName, $dimName, $fromId, $toId)
	{
		if ($dbName != null)
		{
			try
			{
				// adjusting dimName for different working modes
				$conn = $this->_palo_init($serv_id);
				if ($_SESSION['servid_' . $serv_id][$dbName][$dimName]['workingMode'] == 'subsets')
				{
					$listSubset = palo_dimension_list_elements($conn, $dbName, '#_SUBSET_', true);
					$listElems = array();
					foreach($listSubset as $subset)
						if (palo_data($conn, $dbName, '#_SUBSET_GLOBAL', $dimName, $subset['name']) != null)
							$listElems[] = array('name' => $subset['name'], 'type' => 'subset', 'identifier' => $subset['identifier']);
				}
				else
				{
					$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
					$listElems = palo_dimension_list_elements($conn, $dbName, $dimName, true);
				}
				$this->_palo_disconnect($conn);

				$numOfElems = count($listElems);
				$listElemsOut = array();
				for ($i=$fromId; $i<$toId && $i<$numOfElems; $i++)
					$listElemsOut[] = $listElems[$i];

				return array(array($fromId, $toId, count($listElemsOut), $numOfElems), $listElemsOut, false, '');
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [getDimElems|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [getDimElems|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function addElem($serv_id, $dbName, $dimName, $elemType, $elemName, $parentElemName, $consolidFactor, $clear)
	{
		if ($dbName != null)
		{
			try
			{
				$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
				$conn = $this->_palo_init($serv_id);

				if ($this->_findElem($conn, $dbName, $dimName, $elemName) !== false)
				{
					$this->_palo_disconnect($conn);
					return array('', false, true, 'Func [addElem|1006]: ' . PaloConf::$errStr[1006]);
				}
				else
					$result = palo_eadd($conn, $dbName, $dimName, $elemType, $elemName, $parentElemName, $consolidFactor, $clear, false);

				if ($result)
				{
					$elem = $this->_findElem($conn, $dbName, $dimName, $elemName);
					$this->_palo_disconnect($conn);
					return array($elem, $result, false, '');
				}
				else
				{
					$this->_palo_disconnect($conn);
					return array($dimName, $result, true, 'Func [addElem|101]: ' . PaloConf::$errStr[101]);
				}
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [addElem|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [addElem|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function deleteElems($serv_id, $dbName, $dimName, $listElems)
	{
		if ($dbName != null)
		{
			try
			{
				$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
				$conn = $this->_palo_init($serv_id);

				$parentElems = array();
				for($i=0; $i<count($listElems); $i++)
				{
					$parentElems[$i] = array();
					for ($j=1; $j<=palo_eparentcount($conn, $dbName, $dimName, $listElems[$i]); $j++)
						array_unshift($parentElems[$i],  palo_eparentname($conn, $dbName, $dimName, $listElems[$i], $j));

					$result = palo_edelete($conn, $dbName, $dimName, $listElems[$i]);
					if (!$result)
						break;
				}

				$parentEmptyElems = array();
				for ($i=0; $i<count($parentElems); $i++)
					for ($j=0; $j<count($parentElems[$i]); $j++)
						if (palo_echildcount($conn, $dbName, $dimName, $parentElems[$i][$j]) == 0)
						{
							if (!in_array($parentElems[$i][$j], $parentEmptyElems))
								$parentEmptyElems[] = $parentElems[$i][$j];
							array_splice($parentElems[$i], $j, 1);
						}
				$this->_palo_disconnect($conn);

				if ($result)
					return array(array($parentElems, $parentEmptyElems), $result, false, '');
				else
					return array('', $result, true, 'Func [deleteElems|101]: ' . PaloConf::$errStr[101]);
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [deleteElems|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [deleteElems|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function renameElem($serv_id, $dbName, $dimName, $elemName, $newElemName)
	{
		if ($dbName != null)
		{
			try
			{
				$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
				$conn = $this->_palo_init($serv_id);

				$parentElems = array();
				for ($i=1; $i<=palo_eparentcount($conn, $dbName, $dimName, $elemName); $i++)
					array_unshift($parentElems,  palo_eparentname($conn, $dbName, $dimName, $elemName, $i));

				$result = palo_erename($conn, $dbName, $dimName, $elemName, $newElemName, true);
				$this->_palo_disconnect($conn);

				array_unshift($parentElems,  $newElemName);

				if ($result)
					return array($parentElems, $result, false, '');
				else
					return array('', $result, true, 'Func [renameElem|101]: ' . PaloConf::$errStr[101]);
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [renameElem|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [renameElem|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function getChildElems($serv_id, $dbName, $dimName, $elemName)
	{
		if ($dbName != null)
		{
			try
			{
				$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
				$conn = $this->_palo_init($serv_id);
				$listElems =  palo_element_list_consolidation_elements($conn, $dbName, $dimName,  $elemName);
				$this->_palo_disconnect($conn);

				return array($elemName, $listElems, false, '');
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [getChildElems|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [getChildElems|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function setChildElems($serv_id, $dbName, $dimName, $elemName, $elemType, $listElems)
	{
		if ($dbName != null)
		{
			try
			{
				$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
				$conn = $this->_palo_init($serv_id);
				$fixedElemList = array();
				for ($i = 0; $i < count($listElems); $i++)
					$fixedElemList[$i] = array($listElems[$i]['name'], $listElems[$i]['factor']);

				if (count($fixedElemList) > 0)
					$typeOfElem = 'C';
				else
				{
					if ($elemType == 'string')
						$typeOfElem = 'S';
					else
						$typeOfElem = 'N';
				}

				$result = palo_eupdate($conn, $dbName, $dimName, $elemName, $typeOfElem, $fixedElemList);
				$this->_palo_disconnect($conn);

				if ($result)
					return array($elemName, $result, false, '');
				else
					return array('', $result, true, 'Func [deleteElems|101]: ' . PaloConf::$errStr[101]);
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [setChildElems|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [setChildElems|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function changeElemType($serv_id, $dbName, $dimName, $elemName, $elemType)
	{
		if ($dbName != null)
		{
			try
			{
				$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
				$conn = $this->_palo_init($serv_id);

				switch ($elemType)
				{
					case 'consolidated':
						$typeOfElem = 'C';
						break;
					case 'string':
						$typeOfElem = 'S';
						break;

					default:
						$typeOfElem = 'N';
				}

				$result = palo_eupdate($conn, $dbName, $dimName, $elemName, $typeOfElem, null);
				$this->_palo_disconnect($conn);

				if ($result)
					return array($elemType, $result, false, '');
				else
					return array('', $result, true, 'Func [renameElem|101]: ' . PaloConf::$errStr[101]);
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [renameElem|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [renameElem|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function moveElemUp($serv_id, $dbName, $dimName, $elemNames)
	{
		if ($dbName != null)
		{
			try
			{
				$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
				$conn = $this->_palo_init($serv_id);
				for ($i=0; $i<count($elemNames); $i++)
				{
					$elemName = $elemNames[$i];

					$newIndex = palo_eindex($conn, $dbName, $dimName, $elemName) - 2;
					$result = palo_emove($conn, $dbName, $dimName, $elemName, $newIndex);
				}
				$this->_palo_disconnect($conn);

				if ($result)
					return array($elemName, $result, false, '');
				else
					return array('', $result, true, 'Func [moveElemUp|101]: ' . PaloConf::$errStr[101]);
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [moveElemUp|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [moveElemUp|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function moveElemDown($serv_id, $dbName, $dimName, $elemNames)
	{
		if ($dbName != null)
		{
			try
			{
				$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
				$conn = $this->_palo_init($serv_id);

				for ($i=0; $i<count($elemNames); $i++)
				{
					$elemName = $elemNames[$i];

					$newIndex = palo_eindex($conn, $dbName, $dimName, $elemName);
					$result = palo_emove($conn, $dbName, $dimName, $elemName, $newIndex);
				}
				$this->_palo_disconnect($conn);

				if ($result)
					return array($elemName, $result, false, '');
				else
					return array('', $result, true, 'Func [moveElemDown|101]: ' . PaloConf::$errStr[101]);
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [moveElemDown|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [moveElemDown|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function moveToBeginning($serv_id, $dbName, $dimName, $elemNames)
	{
		if ($dbName != null)
		{
			try
			{
				$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
				$conn = $this->_palo_init($serv_id);
				for ($i=0; $i<count($elemNames); $i++)
					$result = palo_emove($conn, $dbName, $dimName, $elemNames[$i], $i);
				$this->_palo_disconnect($conn);

				if ($result)
					return array('', $result, false, '');
				else
					return array('', $result, true, 'Func [moveToBeginning|101]: ' . PaloConf::$errStr[101]);
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [moveToBeginning|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [moveToBeginning|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function moveToEnd($serv_id, $dbName, $dimName, $elemNames)
	{
		if ($dbName != null)
		{
			try
			{
				$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
				$conn = $this->_palo_init($serv_id);
				$lastId =  palo_ecount($conn, $dbName, $dimName) - 1;
				for ($i=0; $i<count($elemNames); $i++)
					$result = palo_emove($conn, $dbName, $dimName, $elemNames[$i], $lastId);
				$this->_palo_disconnect($conn);

				if ($result)
					return array('', $result, false, '');
				else
					return array('', $result, true, 'Func [moveToBeginning|101]: ' . PaloConf::$errStr[101]);
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [moveToBeginning|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [moveToBeginning|1003]: ' . PaloConf::$errStr[1003]);
	}

	private function _findElem($conn, $dbName, $dimName, $elemName)
	{
		try
		{
			$listElems = palo_dimension_list_elements($conn, $dbName, $dimName);

			foreach ($listElems as $elem)
				if ($elem['name'] == $elemName)
					return $elem;
		}
		catch (Exception $e)
		{
			throw $e;
		}

		return false;
	}

	public function getElemsCount($serv_id, $dbName, $dimName)
	{
		if ($dbName != null)
		{
			try
			{
				// adjusting dimName for different working modes
				$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
				$conn = $this->_palo_init($serv_id);
				$listElems = palo_dimension_list_elements($conn, $dbName, $dimName);
				$this->_palo_disconnect($conn);

				$numOfElems = count($listElems);
				$countC = 0;
				$countS = 0;
				$countN = 0;
				foreach ($listElems as $elem)
				{
					if ($elem['type'] == 'numeric')
						$countN++;
					else if ($elem['type'] == 'string')
						$countS++;
					else if ($elem['type'] == 'consolidated')
						$countC++;
				}

				return array('', array($numOfElems, $countC, $countN, $countS), false, '');
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [getElemsCount|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [getElemsCount|1003]: ' . PaloConf::$errStr[1003]);
	}

	public function searchElem($serv_id, $dbName, $dimName, $elemName, $attr =null)
	{
		if ($dbName != null)
		{
			try
			{
				$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
				$cubeName = '#_' . $dimName;
				$conn = $this->_palo_init($serv_id);
				$listElems = palo_dimension_list_elements($conn, $dbName, $dimName);

				if ($attr != null)
				{
					$reqList = array();
					for ($i = 0; $i < count($listElems); $i++)
						$reqList[$i] = $listElems[$i]['name'];

					array_unshift($reqList, count($reqList), 1);

					$listAttributedElemNames = palo_datav($conn, $dbName, $cubeName, $attr, $reqList);
					array_splice($listAttributedElemNames, 0, 2);

					for ($i = 0; $i < count($listAttributedElemNames); $i++)
						if ($elemName == $listAttributedElemNames[$i])
						{
							$elemPath = $this->_getElemPath($conn, $dbName, $dimName, $listElems[$i]['name']);

							$this->_palo_disconnect($conn);
							return array(true, '/root/' . $elemPath, false, '');
						}
				}
				else
				{
					for ($i = 0; $i < count($listElems); $i++)
						if ($elemName == $listElems[$i]['name'])
						{
							$elemPath = $this->_getElemPath($conn, $dbName, $dimName, $listElems[$i]['name']);

							$this->_palo_disconnect($conn);
							return array(true, '/root/' . $elemPath, false, '');
						}
				}

				$this->_palo_disconnect($conn);

				return array(false, '', false, '');
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [searchElem|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [searchElem|1003]: ' . PaloConf::$errStr[1003]);
	}

	private function _getElemPath($conn, $dbName, $dimName, $elemName)
	{
		// allways retrive first parent of this element if element have more then one parent
		if (palo_eparentcount($conn, $dbName, $dimName, $elemName))
		{
			$parent = palo_eparentname($conn, $dbName, $dimName, $elemName, 1);
			$tmpStr = $this->_getElemPath($conn, $dbName, $dimName, $parent);

			return $tmpStr . '/' . $elemName;
		}
		else
			return $elemName;
	}

	public function getAttributedElems($serv_id, $dbName, $dimName, $attr, $fromId, $toId)
	{
		if ($dbName != null)
		{
			try
			{
				$conn = $this->_palo_init($serv_id);
				$listElems = palo_dimension_list_elements($conn, $dbName, $dimName, true);

				$numOfElems = count($listElems);
				$listElemsOut = array();
				for ($i=$fromId; $i<$toId && $i<$numOfElems; $i++)
				{
					$listElemsOut[] = $listElems[$i];
					$listElemsOut[count($listElemsOut) - 1]['id'] = $listElems[$i]['name'];
					$listElemsOut[count($listElemsOut) - 1]['attr'] = $attr;
				}

				if ($attr != null)
				{
					$cubeName = '#_' . $dimName;
					$reqList = array();
					for ($i = 0; $i < count($listElemsOut); $i++)
						$reqList[$i] = $listElemsOut[$i]['name'];

					array_unshift($reqList, count($reqList), 1);

					$listAttributedElemNames = palo_datav($conn, $dbName, $cubeName, $attr, $reqList);
					array_splice($listAttributedElemNames, 0, 2);

					for ($i = 0; $i < count($listElemsOut); $i++)
						if (!empty($listAttributedElemNames[$i]))
							$listElemsOut[$i]['name'] = $listAttributedElemNames[$i];
				}
				$this->_palo_disconnect($conn);

				return array(array($fromId, $toId, count($listElemsOut), $numOfElems, $attr), $listElemsOut, false, '');
			}
			catch (Exception $e)
			{
				return array('', false, true, 'Func [getAttributedElems|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
			}
		}
		else
			return array('', false, true, 'Func [getAttributedElems|1003]: ' . PaloConf::$errStr[1003]);
	}

	// *** Other ***
	public function setWorkingMode($serv_id, $dbName, $dimName, $newMode)
	{
		$_SESSION['servid_' . $serv_id][$dbName][$dimName]['workingMode'] = $newMode;

		return $newMode;
	}

	private function _adjustDimModeName($serv_id, $dbName, $dimName)
	{
		if (!isset($_SESSION['servid_' . $serv_id][$dbName][$dimName]['workingMode']))
			$_SESSION['servid_' . $serv_id][$dbName][$dimName]['workingMode'] = PaloConf::$paloConf['workingMode'];

		if ($_SESSION['servid_' . $serv_id][$dbName][$dimName]['workingMode'] == 'attributes') //attributes
			return '#_' . $dimName . '_';

		return $dimName;
	}

	// *** Tree Panel ***
	public function getTreeNodes()
	{
		$elemID = isset($_POST['node']) ? $_POST['node'] : null;
		$elemID = stripslashes($elemID);
		$serv_id = isset($_GET['servId']) ? $_GET['servId'] : null;
		$dbName = isset($_GET['dbName']) ? $_GET['dbName'] : null;
		$dimName = isset($_GET['dimName']) ? $_GET['dimName'] : null;
		$attr = isset($_GET['attr']) ? $_GET['attr'] : null;
		$nodes = array();

		$dimName = $this->_adjustDimModeName($serv_id, $dbName, $dimName);
		if ($attr != null)
			$cubeName = '#_' . $dimName;

		if (($serv_id != null) && ($dbName != null) && ($dimName != null) && ($elemID != null))
		{
			$conn = $this->_palo_init($serv_id);

			if ($elemID == 'root')
			{
				$listElems = palo_dimension_list_elements2($conn, $dbName, $dimName, true);

				foreach ($listElems as $elem)
					if ($elem['num_parents'] == 0)
					{
						$isLeaf = ($elem['type'] == 'consolidated') ? false : true;
						$nodes[] = array('text' => $elem['name'], 'id' => $elem['name'], 'leaf' => $isLeaf, '_num_id' => $elem['identifier']);
					}
			}
			else
			{
				$listElems =  palo_element_list_consolidation_elements($conn, $dbName, $dimName, $elemID);

				foreach ($listElems as $elem)
				{
					$tmpIndex = palo_eindex($conn, $dbName, $dimName, $elem['name']) - 1;
					$isLeaf = ($elem['type'] == 'consolidated') ? false : true;
					$nodes[] = array('text' => $elem['name'], 'id' => $elem['name'], 'leaf' => $isLeaf , '_num_id' => $tmpIndex);
				}
			}

			if ($attr != null)
			{
				$reqList = array();
				for ($i = 0; $i < count($nodes); $i++)
					$reqList[$i] = $nodes[$i]['text'];

				array_unshift($reqList, count($reqList), 1);

				$listAttributedElemNames = palo_datav($conn, $dbName, $cubeName, $attr, $reqList);
				array_splice($listAttributedElemNames, 0, 2);

				for ($i = 0; $i < count($nodes); $i++)
					if (!empty($listAttributedElemNames[$i]))
						$nodes[$i]['text'] = $listAttributedElemNames[$i];
			}

			$this->_palo_disconnect($conn);
		}

		return $nodes;
	}

	// *** Paste View Handler ***
	public function handlerPasteView($inVal)
	{
		// Make paste view to be always at position A1
		if (!$inVal[0][8] && !$inVal[0][9])
		{
			$inVal[0][0] = 1;
			$inVal[0][1] = 1;
		}
		// ***

		$retArr = array();
		$settings = $inVal[0];
		$serv_id = $settings[2];
		$dbName = $settings[3];

		try
		{
			$workbook = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWbId() : '1');
			$sheet = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWsId() : '1');

			if (!isset($_SESSION['palo_data'][$workbook]) || !isset($_SESSION['palo_data'][$workbook][$sheet]) || !isset($_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states']))
			{
				if (!isset($_SESSION['palo_data'][$workbook]))
					$_SESSION['palo_data'][$workbook] = array();

				if (!isset($_SESSION['palo_data'][$workbook][$sheet]))
					$_SESSION['palo_data'][$workbook][$sheet] = array();

				if (!isset($_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states']))
					$_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'] = array();
			}

			// in case that Paste View is pasted at A1 current paste view at that position will be deleted
			$res = ccmd(array(array('wget', '', array(), array('e_id','n_location'), array('e_type' => 'palo_pv'))));
			if ($res[0][0] && (!$settings[9]))
			{
				$wbPasteViews = $res[0][1];
				for ($i=0; $i<count($wbPasteViews); $i++)
				{
					$pv_fields = explode(',', $wbPasteViews[$i]['n_location']);
					if ($pv_fields[1] == '$A$1')
					{
						$delFnc = array('clr');
						for ($pvf_n=0; $pvf_n < count($pv_fields); $pvf_n++)
						{
							if (strpos($pv_fields[$pvf_n], ':') === false)
							{
								$cell_pos = $this->_excel2Cord($pv_fields[$pvf_n]);
								$delFnc[] = array(15, $cell_pos[0], $cell_pos[1], $cell_pos[0], $cell_pos[1]);
							}
							else
							{
								$cells = explode(':', $pv_fields[$pvf_n]);
								$start_cell = $this->_excel2Cord($cells[0]);
								$end_cell = $this->_excel2Cord($cells[1]);

								$delFnc[] = array(15, $start_cell[0], $start_cell[1], $end_cell[0], $end_cell[1]);
							}
						}

						$wdelFnc = array('wdel', '', array($wbPasteViews[$i]['e_id']));
						unset($_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId]);
					}
				}
			}

			// Edit
			if ($settings[9])
			{
				$pasteViewId = $settings[10][0];

				$settings[0] -= $settings[10][1];
				$settings[1] -= $settings[10][2];

				$diffX = $settings[0] - $_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId][0][0];
				$diffY = $settings[1] - $_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId][0][1];

				// Delete Old Paste View
				$delFnc = array('clr');
				$res = ccmd(array(array('wget', '', array(), array('e_id','n_location'), array('pv_id' => $pasteViewId, 'e_type' => 'palo_pv'))));
				if ($res[0][0])
				{
					$pv_fields = explode(',', $res[0][1][0]['n_location']);

					for ($pvf_n=0; $pvf_n < count($pv_fields); $pvf_n++)
					{
						if (strpos($pv_fields[$pvf_n], ':') === false)
						{
							$cell_pos = $this->_excel2Cord($pv_fields[$pvf_n]);
							$delFnc[] = array(15, $cell_pos[0], $cell_pos[1], $cell_pos[0], $cell_pos[1]);
						}
						else
						{
							$cells = explode(':', $pv_fields[$pvf_n]);
							$start_cell = $this->_excel2Cord($cells[0]);
							$end_cell = $this->_excel2Cord($cells[1]);

							$delFnc[] = array(15, $start_cell[0], $start_cell[1], $end_cell[0], $end_cell[1]);
						}
					}
				}
			}
			else
			{
				$pasteViewId = uniqid('pv_');

				while(isset($_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId]))
					$pasteViewId = uniqid('pv_');
			}

			$conn = $this->_palo_init($serv_id);
			$pageDims = $inVal[1];
			$columnDims = $inVal[2];
			$rowDims = $inVal[3];
			$n_loc = '';

			$header = array('cdrg', array('cm' => false),
				array($settings[0], $settings[1], 1,
					array('v' => $serv_id . '/' . $dbName, 's' => PaloConf::$paloConf['styleConsolidated'] . 'background-color:' . PaloConf::$paloConf['colorHostDb'] . ';'),
					array('v' => $settings[4], 's' => PaloConf::$paloConf['styleConsolidated'] . 'background-color:' . PaloConf::$paloConf['colorHostDb'] . ';')));
			$n_loc .= $this->_cord2Excel($settings[0], $settings[1]) . ',' . $this->_cord2Excel($settings[0], $settings[1] + 1);

			$relativeY = 2;
			foreach ($pageDims as $dim)
			{
				$elem = $this->_hpv_getElems($conn, $dbName, $dim, 1);
				$fncName = '=PALO.ENAME(' . $this->_cord2Excel($settings[0], $settings[1]) . PaloConf::$paloConf['paloAttrsSeparator'] . '"' . $dim[0] . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '"' . $elem[0][0] . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '1' . PaloConf::$paloConf['paloAttrsSeparator'] . '""' . PaloConf::$paloConf['paloAttrsSeparator'] . '"")';

				$headerCell = array(
					'v' => $fncName,
					's' => (($elem[0][2] == 'C') ? (PaloConf::$paloConf['styleConsolidated']) : '') . 'background-color:' . PaloConf::$paloConf['colorPageElems'] . ';' .
					(($settings[5]) ? PaloConf::$paloConf['wrapText'] : ''),
					'a' => array('dblclick' => array(PaloConf::$paloConf['nofnc_openChooseElement_inJS'], array(
							'working_mode' => 1,
							'serv_id' => $serv_id,
							'db_name' => $dbName,
							'dim_name' => $dim[0],
							'edit_data' => array($elem[0]),
							'pasteview_id' => $pasteViewId,
							'edit_y' => $relativeY++
						)
					))
				);
				if (!empty($elem[0][1]))
					$headerCell['o'] = ';;;"' .  $this->_hpv_getAttrElem($conn, $dbName, $dim[0], $elem[0][1], $elem[0][0]) . '"';
				$header[2][] = $headerCell;

				$n_loc .= ',' . $this->_cord2Excel($settings[0], $settings[1] + $relativeY - 1);
			}
			$retArr[] = $header;

			// set X and Y minus factors
			$xFactor = (count($columnDims) > 0) ? 1 : 0;
			$yFactor = (count($rowDims) > 0) ? 1 : 0;

			// calculate starting cell for data (including empty space for dbclick->paste view edit dialog
			$rowColCellsX = $settings[0];
			$rowColCellsY = $settings[1] + count($header[2]) - 2;

			// Add dbl click to open filled Paste View
			if (count($columnDims) > 0 && count($rowDims) > 0)
			{
				$caArr = array('cdrn', array('cm' => false), array($rowColCellsX, $rowColCellsY, $rowColCellsX + count($rowDims) - 1, $rowColCellsY + count($columnDims) - 1));
				for ($i=$rowColCellsY; $i<($rowColCellsY + count($columnDims)); $i++)
					for ($j=$rowColCellsX; $j<($rowColCellsX + count($rowDims)); $j++)
						$caArr[2][] = array('a' => array('dblclick' => array(PaloConf::$paloConf['getPasteViewInitData_inJS'], $pasteViewId, $j - $settings[0], $i - $settings[1])));
			}
			else
			{
				$caArr = array('cdrn', array('cm' => false), array($rowColCellsX, $rowColCellsY - 1, $rowColCellsX, $rowColCellsY - 1));
				$caArr[2][] = array('a' => array('dblclick' => array(PaloConf::$paloConf['getPasteViewInitData_inJS'], $pasteViewId, $rowColCellsX - $settings[0], $rowColCellsY - $settings[1] - 1)));
			}

			$retArr[] = $caArr;

			// rows
			$lastY = $this->_hpv_addRow($conn, $dbName, $settings[5], &$retArr, $this->_cord2Excel($settings[0], $settings[1]), $rowDims, 0, $rowColCellsX, $rowColCellsY + count($columnDims), $settings[0], $settings[1], $pasteViewId, $settings[7]);

			// cols
			$lastX = $this->_hpv_addCol($conn, $dbName, $settings[5], &$retArr, $this->_cord2Excel($settings[0], $settings[1]), $columnDims, 0, $rowColCellsX + count($rowDims), $rowColCellsY, $settings[0], $settings[1], $pasteViewId);
			if ($settings[6] != -1)
				$retArr[] = array('scr', 0, array($settings[0], $lastX - (($xFactor) ? 1 : 0), round($settings[6] * 10 / PaloConf::$paloConf['pointToPixel'])));

			// data
			$cubeDims = palo_cube_list_dimensions($conn, $dbName, $settings[4]);
			$tmpDims = array_flip($cubeDims);

			$fncPreName = '=PALO.DATA(';
			for ($i=0; $i<2; $i++)
				$fncPreName .= $this->_cord2Excel($settings[0], $settings[1] + $i)  . PaloConf::$paloConf['paloAttrsSeparator'];

			for ($i=0; $i<count($pageDims); $i++)
				$tmpDims[$pageDims[$i][0]] = $this->_cord2Excel($settings[0], $settings[1] + $i + 2);

			$dataCellsX = $settings[0] + count($rowDims);
			$dataCellsY = $settings[1] + count($header[2]) - 2 + count($columnDims);

			$data = array($dataCellsX, $dataCellsY, $lastX - $xFactor, $lastY - $yFactor);
			if (count($columnDims) > 0 && count($rowDims) > 0)
			{
				for ($i=$dataCellsY; $i<$lastY; $i++)
				{
					for ($j=$dataCellsX; $j<$lastX; $j++)
					{
						for ($k=0; $k<count($columnDims); $k++)
							$tmpDims[$columnDims[$k][0]] = $this->_cord2Excel($j, $rowColCellsY + $k, 1);

						for ($k=0; $k<count($rowDims); $k++)
							$tmpDims[$rowDims[$k][0]] = $this->_cord2Excel($rowColCellsX + $k, $i, 2);

						$attrs = '';
						for ($k=0; $k<count($cubeDims); $k++)
							$attrs .= $tmpDims[$cubeDims[$k]] . PaloConf::$paloConf['paloAttrsSeparator'];
						$attrs = substr($attrs, 0, strlen($attrs) - 1);

						$fncName = $fncPreName . $attrs . ')';
						$data[] = array('v' => $fncName);
					}
				}
			}
			else if (count($rowDims) > 0)
			{
				for ($i=$dataCellsY; $i<$lastY; $i++)
				{
					for ($k=0; $k<count($rowDims); $k++)
						$tmpDims[$rowDims[$k][0]] = $this->_cord2Excel($rowColCellsX + $k, $i, 2);

					$attrs = '';
					for ($k=0; $k<count($cubeDims); $k++)
						$attrs .= $tmpDims[$cubeDims[$k]] . PaloConf::$paloConf['paloAttrsSeparator'];
					$attrs = substr($attrs, 0, strlen($attrs) - 1);

					$fncName = $fncPreName . $attrs . ')';
					$data[] = array('v' => $fncName);
				}
			}
			else if (count($columnDims) > 0)
			{
				for ($i=$dataCellsX; $i<$lastX; $i++)
				{
					for ($k=0; $k<count($columnDims); $k++)
						$tmpDims[$columnDims[$k][0]] = $this->_cord2Excel($i, $rowColCellsY + $k, 1);

					$attrs = '';
					for ($k=0; $k<count($cubeDims); $k++)
						$attrs .= $tmpDims[$cubeDims[$k]] . PaloConf::$paloConf['paloAttrsSeparator'];
					$attrs = substr($attrs, 0, strlen($attrs) - 1);

					$fncName = $fncPreName . $attrs . ')';
					$data[] = array('v' => $fncName);
				}
			}
			else
			{
				$attrs = '';
				for ($k=0; $k<count($cubeDims); $k++)
					$attrs .= $tmpDims[$cubeDims[$k]] . PaloConf::$paloConf['paloAttrsSeparator'];
				$attrs = substr($attrs, 0, strlen($attrs) - 1);

				$fncName = $fncPreName . $attrs . ')';
				$data[] = array('v' => $fncName);
			}

			$retArr[] = array('cdrn', array('cm' => false), $data);
			$retArr[] = array('cdrn', array('cm' => true), array($dataCellsX, $dataCellsY, $lastX - $xFactor, $lastY - $yFactor,
				array('s' => 'background-color:' . PaloConf::$paloConf['colorData'] . ';' .
					(($settings[5]) ? PaloConf::$paloConf['wrapText'] : '')
				)));
			$retArr[] = array('sbrd', array($dataCellsX, $dataCellsY, $lastX - $xFactor, $lastY - $yFactor), 63, '1px ' . PaloConf::$paloConf['colorDataBorder'] . ' dotted');

			// Close Palo connection
			$this->_palo_disconnect($conn);

			// Storing status for current Paste View
			$settings2 = array($settings[0], $settings[1], $lastX + ((!$xFactor)? 1 : 0), $lastY + ((!$yFactor)? 1 : 0));
			$_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId] = array($settings2, $inVal);

			// Edit Mode
			if ($settings[9] && isset($delFnc))
			{
				// TODO: change when new WUPD is made with "WHERE property = value" option
				if ($res[0][0])
				{
					$updPaloPv_wsel = array('wupd', '', array(
						$res[0][1][0]['e_id'] => array('n_location' => '=' . $this->_cord2Excel($rowColCellsX, $rowColCellsY) . ':' . $this->_cord2Excel($lastX - $xFactor, $lastY - $yFactor) . ',' . $n_loc)
					));

					$retArr[] = $updPaloPv_wsel;
				}

				array_unshift($retArr, $delFnc);
			}
			else
			{
				if (isset($delFnc) && isset($wdelFnc))
				{
					array_unshift($retArr, $delFnc);
					array_unshift($retArr, $wdelFnc);
				}

				// add wsel
				$addPaloPv_wsel = array('wadd', '', array(
					'e_type' => 'palo_pv',
					'pv_id' => $pasteViewId,
					'n_location' => '=' . $this->_cord2Excel($rowColCellsX, $rowColCellsY) . ':' . $this->_cord2Excel($lastX - $xFactor, $lastY - $yFactor) . ',' . $n_loc
				));

				$retArr[] = $addPaloPv_wsel;
			}

			return $retArr;
		}
		catch (Exception $e)
		{
			return array('', '', true, 'Func [handlerPasteView|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
		}
	}

	private function _hpv_addRow($inConn, $inDbName, $wrapText, $inRetArr, $inServDbStr, $inRowDims, $rowId, $startX, $startY, $relativeX, $relativeY, $pasteViewId, $hasIndent)
	{
		$tmpArr = $this->_hpv_getElems($inConn, $inDbName, $inRowDims[$rowId]);
		$currX = $startX;
		$currY = $startY;
		$totalNumOfRows = count($inRowDims);

		foreach ($tmpArr as $elem)
		{
			if (((isset($elem[3])) ? $elem[3] : true))
			{
				$indent = (($hasIndent) ? (palo_eindent($inConn, $inDbName, $inRowDims[$rowId][0], $elem[0]) - 1) : 0);
				$fncName = '=PALO.ENAME(' . $inServDbStr . PaloConf::$paloConf['paloAttrsSeparator'] . '"' . $inRowDims[$rowId][0] . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '"' . $elem[0] . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '3' . PaloConf::$paloConf['paloAttrsSeparator'] . '"' . $elem[0] . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '"")';
				$cell = array($currX, $currY, 1,
					array('v' => $fncName,
						's' => (($elem[2] == 'C') ? PaloConf::$paloConf['styleConsolidated'] : PaloConf::$paloConf['styleNormal']) .
							 PaloConf::$paloConf['colStyle' . (($totalNumOfRows - $rowId) % 3)] .
							'text-indent:' . $indent . 'em;' .
							(($wrapText) ? PaloConf::$paloConf['wrapText'] : '')
					));
				if ($elem[2] == 'C')
					$cell[3]['a'] = array('dblclick' => array(PaloConf::$paloConf['handlerExpandCollapsePasteView_inJS'], $currX - $relativeX, $currY - $relativeY, $pasteViewId, $inRowDims[$rowId][0], $elem[0]));
				if (!empty($elem[1]))
					$cell[3]['o'] = ';;;"' .  $this->_hpv_getAttrElem($inConn, $inDbName, $inRowDims[$rowId][0], $elem[1], $elem[0]) . '"';

				if ($totalNumOfRows > ($rowId + 1))
				{
					$lastId = $this->_hpv_addRow($inConn, $inDbName, $wrapText, &$inRetArr, $inServDbStr, $inRowDims, $rowId + 1, $currX + 1, $currY, $relativeX, $relativeY, $pasteViewId, $hasIndent);
					if (($lastId - 1) > $currY)
						$inRetArr[] = array('cdrn', array('cm' => false), array($currX, $currY+1, $currX, $lastId - 1, array('v' => '=' . $this->_cord2Excel($currX, $currY, 0),
						'o' => ';;;" "'
					)));
					$currY = $lastId;
				}
				else
					$currY++;

				$inRetArr[0][] = $cell;

				if ($currY >= PaloConf::$paloConf['pvMaxRows'])
					return $currY;
			}
		}

		return $currY;
	}

	private function _hpv_addCol($inConn, $inDbName, $wrapText, $inRetArr, $inServDbStr, $inColDims, $colId, $startX, $startY, $relativeX, $relativeY, $pasteViewId)
	{
		$tmpArr = $this->_hpv_getElems($inConn, $inDbName, $inColDims[$colId]);
		$currX = $startX;
		$currY = $startY;
		$totalNumOfCols = count($inColDims);

		foreach ($tmpArr as $elem)
		{
			if (((isset($elem[3])) ? $elem[3] : true))
			{
				$fncName = '=PALO.ENAME(' . $inServDbStr . PaloConf::$paloConf['paloAttrsSeparator'] . '"' . $inColDims[$colId][0] . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '"' . $elem[0] . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '3' . PaloConf::$paloConf['paloAttrsSeparator'] . '"' . $elem[0] . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '"")';
				$cell = array($currX, $currY, 1,
					array('v' => $fncName,
							's' => (($elem[2] == 'C') ? PaloConf::$paloConf['styleConsolidated'] : PaloConf::$paloConf['styleNormal']) .
								PaloConf::$paloConf['rowStyle' . (($totalNumOfCols - $colId) % 3)] .
								(($wrapText) ? PaloConf::$paloConf['wrapText'] : '')
					));
				if ($elem[2] == 'C')
					$cell[3]['a'] = array('dblclick' => array(PaloConf::$paloConf['handlerExpandCollapsePasteView_inJS'], $currX - $relativeX, $currY - $relativeY, $pasteViewId, $inColDims[$colId][0], $elem[0]));
				if (!empty($elem[1]))
					$cell[3]['o'] = ';;;"' .  $this->_hpv_getAttrElem($inConn, $inDbName, $inColDims[$colId][0], $elem[1], $elem[0]) . '"';

				if ($totalNumOfCols > ($colId + 1))
				{
					$lastId = $this->_hpv_addCol($inConn, $inDbName, $wrapText, &$inRetArr, $inServDbStr, $inColDims, $colId + 1, $currX, $currY + 1, $relativeX, $relativeY, $pasteViewId);
					if (($lastId - 1) > $currX)
						$inRetArr[] = array('cdrn', array('cm' => false), array($currX+1, $currY, $lastId - 1, $currY, array('v' => '=' . $this->_cord2Excel($currX, $currY, 0),
							'o' => ';;;" "'
						)));
					$currX = $lastId;
				}
				else
					$currX++;

				$inRetArr[0][] = $cell;

				if ($currX >= PaloConf::$paloConf['pvMaxCols'])
					return $currX;
			}
		}

		return $currX;
	}

	private function _hpv_getElems($inConn, $inDbName, $inDim, $inCount =null)
	{
		if (empty($inDim[1]))
		{
			$listElems = palo_dimension_list_elements2($inConn, $inDbName, $inDim[0], true);

			if ($inCount == 1)
			{
				foreach ($listElems as $elem)
					if (count($elem['parents']) == 0)
					{
						return array(array($elem['name'], null, (($elem['type'] == 'consolidated') ? 'C' : '')));
						break;
					}
			}
			else
			{
				$retArr = array();
				foreach ($listElems as $elem)
					if (count($elem['parents']) == 0)
						$retArr[] = array($elem['name'], null, (($elem['type'] == 'consolidated') ? 'C' : ''));

				return $retArr;
			}
		}
		else
			return (($inCount == 1) ? array($inDim[1][0]) : $inDim[1]);

		return array();
	}

	private function _hpv_getAttrElem($inConn, $inDbName, $inDimName, $inAttr, $inElemName)
	{
		$cubeName = '#_' . $inDimName;
		$listAttributedElemNames = palo_datav($inConn, $inDbName, $cubeName, $inAttr, $inElemName);

		return (!empty($listAttributedElemNames[2])) ? $listAttributedElemNames[2] : $inElemName;
	}

	private function _cord2Excel($x, $y, $mode =3)
	{
		if ($x > 26)
			$letters = chr(floor(($x - 1) / 26) + 64) . chr((($x - 1) % 26) + 65);
		else
			$letters = chr($x + 64);

		return (($mode & 2) ? '$' : '') . $letters . (($mode & 1) ? '$' : '') . $y;
	}

	private function _excel2Cord($cell)
	{
		$cell = str_replace('$', '', strtoupper($cell));

		preg_match('/[0-9]+/', $cell, $res);
		$row = intval($res[0]);

		preg_match('/[a-zA-Z]+/', $cell, $res);
		$col = $res[0];

		if (strlen($col) == 2)
			$col = (ord($col[0]) - 64) * 26 + ord($col[1]) - 64;
		else
			$col = ord($col[0]) - 64;

		return array($col, $row);
	}

	public function getPasteViewInitData($inPasteViewId)
	{
		$workbook = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWbId() : '1');
		$sheet = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWsId() : '1');

		if (($sheet != null) && ($workbook != null))
		{
			try
			{
				if (isset($_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states']))
					return array(true, $_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$inPasteViewId][1]);
			}
			catch (Exception $e)
			{}

			return array(false, 'Palo_get_paste_view_init', array());
		}
		else
			return array(false, 'Palo_get_paste_view_init', array());
	}

	// *** Expand/Collapse Paste View ***
	public function handlerExpandCollapsePasteView($inVal)
	{
		$workbook = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWbId() : '1');
		$sheet = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWsId() : '1');
		$pasteViewId = $inVal[3];

		if (isset($_SESSION['palo_data'][$workbook]) && isset($_SESSION['palo_data'][$workbook][$sheet]) && isset($_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states']) &&
			isset($_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId]))
		{
			$pasteViewData =& $_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId];
			$data =& $pasteViewData[1];
			$serv_id = $data[0][2];
			$dbName = $data[0][3];

			$res = ccmd(array(array('wget', '', array(), array('e_id', 'n_location'), array('pv_id' => $pasteViewId, 'e_type' => 'palo_pv'))));
			if ($res[0][0])
			{
				$pv_fields = explode(',', $res[0][1][0]['n_location']);
				$servDbCell = $pv_fields[1];
			}

			$newX = $inVal[0]['c'] - $inVal[1];
			$newY = $inVal[0]['r'] - $inVal[2];

			// Adjusting starting cordinates
			if ((($newX != $pasteViewData[0][0]) || ($newY != $pasteViewData[0][1])) && ($newX > 0) && ($newY > 0))
			{
				$diffX = $pasteViewData[0][2] - $pasteViewData[0][0];
				$diffY = $pasteViewData[0][3] - $pasteViewData[0][1];
				// X
				$data[0][0] = $newX;
				$pasteViewData[0][0] = $newX;
				$pasteViewData[0][2] = $newX + $diffX;
				// Y
				$data[0][1] = $newY;
				$pasteViewData[0][1] = $newY;
				$pasteViewData[0][3] = $newY + $diffY;
			}

			$conn = $this->_palo_init($serv_id);

			// Definine X and Y minus Factor
			$xFactor = (count($data[3]) > 0) ? 1 : 0;
			$yFactor = (count($data[2]) > 0) ? 1 : 0;

			// Data Range TopLeft position
			$startDataRangeX = $pasteViewData[0][0] + count($data[3]);
			$startDataRangeY = $pasteViewData[0][1] + count($data[1]) + 3 + count($data[2]);

			// $cr - 2 = column; 3 = row
			if (($startDataRangeX - $inVal[0]['c']) < 1)
			{
				$cr = 2;
				$colRowIndex = count($data[$cr]) - ($startDataRangeY - $inVal[0]['r']);
				$elemIndex = $inVal[0]['c'] - $startDataRangeX;
			}
			else
			{
				$cr = 3;
				$colRowIndex = count($data[$cr]) - ($startDataRangeX - $inVal[0]['c']);
				$elemIndex = $inVal[0]['r'] - $startDataRangeY;
			}

			$dim =& $data[$cr][$colRowIndex];
			$collapsed = true;

			// Empty dim (Initialize)
			if ($dim[1] == null)
				$dim[1] = $this->_hpv_getElems($conn, $dbName, $dim);

			for ($i=(count($data[$cr]) - 1); $i>$colRowIndex; $i--)
			{
				if ($data[$cr][$i][1] == null)
					$data[$cr][$i][1] = $this->_hpv_getElems($conn, $dbName, $data[$cr][$i]); // Empty dim (Initialize)
				$elemIndex /= count($data[$cr][$i][1]);
			}
			$elemIndex %= count($dim[1]);
			$elemList = $this->_getChildElems($conn, $dbName, $inVal[4], $inVal[5], $dim[1][$elemIndex][1]);

			// Do Collapse
			for ($i=$elemIndex+1; $i<count($dim[1]); $i++)
			{
				if ($this->_elemInArray($elemList, $dim[1][$i]) != -1)
				{
					if ($collapsed)
						$collapsed = false;

					if (((isset($dim[1][$i][3])) ? ($dim[$i][3]) : true))
						$this->_removeSubChilds($conn, $dbName, $inVal[4], $dim[1], $i);

					array_splice($dim[1], $i--, 1);
					continue;
				}
				break;
			}

			if ($collapsed)
				array_splice($dim[1], $elemIndex + 1, 0, $elemList);
			else
			{
				$lastX = $pasteViewData[0][2];
				$lastY = $pasteViewData[0][3];
			}

			$retArr = array(array('cdrg', array('cm' => false)));
			if ($cr == 2)
			{
				$pasteViewData[0][2] = $this->_hpv_addCol($conn, $dbName,
					$data[0][5],
					&$retArr,
					(isset($servDbCell) ? $servDbCell : $this->_cord2Excel($pasteViewData[0][0], $pasteViewData[0][1])),
					$data[$cr],
					0,
					$pasteViewData[0][0] + count($data[3]),
					$pasteViewData[0][1] + count($data[1]) + 3,
					$pasteViewData[0][0],
					$pasteViewData[0][1],
					$pasteViewId
				);
			}
			else if ($cr == 3)
			{
				$pasteViewData[0][3] = $this->_hpv_addRow($conn, $dbName,
					$data[0][5],
					&$retArr,
					(isset($servDbCell) ? $servDbCell : $this->_cord2Excel($pasteViewData[0][0], $pasteViewData[0][1])),
					$data[$cr],
					0,
					$pasteViewData[0][0],
					$pasteViewData[0][1] + count($data[1]) + 3 + count($data[2]),
					$pasteViewData[0][0],
					$pasteViewData[0][1],
					$pasteViewId,
					$data[0][7]
				);
			}

			// Close palo connection
			$this->_palo_disconnect($conn);

			if (!$collapsed)
			{
				$clrStartX = (($cr==2) ? $pasteViewData[0][2] : $pasteViewData[0][0]);
				$clrStartY = (($cr==3) ? $pasteViewData[0][3] : $pasteViewData[0][1] + (count($data[1]) + 3));

				if ($clrStartX <= ($lastX - $xFactor) && $clrStartY <= ($lastY - $yFactor))
				{
					$delFnc = array('clr', array(15,
						$clrStartX,
						$clrStartY,
						$lastX - $xFactor,
						$lastY - $yFactor));
					$retArr[] = $delFnc;
				}
			}
			else
			{
				 if ($cr == 2)
				 {
				 	$retArr[] = array('co', array($startDataRangeX, $startDataRangeY, $startDataRangeX, $pasteViewData[0][3] - 1, $startDataRangeX + 1, $startDataRangeY, $pasteViewData[0][2] - 1, $pasteViewData[0][3] - 1));
				 	if ($data[0][6] != -1)
						$retArr[] = array('scr', 0, array($startDataRangeX, $pasteViewData[0][2] - 1, round($data[0][6] * 10 / PaloConf::$paloConf['pointToPixel'])));
				 }
				 else if ($cr == 3)
				 	$retArr[] = array('co', array($startDataRangeX, $startDataRangeY, $pasteViewData[0][2] - 1, $startDataRangeY, $startDataRangeX, $startDataRangeY + 1, $pasteViewData[0][2] - 1, $pasteViewData[0][3] - 1));
			}

			// TODO: change when new WUPD is made with "WHERE property = value" option
			if ($res[0][0])
			{
				$n_loc = $this->_cord2Excel($pasteViewData[0][0], $pasteViewData[0][1] + count($data[1]) + 3) . ':' . $this->_cord2Excel($pasteViewData[0][2] - 1, $pasteViewData[0][3] - 1);
				for ($pvf_n = 1; $pvf_n < count($pv_fields); $pvf_n++)
					$n_loc .= ',' . $pv_fields[$pvf_n];

				array_unshift($retArr, array('wupd', '', array(
					$res[0][1][0]['e_id'] => array('n_location' => '=' . $n_loc)
				)));
			}

			return $retArr;
		}
	}

	private function _getChildElems($conn, $dbName, $dimName, $elemName, $attrib =null)
	{
		$listElems = palo_element_list_consolidation_elements($conn, $dbName, $dimName, $elemName);

		$retList = array();
		for ($i=0; $i<count($listElems); $i++)
			$retList[] = array($listElems[$i]['name'], $attrib, (($listElems[$i]['type'] == 'consolidated') ? 'C' : ''));

		return $retList;
	}

	private function _generateDataFuncs($conn, $dbName, $inData, $lastX, $lastY)
	{
		$settings = $inData[0];
		$pageDims = $inData[1];
		$columnDims = $inData[2];
		$rowDims = $inData[3];

		$cubeDims = palo_cube_list_dimensions($conn, $dbName, $settings[4]);
		$tmpDims = array_flip($cubeDims);

		$fncPreName = '=PALO.DATA(';
		for ($i=0; $i<2; $i++)
			$fncPreName .= $this->_cord2Excel($settings[0], $settings[1] + $i) . PaloConf::$paloConf['paloAttrsSeparator'];

		for ($i=0; $i<count($pageDims); $i++)
			$tmpDims[$pageDims[$i][0]] = $this->_cord2Excel($settings[0], $settings[1] + $i + 2);

		$rowColCellsX = $settings[0];
		$rowColCellsY = $settings[1] + count($pageDims) + 3;

		$dataCellsX = $rowColCellsX + count($rowDims);
		$dataCellsY = $rowColCellsY + count($columnDims);

		$data = array($dataCellsX, $dataCellsY, $lastX - 1, $lastY - 1);
		for ($i=$dataCellsY; $i<$lastY; $i++)
		{
			for ($j=$dataCellsX; $j<$lastX; $j++)
			{
				for ($k=0; $k<count($columnDims); $k++)
					$tmpDims[$columnDims[$k][0]] = $this->_cord2Excel($j, $rowColCellsY + $k, 1);

				for ($k=0; $k<count($rowDims); $k++)
					$tmpDims[$rowDims[$k][0]] = $this->_cord2Excel($rowColCellsX + $k, $i, 2);

				$attrs = '';
				for ($k=0; $k<count($cubeDims); $k++)
					$attrs .= $tmpDims[$cubeDims[$k]] . PaloConf::$paloConf['paloAttrsSeparator'];

				$attrs = substr($attrs, 0, strlen($attrs) - 1);

				$fncName = $fncPreName . $attrs . ')';
				$data[] = array('v' => $fncName);
			}
		}

		return $data;
	}

	private function _elemInArray($inArr, $inElem)
	{
		for ($i=0; $i<count($inArr); $i++)
			if ($inArr[$i][0] == $inElem[0])
				return $i;

		return -1;
	}

	private function _removeSubChilds($conn, $dbName, $dimName, &$listOfElems, $index)
	{
		$elemList = $this->_getChildElems($conn, $dbName, $dimName, $listOfElems[$index][0]);

		for ($i=$index+1; $i<count($listOfElems); $i++)
		{
			if ($this->_elemInArray($elemList, $listOfElems[$i]) != -1)
			{
				if (($listOfElems[$i][2] == 'C') && ((isset($listOfElems[$i][3])) ? ($listOfElems[$i][3]) : true))
					$this->_removeSubChilds($conn, $dbName, $dimName, $listOfElems, $i);

				array_splice($listOfElems, $i--, 1);
				continue;
			}
			break;
		}
	}

	// *** Select Elements *** //
	public function handlerSelectElements($inVals)
	{
		$settings = $inVals[0];
		$serv_id = $settings[2];
		$dbName = $settings[3];

		$workbook = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWbId() : '1');
		$sheet = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWsId() : '1');

		$dims = array();
		foreach ($inVals[1] as $dimName => $elems)
			$dims[] = array($dimName, $elems);

		$conn = $this->_palo_init($serv_id);

		$retArr = array('cdrg', array('cm' => true), array($settings[0], $settings[1], (($settings[4]) ? 0 : 1)));
		if (count($dims[0][1]) > 1)
		{
			foreach ($dims[0][1] as $elem)
			{
				$cellElem = array(
					'v' => $elem[0],
					's' => (($elem[2] == 'C') ? PaloConf::$paloConf['styleConsolidated'] : PaloConf::$paloConf['styleNormal']),
					'a' => array('palo_pe' => array('type' => 'dim', 'name' => $dims[0][0]))
				);
				if (isset($elem[1]) && !empty($elem[1]))
					$cellElem['o'] = ';;;"' . $this->_hpv_getAttrElem($conn, $dbName, $dims[0][0], $elem[1], $elem[0]) . '"';
				else
					$cellElem['o'] = '';
				$retArr[2][] = $cellElem;

				if ($settings[4])
					$retArr[2][2]++;
			}
		}
		else if (count($dims[0][1]) == 1)
		{
			$elem = $dims[0][1][0];
			$cellElem = array(
				'v' => '=PALO.ENAME("' . $serv_id . '/' . $dbName . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '"' . $dims[0][0] . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '"' . $elem[0] . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '3' . PaloConf::$paloConf['paloAttrsSeparator'] . '"' . $elem[0] . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '"")',
				's' => (($elem[2] == 'C') ? PaloConf::$paloConf['styleConsolidated'] : PaloConf::$paloConf['styleNormal']),
				'a' => array(
					'palo_pe' => array('type' => 'dim', 'name' => $dims[0][0]),
					'dblclick' => array(PaloConf::$paloConf['nofnc_openChooseElement_inJS'], array(
						'working_mode' => 8,
						'serv_id' => $serv_id,
						'db_name' => $dbName,
						'dim_name' => $dims[0][0],
						'edit_data' => array($elem)
					))
				)
			);
			if (isset($elem[1]) && !empty($elem[1]))
				$cellElem['o'] = ';;;"' . $this->_hpv_getAttrElem($conn, $dbName, $dims[0][0], $elem[1], $elem[0]) . '"';
			else
				$cellElem['o'] = '';
			$retArr[2][] = $cellElem;

			if ($settings[4])
				$retArr[2][2]++;
		}

		$this->_palo_disconnect($conn);

		return array($retArr);
	}

	// *** SelectElements Dialog - Other Elements paste function *** //
	public function handlerPutValuesOnGrid($settings, $data)
	{
		$retArr = array('cdrg', array('cm' => false), array($settings[0], $settings[1], (($settings[4]) ? 0 : 1)));
		foreach ($data as $dim)
		{
			$retArr[2][] = array(
				'v' => $dim,
				'a' => array('palo_pe' => array('type' => $settings[5]))
			);

			if ($settings[4])
				$retArr[2][2]++;
		}

		return array($retArr);
	}

	// Choose Element
	public function handlerChooseElements($inVals)
	{
		$workbook = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWbId() : '1');
		$sheet = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWsId() : '1');

		$retArr = array();
		$settings = $inVals[0];
		$dim = $inVals[1];

		$serv_id = $settings[2];
		$dbName = $settings[3];
		$pasteViewId = $settings[4];

		$firstCellX = $settings[0];
		$firstCellY = $settings[1] - $settings[5];
		$state = $_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId];

		if ((($firstCellX != $state[0][0]) || ($firstCellY != $state[0][1])) && ($firstCellX > 0) && ($firstCellY > 0))
		{
			$diffX = $state[0][2] - $state[0][0];
			$diffY = $state[0][3] - $state[0][1];
			// X
			$_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId][1][0][0] = $firstCellX;
			$_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId][0][0] = $firstCellX;
			$_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId][0][2] = $firstCellX + $diffX;
			// Y
			$_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId][1][0][1] = $firstCellY;
			$_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId][0][1] = $firstCellY;
			$_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId][0][3] = $firstCellY + $diffY;
		}

		$res = ccmd(array(array('wget', '', array(), array('e_id','n_location'), array('pv_id' => $pasteViewId, 'e_type' => 'palo_pv'))));
		if ($res[0][0])
		{
			$pv_fields = explode(',', $res[0][1][0]['n_location']);
			$servDbCell = $pv_fields[1];
		}

		$cmdModCell = array('cdrg', array('cm' => false), array($settings[0], $settings[1], 1, array(
			'v' => '=PALO.ENAME(' . ( isset($servDbCell) ? $servDbCell : $this->_cord2Excel($firstCellX, $firstCellY) ) . PaloConf::$paloConf['paloAttrsSeparator'] . '"' . $dim[0] . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '"' . $dim[1][0][0] . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '1' . PaloConf::$paloConf['paloAttrsSeparator'] . '""' . PaloConf::$paloConf['paloAttrsSeparator'] . '"")',
			's' => (($dim[1][0][2] == 'C') ? (PaloConf::$paloConf['styleConsolidated']) : (PaloConf::$paloConf['styleNormal'])) . 'background-color:' . PaloConf::$paloConf['colorPageElems'] . ';',
			'a' => array('dblclick' => array(PaloConf::$paloConf['nofnc_openChooseElement_inJS'], array(
						'working_mode' => 1,
						'serv_id' => $serv_id,
						'db_name' => $dbName,
						'dim_name' => $dim[0],
						'edit_data' => array($dim[1][0]),
						'pasteview_id' => $pasteViewId,
						'edit_y' => $settings[5]
					)
			))
		)));
		if (!empty($dim[1][0][1]))
		{
			$conn = $this->_palo_init($serv_id);
			$cmdModCell[2][3]['o'] = ';;;"' .  $this->_hpv_getAttrElem($conn, $dbName, $dim[0], $dim[1][0][1], $dim[1][0][0]) . '"';
			$this->_palo_disconnect($conn);
		}

		// Saving change
		foreach ($_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId][1][1] as $dimKey => $storedDim)
		{
			if ($storedDim[0] == $dim[0])
			{
				$_SESSION['palo_data'][$workbook][$sheet]['palo_pasteview_states'][$pasteViewId][1][1][$dimKey] = $dim;
				break;
			}
		}

		$retArr[] = $cmdModCell;

		return $retArr;
	}

	// *** PALO WIZARD *** //
	public function connDisconnServer($inArr)
	{
		try
		{
			$conn = $this->_get_config_conn();

			$perms = $this->accessPolicy->calcPerms('Config', 'connections', array($inArr[0]), false, 'dimension element', true);
			if (($perms[$inArr[0]] & AccessPolicy::PERM_WRITE) == AccessPolicy::PERM_WRITE)
			{
				palo_setdata($inArr[1], 'false', $conn, 'Config', '#_connections', 'active', $inArr[0]);
				return array(true);
			}
			else
				return array(false, 'Palo-no_rights_to_change');
		}
		catch (Exception $e)
		{
			return array(false, 'Palo-err_change_conn_status');
		}
	}

	public function createNewDb($inArr)
	{
		try
		{
			$serv_id = $inArr[0];
			$dbName = $inArr[1];

			$conn = $this->_palo_init($serv_id);
			$listDB = palo_root_list_databases($conn);

			if (in_array($dbName, $listDB))
			{
				$this->_palo_disconnect($conn);
				return array(false, '', false, '');
			}
			else
			{
				if (palo_root_add_database($conn, $dbName))
				{
					$this->_palo_disconnect($conn);
					return array(true, '', false, '');
				}
				else
				{
					$this->_palo_disconnect($conn);
					return array(false, '', false, '');
				}
			}
		}
		catch (Exception $e)
		{
			return array('', '', true, 'Func [createNewDb|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
		}
	}

	public function deleteDb($inArr)
	{
		try
		{
			$serv_id = $inArr[0];
			$dbName = $inArr[1];

			$conn = $this->_palo_init($serv_id);
			$listDB = palo_root_list_databases($conn);

			if (in_array($dbName, $listDB))
			{
				if (palo_root_delete_database($conn, $dbName))
				{
					$this->_palo_disconnect($conn);
					return array(true, '', false, '');
				}
				else
				{
					$this->_palo_disconnect($conn);
					return array(false, '', false, '');
				}
			}
			else
			{
				$this->_palo_disconnect($conn);
				return array(false, '', false, '');
			}
		}
		catch (Exception $e)
		{
			return array('', '', true, 'Func [deleteDb|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
		}
	}

	// *** Paste Data Function *** //
	public function handlerPasteDataFunctions($inVals)
	{
		$settings = $inVals[0];
		$serv_id = $settings[4];
		$dbName = $settings[5];

		$workbook = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWbId() : '1');
		$sheet = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWsId() : '1');

		try
		{
			$conn = $this->_palo_init($serv_id);
			$cubeName = $inVals[1];
			$guessARgs = $settings[6];

			$cubeDims = palo_cube_list_dimensions($conn, $dbName, $cubeName);
			$tmpDims = array_flip($cubeDims);

			// gen Data Functions
			if ($guessARgs)
			{
				// Zones
				$zones[2] = array(1, 1, $settings[0] - 1, $settings[1] - 1); // Zone 1
				$zones[0] = array($settings[0], 1, $settings[2], $settings[1] - 1); // Zone 2
				$zones[1] = array(1, $settings[1], $settings[0] - 1, $settings[3]); // Zone 3

				// get cells for all zones
				$cmds = '[';
				for ($i=0; $i<count($zones); $i++)
					$cmds .= '["gval",16,' . $zones[$i][0] . ',' . $zones[$i][1] . ',' . $zones[$i][2] . ',' . $zones[$i][3] . '],';
				$cmds = substr($cmds, 0, -1) . ']';

				// execute commands on core
				$res = json_decode($this->wss_ajax->exec($cmds), true);

				// Server/DB Guess and Cube Guess
				$guessServDb = null;
				$guessCube = null;

				// get all cells from core
				for ($i=0; $i<count($zones); $i++)
				{
					if (isset($res[$i]) && $res[$i][0])
					{
						$rngVals = $res[$i][1];
						$valsLen = count($rngVals);

						for ($j=0; $j<$valsLen; $j++)
						{
							if (!empty($rngVals[$j]) && (strpos($rngVals[$j], 'palo_pe') !== false))
							{
								$attrVal = json_decode($rngVals[$j], true);

								if (isset($attrVal['palo_pe']) && ($attrVal['palo_pe']['type'] == 'dim'))
								{
									$dimName = $attrVal['palo_pe']['name'];

									if (!is_array($tmpDims[$dimName]))
										$tmpDims[$dimName] = array($i, array($zones[$i][0] + intval($j % ($zones[$i][2] - $zones[$i][0] + 1)), $zones[$i][1] + intval($j / ($zones[$i][2] - $zones[$i][0] + 1))));
								}
								else if (isset($attrVal['palo_pe']) && ($attrVal['palo_pe']['type'] == 'serv_db') && ($guessServDb == null))
									$guessServDb = $this->_cord2Excel($zones[$i][0] + intval($j % ($zones[$i][2] - $zones[$i][0] + 1)), $zones[$i][1] + intval($j / ($zones[$i][2] - $zones[$i][0] + 1)));
								else if (isset($attrVal['palo_pe']) && ($attrVal['palo_pe']['type'] == 'cube') && ($guessCube == null))
									$guessCube = $this->_cord2Excel($zones[$i][0] + intval($j % ($zones[$i][2] - $zones[$i][0] + 1)), $zones[$i][1] + intval($j / ($zones[$i][2] - $zones[$i][0] + 1)));
							}
						}
					}
				}

				// gen function start name (with posible guessed serv/db and cube name)
				$fncPreName = '=PALO.DATA(' . (($guessServDb == null) ? ('"' . $serv_id . '/' . $dbName . '"') : $guessServDb) . PaloConf::$paloConf['paloAttrsSeparator'] . (($guessCube == null) ? ('"' . $cubeName . '"') : $guessCube) . PaloConf::$paloConf['paloAttrsSeparator'];

				for ($i=0; $i<count($cubeDims); $i++)
				{
					if (!is_array($tmpDims[$cubeDims[$i]]))
					{
						$tmpDim = array($cubeDims[$i], null);
						$tmpElems = $this->_hpv_getElems($conn, $dbName, $tmpDim, 1);

						$tmpDims[$cubeDims[$i]] = $tmpElems[0][0];
					}
				}

				$data = array($settings[0], $settings[1], $settings[2], $settings[3]);
				for ($j=$settings[1]; $j<=$settings[3]; $j++)
				{
					for ($i=$settings[0]; $i<=$settings[2]; $i++)
					{
						$singleDataFunc = $fncPreName;
						for ($k=0; $k<count($cubeDims); $k++)
						{
							$tmpDimInfo = $tmpDims[$cubeDims[$k]];

							if (!is_array($tmpDimInfo))
								$singleDataFunc .= '"' . $tmpDimInfo . '"';
							else
							{
								if ($tmpDimInfo[0] == 2)
									$singleDataFunc .= $this->_cord2Excel($tmpDimInfo[1][0], $tmpDimInfo[1][1]);
								else if ($tmpDimInfo[0] == 0)
									$singleDataFunc .= $this->_cord2Excel($tmpDimInfo[1][0] + ($i - $settings[0]), $tmpDimInfo[1][1], 1);
								else
									$singleDataFunc .= $this->_cord2Excel($tmpDimInfo[1][0], $tmpDimInfo[1][1] + ($j - $settings[1]), 2);
							}

							if ($k < (count($cubeDims) - 1))
								$singleDataFunc .= PaloConf::$paloConf['paloAttrsSeparator'];
						}
						$singleDataFunc .= ')';

						$data[] = array('v' => $singleDataFunc);
					}
				}
			}
			else
			{
				// gen start of function name for non guessed function
				$fncPreName = '=PALO.DATA("' . $serv_id . '/' . $dbName . '"' . PaloConf::$paloConf['paloAttrsSeparator'] . '"' . $cubeName . '"' . PaloConf::$paloConf['paloAttrsSeparator'];

				for ($i=0; $i<count($cubeDims); $i++)
				{
					$tmpDim = array($cubeDims[$i], null);
					$tmpElems = $this->_hpv_getElems($conn, $dbName, $tmpDim, 1);

					$fncPreName .= '"' . $tmpElems[0][0] . '"';
					if ($i < (count($cubeDims) - 1))
						$fncPreName .= PaloConf::$paloConf['paloAttrsSeparator'];
				}
				$fncPreName .= ')';

				$data = array($settings[0], $settings[1], $settings[2], $settings[3], array('v' => $fncPreName));
			}

			$this->_palo_disconnect($conn);

			return array(array('cdrn', array('cm' => true), $data));
		}
		catch (Exception $e)
		{
			return array('', '', true, 'Func [handlerPasteDataFunctions|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
		}
	}

	private function _isCellInRange($cellCords, $range)
	{
		return (($cellCords[0] >= $range[0]) && ($cellCords[0] <= $range[2]) && ($cellCords[1] >= $range[1]) && ($cellCords[1] <= $range[3]));
	}

	// *** Subset functions *** //
	public function getSubsetNames($serv_id, $dbName, $dimName)
	{
		try
		{
			$conn = $this->_palo_init($serv_id);
			$listSubset = palo_dimension_list_elements($conn, $dbName, '#_SUBSET_', true);

			$listDimGlobalSubsets = array();
			$listDimLocalSubsets = array();
			foreach($listSubset as $subset)
			{
				if (palo_data($conn, $dbName, '#_SUBSET_GLOBAL', $dimName, $subset['name']) != null)
					$listDimGlobalSubsets[] = $subset['name'];
				if (palo_data($conn, $dbName, '#_SUBSET_LOCAL', $dimName, $this->_get_username_for_conn($serv_id), $subset['name']) != null)
					$listDimLocalSubsets[] = $subset['name'];
			}
			$this->_palo_disconnect($conn);

			return array('', array($listDimLocalSubsets, $listDimGlobalSubsets), false, '');
		}
		catch (Exception $e)
		{
			return array('', '', true, 'Func [getGlobalSubsets|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
		}
	}

	public function handlerGetGeneratedSubsetFunc($arrSet, $dimName, $subsetName, $subsetType, $subsetDefArr =null)
	{
		try
		{
			$serv_id = $arrSet[4];
			$dbName = $arrSet[5];

			$conn = $this->_palo_init($serv_id);

			$retStr = '';
			if ($subsetType == 1)
				$retStr = palo_data($conn, $dbName, '#_SUBSET_LOCAL', $dimName, $_SESSION['servid_' . $serv_id]['username'], $subsetName);
			else if ($subsetType == 2)
				$retStr = palo_data($conn, $dbName, '#_SUBSET_GLOBAL', $dimName, $subsetName);
			else if ($subsetType == 0 && $subsetDefArr != null)
				$retStr = $this->_genPaloSubsetXML($subsetDefArr, array('conn' => $conn, 'db_name' => $dbName, 'dim_name' => $dimName));

			$subsetInfo = new DOMDocument();
			$subsetInfo->preserveWhiteSpace = false;
			$subsetInfo->loadXML($retStr);

			$subsetDXPath = new DOMXPath($subsetInfo);
			$subsetDXPath->registerNameSpace('subset', 'http://www.jedox.com/palo/SubsetXML');
			$retStr = $this->_getPaloSubsetFunc($serv_id, $conn, $dbName, $dimName, PaloConf::$paloSubsetDesc, $subsetDXPath);
			$retStr = '=' . $retStr;
			$this->_palo_disconnect($conn);

			return array(array('saf', array($arrSet[0], $arrSet[1], $arrSet[2], $arrSet[3], $retStr)));
		}
		catch (Exception $e)
		{
			return array('', '', true, 'Func [getGeneratedSubsetFunc|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
		}
	}

	private function _getPaloSubsetFunc($serv_id, $inConn, $inDbName, $inDimName, $descArr, $subsetDXPath)
	{
		$nodes = $subsetDXPath->query($descArr['_isSet']['path']);
		if ($nodes->length > 0)
		{
			$funcStr = $descArr['_name'] . '(';

			$loop = true;
			for ($i=1; $loop; $i++)
			{
				if (isset($descArr['@' . $i]))
				{
					$descElem = $descArr['@' . $i];
					$tmpSubExpresion = '';

					switch ($descElem['type'])
					{
						case 'function':
							$tmpSubExpresion = $this->_getPaloSubsetFunc($serv_id, $inConn, $inDbName, $inDimName, $descElem['function_def'], $subsetDXPath);
							break;

						case 'xpath':
							$nodes = $subsetDXPath->query($descElem['path']);
							if (isset($descElem['param']))
							{
								$tmpParamNode = $subsetDXPath->query($descElem['param']);
								if ($tmpParamNode->length > 0)
									$nodes = $tmpParamNode;
							}

							if ($nodes->length == 1)
								$tmpSubExpresion = (isset($tmpParamNode) && ($tmpParamNode->length > 0)) ? $nodes->item(0)->nodeValue : $this->_subsetFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item(0)->nodeValue, $descElem);
							else if ($nodes->length > 1)
							{
								$tmpSubExpresion = '{';
								for ($j=0; $j<$nodes->length; $j++)
									if (strlen($nodes->item($j)->nodeValue) > 0)
										$tmpSubExpresion .= (isset($tmpParamNode) && ($tmpParamNode->length > 0)) ? $nodes->item(0)->nodeValue : (($j < ($nodes->length - 1)) ? ($this->_subsetFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item($j)->nodeValue, $descElem) . PaloConf::$paloConf['paloSubesetFuncSeparator']) : $this->_subsetFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item($j)->nodeValue, $descElem));

								$tmpSubExpresion .= '}';
							}
							break;

						case 'variable':
							switch ($descElem['var_name'])
							{
								case 'server/database':
									$tmpSubExpresion = $serv_id . '/' . $inDbName; // Only for testing
									break;

								case 'dimension':
									$tmpSubExpresion = $inDimName;
									break;
							}
							$tmpSubExpresion = $this->_subsetFormatByDataType($inConn, $inDbName, $inDimName, $tmpSubExpresion, $descElem);
							break;

						case 'xpath_palo_attribute':
							$nodes = $subsetDXPath->query($descElem['path']);

							if ($nodes->length > 0)
								$nodes = $nodes->item(0)->childNodes;

							if ($nodes->length > 0)
							{
								$tmpSubExpresion = '{';
								for ($j=0; $j<$nodes->length; $j++)
								{
									if ($nodes->item($j)->childNodes->length > 1)
									{
										if (strtolower($nodes->item($j)->childNodes->item(0)->nodeName) == 'parameter')
											$tmpSubExpresion .= $nodes->item($j)->childNodes->item(0)->nodeValue . PaloConf::$paloConf['paloSubesetFuncSeparator'];
										else
											$tmpSubExpresion .= $nodes->item($j)->childNodes->item(1)->nodeValue . PaloConf::$paloConf['paloSubesetFuncSeparator'];
									}
									else if ($nodes->item($j)->childNodes->length > 0)
										$tmpSubExpresion .= $this->_subsetFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item($j)->childNodes->item(0)->nodeValue, array('data_type' => 'palo_attribute')) . PaloConf::$paloConf['paloSubesetFuncSeparator'];
								}

								if ($nodes->length > 1)
									$tmpSubExpresion = substr($tmpSubExpresion, 0, -1) . '}';
								else
									$tmpSubExpresion = substr($tmpSubExpresion, 1, -1);
							}
							break;

						case 'xpath_palo_attribute_filter':
							$nodes = $subsetDXPath->query($descElem['path']);

							if (isset($descElem['param']))
								$tmpParamNode = $subsetDXPath->query($descElem['param']);

							if (isset($tmpParamNode) && ($tmpParamNode->length > 0))
								$tmpSubExpresion = $tmpParamNode->item(0)->nodeValue;
							else
							{
								$tmpSubExpresion = '{';
								for ($j=0; $j<$nodes->length; $j++)
								{
									if ($nodes->item($j)->hasChildNodes())
									{
										if (strlen($nodes->item($j)->childNodes->item(0)->nodeValue) > 0)
											$tmpSubExpresion .= $this->_subsetFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item($j)->childNodes->item(0)->nodeValue, array('data_type' => 'palo_attribute')) . PaloConf::$paloConf['paloSubesetFuncSeparator'];
									}
								}
								if ($nodes->item(0)->childNodes->length > 1)
								{
									$tmpSubExpresion = substr($tmpSubExpresion, 0, -1);
									$tmpSubExpresion .= PaloConf::$paloConf['paloSubesetFuncSeparatorArr'];
									for ($j=1; $j<$nodes->item(0)->childNodes->length; $j++)
									{
										for ($k=0; $k<$nodes->length; $k++)
										{
											if (strlen($nodes->item($k)->childNodes->item($j)->nodeValue) > 0)
												$tmpSubExpresion .= $this->_subsetFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item($k)->childNodes->item($j)->nodeValue, $descElem);
											else
												$tmpSubExpresion .= '""';

											if ($k < ($nodes->length - 1))
												$tmpSubExpresion .= PaloConf::$paloConf['paloSubesetFuncSeparator'];
										}

										if ($j < ($nodes->item(0)->childNodes->length - 1))
											$tmpSubExpresion .= PaloConf::$paloConf['paloSubesetFuncSeparatorArr'];
									}
								}
								$tmpSubExpresion .= '}';
							}
							break;

						case 'xpath_palo_cube_element':
							$nodes = $subsetDXPath->query($descElem['path']);
							$tmpSubExpresion = '';

							$dimList = palo_cube_list_dimensions($inConn, $inDbName, palo_get_cube_name($inConn, $inDbName, $nodes->item(0)->childNodes->item(0)->childNodes->item(0)->nodeValue));
							for ($j=1; $j<=count($dimList); $j++)
							{
								// if parameter is set, then set parameter as value
								if ($nodes->item(0)->childNodes->item($j)->childNodes->length > 1)
									$tmpSubExpresion .= $nodes->item(0)->childNodes->item($j)->childNodes->item(0)->nodeValue;
								else
								{
									$elemId = $nodes->item(0)->childNodes->item($j)->childNodes->item(0)->childNodes->item(0)->nodeValue;
									if ($elemId == -1)
									{
										if (strcmp($dimList[$j-1], $inDimName) != 0)
											$tmpSubExpresion .= '"*"';
									}
									else
										$tmpSubExpresion .= '"' . palo_get_element_name($inConn, $inDbName, $dimList[$j-1], $elemId) . '"';
								}

								if ($j < count($dimList))
									$tmpSubExpresion .= PaloConf::$paloConf['paloSubesetFuncSeparator'];
							}
							break;

						case 'xpath_palo_criteria':
							$nodes = $subsetDXPath->query($descElem['path']);

							if ($nodes->item(0)->childNodes->length > 0)
							{
								$tmpSubExpresion = '{';
								for ($j=0; $j<($nodes->item(0)->childNodes->length / 2); $j++)
								{
									$tmpSubExpresion .= '"' . $tmpOp = $nodes->item(0)->childNodes->item($j*2 + 1)->nodeValue . '"';
									$tmpSubExpresion .= PaloConf::$paloConf['paloSubesetFuncSeparator'];

									$tmpPar = $nodes->item(0)->childNodes->item($j*2)->childNodes->item(0)->nodeValue;
									if ($tmpPar !== '0')
									{
										if ((0 + $tmpPar) === 0)
											$tmpSubExpresion .= '"' . $tmpPar . '"';
										else
											$tmpSubExpresion .= $tmpPar;
									}
									else
										$tmpSubExpresion .= $tmpPar;

									if ($j < ($nodes->item(0)->childNodes->length / 2 - 1))
										$tmpSubExpresion .= PaloConf::$paloConf['paloSubesetFuncSeparator'];
								}
								$tmpSubExpresion .= '}';
							}
							break;
					}

					$funcStr .= $tmpSubExpresion . PaloConf::$paloConf['paloSubesetFuncSeparator'];
				}
				else
					$loop = false;
			}

			if (strlen($funcStr) > strlen($descArr['_name'] . '('))
				$funcStr = substr($funcStr, 0, strlen($funcStr) - 1);

			$funcStr .= ')';

			// Workaround
			if (($descArr['_name'] == 'PALO.PICKLIST') && (strlen($funcStr) == 17))
				return '';

			return $funcStr;
		}
		else
			return '';
	}

	private function _subsetFormatByDataType($inConn, $inDbName, $inDimName, $val, $descElem)
	{
		if (isset($descElem['data_type']))
		{
			switch ($descElem['data_type'])
			{
				case 'string':
					return '"' . $val . '"';

				case 'palo_attribute':
					if (is_numeric($val))
						return '"' . palo_get_element_name($inConn, $inDbName, '#_' . $inDimName . '_', $val) . '"';

					return $val;

				case 'palo_element':
					if (is_numeric($val))
						return '"' . palo_get_element_name($inConn, $inDbName, $inDimName, $val) . '"';

					return $val;

				case 'palo_cube':
					return '"' . palo_get_cube_name($inConn, $inDbName, $val) . '"';

				default:
					return $val;
			}
		}
		else
			return $val;
	}

	// *** function for generating subset list ***
	public function getSubsetList($genData)
	{
		try
		{
			$serv_id = $genData['serv_id'];
			$dbName = $genData['db_name'];

			$conn = $this->_palo_init($serv_id);

			$hierOrder = false;
			if ($genData['subset_type'] == 0)
			{
				$listElems = palo_dimension_list_elements2($conn, $dbName, $genData['dim_name'], true);
				$this->_palo_disconnect($conn);

				if ($genData['layouthier'])
				{
					$hierOrder = true;
					$this->_sortElemsInHierarchicalOrder($retArr, $listElems);
				}
				else
				{
					$retArr = array();
					for ($i=0; $i<count($listElems); $i++)
						$retArr[] = $listElems[$i]['name'];
				}
			}
			else
			{
				$retStr = '';
				if ($genData['subset_type'] == 1)
					$retStr = palo_data($conn, $dbName, '#_SUBSET_LOCAL', $genData['dim_name'], $this->_get_username_for_conn($serv_id),  $genData['subset_name']);
				else if ($genData['subset_type'] == 2)
					$retStr = palo_data($conn, $dbName, '#_SUBSET_GLOBAL', $genData['dim_name'], $genData['subset_name']);

				$subsetInfo = new DOMDocument();
				$subsetInfo->preserveWhiteSpace = false;
				$subsetInfo->loadXML($retStr);

				$subsetDXPath = new DOMXPath($subsetInfo);
				$subsetDXPath->registerNameSpace('subset', 'http://www.jedox.com/palo/SubsetXML');
				$execArr = $this->execPHPPaloSubsetFunc($conn, $dbName, $genData['dim_name'], PaloConf::$paloSubsetDesc, $subsetDXPath);
				$this->_palo_disconnect($conn);

				// Generate hierarchical array
				$nodes = $subsetDXPath->query('/subset:subset/subset:sorting_filter/subset:whole/subset:value | /subset:subset/subset:sorting_filter/subset:reverse/subset:value | /subset:subset/subset:indent/subset:value');
				if (($nodes->length == 3) && ($nodes->item(1)->nodeValue == '1') && ($nodes->item(2)->nodeValue != '1'))
				{
					$hierOrder = true;
					$index = 0;
					$retArr = $this->_genHierarchicalOrder($execArr, ($nodes->item(0)->nodeValue == '2'), $genData['quick_preview']);
				}
				else
				{
					$retArr = array();
					$upTo = ($genData['quick_preview'] && (count($execArr) > PaloConf::$paloConf['paloSubsetQucikPreviewListSize'])) ? PaloConf::$paloConf['paloSubsetQucikPreviewListSize'] : count($execArr);
					for ($i=0; $i<$upTo; $i++)
						$retArr[] = $execArr[$i]['name'];
				}
			}

			return array($hierOrder, $retArr, false, '');
		}
		catch (Exception $e)
		{
			return array('', '', true, 'Func [getGeneratedSubsetFunc|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
		}
	}

	public function setDynarangeList($hbConf)
	{
		try
		{
			$genData = $hbConf['_gendata'];

			$retArr = $this->handlerGetGeneratedSubsetFunc(array(0, 0, 0, 0, $genData[0][0], $genData[0][1]), $genData[0][2], '', 0, $genData[1]);

			return array(true, $retArr[0][1][4]);
		}
		catch (Exception $e)
		{
			return array(false, 'Palo_set_dynarange_list_error', array());
		}
	}

	private function _sortElemsInHierarchicalOrder(&$toArr, $listElems, $depth=0, $id=-1)
	{
		if (!isset($toArr))
			$toArr = array();

		foreach($listElems as $elem)
		{
			if ($elem['depth'] == $depth)
			{
				if ($elem['num_parents'] == 0)
				{
					if ($elem['num_children'] == 0)
						$toArr[] = $elem['name'];
					else
					{
						$toArr[] = array('n' => $elem['name'], 'l' => array());
						$this->_sortElemsInHierarchicalOrder($toArr[count($toArr) - 1]['l'], $listElems, $depth+1, $elem['identifier']);
					}
				}
				else
				{
					foreach($elem['parents'] as $parentElem)
					{
						if ($parentElem['identifier'] == $id)
						{
							if ($elem['num_children'] == 0)
								$toArr[] = $elem['name'];
							else
							{
								$toArr[] = array('n' => $elem['name'], 'l' => array());
								$this->_sortElemsInHierarchicalOrder($toArr[count($toArr) - 1]['l'], $listElems, $depth+1, $elem['identifier']);
							}
						}
					}
				}
			}
		}
	}

	function &_genHierarchicalOrder(&$vals, $levelSort =false, $isQuickPreview =false)
	{
		$dataList = array();

		$name = $vals[0]['name'];
		$curr_lvl = $vals[0]['ident'];
		$curr_lists = array($curr_lvl => &$dataList);

		for ($lvl_prev = $curr_lvl, $len = count($vals), $i = 1; $i < $len; $i++)
		{
			$lvl = $vals[$i]['ident'];

			if ((!$levelSort && ($lvl > $lvl_prev)) || ($levelSort && ($lvl < $lvl_prev)))
				if (!$isQuickPreview || count($curr_lists[$curr_lvl]) < PaloConf::$paloConf['paloSubsetQucikPreviewSize'])
					$curr_lists[$lvl] = &$curr_lists[$curr_lvl][array_push($curr_lists[$curr_lvl], array('text' => $name, 'leaf' => false, 'children' => array())) - 1]['children'];
				else
					$curr_lists[$lvl] = &$curr_lists[$curr_lvl][count($curr_lists[$curr_lvl]) - 1]['children'];
			else
				if (!$isQuickPreview || count($curr_lists[$curr_lvl]) < PaloConf::$paloConf['paloSubsetQucikPreviewSize'])
					$curr_lists[$curr_lvl][] = array('text' => $name, 'leaf' => true); //$name;

			$lvl_prev = $lvl;

			if (isset($curr_lists[$lvl]))
				$curr_lvl = $lvl;

			$name = $vals[$i]['name'];
		}

		if (!$isQuickPreview || count($curr_lists[$curr_lvl]) < PaloConf::$paloConf['paloSubsetQucikPreviewSize'])
			$curr_lists[$curr_lvl][] = array('text' => $name, 'leaf' => true); //$name;

		return $dataList;
	}

	public function execPHPPaloSubsetFunc($inConn, $inDbName, $inDimName, $descArr, $subsetDXPath)
	{
		$nodes = $subsetDXPath->query($descArr['_isSet']['path']);
		if ($nodes->length > 0)
		{
			$funcNameStr = strtolower(str_replace('.', '_', $descArr['_name']));
			$tmpFuncArgs = array();

			$loop = true;
			for ($i=1; $loop; $i++)
			{
				if (isset($descArr['@' . $i]))
				{
					$descElem = $descArr['@' . $i];

					switch ($descElem['type'])
					{
						case 'function':
							$tmpFuncArgs[] = $this->execPHPPaloSubsetFunc($inConn, $inDbName, $inDimName, $descElem['function_def'], $subsetDXPath);
							break;

						case 'xpath':
							$nodes = $subsetDXPath->query($descElem['path']);

							if ($nodes->length == 1)
								$tmpFuncArgs[] = $this->_subsetExecPHPFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item(0)->nodeValue, $descElem);
							else if ($nodes->length > 1)
							{
								$tmpArr = array();
								for ($j=0; $j<$nodes->length; $j++)
									if (strlen($nodes->item($j)->nodeValue) > 0)
										$tmpArr[] = (($j < ($nodes->length - 1)) ? ($this->_subsetExecPHPFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item($j)->nodeValue, $descElem)) : $this->_subsetExecPHPFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item($j)->nodeValue, $descElem));

								$tmpFuncArgs[] = $tmpArr;
							}
							else
								$tmpFuncArgs[] = null;

							break;

						case 'variable':
							switch ($descElem['var_name'])
							{
								case 'server/database':
									$tmpVar = $inConn;
									break;

								case 'dimension':
									$tmpVar = $inDimName;
									break;
							}
							$tmpFuncArgs[] = $this->_subsetExecPHPFormatByDataType($inConn, $inDbName, $inDimName, $tmpVar, $descElem);
							break;

						case 'xpath_palo_attribute':
							$nodes = $subsetDXPath->query($descElem['path']);

							if ($nodes->length > 0)
								$nodes = $nodes->item(0)->childNodes;

							if ($nodes->length > 0)
							{
								$tmpArr = array();
								for ($j=0; $j<$nodes->length; $j++)
								{
									if ($nodes->item($j)->childNodes->length > 1)
									{
										if (strtolower($nodes->item($j)->childNodes->item(0)->nodeName) == 'value')
											$tmpArr[] = $this->_subsetExecPHPFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item($j)->childNodes->item(0)->nodeValue, array('data_type' => 'palo_attribute'));
										else
											$tmpArr[] = $this->_subsetExecPHPFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item($j)->childNodes->item(1)->nodeValue, array('data_type' => 'palo_attribute'));
									}
									else
										$tmpArr[] = $this->_subsetExecPHPFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item($j)->childNodes->item(0)->nodeValue, array('data_type' => 'palo_attribute'));
								}

								$tmpFuncArgs[] = $tmpArr;
							}
							else
								$tmpFuncArgs[] = null;

							break;

						case 'xpath_palo_attribute_filter':
							$nodes = $subsetDXPath->query($descElem['path']);

							if ($nodes->length > 0)
							{
								$tmpArr = array();
								$tmpArr[] = ($nodes->item(0)->childNodes->length == 1) ? 2 : $nodes->item(0)->childNodes->length;
								$tmpArr[] = $nodes->length;
								for ($j=0; $j<$nodes->length; $j++)
									if ($nodes->item($j)->hasChildNodes() && (strlen($nodes->item($j)->childNodes->item(0)->nodeValue) > 0))
										$tmpArr[] = $this->_subsetExecPHPFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item($j)->childNodes->item(0)->nodeValue, array('data_type' => 'palo_attribute'));

								if ($nodes->item(0)->childNodes->length > 1)
								{
									for ($j=1; $j<$nodes->item(0)->childNodes->length; $j++)
									{
										for ($k=0; $k < $nodes->length; $k++)
											if (strlen($nodes->item($k)->childNodes->item($j)->nodeValue) > 0)
												$tmpArr[] = $this->_subsetExecPHPFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item($k)->childNodes->item($j)->nodeValue, $descElem);
											else
												$tmpArr[] = null;
									}
								}
								else if ($nodes->item(0)->childNodes->length == 1)
								{
									for ($j=0; $j<$nodes->length; $j++)
										$tmpArr[] = null;
								}

								$tmpFuncArgs[] = $tmpArr;
							}
							break;

						case 'xpath_palo_cube_element':
							$nodes = $subsetDXPath->query($descElem['path']);

							$dimList = palo_cube_list_dimensions($inConn, $inDbName, palo_get_cube_name($inConn, $inDbName, $nodes->item(0)->childNodes->item(0)->childNodes->item(0)->nodeValue));
							for ($j=1; $j<=count($dimList); $j++)
							{
								$tmpArr = array();
								for ($k=0; $k<$nodes->item(0)->childNodes->item($j)->childNodes->item(0)->childNodes->length; $k++)
								{
									$elemId = $nodes->item(0)->childNodes->item($j)->childNodes->item(0)->childNodes->item($k)->nodeValue;
									if ($elemId == -1)
									{
										if (strcmp($dimList[$j-1], $inDimName) != 0)
										{
											$listOfElems = palo_dimension_list_elements2($inConn, $inDbName, $dimList[$j-1]);

											for ($l=0; $l<count($listOfElems); $l++)
												if ($listOfElems[$l]['num_parents'] == 0)
													$tmpArr[] = $listOfElems[$l]['name'];

											if (count($tmpArr) == 1)
												$tmpArr = $tmpArr[0];
										}
										else
											$tmpArr = NULL;
									}
									else
									{
										if ($nodes->item(0)->childNodes->item($j)->childNodes->item(0)->childNodes->length > 1)
											$tmpArr[] = palo_get_element_name($inConn, $inDbName, $dimList[$j-1], intval($elemId));
										else
											$tmpArr = palo_get_element_name($inConn, $inDbName, $dimList[$j-1], intval($elemId));
									}
								}
								$tmpFuncArgs[] = $tmpArr;
							}
							break;

						case 'xpath_palo_criteria':
							$nodes = $subsetDXPath->query($descElem['path']);

							if ($nodes->item(0)->childNodes->length > 0)
							{
								$tmpArr = array();
								for ($j=0; $j<($nodes->item(0)->childNodes->length / 2); $j++)
								{
									$tmpArr[] = ($tmpOp = $nodes->item(0)->childNodes->item($j*2 + 1)->nodeValue);

									$tmpPar = $nodes->item(0)->childNodes->item($j*2)->childNodes->item(0)->nodeValue;
									$tmpArr[] = $tmpPar;
								}
								$tmpFuncArgs[] = $tmpArr;
							}
							else
								$tmpFuncArgs[] = null;

							break;
					}
				}
				else
					$loop = false;
			}

			if ($descArr['_hb_hasDbName'])
				array_splice($tmpFuncArgs, 1, 0, $inDbName); // Database name is on 2nd position of function

			// Workaround for AFILTER
			if ($funcNameStr == 'palo_afilter')
			{
				if (empty($tmpFuncArgs))
					$tmpRetVal = null;
				else
					$tmpRetVal = call_user_func_array($funcNameStr, $tmpFuncArgs);
			}
			else
				$tmpRetVal = call_user_func_array($funcNameStr, $tmpFuncArgs);

			return $tmpRetVal;
		}
		else
			return null;
	}

	private function _subsetExecPHPFormatByDataType($inConn, $inDbName, $inDimName, $val, $descElem)
	{
		if (isset($descElem['data_type']))
		{
			switch ($descElem['data_type'])
			{
				case 'palo_attribute':
					$val = (is_array($val) ? $val[0] : $val);
					return palo_get_element_name($inConn, $inDbName, '#_' . $inDimName . '_', $val);

				case 'palo_element':
					return palo_get_element_name($inConn, $inDbName, $inDimName, $val);

				case 'palo_cube':
					return palo_get_cube_name($inConn, $inDbName, $val);

				case 'bool':
					return ((strtoupper($val) == 'TRUE') ? true : false);

				default:
					return $val;
			}
		}
		else
			return $val;
	}

	// *** Store & Restore funcs *** //
	public function get_palo_data($workbook)
	{
		// save palo preselection
		if (isset($_SESSION['palo_data']) && isset($_SESSION['palo_data']['palo_preselection']))
		{
			$cmds = '[["bget","",[],["e_id"],{"e_type":"palo_pres"}]]';
			$res = json_decode($this->wss_ajax->exec($cmds), true);

			if (isset($res[0]) && $res[0][0] && isset($res[0][1]) && isset($res[0][1][0]))
				$cmds = json_encode(array(array('bupd', '', array($res[0][1][0]['e_id'] => array('data' => $_SESSION['palo_data']['palo_preselection'])))));
			else
				$cmds = json_encode(array(array('badd', '', array('e_type' => 'palo_pres', 'data' => $_SESSION['palo_data']['palo_preselection']))));

			$res = $this->wss_ajax->exec($cmds);
		}

		return ((isset($_SESSION['palo_data'][$workbook])) ? $_SESSION['palo_data'][$workbook] : array());
	}

	public function set_palo_data($workbook, $arr)
	{
		if (count($arr) > 0)
			$_SESSION['palo_data'][$workbook] = $arr;

		// load palo preselection
		$res = json_decode($this->wss_ajax->exec('[["bget","",[],["data"],{"e_type":"palo_pres"}]]'), true);
		if (isset($res[0]) && $res[0][0])
			$_SESSION['palo_data']['palo_preselection'] = $res[0][1][0]['data'];
	}

	// *** Generation of SUBSET XML *** //
	private function _genPaloSubsetXML(&$dataArr, $setupArr =null)
	{
		$xmlDoc = new DOMDocument();
		$xmlDoc->preserveWhiteSpace = false;
		$this->fixPaloSubsetInObject($dataArr, $setupArr);
		$this->_genXmlFromArr($xmlDoc, $xmlDoc, $dataArr);

		$docXslt = new DOMDocument();
		$xsl = new XSLTProcessor();

		if (is_file('../../lib/rpc/' . PaloConf::$paloConf['paloSubsetGrammarFile']))
			$docXslt->load('../../lib/rpc/' . PaloConf::$paloConf['paloSubsetGrammarFile']);
		else
			die('Can not find ' . PaloConf::$paloConf['paloSubsetGrammarFile'] . ' file!'); // TODO: throw exception
		$xsl->importStyleSheet($docXslt);

		$xmlDoc = $xsl->transformToDoc($xmlDoc);
		$xmlDoc->removeChild($xmlDoc->firstChild);
		$tmpNode = $xmlDoc->createProcessingInstruction('palosubset', 'version="1.0"');
		$xmlDoc->insertBefore($tmpNode, $xmlDoc->firstChild);

		return $xmlDoc->saveXML();
	}

	// fixing some data for Data Arrays with variables
	public function fixPaloSubsetInObject(&$dataArr, $setupArr)
	{
		if ($setupArr != null)
		{
			$conn = $setupArr['conn'];
			$dbName = $setupArr['db_name'];
			$dimName = $setupArr['dim_name'];

			$subset =& $dataArr['subset'];

			// general
			if (isset($subset['alias1']) && isset($subset['alias1']['param']))
				$subset['alias1']['value'] = palo_eindex($conn, $dbName, '#_' . $dimName . '_', $subset['alias1']['value']) - 1;
			if (isset($subset['alias2']) && isset($subset['alias2']['param']))
				$subset['alias2']['value'] = palo_eindex($conn, $dbName, '#_' . $dimName . '_', $subset['alias2']['value']) - 1;

			// hier
			if (isset($subset['hier']))
			{
				if (isset($subset['hier']['above']))
					$subset['hier']['above']['value'] = ($subset['hier']['above']['value']) ? 'true' : 'false';

				if (isset($subset['hier']['exclusive']))
					$subset['hier']['exclusive']['value'] = ($subset['hier']['exclusive']['value']) ? 'true' : 'false';

				// element identifier
				if (isset($subset['hier']['element']))
					$sub_name = 'element';
				else if (isset($subset['hier']['revolve_element']))
					$sub_name = 'revolve_element';
				if (isset($subset['hier']['element']) || isset($subset['hier']['revolve_element']))
					$subset['hier'][$sub_name]['value'] = palo_get_element_id($conn, $dbName, $dimName, $subset['hier'][$sub_name]['value']);
			}
			// text
			if (isset($subset['text']))
			{
				if (!is_array($subset['text']['regexes']['value']))
					$subset['text']['regexes']['value'] = array($subset['text']['regexes']['value']);
			}
			// picklist
			if (isset($subset['pick']))
			{
				$tmpList =& $subset['pick']['elems']['value'];

				if (is_array($tmpList))
					foreach($tmpList as $key => $val)
						$tmpList[$key] = palo_get_element_id($conn, $dbName, $dimName, $val);
				else
					$tmpList = palo_get_element_id($conn, $dbName, $dimName, $tmpList);
			}
			// attribute
			if (isset($subset['attr']))
			{
				if (isset($subset['attr']['attribute_filter']) && is_array($subset['attr']['attribute_filter']['value']))
				{
					$attribs = palo_dimension_list_elements($conn, $dbName, '#_' . $dimName . '_');

					$tmpArr = array(); $numOfAttrs = count($attribs); $tmpVals = $subset['attr']['attribute_filter']['value'];
					for ($i=0; $i<$numOfAttrs; $i++)
					{
						$tmpArr[$attribs[$i]['identifier']] = array();
						$numOfRows = count($tmpVals) / $numOfAttrs;
						for ($j=0; $j<$numOfRows; $j++)
							$tmpArr[$attribs[$i]['identifier']][] = $tmpVals[$i*$numOfRows+$j];
					}

					$subset['attr']['attribute_filter']['value'] = $tmpArr;
				}
			}
			// data
			if (isset($subset['data']))
			{
				if (isset($subset['data']['subcube']) && isset($subset['data']['subcube']['subcube']))
				{
					$cubeDims = palo_cube_info($conn, $dbName, $subset['data']['subcube']['source_cube']['value']);
					$cubeDims = $cubeDims['dimensions'];

					for ($i=0; $i<count($subset['data']['subcube']['subcube']); $i++)
					{
						$dimValues =& $subset['data']['subcube']['subcube'][$i]['value'];
						if ($dimValues != '*')
						{
							if (is_array($dimValues))
							{
								for ($j=0; $j<count($dimValues); $j++)
									$dimValues[$j] = palo_get_element_id($conn, $dbName, $cubeDims[$i], $dimValues[$j]);
							}
							else
								$dimValues = palo_get_element_id($conn, $dbName, $cubeDims[$i], $dimValues);
						}
						else
							$dimValues = -1;
					}
				}

				if (isset($subset['data']['subcube']) && isset($subset['data']['subcube']['source_cube']))
					$subset['data']['subcube']['source_cube']['value'] = palo_get_cube_id($conn, $dbName, $subset['data']['subcube']['source_cube']['value']);
			}
			// Sort
			if (isset($subset['sort']['attribute']) && (isset($subset['sort']['attribute']['param'])) && ($setupArr != null))
				$subset['sort']['attribute']['value'] = palo_eindex($conn, $dbName, '#_' . $dimName . '_', $subset['sort']['attribute']['value']) - 1;
		}
	}

	public function getSubsetDialogData($inArr)
	{
		try
		{
			$serv_id = $inArr[0];
			$dbName = $inArr[1];
			$dimName = $inArr[2];
			$subsetName = $inArr[3];
			$subsetType = $inArr[4];

			$conn = $this->_palo_init($serv_id);

			$retStr = '';
			if ($subsetType == 1)
				$retStr = palo_data($conn, $dbName, '#_SUBSET_LOCAL', $dimName, $this->_get_username_for_conn($serv_id), $subsetName);
			else if ($subsetType == 2)
				$retStr = palo_data($conn, $dbName, '#_SUBSET_GLOBAL', $dimName, $subsetName);

			$subsetInfo = new DOMDocument();
			$subsetInfo->preserveWhiteSpace = false;
			$subsetInfo->loadXML($retStr);

			$subsetDXPath = new DOMXPath($subsetInfo);
			$subsetDXPath->registerNameSpace('subset', 'http://www.jedox.com/palo/SubsetXML');
			$ret = $this->_getPaloSubsetObject($serv_id, $conn, $dbName, $dimName, PaloConf::$paloSubsetDesc, $subsetDXPath);
			$this->_fixPaloSubsetOutObject($ret, array('conn' => $conn, 'db_name' => $dbName, 'dim_name' => $dimName));
			$this->_palo_disconnect($conn);

			return array('', $ret, false, '');
		}
		catch (Exception $e)
		{
			return array('', '', true, 'Func [getGeneratedSubsetFunc|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
		}
	}

	private function _fixPaloSubsetOutObject(&$dataArr, $setupArr)
	{
		if (isset($dataArr['subset']) && isset($setupArr))
		{
			$conn = $setupArr['conn'];
			$dbName = $setupArr['db_name'];
			$dimName = $setupArr['dim_name'];

			$subset =& $dataArr['subset'];

			// general
			if (isset($subset['attribs']))
			{
				if (is_array($subset['attribs']['value']))
				{
					$subset['alias1'] = array('value' => $subset['attribs']['value'][0]);
					$subset['alias2'] = array('value' => $subset['attribs']['value'][1]);
				}
				else if ($subset['attribs']['value'] != null)
					$subset['alias1'] = array('value' => $subset['attribs']['value']);

				if (is_array($subset['attribs']['param']))
				{
					$subset['alias1']['param'] = $subset['attribs']['param'][0];
					$subset['alias2']['param'] = $subset['attribs']['param'][1];
				}
				else if ($subset['attribs']['param'] != null)
					$subset['alias1']['param'] = $subset['attribs']['param'];

				unset($subset['attribs']);
			}

			// hier
			if (isset($subset['hier']))
			{
				// element identifier
				if (isset($subset['hier']['element']))
					$sub_name = 'element';
				else if (isset($subset['hier']['revolve_element']))
					$sub_name = 'revolve_element';
				if (isset($subset['hier']['element']) || isset($subset['hier']['revolve_element']))
					$subset['hier'][$sub_name]['value'] = palo_get_element_name($conn, $dbName, $dimName, $subset['hier'][$sub_name]['value']);
			}

			// picklist
			if (isset($subset['pick']) && isset($subset['pick']['elems']))
			{
				if (is_array($subset['pick']['elems']['value']))
				{
					foreach ($subset['pick']['elems']['value'] as $key => $val)
						$subset['pick']['elems']['value'][$key] = palo_get_element_name($conn, $dbName, $dimName, $val);
				}
				else
					$subset['pick']['elems']['value'] = palo_get_element_name($conn, $dbName, $dimName, $subset['pick']['elems']['value']);
			}

			// Data
			if (isset($subset['data']) && isset($subset['data']['subcube']) && isset($subset['data']['subcube']['source_cube']))
				$subset['data']['subcube']['source_cube']['value'] = palo_get_cube_name($conn, $dbName, $subset['data']['subcube']['source_cube']['value']);
		}

		return $inArr;
	}

	private function _getPaloSubsetObject($serv_id, $inConn, $inDbName, $inDimName, $descArr, $subsetDXPath)
	{
		$nodes = $subsetDXPath->query($descArr['_isSet']['path']);
		if ($nodes->length > 0)
		{
			$loop = true;
			for ($i=1; $loop; $i++)
			{
				if (isset($descArr['@' . $i]))
				{
					$descElem = $descArr['@' . $i];
					$tmpSubArr = array();

					switch ($descElem['type'])
					{
						case 'function':
							$tmpSubArr = $this->_getPaloSubsetObject($serv_id, $inConn, $inDbName, $inDimName, $descElem['function_def'], $subsetDXPath);
							break;

						case 'xpath':
							$nodes = $subsetDXPath->query($descElem['path']);
							if (isset($descElem['param']))
							{
								$tmpParamNodes = $subsetDXPath->query($descElem['param']);
								if ($tmpParamNodes->length == 1)
									$paramVarName = $tmpParamNodes->item(0)->nodeValue;
								else if ($tmpParamNodes->length > 1)
								{
									$paramVarName = array();
									for ($j=0; $j<$tmpParamNodes->length; $j++)
										$paramVarName[] = $tmpParamNodes->item($j)->nodeValue;
								}
							}

							if ($nodes->length == 1)
								$tmpSubArr[$descElem['_objName']] = array('value' => $nodes->item(0)->nodeValue, 'param' => ((isset($paramVarName)) ? $paramVarName : null));
								//$tmpSubArr[$descElem['_objName']] = array('value' => $this->_subsetFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item(0)->nodeValue, $descElem), 'param' => ((isset($paramVarName)) ? $paramVarName : null));
							else if ($nodes->length > 1)
							{
								$tmpSubArr[$descElem['_objName']] = array('value' => array(), 'param' => ((isset($paramVarName)) ? $paramVarName : null));
								for ($j=0; $j<$nodes->length; $j++)
									if (strlen($nodes->item($j)->nodeValue) > 0)
										$tmpSubArr[$descElem['_objName']]['value'][] = $nodes->item($j)->nodeValue; //$this->_subsetFormatByDataType($inConn, $inDbName, $inDimName, $nodes->item($j)->nodeValue, $descElem);
							}
							if (isset($paramVarName))
								unset($paramVarName);
							break;

						case 'variable':
							switch ($descElem['var_name'])
							{
								case 'server/database':
									$tmpSubArr[$descElem['_objName']] = $serv_id . '/' . $inDbName; // Only for testing
									break;

								case 'dimension':
									$tmpSubArr[$descElem['_objName']] = $inDimName;
									break;
							}
							break;

						case 'xpath_palo_attribute':
							$nodes = $subsetDXPath->query($descElem['path']);

							if ($nodes->length > 0)
							{
								$nodes = $nodes->item($j)->childNodes;
								for ($j=0; $j<$nodes->length; $j++)
								{
									if ($nodes->item($j)->childNodes->length > 1)
									{
										if (strtolower($nodes->item($j)->childNodes->item(0)->nodeName) == 'value')
											$tmpSubArr[$nodes->item($j)->nodeName] = array(
												'value' => $nodes->item($j)->childNodes->item(0)->nodeValue,
												'param' => $nodes->item($j)->childNodes->item(1)->nodeValue
											);
										else
											$tmpSubArr[$nodes->item($j)->nodeName] = array(
												'value' => $nodes->item($j)->childNodes->item(1)->nodeValue,
												'param' => $nodes->item($j)->childNodes->item(0)->nodeValue
											);
									}
									else
										$tmpSubArr[$nodes->item($j)->nodeName] = array(
											'value' => $nodes->item($j)->childNodes->item(0)->nodeValue
										);
								}
							}
							break;

						case 'xpath_palo_attribute_filter':
							$nodes = $subsetDXPath->query($descElem['path']);

							if (isset($descElem['param']))
							{
								$tmpParamNodes = $subsetDXPath->query($descElem['param']);
								if ($tmpParamNodes->length == 1)
									$paramVarName = $tmpParamNodes->item(0)->nodeValue;
								else if ($tmpParamNodes->length > 1)
								{
									$paramVarName = array();
									for ($j=0; $j<$tmpParamNodes->length; $j++)
										$paramVarName[] = $tmpParamNodes->item($j)->nodeValue;
								}
							}

							$tmpSubArr[$descElem['_objName']] = array('value' => array(), 'param' => ((isset($paramVarName)) ? $paramVarName : null));

							for ($j=0; $j<$nodes->length; $j++)
							{
								$numOfChilds = $nodes->item($i)->childNodes->length;
								for ($k=1; $k<$numOfChilds; $k++)
								{
									if (strlen($nodes->item($j)->childNodes->item($k)->nodeValue) > 0)
										$tmpSubArr[$descElem['_objName']]['value'][] = $nodes->item($j)->childNodes->item($k)->nodeValue;
									else
										$tmpSubArr[$descElem['_objName']]['value'][] = '';
								}
							}
							break;

						case 'xpath_palo_cube_element':
							$nodes = $subsetDXPath->query($descElem['path']);
							$tmpSubArr[$descElem['_objName']] = array();

							$dimList = palo_cube_list_dimensions($inConn, $inDbName, palo_get_cube_name($inConn, $inDbName, $nodes->item(0)->childNodes->item(0)->childNodes->item(0)->nodeValue));
							for ($j=1; $j<=count($dimList); $j++)
							{
								$dimCord = $nodes->item(0)->childNodes->item($j);
								$serialNumber = count($tmpSubArr[$descElem['_objName']]);

								if ($dimCord->childNodes->length > 1)
								{
									$tmpSubArr[$descElem['_objName']][$serialNumber]['param'] = $dimCord->childNodes->item(0)->nodeValue;
									$elems = $dimCord->childNodes->item(1)->childNodes;
								}
								else
									$elems = $dimCord->childNodes->item(0)->childNodes;

								for ($k=0; $k<$elems->length; $k++)
								{
									$elemId =  $elems->item($k)->nodeValue;
									if ($elemId == -1)
									{
										if (strcmp($dimList[$j-1], $inDimName) != 0)
											$tmpSubArr[$descElem['_objName']][$serialNumber]['value'] = '*';
									}
									else
									{
										$elemName = palo_get_element_name($inConn, $inDbName, $dimList[$j-1], $elemId);
										if ($k > 0)
										{
											if ($k == 1)
												$tmpSubArr[$descElem['_objName']][$serialNumber]['value'] = array($tmpSubArr[$descElem['_objName']][$serialNumber]['value'], $elemName);
											else
												array_push($tmpSubArr[$descElem['_objName']][$serialNumber]['value'], $elemName);
										}
										else
											$tmpSubArr[$descElem['_objName']][$serialNumber]['value'] = $elemName;
									}
								}
							}
							break;

						case 'xpath_palo_criteria':
							$nodes = $subsetDXPath->query($descElem['path']);

							if ($nodes->item(0)->childNodes->length > 0)
							{
								$tmpSubArr[$descElem['_objName']] = array();
								for ($j=0; $j<($nodes->item(0)->childNodes->length / 2); $j++)
								{
									$tmpSubArr[$descElem['_objName']][] = $tmpOp = $nodes->item(0)->childNodes->item($j*2 + 1)->nodeValue;
									$parNodes = $nodes->item(0)->childNodes->item($j*2)->childNodes;
									if ($parNodes->length > 1)
										$tmpSubArr[$descElem['_objName']][] = array(
											'value' => $parNodes->item(1)->nodeValue,
											'param' => $parNodes->item(0)->nodeValue
										);
									else
										$tmpSubArr[$descElem['_objName']][] = array(
											'value' => $parNodes->item(0)->nodeValue,
											'param' => null
										);
								}
							}
							break;
					}

					foreach ($tmpSubArr as $key => $val)
						$funcArr[$descArr['_objName']][$key] = $val;
				}
				else
					$loop = false;
			}

			return $funcArr;
		}
		else
			return array();
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

	public function getSubsetListByXML($inArr, $forceList =false)
	{
		try
		{
			// +++ init vars +++ //
			$settings = $inArr[0];
			$dataArr = $inArr[1];

			$serv_id = $settings[0];
			$dbName = $settings[1];
			$dimName = $settings[2];
			$quickPreview = (isset($settings[3])) ? $settings[3] : false;

			$hierOrder = false;
			// --- init vars --- //

			$conn = $this->_palo_init($serv_id);

			$xmlDoc = new DOMDocument();
			$xmlDoc->preserveWhiteSpace = false;
			$xmlStr = $this->_genPaloSubsetXML($dataArr, array('conn' => $conn, 'db_name' => $dbName, 'dim_name' => $dimName));
			$xmlDoc->loadXML($xmlStr);

			$subsetDXPath = new DOMXPath($xmlDoc);
			$subsetDXPath->registerNameSpace('subset', 'http://www.jedox.com/palo/SubsetXML');
			$execArr = $this->execPHPPaloSubsetFunc($conn, $dbName, $dimName, PaloConf::$paloSubsetDesc, $subsetDXPath);

			$nodes = $subsetDXPath->query('/subset:subset/subset:sorting_filter/subset:whole/subset:value | /subset:subset/subset:sorting_filter/subset:reverse/subset:value | /subset:subset/subset:indent/subset:value');
			if ((!$forceList) && ($nodes->length == 3) && ($nodes->item(1)->nodeValue == '1') && ($nodes->item(2)->nodeValue != '1'))
			{
				$hierOrder = true;
				$index = 0;
				$retArr = $this->_genHierarchicalOrder($execArr, ($nodes->item(0)->nodeValue == '2'), $quickPreview);
			}
			else
			{
				$retArr = array();
				$upTo = ($quickPreview && (count($execArr) > PaloConf::$paloConf['paloSubsetQucikPreviewListSize'])) ? PaloConf::$paloConf['paloSubsetQucikPreviewListSize'] : count($execArr);
				for ($i=0; $i<$upTo; $i++)
					$retArr[] = $execArr[$i]['name'];
			}

			return array($hierOrder, $retArr, false, '');
		}
		catch (Exception $e)
		{
			return array('', '', true, 'Func [getSubsetListByXML|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
		}
	}

	public function savePaloSubset($inArr)
	{
		try
		{
			// +++ init vars +++ //
			$settings = $inArr[0];
			$dataArr = $inArr[1];

			$serv_id = $settings[0];
			$dbName = $settings[1];
			$dimName = $settings[2];
			$subsetName = $settings[3];
			$subsetType = $settings[4];
			// --- init vars --- //

			$conn = $this->_palo_init($serv_id);
			$result = palo_eindex($conn, $dbName, '#_SUBSET_', $subsetName, true);
			if ($result == '')
				$result = palo_eadd($conn, $dbName, '#_SUBSET_', 'S', $subsetName, '', 1, FALSE, true);

			if ($result)
			{
				$dataArr['subset']['id'] = -1; // TODO: should be assinged proper ID number
				$dataArr['subset']['sourceDimensionId'] = 2;
				if ($subsetType == 2)
 					palo_dataset($this->_genPaloSubsetXML($dataArr, array('conn' => $conn, 'db_name' => $dbName, 'dim_name' => $dimName)), false, $conn, $dbName, '#_SUBSET_GLOBAL', array($dimName, $subsetName));
 				else if ($subsetType == 1)
 					palo_dataset($this->_genPaloSubsetXML($dataArr, array('conn' => $conn, 'db_name' => $dbName, 'dim_name' => $dimName)), false, $conn, $dbName, '#_SUBSET_LOCAL', array($dimName, $this->_get_username_for_conn($serv_id), $subsetName));
			}
			else
				return array(false, 'Palo_subset_create_error', array('ss_name' => $subsetName));
			$this->_palo_disconnect($conn);

			return array(true, $subsetName);
		}
		catch (Exception $e)
		{
			return array(false, 'Palo_subset_create_error', array('ss_name' => $subsetName));
		}
	}

	public function deletePaloSubset($settings)
	{
		$serv_id = $settings[0];
		$dbName = $settings[1];
		$subsetName = $settings[2];

		$conn = $this->_palo_init($serv_id);
		$result = palo_edelete($conn, $dbName, '#_SUBSET_', $subsetName, true);
		$this->_palo_disconnect($conn);

		return (($result) ? array($subsetName, '', false, '') : array('', '', true, 'Func [deletePaloSubset|1010]: ' . PaloConf::$errStr[1010]));
	}

	public function renamePaloSubset($settings)
	{
		$serv_id = $settings[0];
		$dbName = $settings[1];
		$subsetName = $settings[2];
		$newSubsetName = $settings[3];

		$conn = $this->_palo_init($serv_id);
		$result = palo_erename($conn, $dbName, '#_SUBSET_', $subsetName, $newSubsetName, true);
		$this->_palo_disconnect($conn);

		return (($result) ? array(array($subsetName, $newSubsetName), '', false, '') : array('', '', true, 'Func [renamePaloSubset|1011]: ' . PaloConf::$errStr[1011]));
	}

	// *** PALO Rules functions *** //
	public function getRules($settings)
	{
		try
		{
			$serv_id = $settings[0];
			$dbName = $settings[1];
			$cubeName = $settings[2];

			$conn = $this->_palo_init($serv_id);
			$result = palo_cube_rules($conn, $dbName, $cubeName);
			$this->_palo_disconnect($conn);

			return array('', $result, false, '');
		}
		catch (Exception $e)
		{
			return array('', '', true, 'Func [getRules|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
		}
	}

	public function createRule($inArr)
	{
		try
		{
			$settings = $inArr[0];
			$data = $inArr[1];

			$serv_id = $settings[0];
			$dbName = $settings[1];
			$cubeName = $settings[2];

			$conn = $this->_palo_init($serv_id);
			$result = palo_cube_rule_create($conn, $dbName, $cubeName, $data['definition'], $data['extern_id'], $data['comment'], $data['activate']);
			$this->_palo_disconnect($conn);

			return array('', $result, false, '');
		}
		catch (Exception $e)
		{
			return array('', '', true, 'Func [createRule|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
		}
	}

	public function deleteRule($inArr)
	{
		try
		{
			$settings = $inArr[0];
			$ruleId = $inArr[1];

			$serv_id = $settings[0];
			$dbName = $settings[1];
			$cubeName = $settings[2];

			$conn = $this->_palo_init($serv_id);
			$result = palo_cube_rule_delete($conn, $dbName, $cubeName, $ruleId);
			$this->_palo_disconnect($conn);

			return array('', $result, false, '');
		}
		catch (Exception $e)
		{
			return array('', '', true, 'Func [deleteRule|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
		}
	}

	public function modifyRule($inArr)
	{
		try
		{
			$settings = $inArr[0];
			$dataArr = $inArr[1]; // 0 = old rule, 1 = new rule

			$serv_id = $settings[0];
			$dbName = $settings[1];
			$cubeName = $settings[2];

			$conn = $this->_palo_init($serv_id);
			$result = palo_cube_rule_modify($conn, $dbName, $cubeName, $dataArr[0]['identifier'], $dataArr[1]['def'], $dataArr[0]['extern_id'], $dataArr[1]['comment'], $dataArr[1]['active']);
			$this->_palo_disconnect($conn);

			return array('', $result, false, '');
		}
		catch (Exception $e)
		{
			return array('', '', true, 'Func [deleteRule|0001]: ' . PaloConf::$errStr[1] . $e->getMessage());
		}
	}

	// Page Setup Functions
	public function storePageSetup($inObj)
	{
		try
		{
			$workbook = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWbId() : '1');
			$sheet = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWsId() : '1');

			$_SESSION['wss_page_setup'][$workbook][$sheet] = $inObj;

			return array(true);
		}
		catch (Exception $e)
		{
			return array(false, $e->getMessage());
		}
	}

	public function getPageSetup()
	{
		try
		{
			$workbook = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWbId() : '1');
			$sheet = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWsId() : '1');

			if (isset($_SESSION['wss_page_setup']) && isset($_SESSION['wss_page_setup'][$workbook]) && isset($_SESSION['wss_page_setup'][$workbook][$sheet]))
				return array(true, $_SESSION['wss_page_setup'][$workbook][$sheet]);
			else
				return array(true, null);
		}
		catch (Exception $e)
		{
			return array(false, 'Palo_error_get_page_setup', array());
		}
	}

	public function get_page_setup_data($workbook)
	{
		return ((isset($_SESSION['wss_page_setup'][$workbook])) ? $_SESSION['wss_page_setup'][$workbook] : array());
	}

	public function set_page_setup_data($workbook, $arr)
	{
		$_SESSION['wss_page_setup'][$workbook] = $arr;
	}

	public function handlerImportPaloDataFunc($negativeLineNum)
	{
		$workbook = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWbId() : '1');
		$sheet = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWsId() : '1');

		if (isset($_SESSION['palo_import']) && isset($_SESSION['palo_import'][$workbook]) && isset($_SESSION['palo_import'][$workbook][$sheet]))
		{
			$textFile = new TextFile();

			$conf = $_SESSION['palo_import'][$workbook][$sheet]['conf'];

			$textFile->setSep($conf['sep']);
			$textFile->setPoint($conf['d_point']);
			$textFile->setHeader($conf['header']);

			// num_of_lines <- number of lines in file
			$textFile->importFileLine($_SESSION['palo_import'][$workbook][$sheet]['file'], $_SESSION['palo_import'][$workbook][$sheet]['num_of_lines'] - $negativeLineNum);

			// delete file in case of last import
			$lineNum = $negativeLineNum - (($conf['header']) ? 2 : 1);
			if ($lineNum == 0)
			{
				unlink($_SESSION['palo_import'][$workbook][$sheet]['file']);
				unset($_SESSION['palo_import']);
			}

			return $textFile->genCmds();
		}
	}

	public function doImportCleanup()
	{
		$workbook = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWbId() : '1');
		$sheet = (($this->wss_ajax != null) ? $this->wss_ajax->getCurrWsId() : '1');

		if (isset($_SESSION['palo_import']) && isset($_SESSION['palo_import'][$workbook]) && isset($_SESSION['palo_import'][$workbook][$sheet]))
		{
			unlink($_SESSION['palo_import'][$workbook][$sheet]['file']);
			unset($_SESSION['palo_import']);
		}

		return array(true);
	}
}

?>