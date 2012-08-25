<h3>Update in progress:</h3>
<?php

/*
 * \brief upgrade db
 *
 * \file db_upgrade_to_1.0.php
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
 * <a href="http://www.jedox.com/license_palo_suite.txt">
 *   http://www.jedox.com/license_palo_suite.txt
 * </a>
 *
 * If you are developing and distributing open source applications under the
 * GPL License, then you are free to use Palo under the GPL License.  For OEMs,
 * ISVs, and VARs who distribute Palo with their products, and do not license
 * and distribute their source code under the GPL, Jedox provides a flexible
 * OEM Commercial License.
 *
 * \author
 * Vladislav Malicevic <vladislav.malicevic@jedox.com>
 *
 * \version
 * SVN: $Id: db_upgrade_to_1.0.php 2977 2010-03-24 10:22:55Z vladislavm $
 *
 */

require('../etc/config.php');

// constants
define('DB', 'Config');
define('CUBE1', '#_connections');
define('CUBE2', '#_CONFIGURATION');
define('DIM1', '#_connections_');
define('DIM2', 'connections');
define('DIM3', '#_CONFIGURATION_');
define('VER', 'PSVersion');
define('SEED', 'JvKnrQWPsThuJteNQAuH');

// variables
$errors_detected = 0;
$a = array();
$b = array();
$c = array();
$d = array();

// elements to be added
$element_list = array(
                            array(
                                    'name' => 'uuid',
                                    'type' => 'S'
                                ),
                            array(
                                    'name' => 'useLoginCred',
                                    'type' => 'N'
                                )
                    );

// output ticks(or error) and clear error buffer
function print_palo_error( $err )
{
    global $errors_detected;
    if ($err['paloerrorcode'] !== 0 || strlen($err['desc']) > 0)
    {
        echo '<font color="#aa0000">' . $err['paloerrorcode'] - $err['desc']. '</font><br>';
        ++$errors_detected;
        palo_clear_error();
    }
    else
    {
        echo '<b>.</b>';
    }
}

$conn = palo_init(CFG_PALO_HOST, CFG_PALO_PORT, CFG_PALO_USER, CFG_PALO_PASS);
palo_ping($conn);

print_palo_error( palo_error() );

// check if element is there by checking type
palo_etype($conn, DB, DIM3, VER);
$err = palo_error();
palo_clear_error();

if($err['paloerrorcode'] === 0 && (palo_data($conn, DB, CUBE2, VER) >= '1.0') )
{
    // disconnect the connection to server
	palo_disconnect($conn);
	
    // if version information exists and version is already 1.0 then exit
    exit('<br><font color="#00aa00">No update needed</font>');
}
else if($err['paloerrorcode'] === 4004)
{
    // if version information does not exist then add it and add missing fields
    palo_eadd($conn, DB, DIM3, 'S', VER, '', 1, 0); 
    print_palo_error( palo_error() );
    palo_setdata('1.0','false', $conn, DB, CUBE2, VER);
    print_palo_error( palo_error() );
    
    // make sure we update cache
    palo_ping($conn);
    
    // prepare default values for new fields for every connection we find in DB
    $a = palo_dimension_list_elements($conn, DB, DIM2);
    print_palo_error( palo_error() );
    foreach($a as $value)
    {
        $b[] = array('uuid', $value['name']);    
        $c[] = sha1(uniqid(SEED . mt_rand(), true));
        $d[] = array('useLoginCred', $value['name']);
        $e[] = '0';
    }
    
    // create new field 
    foreach($element_list as $item)
    {
        palo_etype($conn, DB, DIM1, $item['name']);
        $err = palo_error();
        if($err['paloerrorcode'] === 4004)
        {
            palo_clear_error();
            palo_eadd($conn, DB, DIM1, $item['type'], $item['name'], '', 1, 0);
            print_palo_error( palo_error() );
        }
        else
        {
            print_palo_error( $err );
        }
    }
    
    // inject new values into newly added fields
    palo_ping($conn);
    print_palo_error( palo_error() );
    palo_setdata_bulk($conn, DB, CUBE1, $b, $c);
    print_palo_error( palo_error() );
    // make sure we update cache
    palo_ping($conn);
    palo_setdata_bulk($conn, DB, CUBE1, $d, $e);
    print_palo_error( palo_error() );
    
    // disconnect the connection to server
	palo_disconnect($conn);
}
else
{
    // print error code
    print_palo_error( $err );
    
    // disconnect the connection to server
	palo_disconnect($conn);
	
	// if some other error occured
    exit('<br><font color="#aa0000">Errors encountered. Upgrade process failed. Please contact <a href="mailto:support@jedox.com">support@jedox.com</a></font><br>');
}

if ($errors_detected > 0)
{
    echo '<br><font color="#aa0000">Some errors were detected. Upgrade process probably failed. Please contact <a href="mailto:support@jedox.com">support@jedox.com</a></font><br>';
}
else
{
    echo '<br><font color="#00aa00">Upgrade process succeded</font><br>';
}
?>