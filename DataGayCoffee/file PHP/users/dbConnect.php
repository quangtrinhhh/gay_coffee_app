<?php
$host = 'mysql5026.site4now.net';
$dbname = 'db_aa6c53_kiotvi';
$username = 'aa6c53_kiotvi';
$password = 'tomtom0812';

try {

    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
} catch (PDOException $pe) {
    die("Could not connect to the database $dbname :" . $pe->getMessage());
}
